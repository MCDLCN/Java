package model.items;

import main_logic.Board;
import main_logic.Game;
import main_logic.enums.ItemCode;
import model.entities.classes.PlayerCharacter;
import model.entities.classes.Warrior;
import model.entities.classes.Wizard;
import model.items.defensives.Armour;
import model.items.consumables.Potion;
import model.items.offensives.Weapon;
import model.items.scrolls.Scroll;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generates chest loot based on player class, board progress, and missing inventory categories.
 *
 * <p>Rules:
 * <ul>
 *   <li>Potions are the most common loot category.</li>
 *   <li>Wizards are more likely to receive scrolls.</li>
 *   <li>Warriors are more likely to receive weapons.</li>
 *   <li>If the player has no item of a category, that category gets a bonus weight.</li>
 *   <li>Large potions are rare early and become more common later.</li>
 *   <li>Potions drop in bundles of 1 to 3.</li>
 * </ul>
 */
public class ChestLootGenerator {

    private static final int POTION_BASE_WEIGHT = 45;
    private static final int WEAPON_BASE_WEIGHT = 20;
    private static final int ARMOUR_BASE_WEIGHT = 15;
    private static final int SCROLL_BASE_WEIGHT = 20;

    private static final int CLASS_BIAS_BONUS = 15;
    private static final int MISSING_CATEGORY_BONUS = 10;

    private final Random random;

    /**
     * Creates a loot generator with a new random instance.
     */
    public ChestLootGenerator() {
        this(new Random());
    }

    /**
     * Creates a loot generator with a provided random instance.
     *
     * @param random random source
     */
    public ChestLootGenerator(Random random) {
        this.random = random;
    }

    /**
     * Generates loot for a chest.
     *
     * <p>Progress should be a value between 0.0 and 1.0, where:
     * <ul>
     *   <li>0.0 = very early board</li>
     *   <li>1.0 = very late board</li>
     * </ul>
     *
     * @param player the player opening the chest
     * @param board the board on which the chest is opened
     * @return generated loot result
     */
    public LootResult generateLoot(PlayerCharacter player, Board board) {
        double progress = (double) player.getPosition() / (board.getSize() - 1);
        double clampedProgress = clamp(progress, 0.0, 1.0);

        int potionWeight = POTION_BASE_WEIGHT;
        int weaponWeight = WEAPON_BASE_WEIGHT;
        int armourWeight = ARMOUR_BASE_WEIGHT;
        int scrollWeight = SCROLL_BASE_WEIGHT;

        if (player instanceof Wizard) {
            scrollWeight += CLASS_BIAS_BONUS;
        } else if (player instanceof Warrior) {
            weaponWeight += CLASS_BIAS_BONUS;
        }

        if (!hasWeapon(player)) {
            weaponWeight += MISSING_CATEGORY_BONUS;
        }

        if (!hasArmour(player)) {
            armourWeight += MISSING_CATEGORY_BONUS;
        }

        if (!hasPotion(player)) {
            potionWeight += MISSING_CATEGORY_BONUS;
        }

        if (!hasScroll(player)) {
            scrollWeight += MISSING_CATEGORY_BONUS;
        }

        LootCategory category = weightedPick(List.of(
                new WeightedCategory(LootCategory.POTION, potionWeight),
                new WeightedCategory(LootCategory.WEAPON, weaponWeight),
                new WeightedCategory(LootCategory.ARMOUR, armourWeight),
                new WeightedCategory(LootCategory.SCROLL, scrollWeight)
        ));

        return switch (category) {
            case POTION -> generatePotionLoot(clampedProgress);
            case WEAPON -> generateWeaponLoot();
            case ARMOUR -> generateArmourLoot();
            case SCROLL -> generateScrollLoot();
        };
    }

    /**
     * Generates potion loot.
     *
     * <p>Standard potions are common. Large potions are rare early and scale with progress.</p>
     *
     * @param progress board progression from 0.0 to 1.0
     * @return potion loot result
     */
    private LootResult generatePotionLoot(double progress) {
        boolean largePotion = rollLargePotion(progress);
        int quantity = 1 + random.nextInt(3);

        Potion potion = (Potion) (largePotion
                        ? ItemFactory.create(ItemCode.LARGE_HEALING_POTION)
                        : ItemFactory.create(ItemCode.STANDARD_HEALING_POTION));

        return new LootResult(potion, quantity);
    }

    /**
     * Generates weapon loot.
     *
     * @return weapon loot result
     */
    private LootResult generateWeaponLoot() {
        Weapon weapon = (Weapon) switch (random.nextInt(3)) {
            case 0 -> ItemFactory.create(ItemCode.CLUB);
            case 1 -> ItemFactory.create(ItemCode.LONGSWORD);
            default -> ItemFactory.create(ItemCode.GREATSWORD);
        };

        return new LootResult(weapon, 1);
    }

    /**
     * Generates armour loot.
     *
     * @return armour loot result
     */
    private LootResult generateArmourLoot() {
        Armour armour = (Armour) switch (random.nextInt(3)) {
            case 0 -> ItemFactory.create(ItemCode.STUDDED_LEATHER);
            case 1 -> ItemFactory.create(ItemCode.BREASTPLATE);
            default -> ItemFactory.create(ItemCode.FULL_PLATE);
        };
        return new LootResult(armour, 1);
    }

    /**
     * Generates scroll loot.
     *
     * @return scroll loot result
     */
    private LootResult generateScrollLoot() {
        Scroll scroll = (Scroll) (random.nextBoolean()
                        ? ItemFactory.create(ItemCode.FIREBALL_SCROLL)
                        : ItemFactory.create(ItemCode.LIGHTNING_BOLT_SCROLL));

        return new LootResult(scroll, 1);
    }

    /**
     * Rolls whether the potion should be a large potion.
     *
     * <p>Chance scales from 5% early to 30% late.</p>
     *
     * @param progress board progression from 0.0 to 1.0
     * @return true if a large potion should be generated
     */
    private boolean rollLargePotion(double progress) {
        int chance = (int) Math.round(5 + (25 * progress));
        return random.nextInt(100) < chance;
    }

    /**
     * Checks whether the player has at least one weapon in inventory.
     *
     * @param player player to inspect
     * @return true if the player has a weapon
     */
    private boolean hasWeapon(PlayerCharacter player) {
        return player.getInventory().hasItemType(Weapon.class);
    }

    /**
     * Checks whether the player has at least one armour item in inventory.
     *
     * @param player player to inspect
     * @return true if the player has armour
     */
    private boolean hasArmour(PlayerCharacter player) {
        return player.getInventory().hasItemType(Armour.class);
    }

    /**
     * Checks whether the player has at least one potion in inventory.
     *
     * @param player player to inspect
     * @return true if the player has a potion
     */
    private boolean hasPotion(PlayerCharacter player) {
        return player.getInventory().hasItemType(Potion.class);
    }

    /**
     * Checks whether the player has at least one scroll in inventory.
     *
     * @param player player to inspect
     * @return true if the player has a scroll
     */
    private boolean hasScroll(PlayerCharacter player) {
        return player.getInventory().hasItemType(Scroll.class);
    }


    /**
     * Picks a category using weighted random selection.
     *
     * @param categories weighted categories
     * @return selected category
     */
    private LootCategory weightedPick(List<WeightedCategory> categories) {
        int totalWeight = 0;

        for (WeightedCategory category : categories) {
            totalWeight += category.weight();
        }

        int roll = random.nextInt(totalWeight);
        int cumulative = 0;

        for (WeightedCategory category : categories) {
            cumulative += category.weight();
            if (roll < cumulative) {
                return category.category();
            }
        }

        throw new IllegalStateException("Failed to pick a loot category.");
    }

    /**
     * Clamps a value between a minimum and a maximum.
     *
     * @param value value to clamp
     * @param min minimum value
     * @param max maximum value
     * @return clamped value
     */
    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Loot categories used for weighted selection.
     */
    private enum LootCategory {
        POTION,
        WEAPON,
        ARMOUR,
        SCROLL
    }

    /**
     * Weighted loot category entry.
     *
     * @param category loot category
     * @param weight selection weight
     */
    private record WeightedCategory(LootCategory category, int weight) {
    }

    /**
     * Represents generated chest loot.
     *
     * @param item generated item
     * @param quantity quantity of the item
     */
    public record LootResult(Item item, int quantity) {
    }
}