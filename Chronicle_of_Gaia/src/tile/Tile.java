package tile;

import entities.Creature;

/**
 * Defines a board tile: a short description and an effect triggered when a creature enters the tile.
 */
public interface Tile {

    /**
     * Returns a short human-readable description of the tile.
     * @return Short description of this tile.
     */
    String describe();

    /**
     * Applies this tile's effect when a creature enters it.
     * @param creature creature value.
     */
    void onEnter(Creature creature);
}
