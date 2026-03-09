package model.items.offensives;

import main_logic.dice.DamageDice;
import main_logic.enums.ItemCode;
import model.items.Item;

/**
 * Base type for offensive equipment model.items such as weapons.
 */
public abstract class OffensiveEquipment extends Item {
    /**
     * damage field.
     */
    private DamageDice damage;

    /**
     * Creates a new OffensiveEquipment instance.
     * @param name name value.
     * @param damage damage value.
     */
    protected OffensiveEquipment(ItemCode name, DamageDice damage) {
        super(name);
        this.damage = damage;
    }

    /**
     * getDamage operation.
     * @return Requested value.
     */
    public DamageDice getDamage() {
        return damage;
    }

    /**
     * rollDamage operation.
     * @return Result value.
     */
    public int rollDamage() {
        return damage.roll();
    }

    @Override
    public boolean isStackable(){
        return false;
    }
}