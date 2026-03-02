package crawlinmydungeon.dice;

/**
 * Die enum.
 */
public enum Die {
    /**
     * D 4.
     *
     * @param 4 4.
     *
     * @return result.
     */
    D4(4), D6(6), D8(8), D10(10), D12(12), D20(20);

    /**
     * Sides.
     */
    public final int sides;

    /**
     * Die.
     *
     * @param sides sides.
     */
    Die(int sides) {
        this.sides = sides;
    }
}