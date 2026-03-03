package items.offensives;

import crawlinmydungeon.dice.DamageDice;

/**
 * Base type for offensive equipment items such as weapons.
 */
public abstract class OffensiveEquipment {
    /**
     * Display name of the creature.
     */
    private String name;
    /**
     * damage field.
     */
    private DamageDice damage;

    /**
     * Creates a new OffensiveEquipment instance.
     * @param name name value.
     * @param damage damage value.
     */
    protected OffensiveEquipment(String name, DamageDice damage) {
        this.name = name;
        this.damage = damage;
    }
    /**
     * getName operation.
     * @return Requested value.
     */
    public String getName() {
        return name;
    }
    /**
     * getDamage operation.
     * @return Requested value.
     */
    public DamageDice getDamage() {
        return damage;
    }

    /**
     * rollDamage operation.
     * @return Result value.
     */
    public int rollDamage() {
        return damage.roll();
    }
}