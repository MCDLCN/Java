package entities.classes;

import entities.Character;

import java.util.Map;

public class Wizard extends Character {
    public Wizard(int HP, String name, Map<String, Integer> stats) {
        super(HP, 10, name, stats);
    }
}
