package tile;

import main_logic.Game;
import main_logic.enums.EncounterResult;
import main_logic.enums.EnemyType;
import main_logic.service.CombatService;
import main_logic.session.GameSession;
import model.entities.classes.PlayerCharacter;
import model.entities.evilaaaneighbours.Enemy;
import model.entities.evilaaaneighbours.EnemyFactory;
import utilities.Console;

/**
 * A tile that represents an encounter with an enemy creature. Combat behavior is planned but currently implemented as a placeholder.
 */
public class EnemyTile implements Tile {

    /**
     * Enemy creature encountered on this tile.
     */
    private final EnemyType enemyT;

    /**
     * Creates a new EnemyTile instance.
     * @param enemy enemy value.
     */
    public EnemyTile(EnemyType enemy) {
        this.enemyT = enemy;
    }

    /**
     * Returns a short human-readable description of the tile.
     * @return Short description of this tile.
     */
    @Override
    public String describe() {
        return "An enemy appears!";
    }


    /**
     * Applies this tile's effect when a creature enters it.
     * Combat is intended to loop until the enemy dies or the player attempts to flee.
     * Fleeing is a DEX (stealth) check against the enemy's WIS (perception) check:
     * on success the player moves back 2 tiles; on failure the player loses their turn.
     * @param session Current state of the game.
     */
    @Override
    public EncounterResult onEnter(GameSession session) {
        PlayerCharacter player = session.getPlayer();
        Enemy enemy = EnemyFactory.create(enemyT);
        CombatService combatService = new CombatService();
        EncounterResult result = combatService.startFight(player, enemy);
        if (result == EncounterResult.VICTORY) {
            int xpReward = enemy.getXpReward();
            int levelsGained = player.gainXp(xpReward);

            Console.print("You gained " + xpReward + " XP.", Console.ConsoleColor.GREEN);
            Console.print("XP: " + player.getCurrentXp() + " / " + player.getXpForNextLevel());

            if (levelsGained > 0) {
                session.setPendingLevelUp(true);
            }
        }
        return result;
    }

    /**
     * Returns the enemy instance stored by this tile.
     *
     * @return the enemy contained in the tile
     */
    public EnemyType getEnemyType(){
        return this.enemyT;
    }
}