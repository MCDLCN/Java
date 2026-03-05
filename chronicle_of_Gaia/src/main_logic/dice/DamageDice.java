package main_logic.dice;

/**
 * Dice bundle used specifically for damage/healing rolls (e.g., weapon damage, potion effects).
 */
public class DamageDice {

    /**
     * Number of dice to roll.
     */
    private final int count;
    /**
     * Die size used for each roll (e.g., d8).
     */
    private final Die die;

    /**
     * Creates a new DamageDice instance.
     * @param count count value.
     * @param die die value.
     */
    public DamageDice(int count, Die die) {
        this.count = count;
        this.die = die;
    }

    /**
     * Rolls the configured dice and returns the total.
     * @return A pseudo-random roll total.
     */
    public int roll() {
        int total = 0;
        for (int i = 0; i < count; i++) {
            total += Dice.roll(die.sides);
        }
        return total;
    }
}
