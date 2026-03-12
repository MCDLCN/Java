package model.entities.classes;

import main_logic.dice.Dice;
import main_logic.enums.CharacterClass;
import main_logic.enums.ItemType;
import main_logic.enums.Stat;
import model.entities.Creature;
import model.entities.Stats;
import model.entities.equipment.Equipment;
import model.inventory.Inventory;
import model.items.Item;
import model.items.defensives.Armour;
import model.items.defensives.Shield;
import model.items.offensives.Weapon;
import model.items.scrolls.Scroll;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;


/**
 * Base class for all player-controlled characters.
 *
 * <p>Handles:
 * <ul>
 * <li>Level management</li>
 * <li>Hit-die based maximum HP calculation</li>
 * <li>HP initialization for new or loaded characters</li>
 * </ul>
 *
 * <p>Subclasses only need to provide their hit die and class metadata.</p>
 */
public abstract class PlayerCharacter extends Creature {

    protected Equipment equipment = new Equipment();
    protected int level;
    private int currentXp;
    protected final int hitDie;
    protected int position;
    private final Inventory inventory = new Inventory();
    private final long classId;
    private final EnumMap<Stat, Integer> trainingProgress = new EnumMap<>(Stat.class);
    protected int unspentStatPoints;
    private static final int MAX_LEVEL = 20;
    private final CharacterClass characterClass;
    private Set<ItemType> allowedItems = new HashSet<>();

    //----- Constructors -----

    /**
     * Constructor used when creating a new character.
     * maxHp is computed from level and stats.
     *
     * @param level the starting level
     * @param name the character name
     * @param stats the generated stats
     * @param hitDie the class hit die
     * @param classId persisted class id
     * @param characterClass persisted class name
     */
    protected PlayerCharacter(int level, String name, Stats stats, int hitDie, long classId, CharacterClass characterClass) {
        super(name, stats,
                computeMaxHp(level, stats, hitDie),
                computeMaxHp(level, stats, hitDie));
        this.level = level;
        this.hitDie = hitDie;
        this.classId = classId;
        this.currentXp = 0;
        this.unspentStatPoints = 0;
        this.characterClass = characterClass;
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
     * @param classId persisted class id
     * @param characterClass persisted class name
     */
    protected PlayerCharacter(int level, String name, Stats stats, int hitDie, int maxHp, int hp,
                              long classId, int currentXp, int unspentStatPoints, CharacterClass characterClass) {
        super(name, stats, maxHp, hp);
        this.level = level;
        this.hitDie = hitDie;
        this.classId = classId;
        this.characterClass = characterClass;
        this.currentXp = currentXp;
        this.unspentStatPoints = unspentStatPoints;
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
            baseAc = switch (armour.getArmourType()) {
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

    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Returns the persisted class id used for character persistence.
     *
     * @return the class id
     */
    public long getClassId() {
        return classId;
    }

    /**
     * Returns the persisted class name used for display and reconstruction.
     *
     * @return the class name
     */
    public CharacterClass getCharacterClass() {
        return this.characterClass;
    }

    @Override
    public String getTypeName(){ return this.characterClass.name();}

    public abstract boolean canEquip(Item item);

    /**
     * Returns the current training progress for the given stat.
     */
    public int getOneTrainingProgress(Stat stat) {
        return trainingProgress.getOrDefault(stat, 0);
    }

    /**
     * Returns the current training progress for all stats.
     */
    public EnumMap<Stat, Integer> getTrainingProgress() {
        return new EnumMap<>(trainingProgress);
    }

    /**
     * Returns the modifier used for attack rolls.
     */
    public abstract int getAttackModifier();


    /**
     * Rolls the damage of the currently used attack.
     */
    public abstract int rollAttackDamage(boolean critical);

    public int getCurrentXp() {
        return currentXp;
    }

    public int getUnspentStatPoints() {
        return unspentStatPoints;
    }

    public Set<ItemType> getAllowedItems() {
        return allowedItems;
    }

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

    public void equipScroll(Scroll scroll) {
        equipment.setScroll(scroll);
    }

    public void setTrainingProgress (EnumMap<Stat, Integer> trainingProgress){
        this.trainingProgress.clear();
        if (trainingProgress != null) {
            this.trainingProgress.putAll(trainingProgress);
        }
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

        int avgRoll = (hitDie + 1) / 2;

        int remainingLevels = level - 1;

        return firstLevelHp + remainingLevels * (avgRoll + conMod);
    }

    /**
     * Applies one training action to the chosen stat.
     *
     * <p>Each stat must be trained twice to gain +1.</p>
     *
     * @param stat chosen stat
     * @return if the training was complete, in progress or the stat is already at cap
     */
    public TrainingResult train(Stat stat) {
        if (stat == null) {
            return TrainingResult.CAPPED;
        }

        if (getOneStat(stat) >= 20) {
            trainingProgress.put(stat, 0);
            return TrainingResult.CAPPED;
        }

        int progress = trainingProgress.getOrDefault(stat, 0) + 1;

        if (progress >= 2) {
            stats.set(stat, stats.get(stat) + 1);
            trainingProgress.put(stat, 0);
            return TrainingResult.INCREASED;
        }

        trainingProgress.put(stat, progress);
        return TrainingResult.PROGRESS;
    }

    /**
     * Heals by rolling a number of hit dice equal to the character level.
     *
     * @return total healed HP
     */
    public int rest() {
        int heal = 0;

        for (int i = 0; i < level; i++) {
            heal += Dice.roll(hitDie);
        }

        int hpBefore = getHp();
        healthChange(heal);
        return getHp() - hpBefore;
    }

    // ----- XP handler -----

    /**
     * Returns the XP required to reach the next level.
     */
    public int getXpForNextLevel() {
        if (level >= MAX_LEVEL) {
            return XP_FOR_LEVEL[MAX_LEVEL];
        }
        return XP_FOR_LEVEL[level + 1];
    }


    public int getXpRemainingToLevel() {
        return getXpForNextLevel() - currentXp;
    }

    private int determineLevelFromXp(int xp) {
        for (int level = MAX_LEVEL; level >= 1; level--) {
            if (xp >= XP_FOR_LEVEL[level]) {
                return level;
            }
        }
        return 1;
    }

    private static final int[] XP_FOR_LEVEL = {
            0,      // unused (no level 0)
            0,      // level 1
            30,
            90,
            270,
            650,
            1400,
            2300,
            3400,
            4800,
            6400,
            8500,
            10000,
            12000,
            14000,
            16500,
            19500,
            22500,
            26500,
            30500,
            35500
    };

    /**
     * Awards XP and applies all crossed level-ups.
     *
     * @param amount awarded XP
     * @return number of levels gained
     */
    public int gainXp(int amount) {
        if (amount <= 0) {
            return 0;
        }

        int oldLevel = this.level;
        this.currentXp += amount;

        int targetLevel = determineLevelFromXp(this.currentXp);

        while (this.level < targetLevel) {
            applyLevelUp();
        }

        return this.level - oldLevel;
    }

    /**
     * Applies one level-up:
     * increases level, recalculates max HP, heals by gained max HP,
     * and grants 2 unspent stat points.
     */
    private void applyLevelUp() {
        int oldMaxHp = this.maxHp;

        this.level++;
        this.maxHp = computeMaxHp(this.level, this.stats, this.hitDie);

        int gainedHp = this.maxHp - oldMaxHp;
        setHp(this.hp + gainedHp);

        this.unspentStatPoints += 2;
    }

    /**
     * Spends one stat point on the chosen stat.
     *
     * @param stat stat to increase
     * @return true if the point was spent
     */
    public boolean spendStatPoint(Stat stat) {
        if (stat == null || unspentStatPoints <= 0) {
            return false;
        }

        int currentValue = stats.get(stat);
        if (currentValue >= 20) {
            return false;
        }

        int oldMaxHp = this.maxHp;

        stats.set(stat, currentValue + 1);
        unspentStatPoints--;

        this.maxHp = computeMaxHp(this.level, this.stats, this.hitDie);
        int gainedHp = this.maxHp - oldMaxHp;

        if (gainedHp > 0) {
            setHp(this.hp + gainedHp);
        }

        return true;
    }

    // ----- Helpers -----

    /**
     * Builds the full character inspection view for out-of-combat menus.
     *
     * @return formatted character summary
     */
    public String characterInspection() {
        return "Name: " + getName()
                + "\nClass: " + getCharacterClass().name()
                + "\nLevel: " + getLevel()
                + "\nHP: " + getHp() + "/" + getMaxHp()
                + "\nXP: " + getCurrentXp() + " / " + getXpForNextLevel()
                + "\nSTR: " + getOneStat(Stat.STR)
                + "\nDEX: " + getOneStat(Stat.DEX)
                + "\nCON: " + getOneStat(Stat.CON)
                + "\nINT: " + getOneStat(Stat.INT)
                + "\nWIS: " + getOneStat(Stat.WIS)
                + "\nCHA: " + getOneStat(Stat.CHA);
    }

    // ----- Enums -----

    public enum TrainingResult {
        PROGRESS,
        INCREASED,
        CAPPED
    }

}
