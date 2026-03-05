package model.entities.evilaaaneighbours;

import model.entities.Creature;
import model.entities.Stats;

/**
 * Base class for enemies with fixed AC and max HP.
 */
public abstract class Enemy extends Creature {

    protected final int ac;

    protected Enemy(String name, Stats stats, int maxHp, int ac, int hp) {
        super(name, stats, maxHp, hp);
        this.ac = ac;
    }

    @Override
    public int getAc() {
        return ac;
    }
}