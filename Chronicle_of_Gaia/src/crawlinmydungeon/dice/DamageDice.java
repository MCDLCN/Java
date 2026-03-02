package crawlinmydungeon.dice;

public class DamageDice {

    private final int count;
    private final Die die;

    public DamageDice(int count, Die die) {
        this.count = count;
        this.die = die;
    }

    public int roll() {
        int total = 0;
        for (int i = 0; i < count; i++) {
            total += Dice.roll(die.sides);
        }
        return total;
    }
}