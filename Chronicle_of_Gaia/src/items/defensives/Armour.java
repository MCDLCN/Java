package items.defensives;

/**
 * Armour class.
 */
public class Armour extends DefensiveEquipement{

    /**
     * Ac.
     */
    private int ac;
    /**
     * Name.
     */
    private String name;
    /**
     * Description.
     */
    private String description;
    /**
     * Type.
     */
    private ArmourType type;

    /**
     * Armour.
     *
     * @param ac ac.
     * @param name name.
     * @param description description.
     * @param type type.
     */
    public Armour(int ac, String name, String description, ArmourType type) {
        super(ac, name, description);
        this.type = type;
    }

    /**
     * Get type.
     *
     * @return result.
     */
    public ArmourType getType() {
        return type;
    }

    /**
     * Armour type enum.
     */
    public enum ArmourType {
        LIGHT,
        MEDIUM,
        HEAVY
    }
}
