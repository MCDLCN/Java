package entities;

import Utilities.Console;
import crawlinmydungeon.enums.Stat;
import items.armours.Armour;

import java.util.HashMap;
import java.util.Map;

/**
 * this is the abstract class for all characters, PC and NPC
 */
public abstract class Creature {
    /**
     * The stats of the character will be here
     */
    private Map<Stat, Integer> stats;
    /**
     * Those are the HP of the character
     */
    private int hp;
    /**
     * The max HP of the character, for healing
     */
    private int maxHp;
    /**
     * This will be the Armour Class of the character
     */
    private int ac;
    /**
     * This is the name of the character
     */
    private String name;


    /**
     * Constructor for the abstract class
     * @param hp How much health point the character has
     * @param ac What's the Armour Class of the character
     * @param name Who is this?
     */
    public Creature(int hp, int ac, String name, Map<Stat, Integer> stats) {
        this.hp = hp;
        this.maxHp = hp;
        this.ac = ac;
        this.name = name;
        this.stats = stats;
    }

    // ===== Getters =====

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAc() {
        return ac;
    }

    public String getName() {
        return name;
    }

    public Map<Stat, Integer> getStats() {
        return stats;
    }

    public int getOneStat(Stat stat){
        return  stats.get(stat);
    }

    // ===== Setters =====

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

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setStats(Map<Stat, Integer> stats) {
        this.stats = stats;
    }

    public void setOneStat(Stat stat, int value) {
        this.stats.put(stat, value);
    }

    // ===== Game Logic Methods =====

    /**
     * Change the health of the character
     * @param change positive if healing, negative if damage
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
     * Returns the modifier for a given stat
     * @param stat the name of the stat
     * @return the modifier for the stat
     */
    protected int getStatModifier(Stat stat) {
        int value = stats.getOrDefault(stat, 10);
        return (value - 10) / 2;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public abstract String getTypeName();

    @Override
    public String toString(){
        return "You are a "+this.getTypeName()+" your name's "+this.getName();
    }
}
