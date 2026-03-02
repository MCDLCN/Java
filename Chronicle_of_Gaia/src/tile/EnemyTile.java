package tile;

import entities.Creature;
import Utilities.Console;

public class EnemyTile implements Tile {

    private final Creature enemy;

    public EnemyTile(Creature enemy) {
        this.enemy = enemy;
    }

    @Override
    public String describe() {
        return "An enemy appears!";
    }


    @Override
    public void onEnter(Creature creature) {
        Console.print("THIS IS A FIGHT", Console.ConsoleColor.BRIGHT_RED);
        Console.print("You encounter a " + enemy.getName(), Console.ConsoleColor.RED);

        // Simple placeholder combat
        enemy.healthChange(-5);
        Console.print("You hit the enemy!", Console.ConsoleColor.GREEN);
    }
}
