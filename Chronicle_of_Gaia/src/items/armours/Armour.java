package items.armours;

public class Armour {

    private int ac;
    private String name;
    private String description;
    private ArmourType type;

    public Armour(int ac, String name, String description, ArmourType type) {
        this.ac = ac;
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public int getAc() {
        return ac;
    }

    public ArmourType getType() {
        return type;
    }

    public enum ArmourType {
        LIGHT,
        MEDIUM,
        HEAVY
    }
}
