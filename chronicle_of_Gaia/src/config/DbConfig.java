package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads database configuration from db.properties.
 */
public final class DbConfig {

    private static final String CONFIG_FILE = "/db.properties";

    private final String url;
    private final String user;
    private final String password;
    private final String driver;

    public DbConfig() {
        Properties properties = new Properties();

        try (InputStream input = DbConfig.class.getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException("Database config file not found: " + CONFIG_FILE);
            }

            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load database config.", e);
        }

        this.url = properties.getProperty("db.url");
        this.user = properties.getProperty("db.user");
        this.password = properties.getProperty("db.password");
        this.driver = properties.getProperty("db.driver");
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDriver() {
        return driver;
    }
}