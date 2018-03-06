package com.panxiantong.gomoku;

import java.util.*;

import static com.panxiantong.gomoku.Constants.*;

public class Type {

    private static final Map<String, Integer> typeToInt = new HashMap<>();

    //TODO: decide which to use.
    static {
        typeToInt.put("none", 0);
        typeToInt.put("none", 0);
        typeToInt.put("oneS", 1);
        typeToInt.put("one", 2);
        typeToInt.put("twoS", 3);
        typeToInt.put("two2", 4);
        typeToInt.put("two1", 5);
        typeToInt.put("two", 6);
        typeToInt.put("threeS", 7);
        typeToInt.put("threeJ", 8);
        typeToInt.put("three", 9);
        typeToInt.put("fourS", 0);
        typeToInt.put("fourD", 1);
        typeToInt.put("four", 2);
        typeToInt.put("five", 3);
    }

    enum TypeEnum {
        NONE, ONES, ONE, TWOS, TWO2, TWO1, TWO, THREES, THREEJ, THREE, FOURS, FOURD, FOUR, FIVE

    }

    protected int[] black = new int[4];
    protected int[] white = new int[4];

    public Type() {
        this(new int[4], new int[4]);
    }

    public Type(int[] black, int[] white) {
        if (black.length != 4 || white.length != 4) {
            throw new IllegalArgumentException();
        }
        this.black = black;
        this.white = white;

    }

    public int getScore(int who){

        //TODO: optimize
        int value = 0;
        int[] type1, type2;
        if (who == 1) {
            type1 = black;
            type2 = white;
        } else {
            type1 = white;
            type2 = black;
        }
        for (int t : type1) {
            value += Calculate.score_1.get(t);
        }
        for (int t : type2) {
            value += Calculate.score_2.get(t);
        }
        return value;
    }


    /**
     * given a board and a Pos p, return a Type
     *
     * @param board
     * @param p
     */
    public static Type getType(int[][] board, Pos p) {

        int n = 4;

        Type type = new Type();

        if (p.plus(n).onArray(board) != 0) {
            System.out.println("no type at " + p.toString());
        } else {
            for (int i = 0; i < dir4.length; i++) {
                // for black

                StringBuilder sb = new StringBuilder();
                for (int k = -4; k <= 4; k++) {
                    if (k == 0) {
                        sb.append('_');
                    } else {
                        int value = p.plus(n).plus(dir4[i].times(k)).onArray(board);
                        if (value == -1) {
                            sb.append(2);
                        } else {
                            sb.append(value);
                        }
                    }


                }
                String typeStr = Calculate.chess_type.get(sb.toString());

                type.black[i] = typeToInt.get(typeStr);

                // for white
                sb.setLength(0);
                for (int k = -4; k <= 4; k++) {
                    if (k == 0) {
                        sb.append('_');
                    } else {
                        int value = p.plus(n).plus(dir4[i].times(k)).onArray(board);
                        if (value == -1) {
                            sb.append(2);
                        } else if (value == 0) {
                            sb.append(0);
                        } else {
                            sb.append(3 - value);
                        }
                    }


                }
                typeStr = Calculate.chess_type.get(sb.toString());

                type.white[i] = typeToInt.get(typeStr);


//                int type_elem = 0;
//
//
//                for (int k = -4; k <= 4; k++) {
//                    type_elem = 3 * type_elem + 1 + coff * p.plus(n).plus(dir4[i].times(k)).onArray(board);
//                }
//
//                if (!Calculate.chess_type0.containsKey(type_elem)) {
//                    throw new NullPointerException("p: " + p.toString() + ", dir: " + i + ", coff: " + coff);
//                }
//                type[i] = Calculate.chess_type0.get(type_elem);
            }
        }

        return type;

    }




    @Override
    public String toString() {
        return "black: " + Arrays.toString(black) + ", white: " + Arrays.toString(white);
    }
}
