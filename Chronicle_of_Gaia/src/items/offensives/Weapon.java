package items.offensives;

import crawlinmydungeon.dice.DamageDice;

/**
 * Offensive equipment item that contributes to damage output.
 */
public class Weapon extends OffensiveEquipment {

    /**
     * Creates a new Weapon instance.
     * @param name name value.
     * @param damage damage value.
     */
    public Weapon(String name, DamageDice damage) {
        super(name, damage);
    }
}