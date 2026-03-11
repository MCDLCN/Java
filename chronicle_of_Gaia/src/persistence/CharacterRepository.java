package persistence;

import dto.CharacterSummary;
import dto.LoadedGame;
import model.entities.Stats;
import model.entities.classes.PlayerCharacter;
import model.entities.classes.Warrior;
import model.entities.classes.Wizard;
import main_logic.enums.Stat;

import java.sql.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

public class CharacterRepository {
    public CharacterRepository() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = """
            CREATE TABLE IF NOT EXISTS characters (
              id          BIGINT AUTO_INCREMENT PRIMARY KEY,
              name        VARCHAR(64)  NOT NULL UNIQUE,
              save_id     BIGINT       NOT NULL UNIQUE,
              class_id    BIGINT       NULL,
              level       INT          NOT NULL,
              current_xp INT NOT NULL DEFAULT 0,
              unspent_stat_points INT NOT NULL DEFAULT 0,
              max_hp      INT          NOT NULL,
              current_hp  INT          NOT NULL,

              str INT NOT NULL,
              dex INT NOT NULL,
              con INT NOT NULL,
              int_stat INT NOT NULL,
              wis INT NOT NULL,
              cha INT NOT NULL,

              training_progress VARCHAR(255) NOT NULL DEFAULT '',
              position INT NOT NULL,
              FOREIGN KEY (save_id) REFERENCES saves(id) ON DELETE CASCADE,
              FOREIGN KEY (class_id) REFERENCES classes(id)
            );
            """;

            stmt.execute(sql);
        }

        migrateLegacyTypeColumn();
        migrateTrainingProgressColumn();
        migrateXpColumns();
    }

    /**
     * Inserts a new player character into the database.
     *
     * @param saveId the save slot id
     * @param player the character to persist
     * @return the generated character id
     * @throws SQLException if the database operation fails
     */
    public long create(long saveId, PlayerCharacter player) throws SQLException {
        try (Connection con = getConnection()) {
            boolean hasTypeColumn = hasColumn(con, "characters", "type");

            String sql = hasTypeColumn
                    ? """
                    INSERT INTO characters
                    (name, save_id, type, class_id, level, current_xp, unspent_stat_points, max_hp, current_hp, str, dex, con, int_stat, wis, cha, training_progress, position)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """
                    : """
                    INSERT INTO characters
                    (name, save_id, class_id, level, current_xp, unspent_stat_points, max_hp, current_hp, str, dex, con, int_stat, wis, cha, training_progress, position)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """;

            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                int index = 1;
                ps.setString(index++, player.getName());
                ps.setLong(index++, saveId);

                if (hasTypeColumn) {
                    ps.setString(index++, normalizeClassName(player.getClassName()));
                }

                ps.setLong(index++, player.getClassId());
                ps.setInt(index++, player.getLevel());
                ps.setInt(index++, player.getCurrentXp());
                ps.setInt(index++, player.getUnspentStatPoints());
                ps.setInt(index++, player.getMaxHp());
                ps.setInt(index++, player.getHp());

                ps.setInt(index++, player.getOneStat(Stat.STR));
                ps.setInt(index++, player.getOneStat(Stat.DEX));
                ps.setInt(index++, player.getOneStat(Stat.CON));
                ps.setInt(index++, player.getOneStat(Stat.INT));
                ps.setInt(index++, player.getOneStat(Stat.WIS));
                ps.setInt(index++, player.getOneStat(Stat.CHA));

                ps.setString(index++, serializeTrainingProgress(player.getTrainingProgress()));
                ps.setInt(index, player.getPosition());

                ps.setInt(index, player.getPosition());

                ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next()) {
                    throw new SQLException("Failed to create character: no generated key returned.");
                }
                return rs.getLong(1);
            }
        }
    }}

    /**
     * Updates an existing player character by id.
     *
     * @param characterId the persisted character id
     * @param saveId the save slot id
     * @param player the character to persist
     * @throws SQLException if the database operation fails
     */
    public void update(long characterId, long saveId, PlayerCharacter player) throws SQLException {
        try (Connection con = getConnection()) {
            boolean hasTypeColumn = hasColumn(con, "characters", "type");

            String sql = hasTypeColumn
                    ? """
                    UPDATE characters
                    SET name = ?,
                        save_id = ?,
                        type = ?,
                        class_id = ?,
                        level = ?,
                        current_xp = ?,
                        unspent_stat_points = ?,
                        max_hp = ?,
                        current_hp = ?,
                        str = ?,
                        dex = ?,
                        con = ?,
                        int_stat = ?,
                        wis = ?,
                        cha = ?,
                        training_progress = ?,
                        position = ?
                    WHERE id = ?
                    """
                    : """
                    UPDATE characters
                    SET name = ?,
                        save_id = ?,
                        class_id = ?,
                        level = ?,
                        current_xp = ?,
                        unspent_stat_points = ?,
                        max_hp = ?,
                        current_hp = ?,
                        str = ?,
                        dex = ?,
                        con = ?,
                        int_stat = ?,
                        wis = ?,
                        cha = ?,
                        training_progress = ?,
                        position = ?
                    WHERE id = ?
                    """;

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                int index = 1;
                ps.setString(index++, player.getName());
                ps.setLong(index++, saveId);

                if (hasTypeColumn) {
                    ps.setString(index++, normalizeClassName(player.getClassName()));
                }

                ps.setLong(index++, player.getClassId());
                ps.setInt(index++, player.getLevel());
                ps.setInt(index++, player.getCurrentXp());
                ps.setInt(index++, player.getUnspentStatPoints());
                ps.setInt(index++, player.getMaxHp());
                ps.setInt(index++, player.getHp());

                ps.setInt(index++, player.getOneStat(Stat.STR));
                ps.setInt(index++, player.getOneStat(Stat.DEX));
                ps.setInt(index++, player.getOneStat(Stat.CON));
                ps.setInt(index++, player.getOneStat(Stat.INT));
                ps.setInt(index++, player.getOneStat(Stat.WIS));
                ps.setInt(index++, player.getOneStat(Stat.CHA));

                ps.setString(index++, serializeTrainingProgress(player.getTrainingProgress()));
                ps.setInt(index++, player.getPosition());
                ps.setLong(index, characterId);

                ps.executeUpdate();
            }
        }
    }

    /**
     * Loads a player character and its associated save slot.
     *
     * @param id the persisted character id
     * @return an Optional containing the loaded game state
     * @throws SQLException if the database query fails
     */
    public Optional<LoadedGame> load(Long id) throws SQLException {
        String sql = """
        SELECT c.*, COALESCE(cl.name, c.type) AS class_name
        FROM characters c
        LEFT JOIN classes cl ON cl.id = c.class_id
        WHERE c.id = ?
        """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }

                long saveId = rs.getLong("save_id");
                String name = rs.getString("name");
                long classId = rs.getLong("class_id");
                String className = rs.getString("class_name");
                int level = rs.getInt("level");
                int currentXp = rs.getInt("current_xp");
                int unspentStatPoints = rs.getInt("unspent_stat_points");
                int maxHp = rs.getInt("max_hp");
                int hp = rs.getInt("current_hp");

                Stats stats = new Stats();
                stats.set(Stat.STR, rs.getInt("str"));
                stats.set(Stat.DEX, rs.getInt("dex"));
                stats.set(Stat.CON, rs.getInt("con"));
                stats.set(Stat.INT, rs.getInt("int_stat"));
                stats.set(Stat.WIS, rs.getInt("wis"));
                stats.set(Stat.CHA, rs.getInt("cha"));

                PlayerCharacter player = createPlayer(classId, className, level, name, stats, maxHp, hp,  currentXp, unspentStatPoints);

                EnumMap<Stat, Integer> trainingProgress =
                        deserializeTrainingProgress(rs.getString("training_progress"));

                player.setTrainingProgress(trainingProgress);

                player.setPosition(rs.getInt("position"));

                return Optional.of(new LoadedGame(id, saveId, player));
            }
        }
    }

    /**
     * Retrieves a list of all stored characters.
     *
     * <p>Each entry contains the character name, class name,
     * and level for display in the load menu.</p>
     *
     * @return a list of character summaries
     * @throws SQLException if the query fails
     */
    public List<CharacterSummary> listCharacters() throws SQLException {

        String sql = """
        SELECT c.id, c.name, COALESCE(cl.name, c.type) AS class_name, c.level
        FROM characters c
        LEFT JOIN classes cl ON cl.id = c.class_id
        ORDER BY c.id
        """;

        List<CharacterSummary> characters = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                String className = rs.getString("class_name");
                int level = rs.getInt("level");

                characters.add(new CharacterSummary(id, name, className, level));
            }
        }

        return characters;
    }

    /**
     * Deletes a character from the database by id.
     *
     * @param characterId the persisted character id
     * @return true if a character was deleted, false otherwise
     * @throws SQLException if the database operation fails
     */
    public boolean delete(long characterId) throws SQLException {

        String sql = "DELETE FROM characters WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, characterId);

            int affectedRows = ps.executeUpdate();

            return affectedRows > 0;
        }
    }

    /**
     * Rebuilds one concrete player class from persisted class metadata.
     *
     * @param classId persisted class id
     * @param className persisted class name
     * @param level persisted level
     * @param name persisted character name
     * @param stats persisted stats
     * @param maxHp persisted max hp
     * @param hp persisted current hp
     * @return reconstructed player instance
     * @throws SQLException when the class is unknown
     */
    private PlayerCharacter createPlayer(long classId, String className, int level, String name,
                                         Stats stats, int maxHp, int hp, int currentXp, int unspentStatPoints)
            throws SQLException {

        String normalizedName = normalizeClassName(className);

        return switch (normalizedName) {
            case "WARRIOR" -> new Warrior(level, name, stats, maxHp, hp, classId, className, currentXp, unspentStatPoints);
            case "WIZARD" -> new Wizard(level, name, stats, maxHp, hp, classId, className, currentXp, unspentStatPoints);
            default ->  throw new SQLException("Unsupported player class: " + className);
        };
    }

    // ----- Migration -----

    /**
     * Migrates legacy enum-based rows to the class catalog foreign key.
     *
     * @throws SQLException if migration fails
     */
    private void migrateLegacyTypeColumn() throws SQLException {
        try (Connection con = getConnection()) {
            if (!hasColumn(con, "characters", "class_id")) {
                try (Statement stmt = con.createStatement()) {
                    stmt.execute("ALTER TABLE characters ADD COLUMN class_id BIGINT NULL");
                }
            }

            if (hasColumn(con, "characters", "type")) {
                String sql = """
                UPDATE characters c
                JOIN classes cl ON LOWER(cl.name) = LOWER(
                    CASE c.type
                        WHEN 'WARRIOR' THEN 'Warrior'
                        WHEN 'WIZARD' THEN 'Wizard'
                        ELSE c.type
                    END
                )
                SET c.class_id = cl.id
                WHERE c.class_id IS NULL
                """;

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.executeUpdate();
                }
            }
        }
    }

    private void migrateTrainingProgressColumn() throws SQLException {
        try (Connection con = getConnection();
             Statement stmt = con.createStatement()) {

            if (!hasColumn(con, "characters", "training_progress")) {
                stmt.execute("ALTER TABLE characters ADD COLUMN training_progress VARCHAR(255) NOT NULL DEFAULT ''");
            }
        }
    }

    private void migrateXpColumns() throws SQLException {
        try (Connection con = getConnection();
             Statement stmt = con.createStatement()) {

            if (!hasColumn(con, "characters", "current_xp")) {
                stmt.execute("ALTER TABLE characters ADD COLUMN current_xp INT NOT NULL DEFAULT 0");
            }

            if (!hasColumn(con, "characters", "unspent_stat_points")) {
                stmt.execute("ALTER TABLE characters ADD COLUMN unspent_stat_points INT NOT NULL DEFAULT 0");
            }
        }
    }


    // ----- Helpers -----

    /**
     * Normalizes a class name for runtime branching.
     *
     * @param className persisted class name
     * @return normalized uppercase class name
     */
    private String normalizeClassName(String className) {
        return className == null ? "" : className.trim().toUpperCase();
    }

    /**
     * Opens a new database connection.
     *
     * @return a new SQL connection to the Chronicle of Gaia database
     * @throws SQLException if the connection cannot be established
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/chronicle_of_gaia",
                "gaia",
                "Gaia2026!"
        );
    }

    private String serializeTrainingProgress(EnumMap<Stat, Integer> trainingProgress) {
        if (trainingProgress == null || trainingProgress.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (Stat stat : Stat.values()) {
            int value = trainingProgress.getOrDefault(stat, 0);

            if (sb.length() > 0) {
                sb.append(";");
            }

            sb.append(stat.name()).append("=").append(value);
        }

        return sb.toString();
    }

    private EnumMap<Stat, Integer> deserializeTrainingProgress(String value) {
        EnumMap<Stat, Integer> trainingProgress = new EnumMap<>(Stat.class);

        if (value == null || value.isBlank()) {
            return trainingProgress;
        }

        String[] entries = value.split(";");

        for (String entry : entries) {
            String[] parts = entry.split("=");

            if (parts.length != 2) {
                continue;
            }

            try {
                Stat stat = Stat.valueOf(parts[0].trim());
                int progress = Integer.parseInt(parts[1].trim());
                trainingProgress.put(stat, progress);
            } catch (IllegalArgumentException ignored) {
            }
        }

        return trainingProgress;
    }


    /**
     * Checks whether a table contains one specific column.
     *
     * @param con open connection
     * @param tableName table name
     * @param columnName column name
     * @return true when the column exists
     * @throws SQLException if metadata access fails
     */
    private boolean hasColumn(Connection con, String tableName, String columnName) throws SQLException {
        DatabaseMetaData metaData = con.getMetaData();

        try (ResultSet rs = metaData.getColumns(con.getCatalog(), null, tableName, columnName)) {
            return rs.next();
        }
    }
}
