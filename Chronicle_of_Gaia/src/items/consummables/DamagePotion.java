package items.consummables;

import crawlinmydungeon.dice.DamageDice;
import entities.Creature;

/**
 * Damage potion class.
 */
public class DamagePotion extends Potion {

    /**
     * Damage potion.
     *
     * @param name name.
     * @param usesRemaining uses remaining.
     * @param damageDice damage dice.
     */
    public DamagePotion(String name, int usesRemaining, DamageDice damageDice) {
        super(name, usesRemaining, damageDice);
    }

    /**
     * Use on.
     *
     * @param target target.
     *
     * @return result.
     */
    public int useOn(Creature target) {
        int dmg = consumeAndRoll();
        target.healthChange(-dmg);
        return dmg;
    }
}
