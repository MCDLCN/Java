package entities.classes;

import crawlinmydungeon.enums.Stat;
import entities.Creature;

import java.util.Map;

public class Warrior extends Creature {
    public Warrior(int HP, String name, Map<Stat, Integer> stats) {
        super(HP, 10, name, stats);
        this.setMaxHp(this.calculateHP());
    }
    @Override
    public String getTypeName() {
        return "Warrior";
    }

    protected int getMaxHitDie() {
        return 10;
    }

    protected int calculateHP(){
        return this.getStatModifier(Stat.CON)+this.getMaxHitDie();
    }
}
