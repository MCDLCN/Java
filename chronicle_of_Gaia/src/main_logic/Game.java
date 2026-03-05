package main_logic;

import main_logic.enums.EncounterResult;
import model.entities.Stats;
import model.entities.classes.PlayerCharacter;
import persistence.BoardRepository;
import persistence.CharacterRepository;
import persistence.CharacterSummary;
import persistence.LoadedGame;
import utilities.Console;
import main_logic.dice.Dice;
import main_logic.enums.CharacterType;
import main_logic.enums.MainChoice;
import main_logic.enums.Stat;
import model.entities.classes.Warrior;
import model.entities.classes.Wizard;
import tile.Tile;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Coordinates the main game loop: character creation, menu navigation, and board traversal. A game is won by landing exactly on the final tile or by rolling beyond it (handled via an exception).
 */
public class Game {
    /**
     * Board instance containing tiles and current position.
     */
    private Board board;
    /**
     * Current player character controlled by the user.
     */
    protected PlayerCharacter player;

    /**
     * Menu handler responsible for user interaction via the console.
     */
    private Menu menu;

    /**
     * To handle the interaction between the game and the database for the character
     */
    private final CharacterRepository characterRepository;

    /**
     * To handle the interaction between the game and the database for the board
     */
    private final BoardRepository boardRepository;

    /**
     * Stores the active save slot id for the current run.
     */
    private long saveId;

    /**
     * Creates the game and initializes repositories.
     *
     * @throws SQLException if database initialization fails
     */
    public Game() throws SQLException {
        this.menu = new Menu();
        this.boardRepository = new BoardRepository();
        this.characterRepository = new CharacterRepository();
    }

    /**
     * Runs the main menu loop until the user chooses to quit.
     */
    public void start() throws SQLException {
        boolean running = true;

        while (running) {
            MainChoice choice = menu.showMainMenu(player != null);

            switch (choice) {
                case CREATE_CHARACTER -> createCharacter();
                case DISPLAY_ALL_CHARACTERS -> showAllCharacters();
                case LOAD_SAVE ->loadGame();
                case DISPLAY_CHARACTER -> displayCharacter();
                case SAVE_GAME  -> saveGame();
                case EDIT_NAME -> editCharacterName();
                case DELETE_CHARACTER -> deleteCharacter();
                case START_GAME -> play();
                case QUIT -> running = false;
            }
        }
    }

    //------ Main menu ------

    /**

     * Creates a new player character.
     *
     * <p>This method:
     * <ul>
     * ```
     <li>Asks the player to choose a character type.</li>
     ```
     * ```
     <li>Prompts for the character's name.</li>
     ```
     * ```
     <li>Generates the character's stats using the point-buy system.</li>
     ```
     * ```
     <li>Instantiates the corresponding player character class.</li>
     ```
     * </ul>
     *
     * <p>A switch expression is used so new character classes can easily
     * be added in the future (e.g. Rogue, Paladin, Ranger).</p>
     */
    private void createCharacter() throws SQLException {

        CharacterType type = menu.askCharacterType();
        String name = menu.askName();

        Stats stats = pointBuyStats(menu);

        player = switch (type) {
            case WARRIOR -> new Warrior(1, name, stats);
            case WIZARD  -> new Wizard(1, name, stats);
        };

        saveId = boardRepository.createSave();
        board = new Board(64);
        boardRepository.saveBoard(saveId, board.toTileRows());
        characterRepository.save(saveId, player);

        menu.showMessage("Character created!");
    }


    /**
    * Displays the current character summary and key stats.
    */
    private void displayCharacter() {
        if (player == null) {
            menu.showMessage("No character created yet.");
            return;
        }
        menu.showMessage(player.toString());
    }


    //------ Character repo ------


    /**
     * Prompts for and applies a new character name.
     */
    private void editCharacterName() throws SQLException {
        if (player == null) {
            menu.showMessage("No character selected yet.");
            return;
        }
        String newName = menu.askName();
        player.setName(newName);
        this.saveCharacter();
        menu.showMessage("Name updated.");
    }


    /**
     * Prompts for a name and loads the corresponding character if it exists
     */
    /*private void loadCharacter() throws SQLException {
        String name  = menu.askName();
        
        Optional<LoadedGame> loaded = this.characterRepository.load(name);
        if (loaded.isPresent()) {
            player = loaded.get().getPlayer();
        } else {
            menu.showMessage("Character not found.");
        }
    }*/

    /**
     * Save the current player character
     * @throws SQLException if connection fails
     */
    private void saveCharacter() throws SQLException {
        
        this.characterRepository.save(this.saveId, this.player);
    }

    /**
     * Display all saved characters
     * @throws SQLException if connection fails
     */
    private void showAllCharacters() throws SQLException {
        
        List<CharacterSummary> characters = this.characterRepository.listCharacters();

        for (CharacterSummary c : characters) {
            menu.showMessage(c.toString());
        }

    }

    /**
     * Delete the current character
     * @throws SQLException if connection fails
     */
    private void deleteCharacter() throws SQLException {
        if(this.characterRepository.delete(this.player.getName())){
            menu.showMessage("Character deleted.");
        };
        this.player = null;
    }


    //------ Board repo ------


    /**
     * Saves the current game state.
     * @throws SQLException if connection fails
     */
    private void saveGame() throws SQLException {
        this.characterRepository.save(saveId, player);
        this.boardRepository.saveBoard(saveId, board.toTileRows());
        menu.showMessage("Game saved");
    }

    /**
     * Loads the game state.
     * @throws SQLException if connection fails
     */
    private void loadGame() throws SQLException {
        String name  = menu.askName();

        Optional<LoadedGame> result = characterRepository.load(name);

        if (result.isEmpty()) {
            menu.showMessage("Character not found.");
            return;
        }

        LoadedGame loaded = result.get();

        this.saveId = loaded.getSaveId();
        this.player = loaded.getPlayer();

        List<BoardRepository.TileRow> rows = boardRepository.loadBoard(saveId);

        this.board = new Board(64);
        board.applyTileRows(rows);
        menu.showMessage("Game loaded");
    }

    //------ Running ------

    /**
     * Runs the core board traversal loop. Each turn rolls movement, advances the board position, and triggers the tile effect. The game ends when the player reaches or overshoots the final tile (overshoot is represented by an exception).
     */
    public void play() {
        Dice mainRoll = new Dice(8);
        if (this.board == null) {
            this.board = new Board(64);
        };
        while (true) {
            menu.showMessage("Time to roll!", Console.ConsoleColor.GREEN);
            int roll = mainRoll.roll();

            try {
                this.player.setPosition(board.moving(this.player.getPosition(), roll));

                if (this.player.getPosition() == board.getLastTileIndex()) {
                    menu.showMessage(player.getName() + " wins!", Console.ConsoleColor.BRIGHT_PURPLE);
                    break;
                }

                Tile tile = board.getTile(this.player.getPosition());

                menu.showMessage("You rolled a " + roll + " and landed on tile n° " + this.player.getPosition(),
                        Console.ConsoleColor.BRIGHT_PURPLE);
                menu.showMessage(tile.describe(), Console.ConsoleColor.CYAN);

                EncounterResult result = tile.onEnter(this);
                if (result == EncounterResult.VICTORY){
                    this.board.setEmpty(this.player.getPosition());
                } else if (result == EncounterResult.DEFEAT){
                    Console.print("You lost, loser", Console.ConsoleColor.GOLD);
                }

            } catch (Board.OutOfBoardException e) {
                menu.showMessage(player.getName() + " wins!", Console.ConsoleColor.BRIGHT_PURPLE);
                break;
            }
        }
    }

    /**
     * Starts a new game by creating a save slot and generating a fresh board.
     */
    private void startNewGame() throws SQLException {
        saveId = boardRepository.createSave();
        this.board.fill();
        boardRepository.saveBoard(saveId, board.toTileRows());
        this.player.setPosition(0);
        this.play();
    }

    //------ Logic ------

    /**
     * Point buy system
     * @param menu the menu used to handle the system
     * @return the stat map after successful assignment
     */
    private Stats pointBuyStats(Menu menu) {
        final int budget = 27;

        while (true) {
            Stats stats = new Stats();
            int spent = 0;

            menu.showMessage("Point-buy: pick values 8..15. Budget: 27 points.");
            menu.showMessage("Costs: 8=0, 9=1, 10=2, 11=3, 12=4, 13=5, 14=7, 15=9\n");

            for (Stat s : Stat.values()) {
                while (true) {
                    int remaining = budget - spent;
                    menu.showMessage("Points left: " + remaining);

                    int val = menu.askInt("Set " + s + " (8-15): ", 8, 15);
                    int cost = pointBuyCost(val);

                    if (cost > remaining) {
                        menu.showMessage("Not enough points for " + s + "=" + val +
                                " (cost " + cost + "). Try a lower value.\n");
                        continue;
                    }

                    stats.set(s, val);
                    spent += cost;

                    menu.showMessage(s + " set to " + val + " (cost " + cost + ").");
                    menu.showMessage("Points left after " + s + ": " + (budget - spent) + "\n");
                    break;
                }
            }

            menu.showMessage("Final spend: " + spent + "/27");
            return stats;
        }
    }

    /**
     * Returns the point-buy cost for a given stat value.
     * @param score score value.
     * @return Result value.
     */
    private int pointBuyCost(int score) {
        return switch (score) {
            case 8  -> 0;
            case 9  -> 1;
            case 10 -> 2;
            case 11 -> 3;
            case 12 -> 4;
            case 13 -> 5;
            case 14 -> 7;
            case 15 -> 9;
            default -> throw new IllegalArgumentException("Score must be 8..15");
        };
    }

}