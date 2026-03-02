package crawlinmydungeon.dice;

import java.util.Random;

/**
 * Dice class.
 */
public class Dice {
    /**
     * Random.
     *
     * @return result.
     */
    private static final Random random = new Random();
    /**
     * Sides.
     */
    private final int sides;

    /**
     * Dice.
     *
     * @param sides sides.
     */
    public Dice(int sides) {
        this.sides = sides;
    }

    /**
     * Roll.
     *
     * @return result.
     */
    public int roll() {
        return random.nextInt(1,sides+1);
    }

    /**
     * Roll.
     *
     * @param sides sides.
     *
     * @return result.
     */
    public static int roll(int sides) {
        return random.nextInt(sides) + 1;
    }
}