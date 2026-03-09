package model.entities.equipment;

import model.items.Item;
import model.items.defensives.Armour;
import model.items.defensives.Shield;
import model.items.offensives.Weapon;
import model.items.scrolls.Scroll;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds currently equipped model.items for a character.
 *
 * <p>Represents equipment slots (armour, shield, weapon). Each slot may be null.</p>
 */
public class Equipment {

    private Armour armour;
    private Shield shield;
    private Weapon weapon;
    private Scroll scroll;

    //------- Setters -------

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
     * Equips a scroll in the weapon slot.
     *
     * @param scroll the scroll to equip, or null to unequip
     */
    public void setScroll(Scroll scroll) {
        this.scroll = scroll;
    }


    //------- Getters -------

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

    public Scroll getScroll() {
        return scroll;
    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        items.add(armour);
        items.add(shield);
        items.add(scroll);
        items.add(weapon);
        return items;
    }
}