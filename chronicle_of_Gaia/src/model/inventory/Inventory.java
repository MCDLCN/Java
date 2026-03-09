package model.inventory;

import main_logic.enums.ItemCode;
import model.items.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents the player's inventory.
 */
public class Inventory {

    private final List<InventoryEntry> items = new ArrayList<>();

    public List<InventoryEntry> getEntries() {
        return items;
    }

    /**
     * Adds an item to the inventory.
     */
    public void addItem(Item item, int quantity) {

        if (item.isStackable()) {
            Optional<InventoryEntry> existing = findEntry(item.getCode());

            if (existing.isPresent()) {
                existing.get().add(quantity);
                return;
            }
        }

        items.add(new InventoryEntry(item, quantity));
    }

    public void addLoadedItem(long inventoryRowId, Item item, int quantity,  boolean equipped) {
        items.add(new InventoryEntry(inventoryRowId, item, quantity, equipped));
    }

    /**
     * Removes items from the inventory.
     */
    public void removeItem(Item item, int quantity) {

        Optional<InventoryEntry> entry = findEntry(item.getCode());

        if (entry.isEmpty()) {
            return;
        }

        InventoryEntry e = entry.get();

        e.remove(quantity);

        if (e.getQuantity() <= 0) {
            items.remove(e);
        }
    }

    /**
     * Finds an inventory entry by item code.
     */
    public Optional<InventoryEntry> findEntry(ItemCode code) {
        return items.stream()
                .filter(e -> e.getItem().getCode() == code)
                .findFirst();
    }

    /**
     * Returns all items of a given type.
     */
    public <T extends Item> List<InventoryEntry> getItemsOfType(Class<T> clazz) {

        List<InventoryEntry> result = new ArrayList<>();

        for (InventoryEntry entry : items) {
            if (clazz.isInstance(entry.getItem())) {
                result.add(entry);
            }
        }

        return result;
    }

    /**
     * Returns whether the player owns an item type.
     */
    public <T extends Item> boolean hasItemType(Class<T> clazz) {
        return items.stream().anyMatch(e -> clazz.isInstance(e.getItem()));
    }

    /**
     * Equip an item.
     */
    public void equip(Item item) {
        findEntry(item.getCode()).ifPresent(e -> e.setEquipped(true));
    }

    /**
     * Unequip an item.
     */
    public void unequip(Item item) {
        findEntry(item.getCode()).ifPresent(e -> e.setEquipped(false));
    }

    /**
     * Returns equipped items of a given type.
     */
    public <T extends Item> Optional<InventoryEntry> getEquipped(Class<T> clazz) {

        return items.stream()
                .filter(e -> clazz.isInstance(e.getItem()))
                .filter(InventoryEntry::isEquipped)
                .findFirst();
    }

    /**
     * Returns what's equipped
     */
    public <T extends Item> boolean hasEquipped(Class<T> clazz) {
        return items.stream()
                .filter(InventoryEntry::isEquipped)
                .anyMatch(e -> clazz.isInstance(e.getItem()));
    }


}