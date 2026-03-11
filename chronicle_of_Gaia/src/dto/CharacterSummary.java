package dto;

/**
 * Represents a lightweight view of a stored character.
 *
 * <p>This class is used when listing available characters
 * without loading the full character object.</p>
 */
public record CharacterSummary(Long id, String name, String className, int level) {

    /**
     * Creates a new character summary.
     *
     * @param name the character name
     * @param className the character class name
     * @param level the character level
     * @param id the character id
     */
    public CharacterSummary {
    }

    @Override
    public String toString() {
        return name + " | " + className + " | Level " + level;
    }
}
