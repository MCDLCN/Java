package items.offensives;

import crawlinmydungeon.dice.DamageDice;

/**
 * Offensive equipement class.
 */
public abstract class OffensiveEquipement {
    /**
     * Name.
     */
    private String name;
    /**
     * Damage.
     */
    private DamageDice damage;

    /**
     * Offensive equipement.
     *
     * @param name name.
     * @param damage damage.
     */
    protected OffensiveEquipement(String name, DamageDice damage) {
        this.name = name;
        this.damage = damage;
    }
    /**
     * Get name.
     *
     * @return result.
     */
    public String getName() {
        return name;
    }
    /**
     * Get damage.
     *
     * @return result.
     */
    public DamageDice getDamage() {
        return damage;
    }

    /**
     * Roll damage.
     *
     * @return result.
     */
    public int rollDamage() {
        return damage.roll();
    }
}
