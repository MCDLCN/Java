package model.items;

import main_logic.enums.ItemCode;

/**
 * Base type for all items.
 */
public abstract class Item {

    private final ItemCode name;

    protected Item(ItemCode name) {
        this.name = name;
    }

    public ItemCode getCode() {
        return name;
    }

    protected abstract boolean isStackable();

    @Override
    public String toString(){

    }
}