import main_logic.Game;

import java.sql.SQLException;

/**
 * Program entry point that starts the game.
 */
public class Main {
    /**
     * main operation.
     * @param args args value.
     */
    public static void main(String[] args) throws SQLException {
        new Game().start();
    }
}