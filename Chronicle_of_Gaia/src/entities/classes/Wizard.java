package entities.classes;

import crawlinmydungeon.enums.Stat;
import entities.Creature;

import java.util.Map;

/**
 * Player character class focused on magic. Uses a smaller hit die to compute maximum HP.
 */
public class Wizard extends Creature {
    /**
     * Hit die value used to calculate the max HP
     */
    private final int hitDie = 8;

    /**
     * Creates a new Wizard instance.
     * @param HP HP value.
     * @param name name value.
     * @param stats stats value.
     */
    public Wizard(int HP, String name, Map<Stat, Integer> stats) {
        super(HP, 10, name, stats);
        this.setMaxHp(this.calculateHP());
        this.setHp(this.getMaxHp());
    }
    /**
     * Returns the display name of this creature type/class.
     * @return Requested value.
     */
    @Override
    public String getTypeName() {
        return "Wizard";
    }

    protected int calculateHP(){
        return this.getStatModifier(Stat.CON)+this.hitDie;
    }
}