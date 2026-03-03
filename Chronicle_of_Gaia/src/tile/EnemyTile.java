package tile;

import entities.Creature;
import Utilities.Console;

/**
 * A tile that represents an encounter with an enemy creature. Combat behavior is planned but currently implemented as a placeholder.
 */
public class EnemyTile implements Tile {

    /**
     * Enemy creature encountered on this tile.
     */
    private final Creature enemy;

    /**
     * Creates a new EnemyTile instance.
     * @param enemy enemy value.
     */
    public EnemyTile(Creature enemy) {
        this.enemy = enemy;
    }

    /**
     * Returns a short human-readable description of the tile.
     * @return Short description of this tile.
     */
    @Override
    public String describe() {
        return "An enemy appears!";
    }


    /**
     * Applies this tile's effect when a creature enters it.
     * Combat is intended to loop until the enemy dies or the player attempts to flee.
     * Fleeing is a DEX (stealth) check against the enemy's WIS (perception) check:
     * on success the player moves back 2 tiles; on failure the player loses their turn.
     * @param creature creature value.
     */
    @Override
    public void onEnter(Creature creature) {
        Console.print("THIS IS A FIGHT", Console.ConsoleColor.BRIGHT_RED);
        Console.print("You encounter a " + enemy.getName(), Console.ConsoleColor.RED);
    }
}