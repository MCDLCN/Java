package crawlinmydungeon;

import entities.evilaaaneighbours.Goblin;

import java.util.concurrent.ThreadLocalRandom;
/**
 * This is where the board will be handled
 */
public class Board {
    /**
     * This is our board
     */
    protected Object[] board;
    /**
     * Current position
     */
    protected int position;

    /**
     * Constructor for the board. It'll immediately run the filling
     */
    public Board() {
        this.fill();
        this.position = 0;
    }

    /**
     * This will fill at random the board of this instance
     */
    protected void fill() {
        assert this.board != null;
        for  (int i = 0; i < 65; i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(1, 101);
            if (randomNum < 51) {
                this.board[i] = new Goblin();
            }
            if  (randomNum >= 51 &&  randomNum < 76) {
                this.board[i] = "chest";
            }
            else {
                this.board[i] = "empty";
            }
        }
    }

    /**
     * This first check that our movement won't make us go back in case the player is fleeing
     * then that we don't go out of the board if rolling more than the max
     * @param roll this is the movement of the character, usually by rolling
     * @return it returns what's on that tile
     */
    protected Object moving(int roll){
        if ((this.position+=roll)<0){
            this.position = 0;
        } else if ((this.position+=roll)>this.board.length) {
            this.position = this.board.length - 1;
        }
        else {
            this.position += roll;
        }
        return this.board[this.position];
    }

    /**
     * If the tile has been cleared we make sure the player cannot meet that encounter again
     */
    protected void emptyCurrent(){
        this.board[this.position] = "empty";
    }
}
