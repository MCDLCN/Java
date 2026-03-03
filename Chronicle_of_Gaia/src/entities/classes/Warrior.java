package entities.classes;

import crawlinmydungeon.enums.Stat;
import entities.Creature;

import java.util.Map;

/**
 * Player character class focused on durability. Uses a larger hit die to compute maximum HP.
 */
public class Warrior extends Creature {
    /**
     * Hit die value used to calculate the max HP
     */
    private final int hitDie = 10;

    /**
     * Creates a new Warrior instance.
     * @param HP HP value.
     * @param name name value.
     * @param stats stats value.
     */
    public Warrior(int HP, String name, Map<Stat, Integer> stats) {
        super(HP, 10, name, stats);
        this.setMaxHp(this.calculateHP());
    }
    /**
     * Returns the display name of this creature type/class.
     * @return Requested value.
     */
    @Override
    public String getTypeName() {
        return "Warrior";
    }

    /**
     * Calculates maximum HP from class hit die and constitution modifier.
     * @return Result value.
     */
    protected int calculateHP(){
        return this.getStatModifier(Stat.CON)+this.hitDie;
    }
}