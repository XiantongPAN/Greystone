package com.panxiantong.gomoku;

import java.util.*;
import java.util.concurrent.CountedCompleter;

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

    //board data: 1: black, 2: white, 0: blank, -1: out bound
    private int[][] board = new int[size + 8][size + 8];


    // around with -1
    //private int[][] conBlack = new int[size + 8][size + 8];

    // around with 1
    //private int[][] conWhite = new int[size + 8][size + 8];

    protected Map<Pos, Type> typeMap = new HashMap<>();

    // Initialize with a blank board
    public CData() {
        for (int i = 0; i < board.length; i++) { // board.length == 15 + 8
            for (int j = 0; j < board.length; j++) {
                Pos p = new Pos(i, j);
                if (!(p.plus(-4).inRect())) {
                    setBoard(p, -1);
                    //setConWhite(p, -1);
                }
            }
        }
    }

    // Initialize with the first point
    public CData(Pos p) {
        this();
        append(p);
    }

    /**
     * "hd,77,a0"->{{7,7},{10,0}}. hd is header, omit it
     *
     * @param ss
     */
    public CData(String ss) {
        this();
        if (ss == null || ss.length() < 4) {
            return;
        }
        for (String s : ss.substring(3, ss.length()).split(",")) {
            // dataSequence.add(new Pos(s));
            append(new Pos(s));
        }
        // len = dataSequence.size();
        // cons = Calculate.construct(dataSequence, 4, -1);

        //TODO: rewrite using CData(List<Pos> a)
    }

    // Initialize with a given point list
    public CData(List<Pos> a) {
        len = a.size();

        // dataSequence = a;
        dataSequence.addAll(a);

        // TODO: calculate board and typemap
    }

//    private void setConBlack(Pos p, int i) {
//        conBlack[p.x][p.y] = i;
//    }
//
//    private void setConWhite(Pos p, int i) {
//        conWhite[p.x][p.y] = i;
//    }
//
//    public int[][] getConBlack() {
//
//        return conBlack;
//    }
//
//    public int[][] getConWhite() {
//
//        return conWhite;
//    }

    private void setBoard(Pos p, int i) {
        board[p.x][p.y] = i;
    }

    public int getBoard(Pos p) {
        return board[p.x + 4][p.y + 4];
    }

    public int[][] getBoard() {
//        int[][] bd = new int[15][15];
//        for (int i = 0; i < bd.length; i++) {
//            for (int j = 0; j < bd.length; j++) {
//                bd[i][j] = board[i + 4][j + 4];
//            }
//        }
//        return bd;
        return board;
    }

    /**
     * get data sequence
     *
     * @return
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

    public int length() {
        return len;
    }

    public int getFinalSide() {
        return 2 - (len % 2);
    }

    // return CData to append more than one time
    public CData append(Pos p) {

        // len
        len++;

        // dataSequence
        dataSequence.add(p);

        // cons
//        if (len % 2 == 0) {
//            setConWhite(p.plus(4), 1);
//            setConBlack(p.plus(4), -1);
//        } else {
//            setConWhite(p.plus(4), -1);
//            setConBlack(p.plus(4), 1);
//        }

        //set board
        setBoard(p.plus(4), getFinalSide());

        // typeMap

        // remove 1 pos
        // for former steps, type map may contains p already.
        if (typeMap.containsKey(p)) {
            typeMap.remove(p);
        }

        // update at most 8 * 4 = 32 pos
        // find the neighbor4 points. collect with set.
        Set<Pos> posSet = new HashSet<>();
        for (Pos dir : dir8) {
            for (int n = 1; n <= 4; n++) {
                Pos pos = dir.times(n).plus(p);
                if (pos.inRect()) {
                    posSet.add(pos);
                    // typeMap.put(pos, Calculate.getType(this, pos));
                }
            }
        }
        posSet.removeAll(dataSequence);


        // calculate type for each points
        Iterator<Pos> iterator = posSet.iterator();
        while (iterator.hasNext()) {
            Pos pos = iterator.next();

            // no need to check
            if (getBoard(pos) != 0) {
                throw new NoSuchElementException(dataSequence.toString());
            }

            typeMap.put(pos, Type.getType(board, pos));
        }
        return this;
    }

    public CData append(int x, int y) {
        return append(new Pos(x, y));
    }

    public void prepend(Pos p) {
        len++;
        dataSequence.add(0, p);
        // TODO: reconstruct
        // cons = Calculate.construct(dataSequence, 4, -1);
    }

    // drop the last data
    public CData drop() {
        len--;

        Pos p = dataSequence.remove(len);

        setBoard(p.plus(4), 0);


        // TODO: type map

        return this;
    }

    // drop the last n data
    public CData drop(int n) {
        int N = Tool.trim(n, 0, len);

        for (int i = 0; i < N; i++) {
            drop();
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


    public boolean isWin() {
        return false;
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
    }

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
        CData d = new CData("00,77,86,88,66,78,76,96,87,98");


//        for (int i = 0; i < 20; i++) {
//            d.append(d.getBestPosition());
//        }
        System.out.println(d.getValue(new Pos(10,8)));

        System.out.println(d.typeMap.get(new Pos(10,8)));

        //System.out.println(Arrays.toString(d.board[4]));
        //System.out.println(d.typeMap.get(new Pos(1, 1)));
        //d.plot();
    }

}
