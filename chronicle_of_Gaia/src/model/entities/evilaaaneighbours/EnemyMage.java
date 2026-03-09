package model.entities.evilaaaneighbours;

import main_logic.enums.Stat;
import model.entities.Stats;

/**
 * A hostile mage enemy based on the SRD mage stat block.
 */
public class EnemyMage extends Enemy {

    public EnemyMage() {
        super("Mage", buildStats(), 40, 12, 40);
    }

    private static Stats buildStats() {
        Stats s = new Stats();
        s.set(Stat.STR, 9);
        s.set(Stat.DEX, 14);
        s.set(Stat.CON, 11);
        s.set(Stat.INT, 17);
        s.set(Stat.WIS, 12);
        s.set(Stat.CHA, 11);
        return s;
    }

    @Override
    public String getTypeName() {
        return "Mage";
    }
}
