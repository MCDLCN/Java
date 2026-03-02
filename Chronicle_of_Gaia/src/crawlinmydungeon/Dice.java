package crawlinmydungeon;

import java.util.Random;

public class Dice {
    private static final Random random = new Random();
    private final int sides;

    public Dice(int sides) {
        this.sides = sides;
    }

    public int roll() {
        return random.nextInt(1,sides+1);
    }
}