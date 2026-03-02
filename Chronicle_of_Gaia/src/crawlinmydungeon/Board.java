package crawlinmydungeon;

import entities.evilaaaneighbours.Goblin;
import tile.ChestTile;
import tile.EmptyTile;
import tile.EnemyTile;
import tile.Tile;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents the dungeon board.
 * <p>
 * The board is composed of {@link Tile} elements randomly generated at
 * instantiation. It also manages the player's current position
 * and movement logic.
 */
public class Board {

    /**
     * The array representing the dungeon tiles.
     */
    private Tile[] board;

    /**
     * The current position of the player on the board.
     */
    private int position;

    /**
     * Constructs a new board of 65 tiles and fills it randomly.
     */
    public Board() {
        this.board = new Tile[65];
        this.position = 0;
        this.fill();
    }

    /**
     * Randomly fills the board with tiles.
     * <ul>
     *     <li>50% chance: {@link EnemyTile}</li>
     *     <li>25% chance: {@link ChestTile}</li>
     *     <li>25% chance: {@link EmptyTile}</li>
     * </ul>
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
     * Moves the player forward or backward on the board.
     *
     * @param roll the number of tiles to move
     * @return the {@link Tile} reached after movement
     * @throws OutOfBoardException if the movement exceeds the board size
     * @throws IllegalStateException if the resulting position is negative
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
     * Replaces the current tile with an {@link EmptyTile}.
     * <p>
     * Used after an encounter has been cleared to prevent repetition.
     */
    public void emptyCurrent() {
        this.board[this.position] = new EmptyTile();
    }

    /**
     * Returns the current position of the player.
     *
     * @return the player’s position index
     */
    public int getPosition() {
        return this.position;
    }

    /**
     * Exception thrown when a movement exceeds the last tile,
     * usually indicating a victory condition.
     */
    public class OutOfBoardException extends RuntimeException {
        /**
         * Out of board exception.
         *
         * @param message message.
         */
        public OutOfBoardException(String message) {
            super(message);
        }
    }

    public int getLastTileIndex(){
        return board.length-1;
    }
}