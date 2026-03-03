package crawlinmydungeon;

import entities.evilaaaneighbours.Goblin;
import tile.ChestTile;
import tile.EmptyTile;
import tile.EnemyTile;
import tile.Tile;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents the dungeon board as a fixed-size sequence of tiles generated when a game starts. Tracks the player's current position and resolves movement, including an out-of-board exception used as a win condition when a roll overshoots the final tile.
 */
public class Board {

    /**
     * Dungeon board instance containing tiles and current position.
     */
    private Tile[] board;

    /**
     * Current zero-based position of the player on the board.
     */
    private int position;

    /**
     * Creates a new Board instance.
     */
    public Board() {
        this.board = new Tile[64];
        this.position = 0;
        this.fill();
    }

    /**
     * Populates the board with randomly selected tiles (enemies, chests, or empty tiles).
     */
    public void fill() {
        for (int i = 0; i < board.length; i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(1, 101);

            if (randomNum < 51) {
                board[i] = new EnemyTile(new Goblin());
            } else if (randomNum < 76) {
                board[i] = new ChestTile();
            } else {
                board[i] = new EmptyTile();
            }
        }
    }

    /**
     * Applies a movement to the board position and returns the tile landed on. Overshooting the end throws an exception treated as a win condition by the game loop.
     * @param roll roll value.
     * @return New board position.
     */
    protected Tile moving(int roll) {

        int newPos = this.position + roll;


        if (newPos > board.length) {
            throw new OutOfBoardException("You rolled past the final tile!");
        }

        if (newPos < 0) {
            throw new IllegalStateException("Position cannot be negative.");
        }

        this.position = newPos;
        return this.board[this.position];
    }

    /**
     * Replaces the tile at the current position with an EmptyTile after it has been cleared.
     */
    public void emptyCurrent() {
        this.board[this.position] = new EmptyTile();
    }

    /**
     * getPosition operation.
     * @return Requested value.
     */
    public int getPosition() {
        return this.position;
    }

    /**
     * Exception class for if the player goes over the board.
     */
    public class OutOfBoardException extends RuntimeException {
        public OutOfBoardException(String message) {
            super(message);
        }
    }

    /**
     * Returns the index of the final tile on the board.
     * @return Requested value.
     */
    public int getLastTileIndex(){
        return board.length-1;
    }
}
