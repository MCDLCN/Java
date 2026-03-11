package tile;

import main_logic.Game;
import main_logic.enums.EncounterResult;
import main_logic.session.GameSession;
import model.entities.classes.PlayerCharacter;
import model.items.ChestLootGenerator;
import utilities.Console;

/**
 * A tile that represents loot. Intended to draw a random item from an item pool and place it into the player's inventory.
 */
public class ChestTile implements Tile {
    private static final ChestLootGenerator LOOT_GENERATOR = new ChestLootGenerator();

    /**
     * Returns a short human-readable description of the tile.
     * @return Short description of this tile.
     */
    @Override
    public String describe() {
        return "You see a chest.";
    }

    /**
     * Applies this tile's effect when a creature enters it.
     * Intended behavior: draw a random item and place it into the player's inventory.
     * Potions are stored in inventory and may be consumed later.
     * @param session Current state of the game.
     */
    @Override
    public EncounterResult onEnter(GameSession session) {
        PlayerCharacter player = session.getPlayer();
        ChestLootGenerator.LootResult loot = LOOT_GENERATOR.generateLoot(player, session.getBoard());
        player.getInventory().addItem(loot.item(), loot.quantity());
        Console.print("You found "+loot.item().getCode()+"!", Console.ConsoleColor.YELLOW);
        return EncounterResult.VICTORY;
    }
}
