package items.defensives;

/**
 * Defensive equipement class.
 */
public abstract class DefensiveEquipement {
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
     * Defensive equipement.
     *
     * @param ac ac.
     * @param name name.
     * @param description description.
     */
    protected DefensiveEquipement(int ac, String name, String description) {
        this.ac = ac;
        this.name = name;
        this.description = description;
    }

    /**
     * Get ac.
     *
     * @return result.
     */
    public int getAc() {
        return ac;
    }
    /**
     * Get name.
     *
     * @return result.
     */
    public String getName() {
        return name;
    }
    /**
     * Get description.
     *
     * @return result.
     */
    public String getDescription() {
        return description;
    }
}
