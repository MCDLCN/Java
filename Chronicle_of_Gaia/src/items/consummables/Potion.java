package items.consummables;

import crawlinmydungeon.dice.DamageDice;

/**
 * Potion class.
 */
public abstract class Potion {

    /**
     * Name.
     */
    private final String name;
    /**
     * Uses remaining.
     */
    private int usesRemaining;
    /**
     * Effect dice.
     */
    private final DamageDice effectDice;

    /**
     * Potion.
     *
     * @param name name.
     * @param usesRemaining uses remaining.
     * @param effectDice effect dice.
     */
    protected Potion(String name, int usesRemaining, DamageDice effectDice) {
        this.name = name;
        this.usesRemaining = usesRemaining;
        this.effectDice = effectDice;
    }

    public String getName() { return name; }
    public int getUsesRemaining() { return usesRemaining; }
    public DamageDice getEffectDice() { return effectDice; }

    /**
     * Can use.
     *
     * @return result.
     */
    protected boolean canUse() {
        return usesRemaining > 0;
    }

    /**
     * Consume and roll.
     *
     * @return result.
     */
    protected int consumeAndRoll() {
        if (!canUse()) throw new IllegalStateException("No uses left");
        usesRemaining--;
        return effectDice.roll();
    }
}
