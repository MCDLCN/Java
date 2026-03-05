package model.items.defensives;

import main_logic.enums.ItemCode;
import model.items.Item;

/**
 * Base type for defensive equipment model.items such as armor and shields.
 */
public abstract class DefensiveEquipment extends Item {
    /**
     * Armor Class used for determining whether attacks hit.
     */
    private int ac;
    /**
     * description field.
     */
    private String description;

    /**
     * Creates a new DefensiveEquipment instance.
     * @param ac ac value.
     * @param name name value.
     * @param description description value.
     */
    protected DefensiveEquipment(int ac, ItemCode name, String description) {
        super(name);
        this.ac = ac;
        this.description = description;
    }

    /**
     * getAc operation.
     * @return Requested value.
     */
    public int getAc() {
        return ac;
    }

    /**
     * getDescription operation.
     * @return Requested value.
     */
    public String getDescription() {
        return description;
    }
}