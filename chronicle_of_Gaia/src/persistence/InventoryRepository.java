package persistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Persists and loads inventory data.
 *
 * <p>Hybrid model:
 * <ul>
 *   <li>Stackable items: stored as one row with instance_json = NULL and quantity >= 1</li>
 *   <li>Unique/rolled items: stored as separate rows with quantity = 1 and instance_json != NULL</li>
 * </ul>
 *
 * <p>Inventory lines are linked to the player using character_name -> characters(name).</p>
 */
public class InventoryRepository {

    private static final String URL = "jdbc:mysql://localhost:3306/chronicle_of_gaia";
    private static final String USER = "gaia";
    private static final String PASS = "Gaia2026!";

    /**
     * Creates the repository and ensures required tables exist.
     *
     * @throws SQLException if schema initialization fails
     */
    public InventoryRepository() throws SQLException {
        initSchema();
    }

    /**
     * Represents a catalog item (static definition).
     */
    public static class ItemRow {

        private final long id;
        private final String code;
        private final String name;
        private final boolean stackable;

        /**
         * Creates an item row.
         *
         * @param id        item id
         * @param code      unique item code (e.g. POTION_HEAL_SMALL)
         * @param name      display name
         * @param stackable true if this item can stack
         */
        public ItemRow(long id, String code, String name, boolean stackable) {
            this.id = id;
            this.code = code;
            this.name = name;
            this.stackable = stackable;
        }

        public long getId() {
            return id;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public boolean isStackable() {
            return stackable;
        }
    }

    /**
     * Represents one inventory line for display/loading.
     */
    public static class InventoryLine {

        private final String itemCode;
        private final String itemName;
        private final boolean stackable;
        private final int quantity;
        private final Optional<String> instanceJson;

        /**
         * Creates an inventory line.
         *
         * @param itemCode     item code
         * @param itemName     item name
         * @param stackable    whether item is stackable
         * @param quantity     quantity
         * @param instanceJson optional instance json for unique/rolled items
         */
        public InventoryLine(String itemCode, String itemName, boolean stackable, int quantity, String instanceJson) {
            this.itemCode = itemCode;
            this.itemName = itemName;
            this.stackable = stackable;
            this.quantity = quantity;
            this.instanceJson = Optional.ofNullable(instanceJson);
        }

        public String getItemCode() {
            return itemCode;
        }

        public String getItemName() {
            return itemName;
        }

        public boolean isStackable() {
            return stackable;
        }

        public int getQuantity() {
            return quantity;
        }

        public Optional<String> getInstanceJson() {
            return instanceJson;
        }
    }

    /**
     * Opens a new database connection.
     *
     * @return a new SQL connection
     * @throws SQLException if the connection cannot be established
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    /**
     * Initializes schema for inventory persistence.
     *
     * @throws SQLException if table creation fails
     */
    private void initSchema() throws SQLException {

        String itemsSql = """
            CREATE TABLE IF NOT EXISTS items (
              id BIGINT AUTO_INCREMENT PRIMARY KEY,
              code VARCHAR(64) NOT NULL UNIQUE,
              name VARCHAR(128) NOT NULL,
              stackable BOOLEAN NOT NULL
            );
            """;

        String inventorySql = """
            CREATE TABLE IF NOT EXISTS inventory_items (
              id BIGINT AUTO_INCREMENT PRIMARY KEY,
              character_name VARCHAR(64) NOT NULL,
              item_id BIGINT NOT NULL,
              quantity INT NOT NULL DEFAULT 1,
              instance_json JSON NULL,
              FOREIGN KEY (character_name) REFERENCES characters(name) ON DELETE CASCADE,
              FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE RESTRICT,
              INDEX idx_inventory_character (character_name),
              INDEX idx_inventory_item (item_id),
              INDEX idx_inventory_character_item (character_name, item_id)
            );
            """;

        try (Connection con = getConnection();
             Statement st = con.createStatement()) {
            st.execute(itemsSql);
            st.execute(inventorySql);
        }
    }

    /**
     * Inserts or updates a catalog item definition.
     *
     * @param code      unique item code
     * @param name      display name
     * @param stackable whether item stacks
     * @throws SQLException if the upsert fails
     */
    public void upsertItem(String code, String name, boolean stackable) throws SQLException {

        String sql = """
            INSERT INTO items (code, name, stackable)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE
              name = VALUES(name),
              stackable = VALUES(stackable)
            """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, code);
            ps.setString(2, name);
            ps.setBoolean(3, stackable);
            ps.executeUpdate();
        }
    }

    /**
     * Adds a stackable item quantity to a character inventory.
     *
     * <p>This method stores stackables as a single row with instance_json = NULL.</p>
     *
     * @param characterName character name (PK in characters)
     * @param itemCode      item code from the catalog
     * @param amount        quantity to add (> 0)
     * @throws SQLException if the operation fails
     */
    public void addStackable(String characterName, String itemCode, int amount) throws SQLException {

        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be > 0");
        }

        String itemSql = "SELECT id, stackable FROM items WHERE code = ?";

        String updateSql = """
            UPDATE inventory_items
            SET quantity = quantity + ?
            WHERE character_name = ?
              AND item_id = ?
              AND instance_json IS NULL
            """;

        String insertSql = """
            INSERT INTO inventory_items (character_name, item_id, quantity, instance_json)
            VALUES (?, ?, ?, NULL)
            """;

        try (Connection con = getConnection()) {

            con.setAutoCommit(false);

            long itemId;
            boolean stackable;

            try (PreparedStatement ps = con.prepareStatement(itemSql)) {
                ps.setString(1, itemCode);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Unknown item code: " + itemCode);
                    }
                    itemId = rs.getLong("id");
                    stackable = rs.getBoolean("stackable");
                }
            }

            if (!stackable) {
                throw new SQLException("Item is not stackable: " + itemCode);
            }

            int updated;
            try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                ps.setInt(1, amount);
                ps.setString(2, characterName);
                ps.setLong(3, itemId);
                updated = ps.executeUpdate();
            }

            if (updated == 0) {
                try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                    ps.setString(1, characterName);
                    ps.setLong(2, itemId);
                    ps.setInt(3, amount);
                    ps.executeUpdate();
                }
            }

            con.commit();
        }
    }

    /**
     * Adds a unique/rolled item instance to a character inventory.
     *
     * <p>Unique items are stored as separate rows with quantity = 1 and instance_json != NULL.</p>
     *
     * @param characterName character name (PK in characters)
     * @param itemCode      item code from the catalog
     * @param instanceJson  JSON payload describing this instance (must be valid JSON)
     * @throws SQLException if the operation fails
     */
    public void addUnique(String characterName, String itemCode, String instanceJson) throws SQLException {

        if (instanceJson == null || instanceJson.isBlank()) {
            throw new IllegalArgumentException("instanceJson must be non-empty");
        }

        String itemSql = "SELECT id, stackable FROM items WHERE code = ?";

        String insertSql = """
            INSERT INTO inventory_items (character_name, item_id, quantity, instance_json)
            VALUES (?, ?, 1, ?)
            """;

        try (Connection con = getConnection()) {

            long itemId;
            boolean stackable;

            try (PreparedStatement ps = con.prepareStatement(itemSql)) {
                ps.setString(1, itemCode);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Unknown item code: " + itemCode);
                    }
                    itemId = rs.getLong("id");
                    stackable = rs.getBoolean("stackable");
                }
            }

            if (stackable) {
                throw new SQLException("Use addStackable for stackable items: " + itemCode);
            }

            try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                ps.setString(1, characterName);
                ps.setLong(2, itemId);
                ps.setString(3, instanceJson);
                ps.executeUpdate();
            }
        }
    }

    /**
     * Loads inventory lines for a character.
     *
     * @param characterName character name
     * @return inventory lines
     * @throws SQLException if loading fails
     */
    public List<InventoryLine> loadInventory(String characterName) throws SQLException {

        String sql = """
            SELECT i.code, i.name, i.stackable, ii.quantity, ii.instance_json
            FROM inventory_items ii
            JOIN items i ON i.id = ii.item_id
            WHERE ii.character_name = ?
            ORDER BY i.name ASC, ii.id ASC
            """;

        List<InventoryLine> lines = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, characterName);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lines.add(new InventoryLine(
                            rs.getString("code"),
                            rs.getString("name"),
                            rs.getBoolean("stackable"),
                            rs.getInt("quantity"),
                            rs.getString("instance_json")
                    ));
                }
            }
        }

        return lines;
    }
}