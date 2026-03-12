package model.entities.classes;

import main_logic.dice.DamageDice;
import main_logic.dice.Die;
import main_logic.enums.CharacterClass;
import main_logic.enums.ItemType;
import main_logic.enums.Stat;
import model.entities.Stats;
import model.inventory.InventoryEntry;
import model.items.Item;
import model.items.offensives.Weapon;
import model.items.scrolls.Scroll;
import utilities.Console;

/**
 * Magic-focused player character class.
 */
public class Wizard extends PlayerCharacter {

    private static final int HIT_DIE = 6;
    private static final DamageDice FIREBOLT_DAMAGE = new DamageDice(1, Die.D10);

    /**
     * Constructor used when creating a new Wizard.
     */
    public Wizard(int level, String name, Stats stats, long classId) {
        super(level, name, stats, HIT_DIE, classId, CharacterClass.WIZARD);
        this.getAllowedItems().add(ItemType.SCROLL);
        this.getAllowedItems().add(ItemType.ARMOUR);
        this.getAllowedItems().add(ItemType.SHIELD);
    }

    /**
     * Constructor used when loading a Wizard from persistence.
     */
    public Wizard(int level, String name, Stats stats, int maxHp, int hp,
                  long classId, int currentXp, int unspentStatPoints) {
        super(level, name, stats, HIT_DIE, maxHp, hp, classId, currentXp, unspentStatPoints, CharacterClass.WIZARD);
        this.getAllowedItems().add(ItemType.SCROLL);
        this.getAllowedItems().add(ItemType.ARMOUR);
        this.getAllowedItems().add(ItemType.SHIELD);
    }

    @Override
    public boolean canEquip(Item item){
        return getAllowedItems().contains(item.getType());
    }

    @Override
    public void equipWeapon(Weapon weapon) {
        throw new UnsupportedOperationException("Wizards cannot equip weapons.");
    }

    @Override
    public int getAttackModifier() {
        return getStats().getModifier(Stat.INT);
    }

    @Override
    public int rollAttackDamage(boolean critical) {
        InventoryEntry equippedScroll = getInventory().getEquipped(Scroll.class).orElse(null);

        if (equippedScroll != null) {
            Scroll scroll = (Scroll) equippedScroll.getItem();
            return scroll.rollDamage(critical);
        }

        return FIREBOLT_DAMAGE.roll(critical);
    }

}
