package model.items     ;

/**
 * Represents a static item definition stored in the items catalog table.
 *
 * <p>An ItemDefinition describes the base properties of an item type
 * (name, code, stackability). It does not represent an item owned by
 * a player. Player-owned items are represented by InventoryItem.</p>
 */
public class ItemDefinition {

    private final long id;
    private final String code;
    private final String name;
    private final boolean stackable;

    /**
     * Creates a new item definition.
     *
     * @param id        database id of the item
     * @param code      unique item code (e.g. POTION_HEAL_SMALL)
     * @param name      display name of the item
     * @param stackable whether the item can stack in inventory
     */
    public ItemDefinition(long id, String code, String name, boolean stackable) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.stackable = stackable;
    }

    /**
     * Returns the database id of the item.
     *
     * @return item id
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the unique item code.
     *
     * @return item code
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the display name of the item.
     *
     * @return item name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns whether the item can stack.
     *
     * @return true if stackable
     */
    public boolean isStackable() {
        return stackable;
    }
}