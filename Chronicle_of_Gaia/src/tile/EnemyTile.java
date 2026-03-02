package tile;

import entities.Creature;
import Utilities.Console;

/**
 * Enemy tile class.
 */
public class EnemyTile implements Tile {

    /**
     * Enemy.
     */
    private final Creature enemy;

    /**
     * Enemy tile.
     *
     * @param enemy enemy.
     */
    public EnemyTile(Creature enemy) {
        this.enemy = enemy;
    }

    @Override
    /**
     * Describe.
     *
     * @return result.
     */
    public String describe() {
        return "An enemy appears!";
    }


    @Override
    /**
     * On enter.
     *
     * @param creature creature.
     */
    public void onEnter(Creature creature) {
        Console.print("THIS IS A FIGHT", Console.ConsoleColor.BRIGHT_RED);
        Console.print("You encounter a " + enemy.getName(), Console.ConsoleColor.RED);

        // Simple placeholder combat
        enemy.healthChange(-5);
        Console.print("You hit the enemy!", Console.ConsoleColor.GREEN);
    }
}
