package persistence;

import main_logic.enums.ItemCode;
import model.inventory.Inventory;
import model.inventory.InventoryEntry;
import model.items.Item;
import model.items.ItemFactory;

import persistence.itemdata.ItemInstanceData;
import persistence.itemdata.ItemInstanceJsonMapper;

import java.sql.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Persists and loads inventory data.
 *
 * <p>Hybrid model:
 * <ul>
 *   <li>Stackable items: stored as one row with instance_json = NULL and quantity >= 1</li>
 *   <li>Unique/rolled items: stored as separate rows with quantity = 1 and instance_json != NULL</li>
 * </ul>
 *
 * <p>Inventory rows are linked to the player using character_id</p>
 */
public class InventoryRepository {

    /**
     * The link to the database and the user/password needed to connect
     */
    private static final String URL = "jdbc:mysql://localhost:3306/chronicle_of_gaia";
    private static final String USER = "gaia";
    private static final String PASS = "Gaia2026!";


    private final ItemInstanceJsonMapper itemInstanceJsonMapper = new ItemInstanceJsonMapper();

    /**
     * Creates the repository and ensures required tables exist.
     *
     * @throws SQLException if schema initialization fails
     */
    public InventoryRepository() throws SQLException {
        initSchema();
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
            )
            """;

        String inventorySql = """
            CREATE TABLE IF NOT EXISTS inventory_items (
              id BIGINT AUTO_INCREMENT PRIMARY KEY,
              character_id BIGINT NOT NULL,
              item_id BIGINT NOT NULL,
              quantity INT NOT NULL DEFAULT 1,
              instance_json JSON NULL,
              equipped BOOLEAN NOT NULL DEFAULT FALSE,
              FOREIGN KEY (character_id) REFERENCES characters(id) ON DELETE CASCADE,
              FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE RESTRICT,
              INDEX idx_inventory_character (character_id),
              INDEX idx_inventory_item (item_id),
              INDEX idx_inventory_character_item (character_id, item_id)
            )
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

    // ----- Adding -----

    /**
     * Adds a stackable item quantity to a character inventory.
     *
     * <p>This stores stackables as a single row with instance_json = NULL.</p>
     *
     * @param characterId character id
     * @param itemCode      item code from the catalog
     * @param amount        quantity to add
     * @throws SQLException if the operation fails
     */
    public void addStackable(long characterId, String itemCode, int amount) throws SQLException {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be > 0");
        }

        String itemSql = "SELECT id, stackable FROM items WHERE code = ?";
        String updateSql = """
            UPDATE inventory_items
            SET quantity = quantity + ?
            WHERE character_id = ?
              AND item_id = ?
              AND instance_json IS NULL
            """;
        String insertSql = """
            INSERT INTO inventory_items (character_id, item_id, quantity, instance_json)
            VALUES (?, ?, ?, NULL)
            """;

        try (Connection con = getConnection()) {
            con.setAutoCommit(false);

            try {
                ItemMeta itemMeta = getItemMeta(con, itemCode);

                if (!itemMeta.stackable()) {
                    throw new SQLException("Item is not stackable: " + itemCode);
                }

                int updated;
                try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                    ps.setInt(1, amount);
                    ps.setLong(2, characterId);
                    ps.setLong(3, itemMeta.id());
                    updated = ps.executeUpdate();
                }

                if (updated == 0) {
                    try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                        ps.setLong(1, characterId);
                        ps.setLong(2, itemMeta.id());
                        ps.setInt(3, amount);
                        ps.executeUpdate();
                    }
                }

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    /**
     * Adds a unique item instance to a character inventory.
     *
     * <p>Unique items are stored as separate rows with quantity = 1.</p>
     *
     * @param characterId character id
     * @param itemCode item code from the catalog
     * @param instanceJson optional instance JSON, defaults to "{}" if null/blank
     * @param equipped whether the item is equipped on insert
     * @throws SQLException if the operation fails
     */
    public void addUnique(long characterId, String itemCode, String instanceJson, boolean equipped) throws SQLException {
        String normalizedJson = normalizeInstanceJson(instanceJson);

        String insertSql = """
        INSERT INTO inventory_items (character_id, item_id, quantity, instance_json, equipped)
        VALUES (?, ?, 1, ?, ?)
        """;

        try (Connection con = getConnection()) {
            ItemMeta itemMeta = getItemMeta(con, itemCode);

            if (itemMeta.stackable()) {
                throw new SQLException("Use addStackable for stackable items: " + itemCode);
            }

            try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                ps.setLong(1, characterId);
                ps.setLong(2, itemMeta.id());
                ps.setString(3, normalizedJson);
                ps.setBoolean(4, equipped);
                ps.executeUpdate();
            }
        }
    }

    /**
     * Adds an item to the database inventory.
     *
     * @param characterId character id
     * @param item item to add
     * @param quantity quantity to add
     * @param instanceJson optional instance JSON override for unique items
     * @throws SQLException if saving fails
     */
    public void addItem(long characterId, Item item, int quantity, String instanceJson) throws SQLException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be > 0");
        }

        if (item.isStackable()) {
            addStackable(characterId, item.getCode().name(), quantity);
            return;
        }

        String jsonToStore = instanceJson;
        if (jsonToStore == null || jsonToStore.isBlank()) {
            jsonToStore = itemInstanceJsonMapper.toJson(item);
        }

        boolean equipped = false;

        for (int i = 0; i < quantity; i++) {
            addUnique(characterId, item.getCode().name(), jsonToStore, equipped);
        }
    }

    // ----- Updating -----

    /**
     * Updates a unique inventory row by its database id.
     *
     * <p>This must be used for unique items so multiple instances of the same item
     * code do not collide.</p>
     *
     * @param inventoryRowId inventory_items.id
     * @param item item whose current state should be persisted
     * @param equipped whether the item is currently equipped
     * @throws SQLException if the update fails
     */
    public void updateUniqueItem(long inventoryRowId, Item item, boolean equipped) throws SQLException {
        if (item.isStackable()) {
            throw new IllegalArgumentException("updateUniqueItem only supports unique items.");
        }

        String jsonToStore = itemInstanceJsonMapper.toJson(item);

        String sql = """
        UPDATE inventory_items
        SET instance_json = ?, equipped = ?
        WHERE id = ?
        """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, normalizeInstanceJson(jsonToStore));
            ps.setBoolean(2, equipped);
            ps.setLong(3, inventoryRowId);

            ps.executeUpdate();
        }
    }

    /**
     * Updates the quantity of a stackable inventory row.
     *
     * @param characterId character id
     * @param itemCode stackable item code
     * @param quantity new quantity
     * @throws SQLException if the update fails
     */
    public void updateStackableItem(long characterId, String itemCode, int quantity) throws SQLException {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be >= 0");
        }

        String sql = """
        UPDATE inventory_items ii
        JOIN items i ON i.id = ii.item_id
        SET ii.quantity = ?
        WHERE ii.character_id = ?
          AND i.code = ?
          AND ii.instance_json IS NULL
        """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setLong(2, characterId);
            ps.setString(3, itemCode);

            ps.executeUpdate();
        }
    }



    // ----- Loading -----

    /**
     * Loads a character inventory from the database.
     *
     * @param characterId character id
     * @return reconstructed inventory
     * @throws SQLException if database access fails
     */
    public Inventory loadInventory(long characterId) throws SQLException {

        String sql = """
        SELECT ii.id,
               i.code,
               ii.quantity,
               ii.instance_json,
               ii.equipped
        FROM inventory_items ii
        JOIN items i ON i.id = ii.item_id
        WHERE ii.character_id = ?
        ORDER BY ii.id ASC
        """;

        Inventory inventory = new Inventory();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, characterId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    long inventoryRowId = rs.getLong("id");
                    ItemCode code = ItemCode.valueOf(rs.getString("code"));
                    int quantity = rs.getInt("quantity");
                    String instanceJson = rs.getString("instance_json");
                    boolean equipped = rs.getBoolean("equipped");

                    ItemInstanceData instanceData =
                            itemInstanceJsonMapper.fromJson(code, instanceJson);

                    Item item = ItemFactory.create(code, instanceData);

                    inventory.addLoadedItem(inventoryRowId, item, quantity, equipped);

                }
            }
        }

        return inventory;
    }



    // ----- removing -----

    /**
     * Removes an item from the database inventory.
     *
     * @param characterId character id
     * @param item          item to remove
     * @param quantity      quantity to remove
     * @throws SQLException if removal fails
     */
    public void removeItem(long characterId, Item item, int quantity) throws SQLException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be > 0");
        }

        try (Connection con = getConnection()) {
            con.setAutoCommit(false);

            try {
                ItemMeta itemMeta = getItemMeta(con, item.getCode().name());

                if (item.isStackable()) {
                    removeStackable(con, characterId, itemMeta.id(), quantity);
                } else {
                    removeUnique(con, characterId, itemMeta.id(), quantity);
                }

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    /**
     * Removes quantity from a stackable row and deletes the row if it reaches zero.
     */
    private void removeStackable(Connection con, long characterId, long itemId, int quantity) throws SQLException {
        String updateSql = """
            UPDATE inventory_items
            SET quantity = quantity - ?
            WHERE character_id = ?
              AND item_id = ?
              AND instance_json IS NULL
            """;

        String deleteSql = """
            DELETE FROM inventory_items
            WHERE character_id = ?
              AND item_id = ?
              AND instance_json IS NULL
              AND quantity <= 0
            """;

        try (PreparedStatement ps = con.prepareStatement(updateSql)) {
            ps.setInt(1, quantity);
            ps.setLong(2, characterId);
            ps.setLong(3, itemId);
            ps.executeUpdate();
        }

        try (PreparedStatement ps = con.prepareStatement(deleteSql)) {
            ps.setLong(1, characterId);
            ps.setLong(2, itemId);
            ps.executeUpdate();
        }
    }

    /**
     * Removes a number of unique rows for a given item.
     */
    private void removeUnique(Connection con, long characterId, long itemId, int quantity) throws SQLException {
        String selectSql = """
            SELECT id
            FROM inventory_items
            WHERE character_id = ?
              AND item_id = ?
            ORDER BY id ASC
            LIMIT ?
            """;

        String deleteSql = "DELETE FROM inventory_items WHERE id = ?";

        try (PreparedStatement selectPs = con.prepareStatement(selectSql)) {
            selectPs.setLong(1, characterId);
            selectPs.setLong(2, itemId);
            selectPs.setInt(3, quantity);

            try (ResultSet rs = selectPs.executeQuery()) {
                while (rs.next()) {
                    long inventoryRowId = rs.getLong("id");

                    try (PreparedStatement deletePs = con.prepareStatement(deleteSql)) {
                        deletePs.setLong(1, inventoryRowId);
                        deletePs.executeUpdate();
                    }
                }
            }
        }
    }

    // ----- Getting -----

    /**
     * Loads item id and stackable metadata from the items catalog.
     */
    private ItemMeta getItemMeta(Connection con, String itemCode) throws SQLException {
        String itemSql = "SELECT id, stackable FROM items WHERE code = ?";

        try (PreparedStatement ps = con.prepareStatement(itemSql)) {
            ps.setString(1, itemCode);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Unknown item code: " + itemCode);
                }

                return new ItemMeta(
                        rs.getLong("id"),
                        rs.getBoolean("stackable")
                );
            }
        }
    }

    // ----- Saving -----

    /**
     * Syncs the full inventory state for one character.
     *
     * <p>Existing rows are updated, new rows are inserted,
     * and rows missing from memory are deleted.</p>
     *
     * @param characterId character owning the inventory
     * @param inventory current in-memory inventory
     * @throws SQLException if persistence fails
     */
    public void saveInventory(long characterId, Inventory inventory) throws SQLException {
        Inventory persistedInventory = loadInventory(characterId);

        Set<Long> currentRowIds = new HashSet<>();

        for (InventoryEntry entry : inventory.getEntries()) {
            Item item = entry.getItem();

            if (item.isStackable()) {
                Optional<InventoryEntry> persistedEntry = persistedInventory.findEntry(item.getCode());

                if (persistedEntry.isPresent()) {
                    updateStackableItem(characterId, item.getCode().name(), entry.getQuantity());

                    Long persistedRowId = persistedEntry.get().getInventoryRowId();
                    if (persistedRowId != null) {
                        currentRowIds.add(persistedRowId);
                        entry.setInventoryRowId(persistedRowId);
                    }
                } else {
                    addStackable(characterId, item.getCode().name(), entry.getQuantity());
                }

                continue;
            }

            if (entry.getInventoryRowId() == null) {
                long generatedRowId = insertUniqueAndReturnRowId(
                        characterId,
                        item.getCode().name(),
                        itemInstanceJsonMapper.toJson(item),
                        entry.isEquipped()
                );

                entry.setInventoryRowId(generatedRowId);
                currentRowIds.add(generatedRowId);
            } else {
                updateUniqueItem(entry.getInventoryRowId(), item, entry.isEquipped());
                currentRowIds.add(entry.getInventoryRowId());
            }
        }

        for (InventoryEntry persistedEntry : persistedInventory.getEntries()) {
            Long persistedRowId = persistedEntry.getInventoryRowId();

            if (persistedRowId != null && !currentRowIds.contains(persistedRowId)) {
                deleteInventoryRow(persistedRowId);
            }
        }
    }

    /**
     * Inserts one unique inventory row and returns its generated id.
     *
     * @param characterId character id
     * @param itemCode item code from the catalog
     * @param instanceJson serialized instance state
     * @param equipped whether the item is equipped
     * @return generated inventory row id
     * @throws SQLException if insert fails
     */
    private long insertUniqueAndReturnRowId(long characterId, String itemCode, String instanceJson, boolean equipped)
            throws SQLException {

        String normalizedJson = normalizeInstanceJson(instanceJson);

        String sql = """
        INSERT INTO inventory_items (character_id, item_id, quantity, instance_json, equipped)
        VALUES (?, ?, 1, ?, ?)
        """;

        try (Connection con = getConnection()) {
            ItemMeta itemMeta = getItemMeta(con, itemCode);

            if (itemMeta.stackable()) {
                throw new SQLException("Use addStackable for stackable items: " + itemCode);
            }

            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, characterId);
                ps.setLong(2, itemMeta.id());
                ps.setString(3, normalizedJson);
                ps.setBoolean(4, equipped);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new SQLException("Failed to insert unique inventory row: no generated key returned.");
                    }
                    return rs.getLong(1);
                }
            }
        }
    }

    /**
     * Deletes one inventory row by its database id.
     *
     * @param inventoryRowId inventory_items.id
     * @throws SQLException if deletion fails
     */
    private void deleteInventoryRow(long inventoryRowId) throws SQLException {
        String sql = "DELETE FROM inventory_items WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, inventoryRowId);
            ps.executeUpdate();
        }
    }

    /**
     * Normalizes instance JSON for unique items.
     */
    private String normalizeInstanceJson(String instanceJson) {
        return (instanceJson == null || instanceJson.isBlank()) ? "{}" : instanceJson;
    }

    /**
     * Catalog item metadata.
     */
    private record ItemMeta(long id, boolean stackable) {
    }
}