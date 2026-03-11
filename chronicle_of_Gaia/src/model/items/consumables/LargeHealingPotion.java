package model.items.consumables;

import main_logic.dice.DamageDice;
import main_logic.dice.Die;
import main_logic.enums.ItemCode;
import model.entities.Creature;

/**
 * Large healing potion based on the SRD greater healing potion.
 */
public class LargeHealingPotion extends HealingPotion {

    private static final int FLAT_BONUS = 4;

    public LargeHealingPotion() {
        super(ItemCode.LARGE_HEALING_POTION, new DamageDice(4, Die.D4));
    }

    @Override
    public int useOn(Creature target) {
        int heal = getEffectDice().roll() + FLAT_BONUS;
        target.healthChange(heal);
        return heal;
    }
}
