package entities;

import items.armours.Armour;

import java.util.HashMap;
import java.util.Map;

/**
 * this is the abstract class for all characters, PC and NPC
 */
public abstract class Character {
    /**
     * The stats of the character will be here
     */
    private Map<String, Integer> stats = new HashMap<String, Integer>();
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
    public Character(int hp, int ac, String name,  Map<String, Integer> stats) {
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

    public Map<String, Integer> getStats() {
        return stats;
    }

    // ===== Game Logic Methods =====

    /**
     * Change the health of the character
     * @param change positive if healing, negative if damage
     */
    private void healthChange(int change){
        this.hp += change;
        if (this.hp > maxHp){
            this.hp = maxHp;
        }
        if (this.hp < 0){
            this.hp = 0;
        }
    }

    // ===== Setters =====

    public void setAc(Armour... armor){

    }
}
