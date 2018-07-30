package com.panxiantong.alphabeta;

import java.util.List;

/**
 * Games have to implement this interface to enable the use of
 * {@link AlphaBeta AlphaBeta}.
 *
 * @author Constantin Brincoveanu
 */
public interface Position {
    /**
     * Get a list of possible moves that can be played in the current game
     * position. If the game is over, this list has to be empty.
     *
     * @return the list of valid {@link com.panxiantong.alphabeta.Move moves}.
     */
    public List<Move> getMoves();

    /**
     * Executes the given move and puts it on a stack, so it can be
     * {@link #undoMove() undone} later. This method doesn't have to check
     * whether the move is valid or not, since {@link AlphaBeta
     * AlphaBeta} only iterates through {@link #getMoves() valid moves}.
     *
     * @param move a valid {@link com.panxiantong.alphabeta.Move move}.
     */
    public void doMove(Move move);

    /**
     * Get the last executed {@link com.panxiantong.alphabeta.Move move} from the moves stack
     * and undo it.
     */
    public void undoMove();

    /**
     * Evaluate the current game position relative to the view of the player who
     * made the first move in the game.
     *
     * @return an integer value that is positive if the first player is in
     * advantage and negative if the second player is in advantage. The
     * value must be bigger than Integer.MIN_VALUE and smaller than
     * Integer.MAX_VALUE.
     */
    public int evaluate();

    /**
     * Return true if the player who will do the next move is the player who has
     * an advantage if the evaluation function is positive.
     *
     * @return true if the current player is the one who wants to maximize the
     * evaluated value.
     */
    public boolean currentlyMaximizing();
}
