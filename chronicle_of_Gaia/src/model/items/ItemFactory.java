package model.items;

import main_logic.dice.DamageDice;
import main_logic.dice.Die;
import main_logic.enums.ItemCode;
import model.items.consumables.DamagePotion;
import model.items.consumables.HealingPotion;
import model.items.defensives.Armour;
import model.items.defensives.Shield;
import model.items.offensives.Weapon;
import model.items.scrolls.FireballScroll;
import model.items.scrolls.LightningBoltScroll;
import persistence.itemdata.ItemInstanceData;
import persistence.itemdata.ScrollInstanceData;

/**
 * Creates runtime item objects from item codes and optional persisted instance data.
 */
public final class ItemFactory {

    private static final int DEFAULT_SCROLL_USES = 3;

    private ItemFactory() {
    }

    /**
     * Creates a default runtime item for the given item code.
     *
     * @param code item code
     * @return runtime item
     */
    public static Item create(ItemCode code) {
        return create(code, null);
    }

    /**
     * Creates a runtime item for the given item code using optional persisted instance data.
     *
     * @param code item code
     * @param instanceData optional per-instance persisted data
     * @return runtime item
     */
    public static Item create(ItemCode code, ItemInstanceData instanceData) {
        return switch (code) {
            case STANDARD_HEALING_POTION -> new HealingPotion(code, new DamageDice(2, Die.D4));
            case LARGE_HEALING_POTION -> new HealingPotion(code, new DamageDice(4, Die.D4));

            case POTION_OF_DAMAGE -> new DamagePotion(code, new DamageDice(2, Die.D4));

            case FIREBALL_SCROLL -> new FireballScroll(resolveScrollUses(instanceData));
            case LIGHTNING_BOLT_SCROLL -> new LightningBoltScroll(resolveScrollUses(instanceData));

            case LONGSWORD -> new Weapon(code, new DamageDice(1, Die.D8));
            case GREATSWORD -> new Weapon(code, new DamageDice(2, Die.D6));
            case CLUB -> new Weapon(code, new DamageDice(1, Die.D4));

            case FULL_PLATE -> new Armour(20, code, "This is a plate armour", Armour.ArmourType.HEAVY);
            case BREASTPLATE -> new Armour(14, code, "This is a breastplate armour", Armour.ArmourType.MEDIUM);
            case STUDDED_LEATHER -> new Armour(12, code, "This is a leather armour", Armour.ArmourType.LIGHT);

            case SHIELD -> new Shield(2, code, "This is a shield");
        };
    }

    /**
     * Resolves scroll uses from persisted instance data.
     *
     * @param instanceData persisted scroll data
     * @return restored uses, or default uses when no data exists
     */
    private static int resolveScrollUses(ItemInstanceData instanceData) {
        if (instanceData instanceof ScrollInstanceData scrollData) {
            return scrollData.getUsesRemaining();
        }

        return DEFAULT_SCROLL_USES;
    }
}