package tile;

import entities.Creature;
import Utilities.Console;

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
     * @param character character who gets the item.
     */
    @Override
    public void onEnter(Creature character) {
        Console.print("You found loot!", Console.ConsoleColor.YELLOW);
    }
}
