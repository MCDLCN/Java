package main_logic.dice;

/**
 * Common die sizes (d4, d6, d8, d10, d12, d20) used by DamageDice and other mechanics.
 */
public enum Die {
    D4(4), D6(6), D8(8), D10(10), D12(12), D20(20);

    /**
     * Number of sides on this die (e.g., 6 for a d6).
     */
    public final int sides;

    /**
     * Creates a new Die instance.
     * @param sides sides value.
     */
    Die(int sides) {
        this.sides = sides;
    }
}
