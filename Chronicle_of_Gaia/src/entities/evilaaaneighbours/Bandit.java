package entities.evilaaaneighbours;

import crawlinmydungeon.enums.Stat;
import entities.Creature;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class Bandit extends Creature {
    public Bandit() {
        Map<Stat, Integer> stats = new EnumMap<>(Stat.class);
        stats.put(Stat.STR, 11);
        stats.put(Stat.DEX, 12);
        stats.put(Stat.CON, 12);
        stats.put(Stat.INT, 10);
        stats.put(Stat.WIS, 10);
        stats.put(Stat.CHA, 10);
        super(11,  12, "Bandit", stats);
    }
    @Override
    public String getTypeName() {
        return "Bandit";
    }
}
