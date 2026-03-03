package items.consummables;

import crawlinmydungeon.dice.DamageDice;
import entities.Creature;

/**
 * A potion intended to deal damage when used.
 */
public class DamagePotion extends Potion {

    /**
     * Creates a new DamagePotion instance.
     * @param name name value.
     * @param usesRemaining usesRemaining value.
     * @param damageDice damageDice value.
     */
    public DamagePotion(String name, int usesRemaining, DamageDice damageDice) {
        super(name, usesRemaining, damageDice);
    }

    /**
     * Damage the creature.
     * @param target target value.
     * @return Result value.
     */
    public int useOn(Creature target) {
        int dmg = consumeAndRoll();
        target.healthChange(-dmg);
        return dmg;
    }
}