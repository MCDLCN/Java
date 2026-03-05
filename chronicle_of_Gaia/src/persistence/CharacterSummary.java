package persistence;

import main_logic.enums.CharacterType;

/**
 * Represents a lightweight view of a stored character.
 *
 * <p>This class is used when listing available characters
 * without loading the full character object.</p>
 */
public class CharacterSummary {

    private final String name;
    private final CharacterType type;
    private final int level;

    /**
     * Creates a new character summary.
     *
     * @param name the character name
     * @param type the character class type
     * @param level the character level
     */
    public CharacterSummary(String name, CharacterType type, int level) {
        this.name = name;
        this.type = type;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public CharacterType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return name + " | " + type + " | Level " + level;
    }
}