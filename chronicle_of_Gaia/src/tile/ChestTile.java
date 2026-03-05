package tile;

import main_logic.Game;
import main_logic.enums.EncounterResult;
import utilities.Console;

/**
 * A tile that represents loot. Intended to draw a random item from an item pool and place it into the player's inventory.
 */
public class ChestTile implements Tile {

    /**
     * Returns a short human-readable description of the tile.
     * @return Short description of this tile.
     */
    @Override
    public String describe() {
        return "You see a chest.";
    }

    /**
     * Applies this tile's effect when a creature enters it.
     * Intended behavior: draw a random item from an item pool and place it into the player's inventory.
     * Potions are stored in inventory and may be consumed later.
     * @param game Current state of the game.
     */
    @Override
    public EncounterResult onEnter(Game game) {
        Console.print("You found loot!", Console.ConsoleColor.YELLOW);
        return EncounterResult.NONE;
    }
}
