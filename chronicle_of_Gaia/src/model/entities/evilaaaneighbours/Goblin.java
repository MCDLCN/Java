package model.entities.evilaaaneighbours;

import main_logic.enums.Stat;
import model.entities.Stats;

/**
 * A basic enemy creature used for early encounters.
 */
public class Goblin extends Enemy {

    public Goblin() {
        super("Goblin", buildStats(), 7, 15, 7);
    }

    private static Stats buildStats() {
        Stats s = new Stats();
        s.set(Stat.STR, 8);
        s.set(Stat.DEX, 14);
        s.set(Stat.CON, 10);
        s.set(Stat.INT, 10);
        s.set(Stat.WIS, 8);
        s.set(Stat.CHA, 8);
        return s;
    }

    @Override
    public String getTypeName() {
        return "Goblin";
    }
}