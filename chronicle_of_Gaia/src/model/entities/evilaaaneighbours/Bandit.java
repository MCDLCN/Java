package model.entities.evilaaaneighbours;

import main_logic.dice.DamageDice;
import main_logic.dice.Die;
import main_logic.enums.Stat;
import model.entities.Stats;

/**
 * An enemy creature representing a humanoid opponent.
 */
public class Bandit extends Enemy {

    private static final DamageDice SCIMITAR_DAMAGE = new DamageDice(1, Die.D6);
    private static final int DAMAGE_BONUS = 1;

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

    @Override
    public int rollDamage(boolean critical) {
        return SCIMITAR_DAMAGE.roll(critical) + DAMAGE_BONUS;
    }

    @Override
    public int getAttackModifier() {
        return getStats().getModifier(Stat.DEX);
    }

    @Override
    public int getXpReward(){
        return 50;
    }
}