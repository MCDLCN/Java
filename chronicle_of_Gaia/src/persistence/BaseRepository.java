package persistence;

import database.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseRepository {


    /**
     * Opens a new database connection.
     *
     * @return a new SQL connection to the Chronicle of Gaia database
     * @throws SQLException if the connection cannot be established
     */
    protected Connection getConnection() throws SQLException {
        return DatabaseManager.getConnection();
    }
}