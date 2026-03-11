package model.entities.evilaaaneighbours;

import main_logic.dice.DamageDice;
import main_logic.dice.Die;
import main_logic.enums.Stat;
import model.entities.Stats;

/**
 * A basic enemy creature used for early encounters.
 */
public class Goblin extends Enemy {

    private static final DamageDice SCIMITAR_DAMAGE = new DamageDice(1, Die.D6);
    private static final int DAMAGE_BONUS = 2;

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
        return 25;
    }
}