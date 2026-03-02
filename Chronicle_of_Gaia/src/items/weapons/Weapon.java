package items.weapons;

import crawlinmydungeon.dice.DamageDice;

public class Weapon {
    private final String name;
    private final DamageDice damage;

    public Weapon(String name, DamageDice damage) {
        this.name = name;
        this.damage = damage;
    }

    public int rollDamage() {
        return damage.roll();
    }
}
