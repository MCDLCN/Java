package model.items.defensives;

import main_logic.enums.ItemCode;

/**
 * Defensive equipment that affects Armor Class (AC) depending on armor type and dexterity modifier rules.
 */
public class Armour extends DefensiveEquipment {
    /**
     * type field.
     */
    private ArmourType type;

    /**
     * Creates a new Armour instance.
     * @param ac ac value.
     * @param name name value.
     * @param description description value.
     * @param type type value.
     */
    public Armour(int ac, ItemCode name, String description, ArmourType type) {
        super(ac, name, description);
        this.type = type;
    }

    /**
     * getType operation.
     * @return Requested value.
     */
    public ArmourType getType() {
        return type;
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    /**
     * Enumeration of the three type of armour there is.
     */
    public enum ArmourType {
        LIGHT,
        MEDIUM,
        HEAVY
    }
}