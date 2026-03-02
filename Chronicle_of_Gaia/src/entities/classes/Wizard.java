package entities.classes;

import crawlinmydungeon.enums.Stat;
import entities.Creature;

import java.util.Map;

public class Wizard extends Creature {
    public Wizard(int HP, String name, Map<Stat, Integer> stats) {
        super(HP, 10, name, stats);
    }
    @Override
    public String getTypeName() {
        return "Wizard";
    }
}
