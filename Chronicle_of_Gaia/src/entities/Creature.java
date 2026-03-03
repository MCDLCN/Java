package entities;

import crawlinmydungeon.enums.Stat;
import items.defensives.Armour;

import java.util.Map;

/**
 * Base type for all creatures (player characters and enemies). Stores combat stats and derived values (HP, AC), and provides utility methods for modifying health and computing stat modifiers.
 */
public abstract class Creature {
    private Map<Stat, Integer> stats;
    /**
     * Current hit points.
     */
    private int hp;
    /**
     * Maximum hit points; used to clamp healing.
     */
    private int maxHp;
    /**
     * Armor Class used for determining whether attacks hit.
     */
    private int ac;
    /**
     * Display name of the creature.
     */
    private String name;


    /**
     * Creates a new Creature instance.
     * @param hp hp value.
     * @param ac ac value.
     * @param name name value.
     * @param stats stats value.
     */
    public Creature(int hp, int ac, String name, Map<Stat, Integer> stats) {
        this.hp = hp;
        this.maxHp = hp;
        this.ac = ac;
        this.name = name;
        this.stats = stats;
    }

    // ===== Getters =====

    /**
     * getHp operation.
     * @return Requested value.
     */
    public int getHp() {
        return hp;
    }

    /**
     * getMaxHp operation.
     * @return Requested value.
     */
    public int getMaxHp() {
        return maxHp;
    }

    /**
     * getAc operation.
     * @return Requested value.
     */
    public int getAc() {
        return ac;
    }

    /**
     * getName operation.
     * @return Requested value.
     */
    public String getName() {
        return name;
    }

    public Map<Stat, Integer> getStats() {
        return stats;
    }

    /**
     * getOneStat operation.
     * @param stat stat name.
     * @return Requested value.
     */
    public int getOneStat(Stat stat){
        return  stats.get(stat);
    }

    // ===== Setters =====

    /**
     * Computes and sets Armor Class based on equipped armor and the creature's dexterity modifier.
     * @param armour armour value.
     */
    public void setAc(Armour armour) {

        int dexMod = getStatModifier(Stat.DEX);

        if (armour == null) {
            this.ac = 10 + dexMod; // unarmored
            return;
        }

        switch (armour.getType()) {

            case HEAVY:
                this.ac = armour.getAc();
                break;

            case MEDIUM:
                this.ac = armour.getAc() + Math.min(dexMod, 2);
                break;

            case LIGHT:
                this.ac = armour.getAc() + dexMod;
                break;
        }
    }

    /**
     * setMaxHp operation.
     * @param maxHp maxHp value.
     */
    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    /**
     * setStats operation.
     * @param stats stats value.
     */
    public void setStats(Map<Stat, Integer> stats) {
        this.stats = stats;
    }

    /**
     * setOneStat operation.
     * @param stat stat value.
     * @param value value value.
     */
    public void setOneStat(Stat stat, int value) {
        this.stats.put(stat, value);
    }


    /**
     * setName operation.
     * @param newName newName value.
     */
    public void setName(String newName) {
        this.name = newName;
    }

    // ===== Game Logic Methods =====

    /**
     * Applies damage or healing and clamps HP to the range [0, maxHp].
     * @param change change value.
     */
    public void healthChange(int change){
        this.hp += change;
        if (this.hp > maxHp){
            this.hp = maxHp;
        }
        if (this.hp < 0){
            this.hp = 0;
        }
    }

    /**
     * Computes the D&D-style modifier for a stat value.
     * @param stat stat value.
     * @return Requested value.
     */
    protected int getStatModifier(Stat stat) {
        int value = stats.getOrDefault(stat, 10);
        return (value - 10) / 2;
    }

    /**
     * Returns the type of the creature
     * @return type value
     */
    public abstract String getTypeName();

    /**
     * Returns a compact summary string for debugging and display.
     * @return Requested value.
     */
    @Override
    public String toString(){
        return "Creature: "+this.getTypeName()+"\nHP: "+this.getHp()+"/"+this.getMaxHp()+"\nAC: "+this.getAc()+"\nName: "+this.getName();
    }
}