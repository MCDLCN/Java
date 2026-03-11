package model.entities.evilaaaneighbours;

import main_logic.enums.Stat;
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

    /**
     * Rolls this enemy's default attack damage.
     *
     * @return rolled damage dealt by the enemy's basic attack
     */
    public abstract int rollDamage(boolean critical);

    /**
     * Returns the ability modifier used for attack rolls.
     */
    public int getAttackModifier() {
        return stats.getModifier(Stat.STR);
    }

    public abstract int getXpReward();
}