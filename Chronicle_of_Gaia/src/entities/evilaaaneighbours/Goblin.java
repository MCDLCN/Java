package entities.evilaaaneighbours;

import crawlinmydungeon.enums.Stat;
import entities.Creature;

import java.util.EnumMap;
import java.util.Map;

/**
 * A basic enemy creature used for early encounters.
 */
public class Goblin extends Creature {
    /**
     * Creates a new Goblin instance.
     */
    public Goblin() {
        Map<Stat, Integer> stats = new EnumMap<>(Stat.class);
        stats.put(Stat.STR, 8);
        stats.put(Stat.DEX, 14);
        stats.put(Stat.CON, 10);
        stats.put(Stat.INT, 10);
        stats.put(Stat.WIS, 8);
        stats.put(Stat.CHA, 8);
        super(7, 15, "Goblin", stats);
    }


    /**
     * Returns the display name of this creature type/class.
     * @return Requested value.
     */
    @Override
    public String getTypeName() {
        return "Goblin";
    }
}