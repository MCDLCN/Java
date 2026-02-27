import java.util.Scanner;  // Import the Scanner class

//Defining the class moving
public class OldMoving {

    //main function
    public static void main(String[] args){
        //used to read the user's input
        Scanner myObj = new Scanner(System.in);
        int truckCapacity = 0;
        int numberBoxes = 0;

        //Prompting the user for a truck capacity then checking if it's valid
        System.out.println("Enter truck capacity");
        if (myObj.hasNextInt()){
            truckCapacity = myObj.nextInt();
            if (truckCapacity < 0){
                System.out.println("Truck capacity is negative");
                System.exit(0);
            }
        } else{
            System.out.println("Enter a valid truck capacity");
            System.exit(0);
        }



        //Prompting the user for the number of boxes then checking if it's valid
        System.out.println("Enter number of boxes");
        if (myObj.hasNextInt()){
            numberBoxes = myObj.nextInt();
            if (numberBoxes < 0){
                System.out.println("Number of boxes is negative");
                System.exit(0);
            }
        } else{
            System.out.println("Enter a valid truck capacity");
            System.exit(0);
        }

        //if previous checks passed we loop to calculate
        int i = 1;
        String ln ="";
        while (numberBoxes>0){

            if (truckCapacity < numberBoxes) {
                numberBoxes -= truckCapacity;
                ln+="The truck moved " + truckCapacity + " boxes \n";
            } else {
                ln+="The truck moved " + numberBoxes + " boxes \n";
                numberBoxes = 0;
            }

            ln+="This was trip number " + i+"\n";
            ln+=numberBoxes + " boxes left\n";
            i++;
        }
        System.out.println(ln);
    }
}