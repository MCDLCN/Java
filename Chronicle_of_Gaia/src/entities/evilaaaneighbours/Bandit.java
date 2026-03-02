package entities.evilaaaneighbours;

import entities.Character;

import java.util.HashMap;
import java.util.Map;

public class Bandit extends Character {
    public Bandit() {
        Map<String, Integer> stats = new HashMap<String, Integer>();
        stats.put("STR", 11);
        stats.put("DEX", 12);
        stats.put("CON", 12);
        stats.put("INT", 10);
        stats.put("WIS", 10);
        stats.put("CHA", 10);
        super(11,  12, "Bandit", stats);
    }
}
