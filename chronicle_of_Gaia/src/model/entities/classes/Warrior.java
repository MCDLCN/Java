package model.entities.classes;

import main_logic.enums.CharacterType;
import model.entities.Stats;

/**

 * Durable melee-focused player character class.
 */
public class Warrior extends PlayerCharacter {

    private static final int HIT_DIE = 10;

    /**
     * Constructor used when creating a new Warrior.
     */
    public Warrior(int level, String name, Stats stats) {
        super(level, name, stats, HIT_DIE);
    }

    /**
     * Constructor used when loading a Warrior from persistence.
     */
    public Warrior(int level, String name, Stats stats, int maxHp, int hp) {
        super(level, name, stats, HIT_DIE, maxHp, hp);
    }

    @Override
    public String getTypeName() {
        return "Warrior";
    }


    @Override
    public CharacterType getCharacterType() {
        return CharacterType.WARRIOR;
    }
}
