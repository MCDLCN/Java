package model.entities.classes;

import main_logic.dice.DamageDice;
import main_logic.dice.Die;
import main_logic.enums.Stat;
import model.entities.Stats;
import model.inventory.InventoryEntry;
import model.items.Item;
import model.items.offensives.Weapon;
import model.items.scrolls.Scroll;

/**
 * Durable melee-focused player character class.
 */
public class Warrior extends PlayerCharacter {

    private static final int HIT_DIE = 10;
    private static final DamageDice DEFAULT_CLUB_DAMAGE = new DamageDice(1, Die.D4);

    /**
     * Constructor used when creating a new Warrior.
     */
    public Warrior(int level, String name, Stats stats, long classId, String className) {
        super(level, name, stats, HIT_DIE, classId, className);
    }

    /**
     * Constructor used when loading a Warrior from persistence.
     */
    public Warrior(int level, String name, Stats stats, int maxHp, int hp,
                   long classId, String className, int currentXp, int unspentStatPoints) {
        super(level, name, stats, HIT_DIE, maxHp, hp, classId, className, currentXp, unspentStatPoints);
    }

    @Override
    public String getTypeName() {
        return getClassName();
    }

    @Override
    public boolean canEquip(Item item){
        if (item instanceof Weapon){
            return true;
        }
        if (item instanceof Scroll){
            return false;
        }
        return true;
    }

    @Override
    public int getAttackModifier() {
        return getStats().getModifier(Stat.STR);
    }


    @Override
    public int rollAttackDamage(boolean critical) {
        InventoryEntry equippedWeapon = getInventory().getEquipped(Weapon.class).orElse(null);

        int baseDamage;
        if (equippedWeapon != null) {
            Weapon weapon = (Weapon) equippedWeapon.getItem();
            baseDamage = weapon.rollDamage(critical);
        } else {
            baseDamage = DEFAULT_CLUB_DAMAGE.roll(critical);
        }

        return baseDamage + getStats().getModifier(Stat.STR);
    }

}
