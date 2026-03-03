package crawlinmydungeon;

import Utilities.Console;
import crawlinmydungeon.enums.CharacterType;
import crawlinmydungeon.enums.MainChoice;

import java.util.Scanner;

/**
 * Handles all console I/O for menus and validated user input using the shared Console helper.
 */
public class Menu {

    private final Scanner scanner = new Scanner(System.in);
    

    /**
     * Displays the main menu and returns the selected action.
     * @param hasCharacter display more menu if a character exists.
     * @return Result value.
     */
    public MainChoice showMainMenu(boolean hasCharacter) {
        Console.print("\n=== Main Menu ===");
        Console.print("1) Create character");

        int displayOption = -1;
        int editOption = -1;
        int startOption = -1;
        int quitOption;

        int nextIndex = 2;

        if (hasCharacter) {
            displayOption = nextIndex++;
            editOption = nextIndex++;
            startOption = nextIndex++;

            Console.print(displayOption + ") Display character info");
            Console.print(editOption + ") Edit character name");
            Console.print(startOption + ") Start game");
        }

        quitOption = nextIndex;
        Console.print(quitOption + ") Quit");

        int choice = askInt("Choice: ", 1, quitOption);

        if (choice == 1) return MainChoice.CREATE_CHARACTER;
        if (hasCharacter && choice == displayOption) return MainChoice.DISPLAY_CHARACTER;
        if (hasCharacter && choice == editOption) return MainChoice.EDIT_NAME;
        if (hasCharacter && choice == startOption) return MainChoice.START_GAME;
        return MainChoice.QUIT;
    }


    /**
     * Prompts the user to choose a character class.
     * @return The selected class.
     */
    public CharacterType askCharacterType() {
        Console.print("\nChoose a class:");
        Console.print("1) Warrior");
        Console.print("2) Wizard");
        int choice = askInt("Choice: ", 1, 2);
        return choice == 1 ? CharacterType.WARRIOR : CharacterType.WIZARD;
    }

    /**
     * Prompts the user for a character name.
     * @return The new name entered by the player.
     */
    public String askName() {
        Console.print("Enter name: ");
        return scanner.nextLine();
    }


    /**
     * Prompts repeatedly until the user provides an integer within the specified bounds.
     * @param prompt prompt text.
     * @param min min value.
     * @param max max value.
     * @return the user's input if it successfully pass the checks.
     */
    public int askInt(String prompt, int min, int max) {
        while (true) {
            Console.print(prompt);
            if (scanner.hasNextInt()) {
                int v = scanner.nextInt();
                scanner.nextLine();
                if (v >= min && v <= max) return v;
            } else {
                scanner.nextLine();
            }
            Console.print("Enter a number between " + min + " and " + max + ".");
        }
    }

    /**
     * Display the message.
     * @param message message value.
     */
    public void showMessage(String message) {
        Console.print(message);
    }

    /**
     * Display the message with a specific color.
     * @param message message value.
     * @param color color value.
     */
    public void showMessage(String message, Console.ConsoleColor color){
        Console.print(message, color);
    }
}
