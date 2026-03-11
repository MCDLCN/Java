package model.entities.evilaaaneighbours;

import main_logic.dice.DamageDice;
import main_logic.dice.Die;
import main_logic.enums.Stat;
import model.entities.Stats;

/**
 * A hostile mage enemy based on the SRD mage stat block.
 */
public class EnemyMage extends Enemy {

    private static final DamageDice FIREBOLT_DAMAGE = new DamageDice(1, Die.D10);

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

    @Override
    public int rollDamage(boolean critical) {
        return Math.max(0, FIREBOLT_DAMAGE.roll(critical));
    }

    @Override
    public int getAttackModifier() {
        return stats.getModifier(Stat.INT);
    }

    @Override
    public int getXpReward(){
        return 2300;
    }
}