package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
public  class CharacterRepository {
    public CharacterRepository() throws SQLException {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/chronicle_of_gaia",
                "gaia",
                "Gaia2026!"
        );
             Statement stmt = conn.createStatement()) {

            String sql = """
            CREATE TABLE IF NOT EXISTS characters (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(50) UNIQUE NOT NULL,
                class VARCHAR(20) NOT NULL,
                life_points INT NOT NULL,
                strength INT NOT NULL
            );
        """;

            stmt.execute(sql);
        }
    }
}