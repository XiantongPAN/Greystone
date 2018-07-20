package com.panxiantong.alphabeta;

import java.util.List;

/**
 * This class is used by {@link com.panxiantong.alphabeta.AlphaBeta AlphaBeta} to run a
 * search through the game tree.
 * 
 * @author Constantin
 *
 */
public class SearchWorker extends Thread {
	private Position game;
	private Move bestMove;
	private int maxDepth;

	/**
	 * Initializes a new {@link com.panxiantong.alphabeta.SearchWorker SearchWorker} thread.
	 * 
	 * @param game
	 *            The game position that should be searched through.
	 */
	public SearchWorker(Position game) {
		this.game = game;
		this.maxDepth = 100;
	}

	/**
	 * Initializes a new {@link com.panxiantong.alphabeta.SearchWorker SearchWorker} thread.
	 * 
	 * @param game
	 *            The game position that should be searched through.
	 * @param maxDepth
	 *            The maximum depth that the {@link com.panxiantong.alphabeta.SearchWorker
	 *            SearchWorker} will search through before it stops itself.
	 */
	public SearchWorker(Position game, int maxDepth) {
		this.game = game;
		this.maxDepth = maxDepth;
	}

	/**
	 * Run the iterative deepening search until the thread is interrupted or the
	 * depth reached the maximum depth (100 by default).
	 */
	public void run() {
		bestMove = null;
		int depth = 1;
		do {
			if (game.currentlyMaximizing()) {
				initMax(depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
			} else {
				initMin(depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
			}
			depth++;
		} while (!isInterrupted() && depth <= maxDepth);
	}

	/**
	 * Stop the calculation if it hasn't been stopped yet and return the
	 * calculated move.
	 * 
	 * @return the calculated best move.
	 */
	public Move getBestMove() {
		return bestMove;
	}

	// Maximization method for the first ply. Here we can consume more time for
	// unnecessary things, because this method is only called once per
	// calculation.
	private int initMax(int depth, int alpha, int beta) {

		// Return evaluated score if this is the end.
		if (depth == 0) {
			return game.evaluate();
		}
		List<Move> moves = game.getMoves();
		if (moves.isEmpty()) {
			return game.evaluate();
		}

		// Put the previously found best move to the beginning of the list, to
		// speed things up a bit (more cut-offs).
		if (bestMove != null && moves.contains(bestMove)) {
			moves.remove(bestMove);
			moves.add(0, bestMove);
		}

		// Iterate through moves
		for (Move move : moves) {
			game.doMove(move);
			int value = min(depth - 1, alpha, beta);
			game.undoMove();
			if (value > alpha) {
				alpha = value;
				if (alpha >= beta) {
					break; // beta cut-off
				}
				bestMove = move;
			}
			// Interrupt
			if (isInterrupted()) {
				break;
			}
		}
		return alpha;
	}

	// Minimization method for the first ply. Here we can consume more time for
	// unnecessary things, because this method is only called once per
	// calculation.
	private int initMin(int depth, int alpha, int beta) {

		// Return evaluated score if this is the end.
		if (depth == 0) {
			return game.evaluate();
		}
		List<Move> moves = game.getMoves();
		if (moves.isEmpty()) {
			return game.evaluate();
		}

		// Put the previously found best move to the beginning of the list, to
		// speed things up a bit (more cut-offs).
		if (bestMove != null && moves.contains(bestMove)) {
			moves.remove(bestMove);
			moves.add(0, bestMove);
		}

		// Iterate through moves
		for (Move move : moves) {
			game.doMove(move);
			int value = max(depth - 1, alpha, beta);
			game.undoMove();
			if (value < beta) {
				beta = value;
				if (beta <= alpha) {
					break; // alpha cut-off
				}
				bestMove = move;
			}
			// Interrupt
			if (isInterrupted()) {
				break;
			}
		}
		return beta;
	}

	// Maximization method for most plies. This method has to work fast.
	private int max(int depth, int alpha, int beta) {

		// Return evaluated score if this is the end.
		if (depth == 0) {
			return game.evaluate();
		}
		List<Move> moves = game.getMoves();
		if (moves.isEmpty()) {
			return game.evaluate();
		}

		// Iterate through moves
		for (Move move : moves) {
			game.doMove(move);
			int value = min(depth - 1, alpha, beta);
			game.undoMove();
			if (value > alpha) {
				alpha = value;
				if (alpha >= beta) {
					break; // beta cut-off
				}
			}
		}
		return alpha;
	}

	// Minimization method for most plies. This method has to work fast.
	private int min(int depth, int alpha, int beta) {

		// Return evaluated score if this is the end.
		if (depth == 0) {
			return game.evaluate();
		}
		List<Move> moves = game.getMoves();
		if (moves.isEmpty()) {
			return game.evaluate();
		}

		// Iterate through moves
		for (Move move : moves) {
			game.doMove(move);
			int value = max(depth - 1, alpha, beta);
			game.undoMove();
			if (value < beta) {
				beta = value;
				if (beta <= alpha) {
					break; // alpha cut-off
				}
			}
		}
		return beta;
	}
}
