package items.defensives;

/**
 * Base type for defensive equipment items such as armor and shields.
 */
public abstract class DefensiveEquipment {
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
     * Creates a new DefensiveEquipment instance.
     * @param ac ac value.
     * @param name name value.
     * @param description description value.
     */
    protected DefensiveEquipment(int ac, String name, String description) {
        this.ac = ac;
        this.name = name;
        this.description = description;
    }

    /**
     * getAc operation.
     * @return Requested value.
     */
    public int getAc() {
        return ac;
    }
    /**
     * getName operation.
     * @return Requested value.
     */
    public String getName() {
        return name;
    }
    /**
     * getDescription operation.
     * @return Requested value.
     */
    public String getDescription() {
        return description;
    }
}