package items.consummables;

import crawlinmydungeon.dice.DamageDice;

/**
 * Consumable item intended to be stored in inventory and consumed later for an effect such as healing or damage.
 */
public abstract class Potion {

    /**
     * Display name of the creature.
     */
    private final String name;
    /**
     * usesRemaining field.
     */
    private int usesRemaining;
    /**
     * effectDice field.
     */
    private final DamageDice effectDice;

    /**
     * Creates a new Potion instance.
     * @param name name value.
     * @param usesRemaining usesRemaining value.
     * @param effectDice effectDice value.
     */
    protected Potion(String name, int usesRemaining, DamageDice effectDice) {
        this.name = name;
        this.usesRemaining = usesRemaining;
        this.effectDice = effectDice;
    }

    // ===== Getters =====

    /**
     * getName operation.
     * @return Requested value.
     */
    public String getName() { return name; }
    /**
     * getUsesRemaining operation.
     * @return Requested value.
     */
    public int getUsesRemaining() { return usesRemaining; }
    /**
     * getEffectDice operation.
     * @return Requested value.
     */
    public DamageDice getEffectDice() { return effectDice; }

    /**
     * canUse operation.
     * @return Result value.
     */
    protected boolean canUse() {
        return usesRemaining > 0;
    }

    /**
     * Check if the potion can be used, if yes roll the dice for it
     * @return Result value.
     */
    protected int consumeAndRoll() {
        if (!canUse()) throw new IllegalStateException("No uses left");
        usesRemaining--;
        return effectDice.roll();
    }
}