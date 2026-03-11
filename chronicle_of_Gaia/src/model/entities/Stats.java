package model.entities;

import main_logic.enums.Stat;

import java.util.EnumMap;
import java.util.Map;

public class Stats {

    private final EnumMap<Stat, Integer> stats = new EnumMap<>(Stat.class);

    public Stats() {
        for (Stat s : Stat.values()) {
            stats.put(s, 0);
        }
    }

    public void set(Stat stat, int value) {
        stats.put(stat, value);
    }

    public int get(Stat stat) {
        return stats.getOrDefault(stat, 0);
    }

    public Map<Stat, Integer> asMap() {
        return stats;
    }

    /**
     * Computes the D&D-style modifier for a stat value.
     * @param stat stat value.
     * @return Requested value.
     */
    public int getModifier(Stat stat) {
        int value = get(stat);
        return Math.floorDiv(value - 10, 2);
    }
}