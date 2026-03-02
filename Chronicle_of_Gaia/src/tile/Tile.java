package tile;

import entities.Creature;

public interface Tile {

    String describe();

    void onEnter(Creature creature);
}