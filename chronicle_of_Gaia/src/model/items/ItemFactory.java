package model.items;

import main_logic.dice.DamageDice;
import main_logic.dice.Die;
import main_logic.enums.ItemCode;
import model.items.consummables.DamagePotion;
import model.items.consummables.HealingPotion;
import model.items.defensives.Armour;
import model.items.defensives.Shield;
import model.items.offensives.Weapon;

/**
 * Creates runtime item objects from item codes.
 */
public final class ItemFactory {

    private ItemFactory() {
    }

    public static Item create(ItemCode code) {
        return switch (code) {
            case POTION_OF_HEALING -> new HealingPotion(code, new DamageDice(2, Die.D4));
            case POTION_OF_DAMAGE -> new DamagePotion(code,new DamageDice(2,Die.D4));

            case LONGSWORD -> new Weapon(code, new DamageDice(1,Die.D8));
            case GREATSWORD -> new Weapon(code, new DamageDice(2, Die.D6));

            case FULL_PLATE -> new Armour(20, code, "This is a plate armour", Armour.ArmourType.HEAVY);
            case BREASTPLATE -> new Armour(14, code, "This is a breastplate armour", Armour.ArmourType.MEDIUM);
            case STUDDED_LEATHER -> new Armour (12, code, "This is a leather armour", Armour.ArmourType.LIGHT);

            case SHIELD -> new Shield(2, code, "This is a shield");
        };
    }
}