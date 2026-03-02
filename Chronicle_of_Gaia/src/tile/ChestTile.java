package tile;

import entities.Creature;
import Utilities.Console;

public class ChestTile implements Tile {

    @Override
    public String describe() {
        return "You see a chest.";
    }

    @Override
    public void onEnter(Creature character) {
        Console.print("You found loot!", Console.ConsoleColor.YELLOW);
    }
}