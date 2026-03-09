package main_logic.service;

import dto.CharacterSummary;
import dto.LoadedGame;
import main_logic.Board;
import main_logic.session.GameSession;
import model.entities.Stats;
import model.entities.classes.PlayerCharacter;
import model.entities.classes.Warrior;
import model.entities.classes.Wizard;
import main_logic.enums.CharacterType;
import model.inventory.Inventory;
import model.inventory.InventoryEntry;
import model.items.Item;
import model.items.defensives.Armour;
import model.items.defensives.Shield;
import model.items.offensives.Weapon;
import model.items.scrolls.Scroll;
import persistence.BoardRepository;
import persistence.CharacterRepository;
import persistence.InventoryRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Coordinates persistence workflows for creating, loading, saving,
 * listing, and deleting game sessions.
 */
public class GameSessionService {

    private static final int BOARD_SIZE = 64;

    private final CharacterRepository characterRepository;
    private final BoardRepository boardRepository;
    private final InventoryRepository inventoryRepository;

    /**
     * Creates the service and initializes repositories.
     *
     * @throws SQLException if database initialization fails
     */
    public GameSessionService() throws SQLException {
        this.characterRepository = new CharacterRepository();
        this.boardRepository = new BoardRepository();
        this.inventoryRepository = new InventoryRepository();
    }

    /**
     * Creates a new persisted game session for a freshly created character.
     *
     * @param type selected character type
     * @param name selected character name
     * @param stats generated character stats
     * @return created game session
     * @throws SQLException if persistence fails
     */
    public GameSession createSession(CharacterType type, String name, Stats stats) throws SQLException {
        PlayerCharacter player = switch (type) {
            case WARRIOR -> new Warrior(1, name, stats);
            case WIZARD -> new Wizard(1, name, stats);
        };

        long saveId = boardRepository.createSave();
        Board board = new Board(BOARD_SIZE);
        boardRepository.saveBoard(saveId, board.toTileRows());

        long characterId = characterRepository.create(saveId, player);

        return new GameSession(characterId, saveId, player, board);
    }

    /**
     * Returns all persisted characters.
     *
     * @return saved character summaries
     * @throws SQLException if loading fails
     */
    public List<CharacterSummary> listCharacters() throws SQLException {
        return characterRepository.listCharacters();
    }

    /**
     * Loads a persisted game session by character id.
     *
     * @param characterId persisted character id
     * @return loaded session when found
     * @throws SQLException if loading fails
     */
    public Optional<GameSession> loadSession(long characterId) throws SQLException {
        Optional<LoadedGame> result = characterRepository.load(characterId);
        if (result.isEmpty()) {
            return Optional.empty();
        }

        LoadedGame loaded = result.get();

        Board board = new Board(BOARD_SIZE);
        board.applyTileRows(boardRepository.loadBoard(loaded.getSaveId()));

        Inventory loadedInventory = inventoryRepository.loadInventory(characterId);

        for (InventoryEntry entry : loadedInventory.getEntries()) {
            loaded.getPlayer().getInventory().addLoadedItem(
                    entry.getInventoryRowId(),
                    entry.getItem(),
                    entry.getQuantity(),
                    entry.isEquipped()
            );
        }

        restoreEquippedItems(loaded.getPlayer());

        return Optional.of(new GameSession(
                loaded.getCharacterId(),
                loaded.getSaveId(),
                loaded.getPlayer(),
                board
        ));
    }

    /**
     * Saves the current game session.
     *
     * @param session current session
     * @throws SQLException if saving fails
     */
    public void saveSession(GameSession session) throws SQLException {
        characterRepository.update(
                session.getCharacterId(),
                session.getSaveId(),
                session.getPlayer()
        );

        boardRepository.saveBoard(
                session.getSaveId(),
                session.getBoard().toTileRows()
        );

        inventoryRepository.saveInventory(
                session.getCharacterId(),
                session.getPlayer().getInventory()
        );
    }

    /**
     * Saves only the current character state.
     *
     * @param session current session
     * @throws SQLException if saving fails
     */
    public void saveCharacter(GameSession session) throws SQLException {
        characterRepository.update(
                session.getCharacterId(),
                session.getSaveId(),
                session.getPlayer()
        );
    }

    /**
     * Deletes the character associated with the given id.
     *
     * @param characterId persisted character id
     * @return true when a character was deleted
     * @throws SQLException if deletion fails
     */
    public boolean deleteSession(long characterId) throws SQLException {
        return characterRepository.delete(characterId);
    }

    /**
     * Rebuilds runtime equipment from equipped inventory entries.
     *
     * @param player loaded player
     */
    private void restoreEquippedItems(PlayerCharacter player) {
        for (InventoryEntry entry : player.getInventory().getEntries()) {
            if (!entry.isEquipped()) {
                continue;
            }

            Item item = entry.getItem();

            if (item instanceof Armour armour) {
                player.equipArmour(armour);
            } else if (item instanceof Shield shield) {
                player.equipShield(shield);
            } else if (item instanceof Scroll scroll) {
                player.equipScroll(scroll);
            } else if (item instanceof Weapon weapon) {
                player.equipWeapon(weapon);
            }
        }
    }
}
