package crawlinmydungeon.dice;

/**
 * Damage dice class.
 */
public class DamageDice {

    /**
     * Count.
     */
    private final int count;
    /**
     * Die.
     */
    private final Die die;

    /**
     * Damage dice.
     *
     * @param count count.
     * @param die die.
     */
    public DamageDice(int count, Die die) {
        this.count = count;
        this.die = die;
    }

    /**
     * Roll.
     *
     * @return result.
     */
    public int roll() {
        int total = 0;
        for (int i = 0; i < count; i++) {
            total += Dice.roll(die.sides);
        }
        return total;
    }
}