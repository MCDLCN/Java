package crawlinmydungeon;

import entities.Character;
import entities.classes.Wizard;

import java.util.HashMap;
import java.util.Map;

/**
 * This will be the game's logic
 */
public class Game {
    /**
     * The board where we'll play
     */
    private Board board;
    /**
     * This is the player
     */
    protected Character character;

    public Game() {
        this.board = new Board();
        this.character = this.characterCreation();
    }

    protected Character characterCreation() {
        Map<String, Integer> stats = new HashMap<String, Integer>();
        return new Wizard(1,"a",stats);
    }


}
