import java.util.Scanner;  // Import the Scanner class

public class Moving {

    public static void main(String[] args){
        Scanner myObj = new Scanner(System.in);
        int truckCapacity = 0;
        int numberBoxes = 0;

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


        int i = 1;
        while (numberBoxes>0){

            if (truckCapacity < numberBoxes) {
                numberBoxes -= truckCapacity;
                System.out.println("The truck moved " + truckCapacity + " boxes");
            } else {
                System.out.println("The truck moved " + numberBoxes + " boxes");
                numberBoxes = 0;
            }

            System.out.println("This the trip number " + i);
            System.out.println(numberBoxes + " boxes left");
            i++;
        }

    }
}