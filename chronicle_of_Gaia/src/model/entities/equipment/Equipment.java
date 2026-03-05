package model.entities.equipment;

import model.items.defensives.Armour;
import model.items.defensives.Shield;
import model.items.offensives.Weapon;

/**
 * Holds currently equipped model.items for a character.
 *
 * <p>Represents equipment slots (armour, shield, weapon). Each slot may be null.</p>
 */
public class Equipment {

    private Armour armour;
    private Shield shield;
    private Weapon weapon;

    /**
     * Equips armour in the armour slot.
     *
     * @param armour the armour to equip, or null to unequip
     */
    public void setArmour(Armour armour) {
        this.armour = armour;
    }

    /**
     * Equips a shield in the shield slot.
     *
     * @param shield the shield to equip, or null to unequip
     */
    public void setShield(Shield shield) {
        this.shield = shield;
    }

    /**
     * Equips a weapon in the weapon slot.
     *
     * @param weapon the weapon to equip, or null to unequip
     */
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    /**
     * @return the currently equipped armour, or null if none
     */
    public Armour getArmour() {
        return armour;
    }

    /**
     * @return the currently equipped shield, or null if none
     */
    public Shield getShield() {
        return shield;
    }

    /**
     * @return the currently equipped weapon, or null if none
     */
    public Weapon getWeapon() {
        return weapon;
    }
}