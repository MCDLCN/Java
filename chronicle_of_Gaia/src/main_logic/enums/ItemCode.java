package main_logic.enums;

public enum ItemCode {
    FIREBALL_SCROLL("Fireball Scroll"),
    LIGHTNING_BOLT_SCROLL("Lightning Bolt Scroll"),

    LONGSWORD("Longsword"),
    GREATSWORD("Greatsword"),
    CLUB("Club"),

    STANDARD_HEALING_POTION("Standard Healing Potion"),
    LARGE_HEALING_POTION("Large Healing Potion"),
    POTION_OF_DAMAGE("Potion of Damage"),

    FULL_PLATE("Full Plate"),
    STUDDED_LEATHER("Studded Leather"),
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
