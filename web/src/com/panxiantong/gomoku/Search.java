package com.panxiantong.gomoku;

import com.panxiantong.alphabeta.*;

import java.util.*;

public class Search implements Position {

    private CData d;

    public Search(CData d) {
        this.d = d;
    }


    /**
     * Get a list of possible moves that can be played in the current game
     * position. If the game is over, this list has to be empty.
     *
     * @return the list of valid {@link Move moves}.
     */
    @Override
    public List<Move> getMoves() {
        var moves = new ArrayList<Move>();
        if (!Calculate.isWin(d)) {
            Pos p = Calculate.vcf0(d);
            if (!p.equals(new Pos(-1, -1))) {
                //return new ArrayList<>().add(Calculate.vcf0(d));
                moves.add(Calculate.vcf0(d));
            } else {
                moves.addAll(d.typeMap.keySet());
                //return new ArrayList<>(Calculate.possiblePosition(d, 2));
                //return new ArrayList<>(d.typeMap.keySet());
            }

        }
        return moves;
    }

    /**
     * Executes the given move and puts it on a stack, so it can be
     * {@link #undoMove() undone} later. This method doesn't have to check
     * whether the move is valid or not, since {@link com.panxiantong.alphabeta.AlphaBeta
     * AlphaBeta} only iterates through {@link #getMoves() valid moves}.
     *
     * @param move a valid {@link Move move}.
     */
    @Override
    public void doMove(Move move) {
        d.append((Pos) move);
    }

    /**
     * Get the last executed {@link Move move} from the moves stack
     * and undo it.
     */
    @Override
    public void undoMove() {
        d.drop();
    }

    /**
     * Evaluate the current game position relative to the view of the player who
     * made the first move in the game.
     *
     * @return an integer value that is positive if the first player is in
     * advantage and negative if the second player is in advantage. The
     * value must be bigger than Integer.MIN_VALUE and smaller than
     * Integer.MAX_VALUE.
     */
    @Override
    public int evaluate() {

        int value = 0;
        for (Move m : getMoves()) {
            value += d.getType((Pos) m).getScore();
        }
        return value;
        //return d.getValue(d.getBestPosition());
        //return currentlyMaximizing() ? -score : score;
    }

    /**
     * Return true if the player who will do the next move is the player who has
     * an advantage if the evaluation function is positive.
     *
     * @return true if the current player is the one who wants to maximize the
     * evaluated value.
     */
    @Override
    public boolean currentlyMaximizing() {
        return d.getFinalSide() == 2;
    }

    public static void main(String[] args) {
        AlphaBeta ab = new AlphaBeta(new Search(new CData("77,78,87")));
        Pos p = (Pos) ab.analyzeDepth(4);
        System.out.println(p);
    }
}
