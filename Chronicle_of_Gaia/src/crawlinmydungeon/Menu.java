package crawlinmydungeon;

import Utilities.Console;
import crawlinmydungeon.enums.CharacterType;
import crawlinmydungeon.enums.MainChoice;

import java.util.Scanner;

/**
 * Menu class.
 */
public class Menu {

    /**
     * Scanner.
     *
     * @param System.in system in.
     *
     * @return result.
     */
    private final Scanner scanner = new Scanner(System.in);
    

    /**
     * Show main menu.
     *
     * @param hasCharacter has character.
     *
     * @return result.
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
     * Ask character type.
     *
     * @return result.
     */
    public CharacterType askCharacterType() {
        Console.print("\nChoose a class:");
        Console.print("1) Warrior");
        Console.print("2) Wizard");
        int choice = askInt("Choice: ", 1, 2);
        return choice == 1 ? CharacterType.WARRIOR : CharacterType.WIZARD;
    }

    /**
     * Ask name.
     *
     * @return result.
     */
    public String askName() {
        Console.print("Enter name: ");
        return scanner.nextLine();
    }


    /**
     * Ask int.
     *
     * @param prompt prompt.
     * @param min min.
     * @param max max.
     *
     * @return result.
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
     * Show message.
     *
     * @param message message.
     */
    public void showMessage(String message) {
        Console.print(message);
    }
}