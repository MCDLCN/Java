package persistence;

import model.entities.classes.PlayerCharacter;

/**
 * Represents a loaded game state.
 *
 * <p>This contains the player character and the save slot id
 * used to retrieve the board and other persisted data.</p>
 */
public class LoadedGame {

    private final long saveId;
    private final PlayerCharacter player;

    /**
     * Creates a loaded game result.
     *
     * @param saveId the save slot identifier
     * @param player the loaded player character
     */
    public LoadedGame(long saveId, PlayerCharacter player) {
        this.saveId = saveId;
        this.player = player;
    }

    public long getSaveId() {
        return saveId;
    }

    public PlayerCharacter getPlayer() {
        return player;
    }
}