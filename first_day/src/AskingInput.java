import java.util.Scanner;

/**
 * Contains a scanner that will be used to get users' input
 *
 * @author me c:
 */
public class AskingInput {
    /**
     * That's the scanner
     */
    public Scanner myObj;

    /**
     * Constructor for AskingInput
     */
    public AskingInput(){
        this.myObj = new Scanner(System.in);
    }

    /**
     * Used to ask an input and make sure it's natural positive integer
     * @param question String that contains the question that'll be asked before getting the input
     * @return the integer input by the user if it's valid
     */
    public int askForInt(String question){
        int toReturn;
        while (true) {
            System.out.println(question);
            String input = this.myObj.nextLine();

            try {
                toReturn = Integer.parseInt(input);

                if (toReturn < 0) {
                    System.out.println("Truck capacity cannot be negative.");
                } else {
                    break;
                }

            } catch (NumberFormatException e) {
                System.out.println("Enter a valid truck capacity.");
            }
        }
        return toReturn;
    }
}
