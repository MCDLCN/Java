package model.items.offensives;

import main_logic.dice.DamageDice;
import main_logic.enums.ItemCode;

/**
 * Offensive equipment item that contributes to damage output.
 */
public class Weapon extends OffensiveEquipment {

    /**
     * Creates a new Weapon instance.
     * @param name name value.
     * @param damage damage value.
     */
    public Weapon(ItemCode name, DamageDice damage) {
        super(name, damage);
    }
}