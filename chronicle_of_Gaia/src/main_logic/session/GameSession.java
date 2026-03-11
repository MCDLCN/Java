package main_logic.session;

import main_logic.Board;
import model.entities.classes.PlayerCharacter;

/**
 * Represents the active runtime game session.
 *
 * <p>A session groups together the persisted identifiers and the runtime
 * objects required to continue a game.</p>
 */
public class GameSession {

    private final long characterId;
    private final long saveId;
    private final PlayerCharacter player;
    private final Board board;
    private boolean pendingLevelUp = false;

    /**
     * Creates a new game session.
     *
     * @param characterId persisted character id
     * @param saveId persisted save slot id
     * @param player current player character
     * @param board current board state
     */
    public GameSession(long characterId, long saveId, PlayerCharacter player, Board board) {
        this.characterId = characterId;
        this.saveId = saveId;
        this.player = player;
        this.board = board;
    }

    /**
     * Returns the persisted character id.
     *
     * @return character id
     */
    public long getCharacterId() {
        return characterId;
    }

    /**
     * Returns the persisted save slot id.
     *
     * @return save slot id
     */
    public long getSaveId() {
        return saveId;
    }

    /**
     * Returns the current player character.
     *
     * @return player character
     */
    public PlayerCharacter getPlayer() {
        return player;
    }



    public boolean hasPendingLevelUp() {
        return pendingLevelUp;
    }

    /**
     * Returns the current board.
     *
     * @return board state
     */
    public Board getBoard() {
        return board;
    }

    public void setPendingLevelUp(boolean b) {
        this.pendingLevelUp = b;
    }
}
