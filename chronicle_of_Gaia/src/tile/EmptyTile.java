package tile;

import main_logic.Game;
import main_logic.enums.EncounterResult;
import utilities.Console;

/**
 * An empty tile with nothing interesting happening
 */
public class EmptyTile implements Tile {

    /**
     * Returns a short human-readable description of the tile.
     * @return Short description of this tile.
     */
    @Override
    public String describe() {
        return "You only see grass around. If anything was here before the janitors cleaned up fast";
    }

    /**
     * Applies this tile's effect when a creature enters it.
     * @param game Current state of the game.
     */
    @Override
    public EncounterResult onEnter(Game game) {
        Console.print("Nothing happens...", Console.ConsoleColor.BRIGHT_GREEN);
        return EncounterResult.NONE;
    }
}
