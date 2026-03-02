package entities.evilaaaneighbours;

import crawlinmydungeon.enums.Stat;
import entities.Creature;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class Goblin extends Creature {
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


    @Override
    public String getTypeName() {
        return "Goblin";
    }
}
