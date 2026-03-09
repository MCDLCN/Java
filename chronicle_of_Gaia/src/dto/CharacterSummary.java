package dto;

import main_logic.enums.CharacterType;

/**
 * Represents a lightweight view of a stored character.
 *
 * <p>This class is used when listing available characters
 * without loading the full character object.</p>
 */
public record CharacterSummary(Long id, String name, CharacterType type, int level) {

    /**
     * Creates a new character summary.
     *
     * @param name  the character name
     * @param type  the character class type
     * @param level the character level
     * @param id    the character id
     */
    public CharacterSummary {
    }

    @Override
    public String toString() {
        return name + " | " + type + " | Level " + level;
    }
}