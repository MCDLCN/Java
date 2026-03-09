package model.entities.evilaaaneighbours;

import main_logic.enums.EnemyType;
import model.entities.Creature;

/**
 * Creates enemy instances from enemy types.
 */
public final class EnemyFactory {

    private EnemyFactory() {
    }

    /**
     * Creates an enemy instance matching the given type.
     *
     * @param type enemy type to create
     * @return concrete enemy instance
     */
    public static Enemy create(EnemyType type) {
        return switch (type) {
            case GOBLIN -> new Goblin();
            case BANDIT -> new Bandit();
            case DRAGON -> new Dragon();
            case ENEMY_MAGE -> new EnemyMage();
        };
    }
}