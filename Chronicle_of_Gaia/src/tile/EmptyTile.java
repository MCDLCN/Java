package tile;

import Utilities.Console;
import entities.Creature;

public class EmptyTile implements Tile {

    @Override
    public String describe() {
        return "You only see grass around. If anything was here before the janitors cleaned up fast";
    }

    @Override
    public void onEnter(Creature player) {
        Console.print("Nothing happens...", Console.ConsoleColor.BRIGHT_GREEN);
    }
}