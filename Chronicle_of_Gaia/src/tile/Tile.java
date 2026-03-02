package tile;

import entities.Creature;

/**
 * Tile interface.
 */
public interface Tile {

    /**
     * Describe.
     *
     * @return result.
     */
    String describe();

    /**
     * On enter.
     *
     * @param creature creature.
     */
    void onEnter(Creature creature);
}