package com.panxiantong.gomoku;

import java.util.*;

import static com.panxiantong.gomoku.Constants.*;

public class Calculate {

    /**
     * It is a tool class which is to provide some useful methods.
     */
    private Calculate() {
    }

    /**
     * Note the importance of using an absolute path. Relative local disk file
     * system paths are an absolute no-go in a Java EE web application.
     *
     * @see <a href = https://stackoverflow.com/questions/2161054>stackoverflow</a>
     */
    private static final String workingPath = "C:/data/";

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

//    public static int[][] construct(ChessData d) {
//        return construct(d, 0, -1);
//    }
//
//    public static int[][] construct(ChessData d, int n) {
//        return construct(d, n, -1);
//    }

//    /**
//     * construct ChessData to matrix form with n circles' padding.
//     *
//     * @param d       ChessData object
//     * @param n       Default to 0
//     * @param padding Default to -1
//     * @return (size=15+2n) matrix
//     */
//    public static int[][] construct(ChessData d, int n, int padding) {
//        int[][] output = new int[size + 2 * n][size + 2 * n]; // all 0's
//
//        for (int i = 0; i < d.length(); i++) {
//            Pos p = d.getData(i);
//            if (p.inRect(0, 0, size, size)) {
//                output[p.x + n][p.y + n] = 2 * ((i + d.length() + 1) % 2) - 1;
//            }
//        }
//        for (int i = 0; i < output.length; i++) {
//            for (int j = 0; j < output.length; j++) {
//                if (!(new Pos(i, j).plus(-n).inRect(0, 0, size, size))) {
//                    output[i][j] = padding;
//                }
//            }
//        }
//
//        return output;
//    }

    public static int[][] construct(List<Pos> dataSequence, int n, int padding) {
        int[][] output = new int[size + 2 * n][size + 2 * n]; // all 0's

        for (int i = 0; i < dataSequence.size(); i++) {
            Pos p = dataSequence.get(i);
            if (p.inRect(0, 0, size, size)) {
                output[p.x + n][p.y + n] = 2 * ((i + dataSequence.size() + 1) % 2) - 1;
            }
        }
        for (int i = 0; i < output.length; i++) {
            for (int j = 0; j < output.length; j++) {
                if (!(new Pos(i, j).plus(-n).inRect(0, 0, size, size))) {
                    output[i][j] = padding;
                }
            }
        }

        return output;
    }

//    /**
//     * did not consider the case of both win
//     *
//     * @param d
//     * @return
//     */
//    private static int whoWin(ChessData d) {
//        ChessData e = d.copy();
//        e.append(-1, -1);
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                if (isWin(d, new Pos(i, j), 1)) {
//                    return -1;
//                }
//                if (isWin(e, new Pos(i, j), -1)) {
//                    return 1;
//                }
//            }
//        }
//        return 0;
//    }
//
//    public static boolean isWin(ChessData d) {
//        int n = 4;
//        return isWin(construct(d, n, 1), d.getData(-1));
//    }

    public static boolean isWin(CData d) {
        for (Pos p : d.getData()) {
            if (isWin(d, p)) {
                System.out.println("win: " + p);
                return true;
            }
        }
        return false;
    }

    public static boolean isWin(CData d, Pos p) {

        int who = d.getBoard(p);
        if (who == 0) {
            return false;
        }

        for (Pos dir : dir8) {
            int sum = 0;
            for (int len = 1; len < 5; len++) {
                // waiting for optimizing
                if (who == d.getBoard(p.plus(dir.times(len)))) {
                    sum++;
                }
            }
            if (sum == 4) {
                return true;
            }
        }
        return false;
    }

//    /**
//     * Check if win with specific point.
//     *
//     * @param d
//     * @param p
//     * @param padding
//     * @return
//     */
//    private static boolean isWin(ChessData d, Pos p, int padding) {
//
//        // circle of turns
//        int n = 4;
//
//        int[][] data = construct(d, n, padding);
//        // ChessData.plot(data);
//
//        // for (int dir = 0; dir < direction.length; dir++) {
//        // int sum = 0;
//        // for (int len = 0; len < 5; len++) {
//        // Pos pp = p.plus(n).plus(direction[dir].times(len));
//        // if (data[pp.x][pp.y] == -1) {
//        // sum++;
//        // }
//        // }
//        // if (sum == 5) {
//        // return true;
//        // }
//        // }
//        int sum = 0;
//        for (int i = -1; i <= 1; i++) {
//            for (int j = -1; j <= 1; j++) {
//                if (i != 0 || j != 0) {
//                    for (int k = 0; k < 5; k++) {
//                        if (data[p.x + i * k + n][p.y + j * k + n] == -1) {
//                            sum++;
//                        }
//                    }
//                    if (sum == 5) {
//                        return true;
//                    } else {
//                        sum = 0;
//                    }
//                }
//            }
//        }
//        return false;
//    }

    // public static String[] getType(int[][] conData, Pos p) {
    // int n = 4;
    // // int[][] data = construct(d, n, -1);
    //
    // Map<Integer, Character> map = new HashMap<>();
    // map.put(1, '1');
    // map.put(-1, '2');
    // map.put(0, '0');
    //
    // String[] type = new String[4];
    // char[] type_elem = new char[9];
    // type_elem[4] = '_';
    // // int[][] direction = { { 1, 1 }, { 1, 0 }, { 1, -1 }, { 0, -1 } };
    // for (int i = 0; i < dir4.length; i++) {
    // for (int k = -4; k <= 4; k++) {
    // if (k != 0) {
    // type_elem[k + 4] =
    // map.get(p.plus(n).plus(dir4[i].times(k)).onArray(conData));
    // }
    // }
    // type[i] = chess_type.get(String.valueOf(type_elem));
    // }
    // return type;
    //
    // }

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
                        key = 3 * key + 0;
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

    public static int[] getType(int[][] conData, Pos p, int coff) {

        int n = 4;

        int[] type = new int[4];

        for (int i = 0; i < dir4.length; i++) {
            int type_elem = 0;

            if (p.plus(n).onArray(conData) != 0) {
                throw new NullPointerException("2p: " + p.toString() + ", dir: " + i + ", coff: " + coff);
            }

            for (int k = -4; k <= 4; k++) {
                type_elem = 3 * type_elem + 1 + coff * p.plus(n).plus(dir4[i].times(k)).onArray(conData);
            }

            if (!chess_type0.containsKey(type_elem)) {
                throw new NullPointerException("p: " + p.toString() + ", dir: " + i + ", coff: " + coff);
            }
            type[i] = chess_type0.get(type_elem);
        }
        return type;

    }

    /**
     * get the type of the point p from 4 directions
     *
     * @param d
     * @param p
     * @return
     */
//    public static String[] getType(ChessData d, Pos p) {
//        int n = 4;
//        int[][] data = construct(d, n, -1);
//
//        String[] type = new String[4];
//        char[] type_elem = new char[9];
//        type_elem[4] = '_';
//        // int[][] direction = { { 1, 1 }, { 1, 0 }, { 1, -1 }, { 0, -1 } };
//        for (int i = 0; i < dir4.length; i++) {
//            for (int k = -4; k <= 4; k++) {
//                if (k == 0) {
//                    continue;
//                }
//                switch (p.plus(n).plus(dir4[i].times(k)).onArray(data)) {
//                    // switch (data[p.x + n + k * direction[i][0]][p.y + n + k * direction[i][1]]) {
//                    case 1:
//                        type_elem[k + 4] = '1';
//                        break;
//                    case -1:
//                        type_elem[k + 4] = '2';
//                        break;
//                    case 0:
//                        type_elem[k + 4] = '0';
//                        break;
//                    default:
//                        break;
//                }
//
//            }
//            type[i] = chess_type.get(String.valueOf(type_elem));
//        }
//        return type;
//
//    }
//
//    /**
//     * @param d
//     * @param p
//     * @param cate
//     * @return
//     */
//    public static int scoreFunction(ChessData d, Pos p, int cate) {
//        String[] type = getType(d, p);
//        if (cate == 1) {
//            score_data = score_data1;
//        } else {
//            score_data = score_data2;
//        }
//        return scoreFunction1(type);
//    }

    /**
     * simple adding score function
     *
     * @param type
     * @return
     */
    private static int scoreFunction1(String[] type) {
        int score = 0;
        for (String s : type) {
            score += score_data.get(s);
        }
        return score;
    }

    /**
     * analysising the four type
     *
     * @param type
     * @return
     */
    @SuppressWarnings("unused")
    private static int scoreFunction2(String[] type) {
        int score = 0;

        return score;
    }

    // public static int[][] possibleScore(ChessData d,int n) {
    // int[][] result = new int[size][size];
    // ChessData e = d.copy();
    // e.append(-1, -1);
    // for (Point p: d.possiblePosition(n)) {
    // if (d.notContain(p.x,p.y)) {
    // result[p.x][p.y] = score(d, p, 1) + score(e, p, 2);
    // }
    // }
    //
    // return result;
    // }

    // Give a score to every point in the board.
//    public static int[][] globalScore(ChessData d) {
//        int[][] result = new int[size][size];
//        ChessData e = d.copy();
//        e.append(-1, -1);
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                Pos p = new Pos(i, j);
//                if (d.notContain(p)) {
//                    result[i][j] = scoreFunction(d, p, 1) + scoreFunction(e, p, 2);
//                }
//            }
//        }
//
//        return result;
//    }

    public static Pos findMax(int[][] data) {
        int x = 0, y = 0, max = 0;

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                if (max < data[i][j]) {
                    max = data[i][j];
                    x = i;
                    y = j;
                }
            }
        }
        return new Pos(x, y);
    }

//    public static int[] oneScore(ChessData d) {
//        int[] result = {0, 0, 0};
//
//        int[][] score = globalScore(d);
//        Pos p = findMax(score);
//        result[0] = score[p.x][p.y];
//        result[1] = p.x;
//        result[2] = p.y;
//        if (d.length() % 2 == 1) {
//            result[0] *= -1;
//        }
//
//        return result;
//    }
//
//    public static int oneScore2(ChessData d) {
//        int result = 0;
//        ChessData e = d.copy();
//        e.append(-1, -1);
//
//        // int s1=0,s2=0;
//        for (Pos p : possiblePosition(d)) {
//            // result[2] = scoreFunction(d, p, 1) + scoreFunction(e, p, 2);
//            result += (scoreFunction(d, p, 1) - scoreFunction(e, p, 2));
//            // s2+=scoreFunction(e, p, 2);
//        }
//        if (d.length() % 2 == 1) {
//            return -result;
//        }
//        return result;
//    }

    public static List<Pos> possiblePosition(CData d, int n) {
        Set<Pos> result = new HashSet<>();

        for (Pos p : d.getData()) {
            for (Pos dir : dir8) {
                for (int i = 1; i <= n; i++) {
                    Pos pos = p.plus(dir.times(i));
                    if (pos.inRect(0, 0, size, size)) {
                        result.add(pos);
                    }

                }
            }
        }
        result.removeAll(d.getData());
        return new ArrayList<>(result);
    }

    // for AI
//    public static List<Pos> possiblePosition(ChessData d) {
//        return possiblePosition(d, 2);
//    }

//    /**
//     * @param n:round
//     * @return all of the possible points with round n.
//     */
//    public static List<Pos> possiblePosition(ChessData d, int n) {
//        List<Pos> result = new ArrayList<>();
//
//        int[][] m = construct(d, n, 0);
//        for (Pos p : d.getData()) {
//            for (int i = -n; i <= n; i++) {
//                for (int j = -n; j <= n; j++) {
//                    m[p.x + n + i][p.y + n + j] = 1;
//                }
//            }
//
//        }
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                Pos p = new Pos(i, j);
//                if (m[i + n][j + n] == 1) {
//                    result.add(p);
//                }
//            }
//        }
//        result.removeAll(d.getData());
//        return result;
//    }
//
//    public static List<Pos> possiblePosition(int[][] con, ChessData d, int n) {
//        List<Pos> result = new ArrayList<>();
//
//        int[][] m = con;// construct(d, n, 0);
//        for (Pos p : d.getData()) {
//            for (int i = -n; i <= n; i++) {
//                for (int j = -n; j <= n; j++) {
//                    m[p.x + n + i][p.y + n + j] = 1;
//                }
//            }
//
//        }
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                Pos p = new Pos(i, j);
//                if (p.plus(n).onArray(m) == 1) {
//                    result.add(p);
//                }
//            }
//        }
//        result.removeAll(d.getData());
//        return result;
//    }
//
//    public static Pos rootSearch(ChessData d, int depth) {
//        // int depth = 1;
//        int best = -100000;
//        Pos pp = new Pos();
//
//        for (Pos p : possiblePosition(d)) {
//            d.append(p);
//            int val = -minMax1(d, depth - 1);
//            // val *= -1;
//
//            if (best < val) {
//                best = val;
//                pp = p;
//            }
//            d.drop();
//        }
//        return pp;
//
//    }
//
//    public static int minMax1(ChessData d, int depth) {
//        // int depth = 1;
//        int best = -100000;
//
//        int temp = oneScore2(d);
//        if (depth <= 0 || Math.abs(temp) > 5000) {
//            return temp;
//        } else {
//            for (Pos p : possiblePosition(d)) {
//                d.append(p);
//                int val = -minMax1(d, depth - 1);
//                // val *= -1;
//
//                if (best < val) {
//                    best = val;
//                }
//                d.drop();
//            }
//        }
//
//        return best;
//
//    }
//
//    public static int alphaBeta(ChessData d, int depth, int alpha, int beta) {
//        int a = alpha;
//        int temp = oneScore2(d);
//        if (depth <= 0 || Math.abs(temp) > 5000) {
//            return temp;
//        } else {
//            for (Pos p : possiblePosition(d)) {
//                d.append(p);
//                int val = -alphaBeta(d, depth - 1, -beta, -a);
//                d.drop();
//
//                if (val >= beta) {
//                    return beta;
//                }
//                if (val > a) {
//                    a = val;
//                }
//
//            }
//            return a;
//        }
//    }
//
//    public static Pos alphaBetaRoot(ChessData d, int depth) {
//        int best = -100000;
//        Pos pp = new Pos();
//
//        for (Pos p : possiblePosition(d)) {
//            d.append(p);
//            int val = -alphaBeta(d, depth - 1, -100000, 100000);
//            // val *= -1;
//
//            if (best < val) {
//                best = val;
//                pp = p;
//            }
//            d.drop();
//        }
//        return pp;
//    }
//
//    public static int[] minMax(ChessData d, int depth) {
//        // int depth = 1;
//        int[] best = {-100000, 0, 0};
//        int[] temp = oneScore(d);
//        if (depth <= 0 || Math.abs(temp[0]) > 7000) {
//            return temp;
//        } else {
//            for (Pos p : possiblePosition(d)) {
//                d.append(p);
//                int[] val = minMax(d, depth - 1);
//                val[0] *= -1;
//                if (best[0] < val[0]) {
//                    best = val;
//                }
//                d.drop();
//            }
//            return best;
//
//        }
//    }

    public static Pos vcf(CData d, int side) {
        return null;
    }

    public static void main(String[] args) {

        CData d = new CData("00,77");

        //Type t = Type.getType(d.getBoard(), new Pos(7, 8));
        //d.typeMap


//
//        for (int i = 0; i < 5; i++) {
//            d = new CData(Tool.runExe(Calculate.engine_wine, "00,"+d.toString()));
//            d.append(d.getBestPosition());
//        }
//
//        System.out.println(d.toString());


        //System.out.println(3 * 3 * 3 * 3 * 3 * 3 * 3 * 3 * 3);
        //System.out.println(chess_type0.values());
        // ChessData d = new
        // ChessData("00,77,86,76,85,75,74,78,79,87,97,96,a7,69,a6,a5");
        // System.out.println(Arrays.toString(getType(d, new Pos(7, 7))));

        // // System.out.println(score_data2.get("three"));
        // System.out.println(chess_type.get("1011_2111"));
        // ChessData d = new ChessData("ow 77");
        // System.out.println(Calculate.alphaBetaRoot(d, 1));
        //
        // System.out.println();
        // // System.out.println(score());
    }

}

