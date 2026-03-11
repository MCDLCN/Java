package model.entities.evilaaaneighbours;

import main_logic.dice.DamageDice;
import main_logic.dice.Die;
import main_logic.enums.Stat;
import model.entities.Stats;

/**
 * A powerful dragon enemy based on the SRD young red dragon stat block.
 */
public class Dragon extends Enemy {

    private static final DamageDice BITE_DAMAGE = new DamageDice(2, Die.D10);
    private static final DamageDice FIRE_DAMAGE = new DamageDice(1, Die.D6);
    private static final int DAMAGE_BONUS = 6;

    public Dragon() {
        super("Young Red Dragon", buildStats(), 178, 18, 178);
    }

    private static Stats buildStats() {
        Stats s = new Stats();
        s.set(Stat.STR, 23);
        s.set(Stat.DEX, 10);
        s.set(Stat.CON, 21);
        s.set(Stat.INT, 14);
        s.set(Stat.WIS, 11);
        s.set(Stat.CHA, 19);
        return s;
    }

    @Override
    public String getTypeName() {
        return "Dragon";
    }

    @Override
    public int rollDamage(boolean critical) {
        return BITE_DAMAGE.roll(critical) + FIRE_DAMAGE.roll(critical) + DAMAGE_BONUS;
    }

    @Override
    public int getXpReward(){
        return 5900;
    }
}