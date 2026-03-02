package tile;

import entities.Creature;
import Utilities.Console;

/**
 * Chest tile class.
 */
public class ChestTile implements Tile {

    @Override
    /**
     * Describe.
     *
     * @return result.
     */
    public String describe() {
        return "You see a chest.";
    }

    @Override
    /**
     * On enter.
     *
     * @param character character.
     */
    public void onEnter(Creature character) {
        Console.print("You found loot!", Console.ConsoleColor.YELLOW);
    }
}