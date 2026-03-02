package crawlinmydungeon;

import entities.evilaaaneighbours.Goblin;
import tile.ChestTile;
import tile.EmptyTile;
import tile.EnemyTile;
import tile.Tile;

import java.util.concurrent.ThreadLocalRandom;
/**
 * This is where the board will be handled
 */
public class Board {
    /**
     * This is our board
     */
    private Tile[] board;;
    /**
     * Current position
     */
    private int position;

    /**
     * Constructor for the board. It'll immediately run the filling
     */
    public Board() {
        this.board = new Tile[65];
        this.position = 0;
        this.fill();
    }

    /**
     * This will fill at random the board of this instance
     */
    public void fill() {
        for  (int i = 0; i < 65; i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(1, 101);
            if (randomNum < 51) {
                board[i] = new EnemyTile(new Goblin());
            }
            else if (randomNum < 76) {
                board[i] = new ChestTile();
            }
            else {
                board[i] = new EmptyTile();
            }
        }
    }

    /**
     * This first check that our movement won't make us go back in case the player is fleeing
     * then that we don't go out of the board if rolling more than the max
     * @param roll this is the movement of the character, usually by rolling
     * @return it returns what's on that tile
     */
    protected Tile moving(int roll) {
        int newPos = this.position + roll;

        if (newPos < 0) newPos = 0;
        if (newPos >= this.board.length) newPos = this.board.length - 1;

        this.position = newPos;
        return this.board[this.position];
    }

    /**
     * If the tile has been cleared we make sure the player cannot meet that encounter again
     */
    public void emptyCurrent(){
        this.board[this.position] = new EmptyTile();
    }
    
    public int getPosition(){
        return this.position;
    }
}
