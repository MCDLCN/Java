package persistence;

import dto.CharacterSummary;
import dto.LoadedGame;
import main_logic.enums.CharacterType;
import main_logic.enums.Stat;
import model.entities.Stats;
import model.entities.classes.PlayerCharacter;
import model.entities.classes.Warrior;
import model.entities.classes.Wizard;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public  class CharacterRepository {
    public CharacterRepository() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = """
            CREATE TABLE IF NOT EXISTS characters (
              id          BIGINT AUTO_INCREMENT PRIMARY KEY,
              name        VARCHAR(64)  NOT NULL UNIQUE,
              save_id     BIGINT       NOT NULL UNIQUE,
              type        VARCHAR(16)  NOT NULL,
              level       INT          NOT NULL,
              max_hp      INT          NOT NULL,
              current_hp  INT          NOT NULL,
    
              str INT NOT NULL,
              dex INT NOT NULL,
              con INT NOT NULL,
              int_stat INT NOT NULL,
              wis INT NOT NULL,
              cha INT NOT NULL,
    
              position INT NOT NULL,
              FOREIGN KEY (save_id) REFERENCES saves(id) ON DELETE CASCADE
            );
            """;

            stmt.execute(sql);
        }
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

        String sql = """
        INSERT INTO characters
        (name, save_id, type, level, max_hp, current_hp, str, dex, con, int_stat, wis, cha, position)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, player.getName());
            ps.setLong(2, saveId);
            ps.setString(3, player.getCharacterType().name());
            ps.setInt(4, player.getLevel());
            ps.setInt(5, player.getMaxHp());
            ps.setInt(6, player.getHp());

            ps.setInt(7, player.getOneStat(Stat.STR));
            ps.setInt(8, player.getOneStat(Stat.DEX));
            ps.setInt(9, player.getOneStat(Stat.CON));
            ps.setInt(10, player.getOneStat(Stat.INT));
            ps.setInt(11, player.getOneStat(Stat.WIS));
            ps.setInt(12, player.getOneStat(Stat.CHA));

            ps.setInt(13, player.getPosition());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next()) {
                    throw new SQLException("Failed to create character: no generated key returned.");
                }
                return rs.getLong(1);
            }
        }
    }

    /**
     * Updates an existing player character by id.
     *
     * @param characterId the persisted character id
     * @param saveId the save slot id
     * @param player the character to persist
     * @throws SQLException if the database operation fails
     */
    public void update(long characterId, long saveId, PlayerCharacter player) throws SQLException {

        String sql = """
        UPDATE characters
        SET name = ?,
            save_id = ?,
            type = ?,
            level = ?,
            max_hp = ?,
            current_hp = ?,
            str = ?,
            dex = ?,
            con = ?,
            int_stat = ?,
            wis = ?,
            cha = ?,
            position = ?
        WHERE id = ?
        """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, player.getName());
            ps.setLong(2, saveId);
            ps.setString(3, player.getCharacterType().name());
            ps.setInt(4, player.getLevel());
            ps.setInt(5, player.getMaxHp());
            ps.setInt(6, player.getHp());

            ps.setInt(7, player.getOneStat(Stat.STR));
            ps.setInt(8, player.getOneStat(Stat.DEX));
            ps.setInt(9, player.getOneStat(Stat.CON));
            ps.setInt(10, player.getOneStat(Stat.INT));
            ps.setInt(11, player.getOneStat(Stat.WIS));
            ps.setInt(12, player.getOneStat(Stat.CHA));

            ps.setInt(13, player.getPosition());
            ps.setLong(14, characterId);

            ps.executeUpdate();
        }
    }

    /**
     * Loads a player character and its associated save slot.
     *
     * @param id the name of the character to load
     * @return an Optional containing the loaded game state
     * @throws SQLException if the database query fails
     */
    public Optional<LoadedGame> load(Long id) throws SQLException {


        String sql = "SELECT * FROM characters WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return Optional.empty();
                }
                long saveId = rs.getLong("save_id");

                String name = rs.getString("name");

                CharacterType type = CharacterType.valueOf(rs.getString("type"));
                int level = rs.getInt("level");
                int maxHp = rs.getInt("max_hp");
                int hp = rs.getInt("current_hp");

                Stats stats = new Stats();
                stats.set(Stat.STR, rs.getInt("str"));
                stats.set(Stat.DEX, rs.getInt("dex"));
                stats.set(Stat.CON, rs.getInt("con"));
                stats.set(Stat.INT, rs.getInt("int_stat"));
                stats.set(Stat.WIS, rs.getInt("wis"));
                stats.set(Stat.CHA, rs.getInt("cha"));

                PlayerCharacter player = switch (type) {
                    case WARRIOR -> new Warrior(level, name, stats, maxHp, hp);
                    case WIZARD -> new Wizard(level, name, stats, maxHp, hp);
                };

                player.setPosition(rs.getInt("position"));

                return Optional.of(new LoadedGame(id, saveId, player));
            }
        }
    }

    /**
     * Retrieves a list of all stored characters.
     *
     * <p>Each entry contains the character name, class type,
     * and level for display in the load menu.</p>
     *
     * @return a list of character summaries
     * @throws SQLException if the query fails
     */
    public List<CharacterSummary> listCharacters() throws SQLException {

        String sql = "SELECT id, name, type, level FROM characters ORDER BY id";

        List<CharacterSummary> characters = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Long id = rs.getLong("id");
                String name = rs.getString("name");
                CharacterType type = CharacterType.valueOf(rs.getString("type"));
                int level = rs.getInt("level");

                characters.add(new CharacterSummary(id, name, type, level));
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


}