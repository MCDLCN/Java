package model.items.consumables;

import main_logic.dice.DamageDice;
import main_logic.enums.ItemCode;
import model.entities.Creature;

/**
 * A potion intended to deal damage when used.
 */
public class DamagePotion extends Potion {

    /**
     * Creates a new DamagePotion instance.
     * @param name name value.
     * @param damageDice damageDice value.
     */
    public DamagePotion(ItemCode name, DamageDice damageDice) {
        super(name, damageDice);
    }

    /**
     * Damage the creature.
     * @param target target value.
     * @return Result value.
     */
    public int useOn(Creature target) {
        int dmg = Roll();
        target.healthChange(-dmg);
        return dmg;
    }
}