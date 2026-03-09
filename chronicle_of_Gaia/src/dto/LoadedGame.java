package dto;

import model.entities.classes.PlayerCharacter;

/**
 * Represents a loaded game state.
 *
 * <p>This contains the persisted character id, the player character,
 * and the save slot id used to retrieve the board and other persisted data.</p>
 */
public class LoadedGame {

    private final long characterId;
    private final long saveId;
    private final PlayerCharacter player;

    /**
     * Creates a loaded game result.
     *
     * @param characterId the persisted character id
     * @param saveId the save slot identifier
     * @param player the loaded player character
     */
    public LoadedGame(long characterId, long saveId, PlayerCharacter player) {
        this.characterId = characterId;
        this.saveId = saveId;
        this.player = player;
    }

    public long getCharacterId() {
        return characterId;
    }

    public long getSaveId() {
        return saveId;
    }

    public PlayerCharacter getPlayer() {
        return player;
    }
}