package model.inventory;

import model.items.Item;

/**
 * Represents one entry in the player's inventory.
 */
public class InventoryEntry {

    private final Item item;
    private int quantity;
    private boolean equipped;
    private Long inventoryRowId;

    /**
     * Constructor used when loading an entry from the database.
     */
    public InventoryEntry(Long inventoryRowId, Item item, int quantity, boolean equipped) {
        this.inventoryRowId = inventoryRowId;
        this.item = item;
        this.quantity = quantity;
        this.equipped = equipped;
    }

    /**
     * Constructor used when creating a new entry during gameplay.
     */
    public InventoryEntry(Item item, int quantity) {
        this.inventoryRowId = null;
        this.item = item;
        this.quantity = quantity;
        this.equipped = false;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isEquipped() {
        return equipped;
    }

    public Long getInventoryRowId() {
        return inventoryRowId;
    }

    public void add(int amount) {
        quantity += amount;
    }

    public void remove(int amount) {
        quantity -= amount;
    }

    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }

    public void setInventoryRowId(Long inventoryRowId) {
        this.inventoryRowId = inventoryRowId;
    }
}