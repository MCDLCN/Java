package main_logic;

import dto.CharacterClassData;
import utilities.Console;
import main_logic.enums.MainChoice;

import java.util.List;
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
        Console.print("2) List all characters");
        Console.print("3) Load game");

        int saveOption = -1;
        int displayOption = -1;
        int editOption = -1;
        int startOption = -1;
        int deleteOption = -1;
        int quitOption;

        int nextIndex = 4;

        if (hasCharacter) {
            saveOption = nextIndex++;
            displayOption = nextIndex++;
            editOption = nextIndex++;
            deleteOption = nextIndex++;
            startOption = nextIndex++;

            Console.print(saveOption + ") Save character");
            Console.print(displayOption + ") Display character info");
            Console.print(editOption + ") Edit character name");
            Console.print(deleteOption + ") Delete character");
            Console.print(startOption + ") Play");
        }

        quitOption = nextIndex;
        Console.print(quitOption + ") Quit");

        int choice = askInt("Choice: ", 1, quitOption);

        if (choice == 1) return MainChoice.CREATE_CHARACTER;
        if (choice == 2) return MainChoice.DISPLAY_ALL_CHARACTERS;
        if (choice == 3) return MainChoice.LOAD_SAVE;
        if (hasCharacter && choice == saveOption) return MainChoice.SAVE_GAME;
        if (hasCharacter && choice == displayOption) return MainChoice.DISPLAY_CHARACTER;
        if (hasCharacter && choice == editOption) return MainChoice.EDIT_NAME;
        if (hasCharacter && choice == deleteOption) return MainChoice.DELETE_CHARACTER;
        if (hasCharacter && choice == startOption) return MainChoice.START_GAME;
        return MainChoice.QUIT;
    }


    /**
     * Prompts the user to choose a character class from the database catalog.
     *
     * @param availableClasses available character classes
     * @return the selected class row
     */
    public CharacterClassData askCharacterClass(List<CharacterClassData> availableClasses) {
        if (availableClasses.isEmpty()) {
            throw new IllegalArgumentException("No character classes are available.");
        }

        Console.print("\nChoose a class:");

        for (int i = 0; i < availableClasses.size(); i++) {
            Console.print((i + 1) + ") " + availableClasses.get(i).name());
        }

        int choice = askInt("Choice: ", 1, availableClasses.size());
        return availableClasses.get(choice - 1);
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

    public Long askLong(String prompt, Long min, Long max) {
        while (true) {
            Console.print(prompt);
            if (scanner.hasNextLong()) {
                Long v = scanner.nextLong();
                scanner.nextLine();
                if (v >= min && v <= max) return v;
            } else {
                scanner.nextLine();
            }
            Console.print("Enter a id between " + min + " and " + max + ".");
        }
    }
}
