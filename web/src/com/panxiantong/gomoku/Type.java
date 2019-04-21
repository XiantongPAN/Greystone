package com.panxiantong.gomoku;

import java.util.*;

import static com.panxiantong.gomoku.Constants.*;

public class Type {

    /**
     * Note the importance of using an absolute path. Relative local disk file
     * system paths are an absolute no-go in a Java EE web application.
     *
     * @see <a href = https://stackoverflow.com/questions/2161054>stackoverflow</a>
     */
    private static final String workingPath = DB_DIR+"data/";

    public static final Map<String, String> chess_type = Tool.importType(workingPath + "TypeTable.txt");

    private static Map<String, Integer> score_data0 = Tool.importData(workingPath + "ScoreTable0.txt");
    private static Map<String, Integer> score_data = Tool.importData(workingPath + "ScoreTable1.txt");
    private static final Map<String, Integer> score_data1 = Tool.importData(workingPath + "ScoreTable1.txt");
    private static final Map<String, Integer> score_data2 = Tool.importData(workingPath + "ScoreTable2.txt");

    public static final Map<Integer, Integer> chess_type0 = resolveType(chess_type, 0);
    private static final Map<Integer, Integer> chess_type1 = resolveType(chess_type, 1);
    private static final Map<Integer, Integer> chess_type2 = resolveType(chess_type, 2);

    public static final List<Integer> score_1 = Tool.importScore(workingPath + "ScoreTable1.txt");
    public static final List<Integer> score_2 = Tool.importScore(workingPath + "ScoreTable2.txt");

    public static String engine_wine = workingPath + "wine.exe";

    private static final Map<String, Integer> typeToInt = new HashMap<>();

    //TODO: decide which to use.
    static {
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
        typeToInt.put("fourS", 10);
        typeToInt.put("fourD", 11);
        typeToInt.put("four", 12);
        typeToInt.put("five", 13);
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

    public boolean isNone() {
        for (int i : black) {
            if (i > 2) {
                return false;
            }
        }
        for (int i : white) {
            if (i > 2) {
                return false;
            }
        }
        return true;
    }


    public void update(CData d, Pos p, int i) {
        int n = 4;
        StringBuilder sb = new StringBuilder();
        for (int k = -4; k <= 4; k++) {
            if (k == 0) {
                sb.append('_');
            } else {
                //int value = p.plus(n).plus(dir4[i].times(k)).onArray(board);
                int value = d.getBoard(dir4[i].times(k).plus(p));
                if (value == -1) {
                    sb.append(2);
                } else {
                    sb.append(value);
                }
            }


        }
        String typeStr = chess_type.get(sb.toString());

        black[i] = typeToInt.get(typeStr);

        // for white
        sb.setLength(0);
        for (int k = -4; k <= 4; k++) {
            if (k == 0) {
                sb.append('_');
            } else {
                int value = d.getBoard(dir4[i].times(k).plus(p));
                if (value == -1) {
                    sb.append(2);
                } else if (value == 0) {
                    sb.append(0);
                } else {// value = 1 or 2
                    sb.append(3 - value);
                }
            }


        }
        typeStr = chess_type.get(sb.toString());

        white[i] = typeToInt.get(typeStr);

    }

    public int getScore(int who) {

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
            value += score_1.get(t);
        }
        for (int t : type2) {
            value += score_2.get(t);
        }
        return value;
    }

    public int getScore(){
        int score = 0;
        for (int t : black) {
            score += score_1.get(t);
        }
        for (int t : white) {
            score -= score_1.get(t);
        }
        return score;

    }

    public boolean become5(){
        for(int i:black){
            if(i>12){
                return true;
            }
        }
        for(int i:white){
            if(i>12){
                return true;
            }
        }
        return false;
    }


    /**
     * given a board and a Pos p, return a Type
     *
     * @param board
     * @param p
     */
    public static Type getType(int[][] board, Pos p) {

        //TODO: optimize
        int n = 4;

        Type type = new Type();

        if (p.plus(n).onArray(board) != 0) {
            System.out.println("no type at " + p.toString());
        } else {
            for (int i = 0; i < dir4.length; i++) {
                // for black


                // use addition to instead compare
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
                String typeStr = chess_type.get(sb.toString());

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
                        } else {// value = 1 or 2
                            sb.append(3 - value);
                        }
                    }


                }
                typeStr = chess_type.get(sb.toString());

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


    public boolean isLevel4(int side) {
        if (side == 1) {
            for (int i : black) {
                //if(i)
            }
        } else if (side == 2) {
            for (int i : white) {

            }
        } else {
            throw new IllegalArgumentException("the side should be 1 or 2");
        }
        return false;

    }


    @Override
    public String toString() {
        return "black: " + Arrays.toString(black) + ", white: " + Arrays.toString(white);
    }

    public static Map<Integer, Integer> resolveType(Map<String, String> type, int n) {
        Map<Integer, Integer> output = new HashMap<>();

        for (Map.Entry<String, String> entry : type.entrySet()) {
            int key = 0;
            String sKey = entry.getKey();
            for (int i = 0; i < sKey.length(); i++) {
                switch (sKey.charAt(i)) {
                    case '1':
                        key = 3 * key + 2;
                        break;
                    case '2':
                        key = 3 * key;
                        break;
                    case '0':
                    case '_':
                        key = 3 * key + 1;
                        break;
                    default:
                        break;
                }
            }

            String sValue = entry.getValue();
            int value0 = score_data0.get(sValue);
            int value1 = score_data1.get(sValue);
            int value2 = score_data2.get(sValue);

            switch (n) {
                case 0:
                    output.put(key, value0);
                    break;
                case 1:
                    output.put(key, value1);
                    break;
                case 2:
                    output.put(key, value2);
                    break;
                default:
                    break;
            }

        }
        return output;
    }
}
