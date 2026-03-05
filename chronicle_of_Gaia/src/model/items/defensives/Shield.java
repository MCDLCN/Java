package model.items.defensives;

import main_logic.enums.ItemCode;

/**
 * Defensive equipment representing a shield, typically increasing AC when equipped.
 */
public class Shield extends DefensiveEquipment{

    /**
     * Creates a new Shield instance.
     * @param ac ac value.
     * @param name name value.
     * @param description description value.
     */
    public Shield(int ac, ItemCode name, String description) {
        super(ac, name, description);
    }

    @Override
    protected boolean isStackable() {
        return false;
    }
}