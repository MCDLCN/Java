package main_logic.service;

import main_logic.dice.Dice;
import main_logic.enums.EncounterResult;
import main_logic.enums.Stat;
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
import java.util.Scanner;

import static main_logic.service.InventoryConsoleService.applyCombatItemInteraction;
import static main_logic.service.InventoryConsoleService.applyItemInteraction;

/**
 * Runs turn-based combat between the player and one enemy.
 */
public class CombatService {


    /**
     * Runs the full combat loop until the player wins, dies, or flees.
     *
     * @param player the player character
     * @param enemy the encountered enemy
     * @return the combat result
     */
    public EncounterResult startFight(PlayerCharacter player, Enemy enemy) {
        Console.print("Combat starts against " + enemy.getName() + "!", Console.ConsoleColor.BRIGHT_RED);

        boolean playerTurn = rollInitiative(player, enemy);

        while (player.getHp() > 0 && enemy.getHp() > 0) {
            printCombatStatus(player, enemy);

            if (playerTurn) {
                EncounterResult result = handlePlayerTurn(player, enemy);

                if (result == EncounterResult.FLED) {
                    return EncounterResult.FLED;
                }
            } else {
                handleEnemyTurn(player, enemy);
            }

            if (enemy.getHp() <= 0) {
                Console.print(enemy.getName() + " dies.", Console.ConsoleColor.GREEN);
                return EncounterResult.VICTORY;
            }

            if (player.getHp() <= 0) {
                Console.print(player.getName() + " has been defeated.", Console.ConsoleColor.BRIGHT_RED);
                return EncounterResult.DEFEAT;
            }

            playerTurn = !playerTurn;
        }

        return player.getHp() > 0 ? EncounterResult.VICTORY : EncounterResult.DEFEAT;
    }

    /**
     * Rolls initiative for both sides and returns whether the player starts.
     */
    private boolean rollInitiative(PlayerCharacter player, Enemy enemy) {
        int playerRaw = Dice.roll(20);
        int playerMod = player.getStats().getModifier(Stat.DEX);
        int playerTotal = playerRaw + playerMod;

        int enemyRaw = Dice.roll(20);
        int enemyMod = enemy.getStats().getModifier(Stat.DEX);
        int enemyTotal = enemyRaw + enemyMod;

        Console.print(player.getName() + " initiative: " + playerRaw + " + " + playerMod + " = " + playerTotal);
        Console.print(enemy.getName() + " initiative: " + enemyRaw + " + " + enemyMod + " = " + enemyTotal);

        if (playerTotal >= enemyTotal) {
            Console.print(player.getName() + " goes first.", Console.ConsoleColor.CYAN);
            return true;
        }

        Console.print(enemy.getName() + " goes first.", Console.ConsoleColor.CYAN);
        return false;
    }

    /**
     * Handles the player's turn.
     */
    private EncounterResult handlePlayerTurn(PlayerCharacter player, Enemy enemy) {
        boolean itemInteractionUsed = false;

        while (true) {
            Console.print("\n" + player.getName() + "'s turn.", Console.ConsoleColor.BRIGHT_PURPLE);

            if (!itemInteractionUsed) {
                Console.print("1) Attack");
                Console.print("2) Use item");
                Console.print("3) Flee");

                int choice = Console.askInt("Choice: ", 1, 3);

                if (choice == 1) {
                    handlePlayerAttack(player, enemy);
                    return EncounterResult.NONE;
                }

                if (choice == 2) {
                    boolean used = handleItemInteraction(player, enemy);

                    if (used) {
                        itemInteractionUsed = true;

                        if (enemy.getHp() <= 0 || player.getHp() <= 0) {
                            return EncounterResult.NONE;
                        }
                    }
                    continue;
                }

                if (tryFlee(player, enemy)) {
                    int newPosition = Math.max(0, player.getPosition() - 2);
                    player.setPosition(newPosition);
                    Console.print(player.getName() + " flees to tile " + newPosition + ".", Console.ConsoleColor.YELLOW);
                    return EncounterResult.FLED;
                }

                Console.print("Flee failed. Turn wasted.", Console.ConsoleColor.YELLOW);
                return EncounterResult.NONE;
            }

            Console.print("1) Attack");
            Console.print("2) Flee");

            int choice = Console.askInt("Choice: ", 1, 2);

            if (choice == 1) {
                handlePlayerAttack(player, enemy);
                return EncounterResult.NONE;
            }

            if (tryFlee(player, enemy)) {
                int newPosition = Math.max(0, player.getPosition() - 2);
                player.setPosition(newPosition);
                Console.print(player.getName() + " flees to tile " + newPosition + ".", Console.ConsoleColor.YELLOW);
                return EncounterResult.FLED;
            }

            Console.print("Flee failed. Turn wasted.", Console.ConsoleColor.YELLOW);
            return EncounterResult.NONE;
        }
    }

    /**
     * Handles the player's attack.
     */
    private void handlePlayerAttack(PlayerCharacter player, Enemy enemy) {
        int rawRoll = Dice.roll(20);
        int modifier = player.getAttackModifier();
        int total = rawRoll + modifier;

        Console.print(player.getName() + " attack roll: "
                + rawRoll + " + " + modifier + " = " + total
                + " vs AC " + enemy.getAc());

        if (rawRoll == 1) {
            Console.print("Critical miss!", Console.ConsoleColor.YELLOW);
            consumeEquippedScrollUseIfNeeded(player);
            return;
        }

        boolean critical = rawRoll == 20;

        if (critical) {
            Console.print("Critical hit!", Console.ConsoleColor.GOLD);
        }

        if (critical || total >= enemy.getAc()) {

            int damage = player.rollAttackDamage(critical);

            enemy.healthChange(-damage);

            Console.print("Damage: " + damage, Console.ConsoleColor.GREEN);
            Console.print(enemy.getName() + " HP: "
                    + enemy.getHp() + "/" + enemy.getMaxHp());
        }
        else {
            Console.print("Miss!", Console.ConsoleColor.YELLOW);
        }

        consumeEquippedScrollUseIfNeeded(player);
    }

    /**
     * Handles the enemy's attack.
     */
    private void handleEnemyTurn(PlayerCharacter player, Enemy enemy) {
        Console.print("\n" + enemy.getName() + "'s turn.", Console.ConsoleColor.RED);

        int rawRoll = Dice.roll(20);
        int modifier = enemy.getAttackModifier();
        int total = rawRoll + modifier;

        Console.print(enemy.getName() + " attack roll: "
                + rawRoll + " + " + modifier + " = " + total
                + " vs AC " + player.getAc());

        if (rawRoll == 1) {
            Console.print("Critical miss!", Console.ConsoleColor.YELLOW);
            return;
        }

        boolean critical = rawRoll == 20;

        if (critical) {
            Console.print("Critical hit!", Console.ConsoleColor.GOLD);
        }

        if (critical || total >= player.getAc()) {

            int damage = enemy.rollDamage(critical);

            player.healthChange(-damage);

            Console.print("Damage: " + damage, Console.ConsoleColor.BRIGHT_RED);
        }
        else {
            Console.print(enemy.getName() + " misses.", Console.ConsoleColor.YELLOW);
        }
    }

    /**
     * Handles the optional item interaction during the player's turn.
     *
     * @return true if an item interaction was used
     */
    private boolean handleItemInteraction(PlayerCharacter player, Enemy enemy) {
        boolean done = false;
        while (!done) {
            InventoryEntry selectedEntry = InventoryConsoleService.chooseInventoryEntry(player);
            if (selectedEntry == null) {
                return false;
            }
            done = applyCombatItemInteraction(player, enemy,selectedEntry);
        }
        return done;
    }



    /**
     * Attempts to flee from combat.
     */
    private boolean tryFlee(PlayerCharacter player, Enemy enemy) {
        int playerRaw = Dice.roll(20);
        int playerMod = player.getStats().getModifier(Stat.DEX);
        int playerTotal = playerRaw + playerMod;

        int enemyRaw = Dice.roll(20);
        int enemyMod = enemy.getStats().getModifier(Stat.WIS);
        int enemyTotal = enemyRaw + enemyMod;

        Console.print(player.getName() + " flee roll: " + playerRaw + " + " + playerMod + " = " + playerTotal);
        Console.print(enemy.getName() + " oppose roll: " + enemyRaw + " + " + enemyMod + " = " + enemyTotal);

        return playerTotal > enemyTotal;
    }

    /**
     * Consumes one use from the equipped scroll after a successful player attack.
     */
    private void consumeEquippedScrollUseIfNeeded(PlayerCharacter player) {
        Optional<InventoryEntry> equippedScrollEntry = player.getInventory().getEquipped(Scroll.class);

        if (equippedScrollEntry.isEmpty()) {
            return;
        }

        InventoryEntry entry = equippedScrollEntry.get();
        Scroll scroll = (Scroll) entry.getItem();

        scroll.consumeUse();

        if (scroll.canUse()) {
            Console.print(scroll.getCode() + " has " + scroll.getUsesRemaining() + " use(s) left.");
            return;
        }

        entry.setEquipped(false);
        player.equipScroll(null);
        player.getInventory().getEntries().remove(entry);

        Console.print(scroll.getCode() + " crumbles to dust.", Console.ConsoleColor.YELLOW);
    }




    /**
     * Prints the current HP state for both sides.
     */
    private void printCombatStatus(PlayerCharacter player, Enemy enemy) {
        Console.print("\n=== Combat Status ===", Console.ConsoleColor.BRIGHT_PURPLE);
        Console.print(player.getName() + ": " + player.getHp() + "/" + player.getMaxHp() + " HP | AC " + player.getAc());
        Console.print(enemy.getName() + ": " + enemy.getHp() + "/" + enemy.getMaxHp() + " HP | AC " + enemy.getAc());
    }

}