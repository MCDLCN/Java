package model.entities.classes;

import main_logic.enums.CharacterType;
import model.entities.Stats;

/**
 * Magic-focused player character class.
 */
public class Wizard extends PlayerCharacter {

    private static final int HIT_DIE = 6;

    /**
     * Constructor used when creating a new Wizard.
     */
    public Wizard(int level, String name, Stats stats) {
        super(level, name, stats, HIT_DIE);
    }

    /**
     * Constructor used when loading a Wizard from persistence.
     */
    public Wizard(int level, String name, Stats stats, int maxHp, int hp) {
        super(level, name, stats, HIT_DIE, maxHp, hp);
    }

    @Override
    public String getTypeName() {
        return "Wizard";
    }
    @Override
    public CharacterType getCharacterType() {
        return CharacterType.WIZARD;
    }
}