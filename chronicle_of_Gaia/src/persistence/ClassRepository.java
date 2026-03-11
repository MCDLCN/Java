package persistence;

import dto.CharacterClassData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClassRepository {

    public ClassRepository() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS classes (
              id   BIGINT AUTO_INCREMENT PRIMARY KEY,
              name VARCHAR(64) NOT NULL UNIQUE
            )
            """);
        }

        seedDefaults();
    }

    /**
     * Returns every available class from the catalog.
     *
     * @return available classes ordered by id
     * @throws SQLException if loading fails
     */
    public List<CharacterClassData> findAll() throws SQLException {
        String sql = "SELECT id, name FROM classes ORDER BY id";
        List<CharacterClassData> classes = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                classes.add(new CharacterClassData(
                        rs.getLong("id"),
                        rs.getString("name")
                ));
            }
        }

        return classes;
    }

    /**
     * Returns one class by its id.
     *
     * @param classId persisted class id
     * @return class metadata when found
     * @throws SQLException if loading fails
     */
    public Optional<CharacterClassData> findById(long classId) throws SQLException {
        String sql = "SELECT id, name FROM classes WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, classId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }

                return Optional.of(new CharacterClassData(
                        rs.getLong("id"),
                        rs.getString("name")
                ));
            }
        }
    }

    /**
     * Returns one class by its name.
     *
     * @param name class name
     * @return class metadata when found
     * @throws SQLException if loading fails
     */
    public Optional<CharacterClassData> findByName(String name) throws SQLException {
        String sql = "SELECT id, name FROM classes WHERE LOWER(name) = LOWER(?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }

                return Optional.of(new CharacterClassData(
                        rs.getLong("id"),
                        rs.getString("name")
                ));
            }
        }
    }

    /**
     * Seeds the default built-in classes when missing.
     *
     * @throws SQLException if seeding fails
     */
    private void seedDefaults() throws SQLException {
        insertIfMissing("Warrior");
        insertIfMissing("Wizard");
    }

    /**
     * Inserts one class row only when it does not already exist.
     *
     * @param name class name
     * @throws SQLException if insert fails
     */
    private void insertIfMissing(String name) throws SQLException {
        String sql = "INSERT INTO classes (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM classes WHERE LOWER(name) = LOWER(?))";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, name);
            ps.executeUpdate();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/chronicle_of_gaia",
                "gaia",
                "Gaia2026!"
        );
    }
}
