package model.items.consumables;

import main_logic.dice.DamageDice;
import main_logic.enums.ItemCode;
import main_logic.enums.ItemType;
import model.items.Item;

/**
 * Consumable item intended to be stored in inventory and consumed later for an effect such as healing or damage.
 */
public abstract class Potion extends Item {

    /**
     * effectDice field.
     */
    private final DamageDice effectDice;

    /**
     * Creates a new Potion instance.
     * @param name name value.
     * @param effectDice effectDice value.
     */
    protected Potion(ItemCode name, DamageDice effectDice) {
        super(name, ItemType.POTION);
        this.effectDice = effectDice;
    }

    // ===== Getters =====
    /**
     * getEffectDice operation.
     * @return Requested value.
     */
    public DamageDice getEffectDice() { return effectDice; }

    /**
     * Check if the potion can be used, if yes roll the dice for it
     * @return Result value.
     */
    protected int Roll() {
        return effectDice.roll();
    }

    @Override
    public boolean isStackable(){
        return true;
    };

}