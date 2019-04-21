package com.panxiantong.gomoku;

import java.util.*;

import static com.panxiantong.gomoku.Constants.*;

/**
 * It is designed to describe one situation in chess board
 */
public class CData {
    /**
     * the number of chess in the board
     */
    private int len = 0;

    /**
     * the chess point sequence. (the most important field,
     * it has already described the whole information)
     */
    private List<Pos> dataSequence = new ArrayList<>();

    /**
     * board data: 1: black, 2: white, 0: blank, -1: out bound
     */
    private int[][] board = new int[size + 8][size + 8];


    // around with -1
    //private int[][] conBlack = new int[size + 8][size + 8];

    // around with 1
    //private int[][] conWhite = new int[size + 8][size + 8];

    /**
     * {7,8}->Type <br/>
     * package private
     */
    Map<Pos, Type> typeMap = new HashMap<>();

    // Initialize with a blank board
    public CData() {
        for (int i = 0; i < board.length; i++) { // board.length == 15 + 8
            for (int j = 0; j < board.length; j++) {
                Pos p = new Pos(i, j).plus(-4);
                if (!p.inRect()) {
                    setBoard(p, -1);
                }
            }
        }
    }

    /**
     * Initialize with the first point
     */
    public CData(Pos p) {
        this();
        append(p);
    }

    /**
     * Initialize from string
     * "77,a0"->[{7,7},{10,0}].
     */
    public CData(String s) {
        this();
//        if (ss == null || ss.length() < 4) {
//            return;
//        }
//        for (String s : ss.substring(3, ss.length()).split(",")) {
//            // dataSequence.add(new Pos(s));
//            append(new Pos(s));
//        }
        if (s == null) {
            return;
        }
        for (String p : s.split(",")) {
            append(new Pos(p));
        }
    }

    /**
     * Initialize with a given point list
     */
    public CData(List<Pos> a) {
        this();
        if (a == null) {
            return;
        }
        for (Pos p : a) {
            append(p);
        }
        //len = a.size();

        // dataSequence = a;
        //dataSequence.addAll(a);

        // TODO: calculate board and type map
    }


    private void setBoard(Pos p, int i) {
        board[p.x + 4][p.y + 4] = i;
    }


    public int getBoard(Pos p) {
        return board[p.x + 4][p.y + 4];
    }

    public int getBoard(int x, int y) {
        return getBoard(new Pos(x, y));
    }

    public int[][] getBoard() {
        return board;
    }

    /**
     * get data sequence
     */
    public List<Pos> getData() {
        return dataSequence;
    }

    /**
     * get data with given index. the index can be negative
     */
    public Pos getData(int i) {
        if (i >= 0 && i < len) {
            // positive index (normal)
            return dataSequence.get(i);
        } else if (i < 0 && i >= -len) {
            // negative index (count backwards)
            return dataSequence.get(len + i);
        } else {
            // the index out of bound, return (-1, -1)
            return new Pos(-1, -1);
        }
    }

    /**
     * @return length
     */
    public int length() {
        return len;
    }

    /**
     * @return last move, 1 is black, 2 is white
     */
    public int getFinalSide() {
        return 2 - (len % 2);
    }

    /**
     * return CData to append more than one time
     */
    public CData append(Pos p) {

        // length
        len++;

        // dataSequence
        dataSequence.add(p);

        //set board
        setBoard(p, getFinalSide());

        // typeMap

        // remove 1 pos
        // for former steps, type map may contains p already.
        typeMap.remove(p);

        Calculate.updateTypeMapOptimized(this, p);
        return this;
    }

    public CData append(int x, int y) {
        return append(new Pos(x, y));
    }

//    public void prepend(Pos p) {
//        len++;
//        dataSequence.add(0, p);
//    }

    // drop the last data
    public CData drop() {
        len--;

        Pos p = dataSequence.remove(len);

        setBoard(p, 0);


        typeMap.put(p, Type.getType(getBoard(), p));
        Calculate.updateTypeMapOptimized(this, p);

        return this;
    }

    // drop the last n data
    public CData drop(int n) {
        int N = Tool.trim(n, 0, len);

        while (N > 0) {
            drop();
            N--;
        }
        return this;
    }

    // contain and not contain

    public boolean notContain(int x, int y) {
        return !dataSequence.contains(new Pos(x, y));
    }

    public boolean notContain(Pos p) {
        return notContain(p.x, p.y);
    }

    /**
     * @return a new CData object that contains the same dataSequence
     */
    public CData copy() {
        return new CData(dataSequence);
    }


    public Type getType(Pos p){
        return typeMap.get(p);
    }

    public void putType(Pos pos,Type t){
        typeMap.put(pos, t);
    }

    public void removeType(Pos pos){
        typeMap.remove(pos);
    }


    /**
     * used to print (web app)
     *
     * @return {7,7},{10,0}->"77,a0"
     */
    @Override
    public String toString() {
        String[] s = new String[len];
        for (int i = 0; i < len; i++) {
            s[i] = dataSequence.get(i).getStr();
        }
        return String.join(",", s);
    }
    //
    // public int getScore() {
    // int maxScore = 0;
    // for (Map.Entry<Pos, int[][]> entry : typeMap.entrySet()) {
    // int score = 0;
    // for (int black : entry.getValue()[0]) {
    // score += Calculate.score_black.get(black);
    // }
    // for (int white : entry.getValue()[1]) {
    // score += Calculate.score_white.get(white);
    // }
    // maxScore = Math.max(maxScore, score);
    // }
    // return maxScore;
    //
    // }


    // public void plot() {
    // for (int[] i : dataMatrix) {
    // System.out.println(Arrays.toString(i));
    // }
    //
    // }

//    public Type getType(){
//
//    }


//    public boolean isWin() {
//        for (Type type : typeMap.values()) {
//            for (int b : type.black) {
//                if (b == five) {
//                    return true;
//                }
//            }
//            for (int w : type.white) {
//                if (w == five) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    public int getValue(Pos p) {
        if (!typeMap.containsKey(p)) {
            return 0;
        }
        return typeMap.get(p).getScore(3 - getFinalSide());

    }


    public Pos getBestPosition() {
        int bestValue = 0;
        Pos bestPos = new Pos();
        for (Pos p : typeMap.keySet()) {
            if (getValue(p) > bestValue) {
                bestValue = getValue(p);
                bestPos = p;
            }
        }
        return bestPos;
    }

    public Pos step() {
        int score = 0;
        Pos pos = null;
        for (Map.Entry<Pos, Type> entry : typeMap.entrySet()) {
            if (score < getValue(entry.getKey())) {
                score = getValue(entry.getKey());
                pos = entry.getKey();
            }
        }
        return pos;
    }


    public void plot() {
        System.out.println("_________________");
        for (int[] line : getBoard()) {
            System.out.print("|");
            for (int i : line) {
                System.out.print(i);
            }
            System.out.println("|");
        }
        System.out.println("_________________");

    }

    /**
     * if the data sequence is the same, then the CData should be same.
     *
     * @return
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataSequence == null) ? 0 : dataSequence.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CData other = (CData) obj;
        if (dataSequence == null) {
            if (other.dataSequence != null)
                return false;
        } else if (!dataSequence.equals(other.dataSequence))
            return false;
        return true;
    }


    // Test
    public static void main(String[] args) {
        CData d = new CData("00,50,01,51,02,52,03,53,54");
        System.out.println(d.getBestPosition());

    }

}
