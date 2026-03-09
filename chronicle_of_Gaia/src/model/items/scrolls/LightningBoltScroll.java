package model.items.scrolls;

import main_logic.dice.DamageDice;
import main_logic.dice.Die;
import main_logic.enums.ItemCode;

/**
 * Scroll version of lightning bolt.
 */
public class LightningBoltScroll extends Scroll {

    private static final int DEFAULT_USES = 3;

    public LightningBoltScroll() {
        super(ItemCode.LIGHTNING_BOLT_SCROLL, new DamageDice(8, Die.D6), DEFAULT_USES);
    }

    public LightningBoltScroll(int usesRemaining) {
        super(ItemCode.LIGHTNING_BOLT_SCROLL, new DamageDice(8, Die.D6), usesRemaining);
    }
}
