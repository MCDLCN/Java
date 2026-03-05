package model.entities;

import main_logic.enums.Stat;

/**
 * Base type for all creatures (player characters and enemies).
 * Stores identity + stats + HP state. AC is provided by subclasses.
 */
public abstract class Creature {

    protected Stats stats;
    protected String name;

    protected int hp;
    protected int maxHp;

    protected Creature(String name, Stats stats, int maxHp, int hp) {
        this.name = name;
        this.stats = stats;
        this.maxHp = maxHp;
        setHp(hp);
    }

    public String getName() {
        return name;
    }

    public Stats getStats() {
        return stats;
    }

    public int getOneStat(Stat stat) {
        return stats.get(stat);
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    /**
     * Sets current HP and clamps it to [0, maxHp].
     */
    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(hp, this.maxHp));
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Applies damage/heal and clamps HP to [0, maxHp].
     */
    public void healthChange(int change) {
        setHp(this.hp + change);
    }

    /**
     * Each creature type provides its AC rule.
     */
    public abstract int getAc();

    /**
     * Returns the type name.
     */
    public abstract String getTypeName();



    @Override
    public String toString() {
        return "Creature: " + getTypeName()
                + "\nHP: " + getHp() + "/" + getMaxHp()
                + "\nAC: " + getAc()
                + "\nName: " + getName();
    }
}