public class Moving {
    Truck truck;
    Boxes boxes;
    AskingInput asker = new AskingInput();
    public Moving(){
        this.truck=this.gettingTruck();
        this.boxes=this.gettingBoxes();
    }

    private Truck gettingTruck(){
         return  new Truck(asker.askForInt("Enter truck capacity"));
    }

    private Boxes gettingBoxes(){
        return  new Boxes(asker.askForInt("Enter number of boxes"));
    }
    
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
