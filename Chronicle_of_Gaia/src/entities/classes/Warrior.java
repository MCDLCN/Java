package entities.classes;

import crawlinmydungeon.enums.Stat;
import entities.Creature;

import java.util.Map;

/**
 * Warrior class.
 */
public class Warrior extends Creature {
    /**
     * Warrior.
     *
     * @param HP hp.
     * @param name name.
     * @param stats stats.
     */
    public Warrior(int HP, String name, Map<Stat, Integer> stats) {
        super(HP, 10, name, stats);
        this.setMaxHp(this.calculateHP());
    }
    @Override
    /**
     * Get type name.
     *
     * @return result.
     */
    public String getTypeName() {
        return "Warrior";
    }

    /**
     * Get max hit die.
     *
     * @return result.
     */
    protected int getMaxHitDie() {
        return 10;
    }

    /**
     * Calculate hp.
     *
     * @return result.
     */
    protected int calculateHP(){
        return this.getStatModifier(Stat.CON)+this.getMaxHitDie();
    }
}
