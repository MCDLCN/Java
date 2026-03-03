package items.defensives;

/**
 * Defensive equipment that affects Armor Class (AC) depending on armor type and dexterity modifier rules.
 */
public class Armour extends DefensiveEquipment {

    /**
     * Armor Class used for determining whether attacks hit.
     */
    private int ac;
    /**
     * Display name of the creature.
     */
    private String name;
    /**
     * description field.
     */
    private String description;
    /**
     * type field.
     */
    private ArmourType type;

    /**
     * Creates a new Armour instance.
     * @param ac ac value.
     * @param name name value.
     * @param description description value.
     * @param type type value.
     */
    public Armour(int ac, String name, String description, ArmourType type) {
        super(ac, name, description);
        this.type = type;
    }

    /**
     * getType operation.
     * @return Requested value.
     */
    public ArmourType getType() {
        return type;
    }

    /**
     * Enumeration of the three type of armour there is.
     */
    public enum ArmourType {
        LIGHT,
        MEDIUM,
        HEAVY
    }
}