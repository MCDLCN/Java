package main_logic.service;

import model.entities.classes.PlayerCharacter;
import model.entities.evilaaaneighbours.Enemy;
import model.inventory.InventoryEntry;
import model.items.Item;
import model.items.consumables.DamagePotion;
import model.items.consumables.HealingPotion;
import model.items.defensives.Armour;
import model.items.defensives.Shield;
import model.items.offensives.Weapon;
import model.items.scrolls.Scroll;
import utilities.Console;

import java.util.List;
import java.util.Optional;

public class InventoryConsoleService {

    public static InventoryEntry chooseInventoryEntry(PlayerCharacter player) {

        List<InventoryEntry> entries = player.getInventory().getEntries();

        if (entries.isEmpty()) {
            Console.print("Inventory is empty.", Console.ConsoleColor.YELLOW);
            return null;
        }

        Console.print("\nChoose an item:");

        for (int i = 0; i < entries.size(); i++) {
            InventoryEntry entry = entries.get(i);
            Item item = entry.getItem();

            String equippedText = entry.isEquipped() ? " [equipped]" : "";
            String quantityText = item.isStackable() ? " x" + entry.getQuantity() : "";

            if (item instanceof Scroll scroll) {
                quantityText += " (" + scroll.getUsesRemaining() + " use(s) left)";
            }

            Console.print((i + 1) + ") " + item.getCode() + quantityText + equippedText);
        }

        Console.print((entries.size() + 1) + ") Cancel");

        int choice = Console.askInt("Choice: ", 1, entries.size() + 1);

        if (choice == entries.size() + 1) {
            return null;
        }

        return entries.get(choice - 1);
    }

    /**
     * Applies one item interaction in a combat scenario.
     */
    public static boolean applyCombatItemInteraction(PlayerCharacter player, Enemy enemy, InventoryEntry entry) {
        Item item = entry.getItem();

        if (item instanceof HealingPotion potion) {
            int heal = potion.useOn(player);
            consumeEntry(player, entry, 1);
            Console.print(player.getName() + " heals " + heal + " HP.", Console.ConsoleColor.GREEN);
            Console.print(player.getName() + " HP: " + player.getHp() + "/" + player.getMaxHp());
            return true;
        }

        if (item instanceof DamagePotion potion) {
            int damage = potion.useOn(enemy);
            consumeEntry(player, entry, 1);
            Console.print(enemy.getName() + " takes " + damage + " damage.", Console.ConsoleColor.GREEN);
            Console.print(enemy.getName() + " HP: " + enemy.getHp() + "/" + enemy.getMaxHp());
            return true;
        }

        if (item instanceof Armour armour) {
            if (entry.isEquipped()) {
                entry.setEquipped(false);
                player.getEquipment().setArmour(null);
                Console.print("Unequipped " + item.getCode() + ".", Console.ConsoleColor.CYAN);
                return true;
            }
            unequipCurrent(player.getInventory().getEquipped(Armour.class));
            player.equipArmour(armour);
            entry.setEquipped(true);
            Console.print("Equipped " + item.getCode() + ".", Console.ConsoleColor.CYAN);
            return true;
        }

        if (item instanceof Shield shield) {
            if (entry.isEquipped()) {
                entry.setEquipped(false);
                player.getEquipment().setShield(null);
                Console.print("Unequipped " + item.getCode() + ".", Console.ConsoleColor.CYAN);
                return true;
            }
            unequipCurrent(player.getInventory().getEquipped(Shield.class));
            player.equipShield(shield);
            entry.setEquipped(true);
            Console.print("Equipped " + item.getCode() + ".", Console.ConsoleColor.CYAN);
            return true;
        }

        if (item instanceof Weapon weapon) {
            if (entry.isEquipped()) {
                entry.setEquipped(false);
                player.getEquipment().setWeapon(null);
                Console.print("Unequipped " + item.getCode() + ".", Console.ConsoleColor.CYAN);
                return true;
            }
            if (!player.canEquip(weapon)) {
                Console.print("You cannot equip weapons.", Console.ConsoleColor.RED);
                return false;
            }
            unequipCurrent(player.getInventory().getEquipped(Weapon.class));
            player.equipWeapon(weapon);
            entry.setEquipped(true);
            Console.print("Equipped " + item.getCode() + ".", Console.ConsoleColor.CYAN);
            return true;
        }

        if (item instanceof Scroll scroll) {
            if (entry.isEquipped()) {
                entry.setEquipped(false);
                player.getEquipment().setScroll(null);
                Console.print("Unequipped " + item.getCode() + ".", Console.ConsoleColor.CYAN);
                return true;
            }
            if (!player.canEquip(scroll)) {
                Console.print("You cannot equip scrolls.", Console.ConsoleColor.RED);
                return false;
            }
            unequipCurrent(player.getInventory().getEquipped(Scroll.class));
            player.equipScroll(scroll);
            entry.setEquipped(true);
            Console.print("Equipped " + item.getCode() + ".", Console.ConsoleColor.CYAN);
            return true;
        }


        Console.print("That item cannot be used in combat.", Console.ConsoleColor.YELLOW);
        return false;
    }

    /**
     * Applies one item interaction in a combat scenario.
     */
    public static boolean applyItemInteraction(PlayerCharacter player, InventoryEntry entry) {
        Item item = entry.getItem();

        if (item instanceof HealingPotion potion) {
            int heal = potion.useOn(player);
            consumeEntry(player, entry, 1);
            Console.print(player.getName() + " heals " + heal + " HP.", Console.ConsoleColor.GREEN);
            Console.print(player.getName() + " HP: " + player.getHp() + "/" + player.getMaxHp());
            return true;
        }

        if (item instanceof DamagePotion) {
            Console.print("You cannot use that out of combat", Console.ConsoleColor.RED);
            return false;
        }

        if (item instanceof Armour armour) {
            unequipCurrent(player.getInventory().getEquipped(Armour.class));
            player.equipArmour(armour);
            entry.setEquipped(true);
            Console.print("Equipped " + item.getCode() + ".", Console.ConsoleColor.CYAN);
            return true;
        }

        if (item instanceof Shield shield) {
            unequipCurrent(player.getInventory().getEquipped(Shield.class));
            player.equipShield(shield);
            entry.setEquipped(true);
            Console.print("Equipped " + item.getCode() + ".", Console.ConsoleColor.CYAN);
            return true;
        }

        if (item instanceof Weapon weapon) {
            if (!player.canEquip(weapon)) {
                Console.print("You cannot equip weapons.", Console.ConsoleColor.RED);
                return false;
            }
            unequipCurrent(player.getInventory().getEquipped(Weapon.class));
            player.equipWeapon(weapon);
            entry.setEquipped(true);
            Console.print("Equipped " + item.getCode() + ".", Console.ConsoleColor.CYAN);
            return true;
        }

        if (item instanceof Scroll scroll) {
            if (!player.canEquip(scroll)) {
                Console.print("You cannot equip scrolls.", Console.ConsoleColor.RED);
                return false;
            }
            unequipCurrent(player.getInventory().getEquipped(Scroll.class));
            player.equipScroll(scroll);
            entry.setEquipped(true);
            Console.print("Equipped " + item.getCode() + ".", Console.ConsoleColor.CYAN);
            return true;
        }


        Console.print("That item cannot be used in combat.", Console.ConsoleColor.YELLOW);
        return false;
    }

    /**
     * Removes quantity from one inventory entry and deletes it if empty.
     */
    private static void consumeEntry(PlayerCharacter player, InventoryEntry entry, int quantity) {
        entry.remove(quantity);

        if (entry.getQuantity() <= 0) {
            player.getInventory().getEntries().remove(entry);
        }
    }

    /**
     * Unequips the currently equipped inventory entry if present.
     */
    private static void unequipCurrent(Optional<InventoryEntry> entry) {
        entry.ifPresent(e -> e.setEquipped(false));
    }

}