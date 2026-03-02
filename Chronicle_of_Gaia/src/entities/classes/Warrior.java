package entities.classes;

import entities.Character;

import java.util.HashMap;
import java.util.Map;

public class Warrior extends Character {
    public Warrior(int HP, String name, Map<String, Integer> stats) {
        super(HP, 10, name, stats);
    }
}
