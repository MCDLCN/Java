package items.consummables;

import crawlinmydungeon.dice.DamageDice;
import entities.Creature;

/**
 * Healing potion class.
 */
public class HealingPotion extends Potion {

    /**
     * Healing potion.
     *
     * @param name name.
     * @param usesRemaining uses remaining.
     * @param healDice heal dice.
     */
    public HealingPotion(String name, int usesRemaining, DamageDice healDice) {
        super(name, usesRemaining, healDice);
    }

    /**
     * Use on.
     *
     * @param target target.
     *
     * @return result.
     */
    public int useOn(Creature target) {
        int heal = consumeAndRoll();
        target.healthChange(+heal);
        return heal;
    }
}