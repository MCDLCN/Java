package model.entities.evilaaaneighbours;

import main_logic.enums.Stat;
import model.entities.Stats;

/**
 * An enemy creature representing a humanoid opponent.
 */
public class Bandit extends Enemy {

    public Bandit() {
        super("Bandit", buildStats(), 11, 12, 11);
    }

    private static Stats buildStats() {
        Stats s = new Stats();
        s.set(Stat.STR, 11);
        s.set(Stat.DEX, 12);
        s.set(Stat.CON, 12);
        s.set(Stat.INT, 10);
        s.set(Stat.WIS, 10);
        s.set(Stat.CHA, 10);
        return s;
    }

    @Override
    public String getTypeName() {
        return "Bandit";
    }
}