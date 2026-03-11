package model.entities.classes;

import main_logic.dice.DamageDice;
import main_logic.dice.Die;
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
    public Wizard(int level, String name, Stats stats, long classId, String className) {
        super(level, name, stats, HIT_DIE, classId, className);
    }

    /**
     * Constructor used when loading a Wizard from persistence.
     */
    public Wizard(int level, String name, Stats stats, int maxHp, int hp,
                  long classId, String className, int currentXp, int unspentStatPoints) {
        super(level, name, stats, HIT_DIE, maxHp, hp, classId, className, currentXp, unspentStatPoints);
    }

    /**
     * Rolls the wizard default attack damage.
     *
     * <p>This is used when no scroll is equipped.</p>
     *
     * @return rolled firebolt damage
     */
    public int rollDefaultAttackDamage() {
        return FIREBOLT_DAMAGE.roll();
    }

    @Override
    public String getTypeName() {
        return getClassName();
    }


    @Override
    public boolean canEquip(Item item){
        if (item instanceof Weapon){
            return false;
        }
        if (item instanceof Scroll){
            return true;
        }
        return true;
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
