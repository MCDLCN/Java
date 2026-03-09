package main_logic.dice;

import java.util.Random;

/**
 * General-purpose dice roller used for game mechanics such as movement. Provides instance-based and static rolling helpers.
 */
public class Dice {
    public static final Random random = new Random();
    /**
     * Number of sides on this die (e.g., 6 for a d6).
     */
    private final int sides;

    /**
     * Creates a new Dice instance.
     * @param sides sides value.
     */
    public Dice(int sides) {
        this.sides = sides;
    }

    /**
     * Rolls the configured dice and returns the total.
     * @return A pseudo-random roll total.
     */
    public int roll() {
        return random.nextInt(1,sides+1);
    }

    /**
     * Rolls the configured dice and returns the total.
     * @param sides sides value.
     * @return A pseudo-random roll total.
     */
    public static int roll(int sides) {
        return random.nextInt(sides) + 1;
    }
}
