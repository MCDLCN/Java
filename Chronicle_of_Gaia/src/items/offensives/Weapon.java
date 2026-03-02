package items.offensives;

import crawlinmydungeon.dice.DamageDice;

/**
 * Weapon class.
 */
public class Weapon extends OffensiveEquipement{

    /**
     * Weapon.
     *
     * @param name name.
     * @param damage damage.
     */
    public Weapon(String name, DamageDice damage) {
        super(name, damage);
    }
}
