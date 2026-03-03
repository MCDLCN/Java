package items.consummables;

import crawlinmydungeon.dice.DamageDice;
import entities.Creature;

/**
 * A potion intended to restore HP when consumed.
 */
public class HealingPotion extends Potion {

    /**
     * Creates a new HealingPotion instance.
     * @param name name value.
     * @param usesRemaining usesRemaining value.
     * @param healDice healDice value.
     */
    public HealingPotion(String name, int usesRemaining, DamageDice healDice) {
        super(name, usesRemaining, healDice);
    }

    /**
     * Heal the creature.
     * @param target target value.
     * @return Result value.
     */
    public int useOn(Creature target) {
        int heal = consumeAndRoll();
        target.healthChange(+heal);
        return heal;
    }
}
