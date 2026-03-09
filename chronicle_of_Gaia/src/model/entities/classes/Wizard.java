package model.entities.classes;

import main_logic.dice.DamageDice;
import main_logic.dice.Die;
import main_logic.enums.CharacterType;
import model.entities.Stats;
import model.items.offensives.Weapon;
import model.items.scrolls.Scroll;

/**
 * Magic-focused player character class.
 */
public class Wizard extends PlayerCharacter {

    private static final int HIT_DIE = 6;
    private static final DamageDice FIREBOLT_DAMAGE = new DamageDice(1, Die.D10);

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

    /**
     * Rolls the wizard default attack damage.
     *
     * <p>This is used when no scroll is equipped.</p>
     *
     * @return rolled firebolt damage
     */
    public int rollDefaultAttackDamage() {
        return FIREBOLT_DAMAGE.roll();
    }


    @Override
    public String getTypeName() {
        return "Wizard";
    }
    @Override
    public CharacterType getCharacterType() {
        return CharacterType.WIZARD;
    }

    @Override
    public void equipScroll(Scroll scroll) {
        getEquipment().setScroll(scroll);
    }

    @Override
    public void equipWeapon(Weapon weapon) {
        throw new UnsupportedOperationException("Wizards cannot equip weapons.");
    }
}