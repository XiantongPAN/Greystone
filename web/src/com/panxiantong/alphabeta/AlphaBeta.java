package com.panxiantong.alphabeta;

/**
 * This class performs Alpha-Beta pruning searches for games that implement
 * {@link com.panxiantong.alphabeta.Position Position}. Alpha-Beta pruning is an adversarial
 * search algorithm used commonly for machine playing of two-player games
 * (Tic-tac-toe, Chess, Go, etc.). It stops completely evaluating a move when at
 * least one possibility has been found that proves the move to be worse than a
 * previously examined move. Such moves need not be evaluated further. When
 * applied to a standard minimax tree, it returns the same move as minimax
 * would, but prunes away branches that cannot possibly influence the final
 * decision. (description from Wikipedia.org)
 *
 * @author Constantin Brincoveanu
 */
public class AlphaBeta {
    private Position game;

    /**
     * Initializes a new {@link com.panxiantong.alphabeta.AlphaBeta AlphaBeta} search.
     *
     * @param game The game position that should be searched through.
     */
    public AlphaBeta(Position game) {
        this.game = game;
    }

    /**
     * Calculates the best move in the current position for the given depth.
     *
     * @param depth the number of plies that will be calculated ahead.
     * @return the calculated best move.
     */
    public Move analyzeDepth(int depth) {
        SearchWorker worker = new SearchWorker(game, depth);
        worker.start();
        try {
            worker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return worker.getBestMove();
    }

    /**
     * Calculates the best move in the current position for the given duration
     * (iterative deepening).
     *
     * @param milliSeconds after this time the search thread will be interrupted.
     * @return the calculated best move.
     */
    public Move analyze(int milliSeconds) {
        SearchWorker worker = new SearchWorker(game);
        worker.start();
        try {
            Thread.sleep(milliSeconds);
            worker.interrupt();
            worker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return worker.getBestMove();
    }
}
