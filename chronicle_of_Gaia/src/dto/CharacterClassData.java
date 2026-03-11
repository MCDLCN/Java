package dto;

/**
 * Represents one available player class loaded from the class catalog.
 *
 * @param id persisted class id
 * @param name display name used in menus and persistence mapping
 */
public record CharacterClassData(long id, String name) {

    @Override
    public String toString() {
        return name;
    }
}
