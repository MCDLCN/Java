package entities.classes;

import crawlinmydungeon.enums.Stat;
import entities.Creature;

import java.util.Map;

/**
 * Wizard class.
 */
public class Wizard extends Creature {
    /**
     * Wizard.
     *
     * @param HP hp.
     * @param name name.
     * @param stats stats.
     */
    public Wizard(int HP, String name, Map<Stat, Integer> stats) {
        super(HP, 10, name, stats);
    }
    @Override
    /**
     * Get type name.
     *
     * @return result.
     */
    public String getTypeName() {
        return "Wizard";
    }
}
