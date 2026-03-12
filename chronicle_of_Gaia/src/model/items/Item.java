package model.items;

import main_logic.enums.ItemCode;
import main_logic.enums.ItemType;

/**
 * Base type for all items.
 */
public abstract class Item {

    private final ItemCode name;
    private final ItemType type;

    protected Item(ItemCode name, ItemType type) {
        this.name = name;
        this.type = type;
    }

    public ItemCode getCode() {
        return name;
    }

    public abstract boolean isStackable();

    public ItemType getType() {
        return type;
    }
}