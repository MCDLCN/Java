package main_logic;

import dto.CharacterClassData;
import dto.CharacterSummary;
import main_logic.enums.EncounterResult;
import main_logic.enums.MainChoice;
import main_logic.enums.Stat;
import main_logic.service.GameSessionService;
import main_logic.service.InventoryConsoleService;
import main_logic.session.GameSession;
import model.entities.Stats;
import model.entities.classes.PlayerCharacter;
import model.entities.evilaaaneighbours.Enemy;
import model.inventory.InventoryEntry;
import utilities.Console;
import main_logic.dice.Dice;
import tile.Tile;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static main_logic.service.InventoryConsoleService.applyCombatItemInteraction;
import static main_logic.service.InventoryConsoleService.applyItemInteraction;

/**
 * Coordinates the main game loop: character creation, menu navigation, and board traversal.
 * A game is won by landing exactly on the final tile or by rolling beyond it.
 */
public class Game {

    /**
     * Menu handler responsible for user interaction via the console.
     */
    private final Menu menu;

    /**
     * Coordinates persistence workflows for game sessions.
     */
    private final GameSessionService gameSessionService;

    /**
     * Active runtime game session.
     */
    private GameSession session;



    /**
     * Creates the game and initializes required services.
     *
     * @throws SQLException if database initialization fails
     */
    public Game() throws SQLException {
        this.menu = new Menu();
        this.gameSessionService = new GameSessionService();
    }

    /**
     * Runs the main menu loop until the user chooses to quit.
     *
     * @throws SQLException if a persistence operation fails
     */
    public void start() throws SQLException {
        boolean running = true;

        while (running) {
            MainChoice choice = menu.showMainMenu(session != null);

            switch (choice) {
                case CREATE_CHARACTER -> createCharacter();
                case DISPLAY_ALL_CHARACTERS -> showAllCharacters();
                case LOAD_SAVE -> loadGame();
                case DISPLAY_CHARACTER -> displayCharacter();
                case SAVE_GAME -> saveGame();
                case EDIT_NAME -> editCharacterName();
                case DELETE_CHARACTER -> deleteCharacter();
                case START_GAME -> play();
                case QUIT -> running = false;
            }
        }
    }

    //------ Main menu ------

    /**
     * Creates a new player character and persists its initial game session.
     *
     * @throws SQLException if persistence fails
     */
    private void createCharacter() throws SQLException {
        List<CharacterClassData> availableClasses = gameSessionService.listAvailableClasses();
        CharacterClassData selectedClass = menu.askCharacterClass(availableClasses);
        String name = menu.askName();
        Stats stats = pointBuyStats(menu);

        session = gameSessionService.createSession(selectedClass, name, stats);
        menu.showMessage("Character created!");
    }

    /**
     * Displays the current character summary and key stats.
     */
    private void displayCharacter() {
        if (session == null) {
            menu.showMessage("No character created yet.");
            return;
        }
        menu.showMessage(session.getPlayer().toString());
    }

    //------ Character session ------

    /**
     * Prompts for and applies a new character name.
     *
     * @throws SQLException if persistence fails
     */
    private void editCharacterName() throws SQLException {
        if (session == null) {
            menu.showMessage("No character selected yet.");
            return;
        }
        String newName = menu.askName();
        session.getPlayer().setName(newName);
        saveCharacter();
        menu.showMessage("Name updated.");
    }

    /**
     * Saves the current player character.
     *
     * @throws SQLException if persistence fails
     */
    private void saveCharacter() throws SQLException {
        if (session == null) {
            menu.showMessage("No character selected yet.");
            return;
        }
        gameSessionService.saveCharacter(session);
    }

    /**
     * Displays all saved characters.
     *
     * @return saved character summaries
     * @throws SQLException if loading fails
     */
    private List<CharacterSummary> showAllCharacters() throws SQLException {
        List<CharacterSummary> characters = gameSessionService.listCharacters();

        if (characters.isEmpty()) {
            menu.showMessage("No character created yet.");
        } else {
            int i = 1;
            for (CharacterSummary character : characters) {
                if (this.session != null) {
                    if (this.session.getCharacterId() == character.id()) {
                        menu.showMessage(i + " | " + character + " (currently selected)");
                    }
                } else {
                    menu.showMessage(i + " | " + character);
                }
                i++;
            }
        }

        return characters;
    }

    /**
     * Deletes the current character.
     *
     * @throws SQLException if deletion fails
     */
    private void deleteCharacter() throws SQLException {
        if (session == null) {
            menu.showMessage("No character selected yet.");
            return;
        }

        if (gameSessionService.deleteSession(session.getCharacterId())) {
            menu.showMessage("Character deleted.");
        }
        session = null;
    }

    //------ Save / load ------

    /**
     * Saves the current game state.
     *
     * @throws SQLException if persistence fails
     */
    private void saveGame() throws SQLException {
        if (session == null) {
            menu.showMessage("No character selected yet.");
            return;
        }
        gameSessionService.saveSession(session);
        menu.showMessage("Game saved");
    }

    /**
     * Loads the game state.
     *
     * @throws SQLException if loading fails
     */
    private void loadGame() throws SQLException {
        List<CharacterSummary> characters = showAllCharacters();

        if (characters.isEmpty()) {
            return;
        }

        int choice = menu.askInt("Enter character's number", 1, characters.size());
        long characterId = characters.get(choice - 1).id();

        Optional<GameSession> loadedSession = gameSessionService.loadSession(characterId);

        if (loadedSession.isEmpty()) {
            menu.showMessage("Character not found.");
            return;
        }

        session = loadedSession.get();
        menu.showMessage("Game loaded");
    }

    //------ Running ------

    /**
     * Runs the core board traversal loop.
     */
    public void play() {
        if (session == null) {
            menu.showMessage("No character selected yet.");
            return;
        }

        Dice mainRoll = new Dice(6);
        while (true) {
            menu.showMessage("Time to roll!", Console.ConsoleColor.GREEN);
            int roll = mainRoll.roll();

            try {
                session.getPlayer().setPosition(
                        session.getBoard().moving(session.getPlayer().getPosition(), roll)
                );

                if (session.getPlayer().getPosition() == session.getBoard().getLastTileIndex()) {
                    menu.showMessage(session.getPlayer().getName() + " wins!", Console.ConsoleColor.BRIGHT_PURPLE);
                    break;
                }

                Tile tile = session.getBoard().getTile(session.getPlayer().getPosition());

                menu.showMessage(
                        "You rolled a " + roll + " and landed on tile n° " + session.getPlayer().getPosition(),
                        Console.ConsoleColor.BRIGHT_PURPLE
                );
                menu.showMessage(tile.describe(), Console.ConsoleColor.CYAN);

                GameSession currentSession = session;
                EncounterResult result = tile.onEnter(this.session);
                if (result == EncounterResult.VICTORY) {
                    session.getBoard().setEmpty(session.getPlayer().getPosition());
                    if (session.hasPendingLevelUp()) {
                        handlePendingStatPoints(session.getPlayer());
                        session.setPendingLevelUp(false);
                    }
                } else if(result == EncounterResult.FLED) {
                    Console.print("You fled like a coward", Console.ConsoleColor.YELLOW);
                } else if (result == EncounterResult.DEFEAT) {
                    Console.print("You lost, loser", Console.ConsoleColor.GOLD);
                    this.session = currentSession;
                    break;
                }

                gameSessionService.saveSession(session);
                if (!handlePostTileMenu(tile)) {
                    return;
                }

            } catch (Board.OutOfBoardException e) {
                menu.showMessage(session.getPlayer().getName() + " wins!", Console.ConsoleColor.BRIGHT_PURPLE);
                break;
            } catch (SQLException e) {
                throw new RuntimeException("Failed to autosave game session.", e);
            }
        }
    }

    private boolean handlePostTileMenu(Tile tile) throws SQLException {
        boolean done = false;
        boolean tileActionUsed = false;

        while (!done) {
            Console.print("\n1) Manage inventory");
            Console.print("2) Inspect character");
            Console.print("3) Save");

            int nextOption = 4;
            int lastTileOption = nextOption - 1;

            if (!tileActionUsed) {
                lastTileOption = tile.printPostTileActions(nextOption);
                nextOption = lastTileOption + 1;
            }

            int mainMenuOption = nextOption++;
            int quitOption = nextOption++;
            int continueOption = nextOption;

            Console.print(mainMenuOption + ") Return to main menu");
            Console.print(quitOption + ") Quit game");
            Console.print(continueOption + ") Continue");

            int choice = menu.askInt("What do you want to do now? ", 1, continueOption);

            if (choice == 1) {
                handleItemInteraction(session.getPlayer());
                continue;
            }

            if (choice == 2) {
                Console.print(session.getPlayer().characterInspection());
                continue;
            }

            if (choice == 3) {
                gameSessionService.saveSession(session);
                Console.print("Game saved.", Console.ConsoleColor.GREEN);
                continue;
            }

            if (!tileActionUsed && tile.handlePostTileAction(session, choice)) {
                tileActionUsed = true;
                gameSessionService.saveSession(session);
                continue;
            }

            if (choice == mainMenuOption) {
                gameSessionService.saveSession(session);
                return false;
            }

            if (choice == quitOption) {
                gameSessionService.saveSession(session);
                System.exit(0);
            }

            if (choice == continueOption) {
                done = true;
            }
        }

        return true;
    }

    /**
     * Handles the optional item interaction .
     */
    private void handleItemInteraction(PlayerCharacter player) {
        boolean done = false;
        while (!done) {
            InventoryEntry selectedEntry = InventoryConsoleService.chooseInventoryEntry(player);
            if (selectedEntry == null) {
                return;
            }
            done = applyItemInteraction(player, selectedEntry);
        }
    }

    // ----- Logic -----

    /**
     * Point buy system.
     *
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

            for (Stat stat : Stat.values()) {
                while (true) {
                    int remaining = budget - spent;
                    menu.showMessage("Points left: " + remaining);

                    int value = menu.askInt("Set " + stat + " (8-15): ", 8, 15);
                    int cost = pointBuyCost(value);

                    if (cost > remaining) {
                        menu.showMessage("Not enough points for " + stat + "=" + value +
                                " (cost " + cost + "). Try a lower value.\n");
                        continue;
                    }

                    stats.set(stat, value);
                    spent += cost;

                    menu.showMessage(stat + " set to " + value + " (cost " + cost + ").");
                    menu.showMessage("Points left after " + stat + ": " + (budget - spent) + "\n");
                    break;
                }
            }

            menu.showMessage("Final spend: " + spent + "/27");
            return stats;
        }
    }

    /**
     * Returns the point-buy cost for a given stat value.
     *
     * @param score score value
     * @return point-buy cost
     */
    private int pointBuyCost(int score) {
        return switch (score) {
            case 8 -> 0;
            case 9 -> 1;
            case 10 -> 2;
            case 11 -> 3;
            case 12 -> 4;
            case 13 -> 5;
            case 14 -> 7;
            case 15 -> 9;
            default -> throw new IllegalArgumentException("Score must be 8..15");
        };
    }

    /**
     * Lets the player spend all pending stat points.
     */
    private void handlePendingStatPoints(PlayerCharacter player) {
        while (player.getUnspentStatPoints() > 0) {
            Console.print("\nLevel up! You have " + player.getUnspentStatPoints() + " stat point(s) to spend.",
                    Console.ConsoleColor.GREEN);

            Stat[] stats = Stat.values();

            for (int i = 0; i < stats.length; i++) {
                int value = player.getOneStat(stats[i]);
                String suffix = value >= 20 ? " (MAX)" : "";
                Console.print((i + 1) + ") " + stats[i] + " = " + value + suffix);
            }

            int choice = menu.askInt("Choose a stat to increase: ", 1, stats.length);
            Stat selectedStat = stats[choice - 1];

            if (!player.spendStatPoint(selectedStat)) {
                Console.print(selectedStat + " cannot be increased.", Console.ConsoleColor.YELLOW);
                continue;
            }

            Console.print(selectedStat + " increased to " + player.getOneStat(selectedStat) + ".",
                    Console.ConsoleColor.CYAN);
            Console.print("HP: " + player.getHp() + "/" + player.getMaxHp());
        }
    }
}
