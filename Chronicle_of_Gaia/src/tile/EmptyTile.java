package tile;

import Utilities.Console;
import entities.Creature;

/**
 * Empty tile class.
 */
public class EmptyTile implements Tile {

    @Override
    /**
     * Describe.
     *
     * @return result.
     */
    public String describe() {
        return "You only see grass around. If anything was here before the janitors cleaned up fast";
    }

    @Override
    /**
     * On enter.
     *
     * @param player player.
     */
    public void onEnter(Creature player) {
        Console.print("Nothing happens...", Console.ConsoleColor.BRIGHT_GREEN);
    }
}