package model.items.consumables;

import main_logic.dice.DamageDice;
import main_logic.dice.Die;
import main_logic.enums.ItemCode;
import model.entities.Creature;

/**
 * Standard healing potion based on the SRD potion of healing.
 */
public class StandardHealingPotion extends HealingPotion {

    private static final int FLAT_BONUS = 2;

    public StandardHealingPotion() {
        super(ItemCode.STANDARD_HEALING_POTION, new DamageDice(2, Die.D4));
    }

    @Override
    public int useOn(Creature target) {
        int heal = getEffectDice().roll() + FLAT_BONUS;
        target.healthChange(heal);
        return heal;
    }
}
