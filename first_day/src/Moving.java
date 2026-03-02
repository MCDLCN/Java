/**
 * Will do the heavy lifting
 * contains a truck and boxes
 * contains the moving process
 */
public class Moving {
    /**
     * Object of the truck class
     */
    Truck truck;
    /**
     * Object of the boxes class
     */
    Boxes boxes;
    /**
     * Object of AskingInput class
     */
    AskingInput asker = new AskingInput();

    /**
     * Constructor for Moving
     * will ask for user input upon instantiating
     */
    public Moving(){
        this.truck=this.gettingTruck();
        this.boxes=this.gettingBoxes();
    }

    /**
     * Will ask the user to give a capacity for the truck
     * @return a truck object
     */
    private Truck gettingTruck(){
         return  new Truck(asker.askForInt("Enter truck capacity"));
    }

    /**
     * Will ask the user to give a number of boxes
     * @return a boxes object
     */
    private Boxes gettingBoxes(){
        return  new Boxes(asker.askForInt("Enter number of boxes"));
    }

    /**
     * Use the instance's truck and boxes to see how many trips it'll take to deliver everything
     * then print out each trip
     */
    public void move(){
        int i = 1;
        String ln ="";
        while (this.boxes.numberBoxes>0){

            if (this.truck.capacity < this.boxes.numberBoxes) {
                this.boxes.numberBoxes -= this.truck.capacity;
                ln+="The truck moved " + this.truck.capacity + " boxes \n";
            } else {
                ln+="The truck moved " + this.boxes.numberBoxes + " boxes \n";
                this.boxes.numberBoxes = 0;
            }

            ln+="This was trip number " + i+"\n";
            ln+=this.boxes.numberBoxes + " boxes left\n";
            i++;
        }
        System.out.println(ln);
    }
}
