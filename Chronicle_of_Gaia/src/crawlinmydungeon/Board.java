package crawlinmydungeon;

import entities.evilaaaneighbours.Goblin;
import tile.ChestTile;
import tile.EmptyTile;
import tile.EnemyTile;
import tile.Tile;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents the dungeon board as a fixed-size sequence of tiles generated when a game starts. Tracks the player's current position and resolves movement, including an out-of-board exception used as a win condition when a roll overshoots the final tile.
 */
public class Board {

    /**
     * Dungeon board instance containing tiles and current position.
     */
    private final ArrayList<Tile> board;


    /**
     * Creates a new Board instance.
     */
    public Board(int size) {
        this.board = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            board.add(new EmptyTile());
        }

        fill();
    }

    /**
     * Returns the index of the final tile on the board.
     * @return Requested value.
     */
    public int getLastTileIndex() {
        return board.size() - 1;
    }

    /**
     * Return the value of the tile
     * @param position the position where we retrieve the tile
     * @return the value of the tile at position
     */
    public Tile getTile(int position) {
        return board.get(position);
    }

    /**
     * Set the tile
     * @param position the position where we set the tile
     * @param tile the tile that'll be assigned at the position
     */
    public void setTile(int position, Tile tile) {
        board.set(position, tile);
    }

    /**
     * Populates the board with randomly selected tiles (enemies, chests, or empty tiles).
     */
    public void fill() {
        for (int i = 0; i < board.size(); i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(1, 101);

            if (randomNum < 51) {
                board.set(i,new EnemyTile(new Goblin()));
            } else if (randomNum < 76) {
                board.set(i,new ChestTile());
            } else {
                board.set(i,new EmptyTile());
            }
        }
    }

    /**
     * Applies a movement to the board position and returns the tile landed on. Overshooting the end throws an exception treated as a win condition by the game loop.
     * @param roll roll value.
     * @param currentPosition the current position of the player(s)
     * @return New board position.
     */
    protected int moving(int currentPosition, int roll) {
        int newPos = currentPosition + roll;

        // overshoot = win via exception (your rule)
        if (newPos > getLastTileIndex()) {
            throw new OutOfBoardException("You rolled past the final tile!");
        }

        if (newPos < 0) {
            throw new IllegalStateException("Position cannot be negative.");
        }

        return newPos;
    }

    /**
     * Replaces the tile at the current position with an EmptyTile after it has been cleared.
     * @param position the position where we empty the tile
     */
    public void emptyAt(int position) {
        board.set(position, new EmptyTile());
    }

    /**
     * Exception class for if the player goes over the board.
     */
    public class OutOfBoardException extends RuntimeException {
        public OutOfBoardException(String message) {
            super(message);
        }
    }


}
