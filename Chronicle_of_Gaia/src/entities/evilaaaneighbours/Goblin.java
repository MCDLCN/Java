package entities.evilaaaneighbours;

import entities.Character;

import java.util.HashMap;
import java.util.Map;

public class Goblin extends Character {
    public Goblin() {
        Map<String, Integer> stats = new HashMap<String, Integer>();
        stats.put("STR", 8);
        stats.put("DEX", 14);
        stats.put("CON", 10);
        stats.put("INT", 10);
        stats.put("WIS", 8);
        stats.put("CHA", 8);
        super(7, 15, "Goblin", stats);
    }


}
