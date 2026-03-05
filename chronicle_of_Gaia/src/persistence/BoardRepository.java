package persistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Persists and loads the board blueprint (64 tiles) from the database.
 *
 * <p>Tiles are stored by linear index (0..63). Only tile type and optional enemy type
 * are persisted. No enemy instances or loot contents are stored here.</p>
 */
public class BoardRepository {

    private static final String URL = "jdbc:mysql://localhost:3306/chronicle_of_gaia";
    private static final String USER = "gaia";
    private static final String PASS = "Gaia2026!";

    /**
     * Creates the repository and ensures required tables exist.
     *
     * @throws SQLException if schema initialization fails
     */
    public BoardRepository() throws SQLException {
        initSchema();
    }

    /**
     * Tile types supported by the board.
     */
    public enum TileType {
        EMPTY,
        CHEST,
        ENEMY
    }

    /**
     * Represents a single persisted tile.
     *
     * @param idx       the tile index (0..63)
     * @param type      the tile type
     * @param enemyType the enemy type identifier (only for ENEMY tiles), otherwise empty
     */
    public static class TileRow {

        private final int idx;
        private final TileType type;
        private final Optional<String> enemyType;

        /**
         * Creates a tile row.
         *
         * @param idx       the tile index (0..63)
         * @param type      the tile type
         * @param enemyType the enemy type identifier, null if not applicable
         */
        public TileRow(int idx, TileType type, String enemyType) {
            this.idx = idx;
            this.type = type;
            this.enemyType = Optional.ofNullable(enemyType);
        }

        public int getIdx() {
            return idx;
        }

        public TileType getType() {
            return type;
        }

        public Optional<String> getEnemyType() {
            return enemyType;
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
     * Initializes schema for board persistence.
     *
     * @throws SQLException if table creation fails
     */
    private void initSchema() throws SQLException {

        String savesSql = """
            CREATE TABLE IF NOT EXISTS saves (
              id BIGINT AUTO_INCREMENT PRIMARY KEY,
              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
            """;

        String tilesSql = """
            CREATE TABLE IF NOT EXISTS board_tiles (
              save_id BIGINT NOT NULL,
              idx INT NOT NULL,
              tile_type VARCHAR(16) NOT NULL,
              enemy_type VARCHAR(32) NULL,
              PRIMARY KEY (save_id, idx),
              FOREIGN KEY (save_id) REFERENCES saves(id) ON DELETE CASCADE
            );
            """;

        try (Connection con = getConnection();
             Statement st = con.createStatement()) {
            st.execute(savesSql);
            st.execute(tilesSql);
        }
    }

    /**
     * Creates a new save slot.
     *
     * @return the generated save id
     * @throws SQLException if the insert fails
     */
    public long createSave() throws SQLException {

        String sql = "INSERT INTO saves () VALUES ()";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next()) {
                    throw new SQLException("Failed to create save: no generated key returned.");
                }
                return rs.getLong(1);
            }
        }
    }

    /**
     * Deletes a save slot (board tiles are deleted by cascade).
     *
     * @param saveId the save id
     * @return true if a save was deleted, false otherwise
     * @throws SQLException if the delete fails
     */
    public boolean deleteSave(long saveId) throws SQLException {

        String sql = "DELETE FROM saves WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, saveId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Saves the full board state (expected 64 rows) for a save slot.
     *
     * <p>This method replaces existing tiles for the given save id.</p>
     *
     * @param saveId the save id
     * @param tiles  the tiles to persist (typically 64 entries)
     * @throws SQLException if the save fails
     */
    public void saveBoard(long saveId, List<TileRow> tiles) throws SQLException {

        String deleteSql = "DELETE FROM board_tiles WHERE save_id = ?";

        String insertSql = """
            INSERT INTO board_tiles (save_id, idx, tile_type, enemy_type)
            VALUES (?, ?, ?, ?)  
            """;

        try (Connection con = getConnection()) {

            con.setAutoCommit(false);

            try (PreparedStatement del = con.prepareStatement(deleteSql)) {
                del.setLong(1, saveId);
                del.executeUpdate();
            }

            try (PreparedStatement ins = con.prepareStatement(insertSql)) {

                for (TileRow t : tiles) {

                    ins.setLong(1, saveId);
                    ins.setInt(2, t.getIdx());
                    ins.setString(3, t.getType().name());

                    if (t.getType() == TileType.ENEMY) {
                        ins.setString(4, t.getEnemyType().orElse(null));
                    } else {
                        ins.setNull(4, Types.VARCHAR);
                    }

                    ins.addBatch();
                }

                ins.executeBatch();
            }

            con.commit();
        }
    }

    /**
     * Loads the full board state for a save slot.
     *
     * @param saveId the save id
     * @return a list of tile rows ordered by idx
     * @throws SQLException if loading fails
     */
    public List<TileRow> loadBoard(long saveId) throws SQLException {

        String sql = """
            SELECT idx, tile_type, enemy_type
            FROM board_tiles
            WHERE save_id = ?
            ORDER BY idx ASC
            """;

        List<TileRow> tiles = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, saveId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    int idx = rs.getInt("idx");
                    TileType type = TileType.valueOf(rs.getString("tile_type"));
                    String enemyType = rs.getString("enemy_type");

                    tiles.add(new TileRow(idx, type, enemyType));
                }
            }
        }

        return tiles;
    }
}