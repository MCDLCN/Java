import java.util.Scanner;

public class AskingInput {
    public Scanner myObj;
    public AskingInput(){
        this.myObj = new Scanner(System.in);
    }

    // Asking a number and checking if it's a natural positive integer
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
