package main_logic.enums;

public enum ItemCode {
    LONGSWORD("Longsword"),
    GREATSWORD("Greatsword"),
    POTION_OF_HEALING("Potion of Healing"),
    POTION_OF_DAMAGE("Potion of Damage"),
    FULL_PLATE("Full Plate"),
    STUDDED_LEATHER("Studed Leather"),
    BREASTPLATE("Breastplate"),
    SHIELD("Shield");

    private final String displayName;

    ItemCode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
