package model.items.consummables;

import main_logic.dice.DamageDice;
import main_logic.enums.ItemCode;
import model.entities.Creature;

/**
 * A potion intended to restore HP when consumed.
 */
public class HealingPotion extends Potion {

    /**
     * Creates a new HealingPotion instance.
     * @param name name value.
     * @param healDice healDice value.
     */
    public HealingPotion(ItemCode name, DamageDice healDice) {
        super(name, healDice);
    }

    /**
     * Heal the creature.
     * @param target target value.
     * @return Result value.
     */
    public int useOn(Creature target) {
        int heal = Roll();
        target.healthChange(+heal);
        return heal;
    }
}
