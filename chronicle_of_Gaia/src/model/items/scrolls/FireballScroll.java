package model.items.scrolls;

import main_logic.dice.DamageDice;
import main_logic.dice.Die;
import main_logic.enums.ItemCode;

/**
 * Scroll version of fireball.
 */
public class FireballScroll extends Scroll {

    private static final int DEFAULT_USES = 3;

    public FireballScroll() {
        super(ItemCode.FIREBALL_SCROLL, new DamageDice(8, Die.D6), DEFAULT_USES);
    }

    public FireballScroll(int usesRemaining) {
        super(ItemCode.FIREBALL_SCROLL, new DamageDice(8, Die.D6), usesRemaining);
    }
}
