package dto;

import main_logic.enums.CharacterClass;

/**
 * Represents one available player class loaded from the class catalog.
 *
 * @param id persisted class id
 * @param name display name used in menus and persistence mapping
 */
public record CharacterClassData(long id, CharacterClass name) {


    public CharacterClass getName() {
        return name;
    }
}
