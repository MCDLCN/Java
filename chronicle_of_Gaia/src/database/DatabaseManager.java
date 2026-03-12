package database;

import config.DbConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Centralizes database connection creation.
 */
public final class DatabaseManager {

    private static final DbConfig CONFIG = new DbConfig();

    static {
        try {
            Class.forName(CONFIG.getDriver());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Failed to load JDBC driver.", e);
        }
    }

    private DatabaseManager() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                CONFIG.getUrl(),
                CONFIG.getUser(),
                CONFIG.getPassword()
        );
    }
}