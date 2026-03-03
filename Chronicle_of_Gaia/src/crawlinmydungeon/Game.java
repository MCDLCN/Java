package crawlinmydungeon;

import Utilities.Console;
import crawlinmydungeon.dice.Dice;
import crawlinmydungeon.enums.CharacterType;
import crawlinmydungeon.enums.MainChoice;
import crawlinmydungeon.enums.Stat;
import entities.Creature;
import entities.classes.Warrior;
import entities.classes.Wizard;
import tile.Tile;

import java.util.EnumMap;
import java.util.Map;

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
    protected Creature player;

    /**
     * Menu handler responsible for user interaction via the console.
     */
    private Menu menu;

    /**
     * Player position
     */
    private int playerPosition;

    /**
     * Creates a new Game instance.
     */
    public Game() {
        this.menu = new Menu();
    }

    /**
     * Runs the main menu loop until the user chooses to quit.
     */
    public void start() {
        boolean running = true;

        while (running) {
            MainChoice choice = menu.showMainMenu(player != null);

            switch (choice) {
                case CREATE_CHARACTER -> createCharacter();
                case DISPLAY_CHARACTER -> displayCharacter();
                case EDIT_NAME -> editCharacterName();
                case START_GAME -> play();
                case QUIT -> running = false;
            }
        }
    }

    /**
     * Creates a new player character using menu prompts and point-buy stats.
     */
    private void createCharacter() {
        CharacterType type = menu.askCharacterType();
        String name = menu.askName();

        Map<Stat,Integer> stats = pointBuyStats(menu);

        if (type == CharacterType.WARRIOR) {
            player = new Warrior(1,name , stats );
        } else {
            player = new Wizard(1, name , stats );
        }

        menu.showMessage("Character created!");
    }


    /**
     * Point buy system
     * @param menu the menu used to handle the system
     * @return the stat map after successful assignment
     */
    private Map<Stat, Integer> pointBuyStats(Menu menu) {
        final int budget = 27;

        while (true) {
            Map<Stat, Integer> stats = new EnumMap<>(Stat.class);
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

                    stats.put(s, val);
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
    
    /**
     * Prompts for and applies a new character name.
     */
    private void editCharacterName() {
        if (player == null) {
            menu.showMessage("No character created yet.");
            return;
        }
        String newName = menu.askName();
        player.setName(newName);
        menu.showMessage("Name updated.");
    }

    /**
     * modifyCharacter operation.
     */
    private void modifyCharacter() {
        String newName = menu.askName();
        player.setName(newName);
    }

    /**
     * Runs the core board traversal loop. Each turn rolls movement, advances the board position, and triggers the tile effect. The game ends when the player reaches or overshoots the final tile (overshoot is represented by an exception).
     */
    public void play() {
        Dice mainRoll = new Dice(8);
        this.playerPosition = 0;
        this.board = new Board(64);
        while (true) {
            menu.showMessage("Time to roll!", Console.ConsoleColor.GREEN);
            int roll = mainRoll.roll();

            try {
                playerPosition = board.moving(playerPosition, roll);

                if (playerPosition == board.getLastTileIndex()) {
                    menu.showMessage(player.getName() + " wins!", Console.ConsoleColor.BRIGHT_PURPLE);
                    break;
                }

                Tile tile = board.getTile(playerPosition);

                menu.showMessage("You rolled a " + roll + " and landed on tile n° " + playerPosition,
                        Console.ConsoleColor.BRIGHT_PURPLE);
                menu.showMessage(tile.describe(), Console.ConsoleColor.CYAN);

                tile.onEnter(player);

            } catch (Board.OutOfBoardException e) {
                menu.showMessage(player.getName() + " wins!", Console.ConsoleColor.BRIGHT_PURPLE);
                break;
            }
        }
    }

}