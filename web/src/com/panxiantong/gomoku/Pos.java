package com.panxiantong.gomoku;

import com.panxiantong.alphabeta.Move;

import static com.panxiantong.gomoku.Constants.*;

/**
 * Two integers representing a location {@code (x,y)} in the chess board.
 */
public class Pos implements Move {

    /**
     * The X coordinate of this <code>Point</code>. If no X coordinate is set it
     * will default to 0.
     *
     */
    protected int x;

    /**
     * The Y coordinate of this <code>Point</code>. If no Y coordinate is set it
     * will default to 0.
     *
     *
     */
    protected int y;

    /**
     * Constructs and initializes a point at the origin (0,&nbsp;0)
     */
    public Pos() {
        this(0, 0);
    }

    /**
     * Constructs and initializes a point at the specified location {@code (x,y)}
     *
     * @param x the X coordinate of the newly constructed <code>Point</code>
     * @param y the Y coordinate of the newly constructed <code>Point</code>
     * @since 1.0
     */
    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }



    /**
     * A hex string representation of a point
     *
     * @param s e.g. "a0" -> {10,0}
     */
    public Pos(String s) {
        int num = Integer.parseInt(s, 16);
        this.x = num / 16;
        this.y = num % 16;
    }

    /**
     * @return Hex string representation of the point
     */
    public String getStr() {
        return Integer.toString(x, 16) + Integer.toString(y, 16);
    }

    public boolean inRect() {
        return inRect(size);
    }

    public boolean inRect(int x) {
        return inRect(0, 0, x, x);
    }

    public boolean inRect(Pos p0, Pos p1) {
        return inRect(p0.x, p0.y, p1.x, p1.y);
    }

    public boolean inRect(int x0, int y0, int x1, int y1) {
        return x >= x0 && y >= y0 && x < x1 && y < y1;
    }

    /**
     * @return a string representation of this point (JSON form)
     */
    @Override
    public String toString() {
        return "{\"x\":" + x + ", \"y\":" + y + "}";
    }


    // Definitions for linear operation

    public Pos plus(int n) {
        return new Pos(x + n, y + n);
    }

    public Pos plus(Pos p) {
        return new Pos(this.x + p.x, this.y + p.y);
    }

    public Pos times(int n) {
        return new Pos(x * n, y * n);
    }

    public Pos times(Pos p) {
        return new Pos(this.x * p.x, this.y * p.y);
    }

    public int onArray(int[][] d) {
        // no need to throw exceptions as it will be done in d[x][y].
        return d[x][y];
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }



    /**
     * Returns the hashcode for this <code>Pos</code>.
     *
     * @return a hash code for this <code>Pos</code>.
     */
    public int hashCode() {
        // x and y may always less than 32 in the chess board
        return (x << 5) + y;
    }

    /**
     * Determines whether or not two points are equal.
     */
    public boolean equals(Object obj) {

        // when using Pos in Map as a Key
        // take care of overwriting the 'equals' and 'hashcode' methods
        if (obj instanceof Pos) {
            Pos p = (Pos) obj;
            return (x == p.x) && (y == p.y);
        }
        return false;
    }

}
