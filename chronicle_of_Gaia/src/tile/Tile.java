package tile;

import main_logic.session.GameSession;
import main_logic.enums.EncounterResult;

public interface Tile {

    String describe();

    EncounterResult onEnter(GameSession session);

    /**
     * Prints tile-specific post-tile actions starting from the given index.
     *
     * @param startIndex first menu index available for this tile
     * @return last used menu index
     */
    default int printPostTileActions(int startIndex) {
        return startIndex - 1;
    }

    /**
     * Handles a tile-specific post-tile action.
     *
     * @param session current game session
     * @param choice chosen menu option
     * @return true when the choice was handled by the tile
     */
    default boolean handlePostTileAction(GameSession session, int choice) {
        return false;
    }
}