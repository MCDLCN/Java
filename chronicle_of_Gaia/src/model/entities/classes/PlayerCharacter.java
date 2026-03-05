package model.entities.classes;

import main_logic.enums.CharacterType;
import main_logic.enums.Stat;
import model.entities.Creature;
import model.entities.Stats;
import model.entities.equipment.Equipment;
import model.items.defensives.Armour;
import model.items.defensives.Shield;
import model.items.offensives.Weapon;

/**

 * Base class for all player-controlled characters.
 *
 * <p>Handles:
 * <ul>
 * ```
 <li>Level management</li>
 ```
 * ```
 <li>Hit-die based maximum HP calculation</li>
 ```
 * ```
 <li>HP initialization for new or loaded characters</li>
 ```
 * </ul>
 *
 * <p>Subclasses only need to provide their hit die.</p>

 */
public abstract class PlayerCharacter extends Creature {

    protected Equipment equipment = new Equipment();
    protected int level;
    protected final int hitDie;
    protected int position;

    //----- Constructors -----

    /**
     * Constructor used when creating a new character.
     * maxHp is computed from level and stats.
     *
     * @param level the starting level
     * @param name the character name
     * @param stats the generated stats
     * @param hitDie the class hit die
     */
    protected PlayerCharacter(int level, String name, Stats stats, int hitDie) {
        super(name, stats,
                computeMaxHp(level, stats, hitDie),
                computeMaxHp(level, stats, hitDie));
        this.level = level;
        this.hitDie = hitDie;
    }

    /**
     * Constructor used when loading a character from persistence.
     *
     * @param level the stored character level
     * @param name the stored character name
     * @param stats the stored stats
     * @param hitDie the class hit die
     * @param maxHp the stored maximum HP
     * @param hp the stored current HP
     */
    protected PlayerCharacter(int level, String name, Stats stats, int hitDie, int maxHp, int hp) {
        super(name, stats, maxHp, hp);
        this.level = level;
        this.hitDie = hitDie;
    }


    //----- Getters -----

    /**
     * @return the character level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @return the character equipment slots
     */
    public Equipment getEquipment() {
        return equipment;
    }

    /**
     * Computes Armor Class from equipped armour, equipped shield, and DEX modifier.
     *
     * @return the computed armor class
     */
    @Override
    public int getAc() {

        int dexMod = stats.getModifier(Stat.DEX);

        Armour armour = equipment.getArmour();
        Shield shield = equipment.getShield();

        int baseAc;

        if (armour == null) {
            baseAc = 10 + dexMod;
        } else {
            baseAc = switch (armour.getType()) {
                case HEAVY -> armour.getAc();
                case MEDIUM -> armour.getAc() + Math.min(dexMod, 2);
                case LIGHT -> armour.getAc() + dexMod;
            };
        }

        int shieldBonus = (shield == null) ? 0 : shield.getAc();
        return baseAc + shieldBonus;
    }

    public int getPosition() {
        return this.position;
    }

    /**
     * Returns the character type enum used for persistence and reconstruction.
     *
     * @return the character type
     */
    public abstract CharacterType getCharacterType();



    //----- Setters -----

    /**
     * Equips armour in the armour slot.
     *
     * @param armour the armour to equip, or null to unequip
     */
    public void equipArmour(Armour armour) {
        equipment.setArmour(armour);
    }

    /**
     * Equips a shield in the shield slot.
     *
     * @param shield the shield to equip, or null to unequip
     */
    public void equipShield(Shield shield) {
        equipment.setShield(shield);
    }

    /**
     * Equips a weapon in the weapon slot.
     *
     * @param weapon the weapon to equip, or null to unequip
     */
    public void equipWeapon(Weapon weapon) {
        equipment.setWeapon(weapon);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    //----- Logic -----

    /**
     * Computes the maximum HP according to the leveling rules.
     *
     * <p>Level 1: full hit die + CON modifier.</p>
     * <p>Levels 2+: average hit die (rounded up) + CON modifier.</p>
     *
     * @param level the character level
     * @param stats the character stats
     * @param hitDie the class hit die
     * @return the computed maximum HP
     */
    protected static int computeMaxHp(int level, Stats stats, int hitDie) {

        int conMod = stats.getModifier(Stat.CON);

        int firstLevelHp = hitDie + conMod;

        int avgRoll = (hitDie + 1) / 2; // rounded-up average

        int remainingLevels = level - 1;

        return firstLevelHp + remainingLevels * (avgRoll + conMod);
    }

}

