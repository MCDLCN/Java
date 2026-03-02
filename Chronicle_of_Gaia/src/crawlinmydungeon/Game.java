package crawlinmydungeon;

import Utilities.Console;
import crawlinmydungeon.dice.Dice;
import crawlinmydungeon.enums.CharacterMenuChoice;
import crawlinmydungeon.enums.CharacterType;
import crawlinmydungeon.enums.MainChoice;
import crawlinmydungeon.enums.Stat;
import entities.Creature;
import entities.classes.Warrior;
import entities.classes.Wizard;
import tile.Tile;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * This will be the game's logic
 */
public class Game {
    /**
     * The board where we'll play
     */
    private Board board;
    /**
     * This is the player
     */
    protected Creature player;

    private Menu menu;

    public Game() {
        this.menu = new Menu();
    }

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

    private void displayCharacter() {
        if (player == null) {
            menu.showMessage("No character created yet.");
            return;
        }
        menu.showMessage(player.toString());
    }
    
    private void editCharacterName() {
        if (player == null) {
            menu.showMessage("No character created yet.");
            return;
        }
        String newName = menu.askName();
        player.setName(newName);
        menu.showMessage("Name updated.");
    }

    private void modifyCharacter() {
        String newName = menu.askName();
        player.setName(newName);
    }

    public void play() {
        Dice mainRoll = new Dice(8);
        this.board = new Board();
        while (true) {
            if (this.board.getPosition() == 64){
                Console.print("You reached the end, congratulation", Console.ConsoleColor.BRIGHT_PURPLE);
                break;
            }
            else {
                Console.print("Time to roll!", Console.ConsoleColor.GREEN);
                int roll = mainRoll.roll();
                Tile tile = board.moving(roll);
                Console.print("You rolled a "+roll+" and landed on tile n° "+this.board.getPosition(), Console.ConsoleColor.BRIGHT_PURPLE);
                Console.print(tile.describe(), Console.ConsoleColor.CYAN);
                tile.onEnter(player);
            }
        }
    }

}
