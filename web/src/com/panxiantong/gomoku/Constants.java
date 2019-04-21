package com.panxiantong.gomoku;


public class Constants {
    private Constants() {
    }

    /**
     * TODO: make it compatible in mac, win, and server.
     */
    public static final String DB_DIR = "/Users/mac/IdeaProjects/Greystone/database/";

    public static final int size = 15;
    // public static final int Row = 15;
    // public static final int Column = 15;

    public static final int X = 30;
    public static final int Y = 60;

    public static final int Chess_size = 35;

    public static final int Grid_size = 40;

    public static final int[] dx = new int[] { 1, 1, 1, 0 };
    public static final int[] dy = new int[] { 1, 0, -1, -1 };

    public static final Pos[] dir4 = new Pos[] { new Pos(1, -1), new Pos(1, 0), new Pos(1, 1), new Pos(0, 1) };

    public static final Pos[] dir8 = new Pos[] { new Pos(1, 1), new Pos(1, 0), new Pos(1, -1), new Pos(0, -1),
            new Pos(-1, -1), new Pos(-1, 0), new Pos(-1, 1), new Pos(0, 1) };

    public static final int none = 0;
    public static final int oneS = 1;
    public static final int one = 2;
    public static final int twoS = 3;
    public static final int two2 = 4;
    public static final int two1 = 5;
    public static final int two = 6;
    public static final int threeS = 7;
    public static final int threeJ = 8;
    public static final int three = 9;
    public static final int fourS = 10;
    public static final int fourD = 11;
    public static final int four = 12;
    public static final int five = 13;



}
