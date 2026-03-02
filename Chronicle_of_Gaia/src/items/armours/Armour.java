package items.armours;

public class Armour {
    int ac;
    String name;
    String description;
    enum type {LIGHT, MEDIUM, HEAVY};

    public  Armour(int ac, String name, String description, String type){
        this.ac = ac;
    }
}
