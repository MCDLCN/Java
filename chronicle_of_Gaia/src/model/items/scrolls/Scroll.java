package model.items.scrolls;

import main_logic.dice.DamageDice;
import main_logic.enums.ItemCode;
import model.items.Item;

/**
 * Base type for magical scrolls with limited uses.
 */
public abstract class Scroll extends Item {

    private final DamageDice damage;
    private int usesRemaining;

    protected Scroll(ItemCode code, DamageDice damage, int usesRemaining) {
        super(code);
        this.damage = damage;
        this.usesRemaining = usesRemaining;
    }

    public DamageDice getDamage() {
        return damage;
    }

    public int rollDamage() {
        return damage.roll();
    }

    public int getUsesRemaining() {
        return usesRemaining;
    }

    public boolean canUse() {
        return usesRemaining > 0;
    }

    public void consumeUse() {
        if (!canUse()) {
            throw new IllegalStateException("No uses remaining on this scroll.");
        }
        usesRemaining--;
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
