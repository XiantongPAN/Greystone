package com.panxiantong.lib;

import java.io.*;
import java.util.*;

import com.panxiantong.gomoku.CData;
import com.panxiantong.gomoku.CNode;
import com.panxiantong.gomoku.Pos;
import com.panxiantong.gomoku.Tool;

public class LibTree {
    private byte[] lib;

    private int index;

    public CNode<LibElement> tree0;

    //public CNode<LibElement> tree;

    public LibTree(CNode<LibElement> tree){
        tree0 = tree;
    }

    public LibTree(String s) {

        lib = Tool.readFileByBytes(s);
        // initialize index.

        index = getBeginningIndex(lib);
    }

    /**
     * the beginning part of the lib:
     * FF
     * 52 65 6E 4C 69 62 (RenLib)
     * FF 03 00 (version3)
     * FF * 10
     * 00 00
     * 78...
     */
    public static int getBeginningIndex(byte[] lib) {
        int index = 0;
        while (lib[index] + lib[index + 1] + lib[index + 2] != -3) {
            index++;
        }
        while (lib[index] == 0 || lib[index] == -1) {
            index++;
        }

        return index;
    }

    // find the first node with the property: tree.getData().getExist() = false
    public static CNode<LibElement> traverse(CNode<LibElement> tree) {
        if (tree == null) {
            throw new NullPointerException("Please input an exist node to the 'traverse' method");
        }
        if (tree.getData().getExist()) {
            if (tree.isRoot()) {
                return null;
            }
            for (CNode<LibElement> node : tree.getChildren()) {
                CNode<LibElement> n = traverse(node);
                // 后序遍历
                if (n != null) {
                    return n;
                }
            }
            return null;
        } else {
            return tree;
        }

    }

    public CNode<LibElement> readLib() {

        CNode<LibElement> tree = new CNode<>(new LibElement(0));
        tree.addChild(new CNode<>(new LibElement()));
        CNode<LibElement> n = null;// tree.traverse();

        int temp = 0;

        while ((n = traverse(tree)) != null) {
            System.out.println("index: " + index);
            // temp = lib[index];
            // while (temp == 0) {// 1 or 2
            // temp = lib[++index];
            // }
            // n.setData(new LibElement(temp));

            n.setData(new LibElement(lib[index]));

            process(n, lib[++index]);
        }
        // tree.setParent(new ChessNode());
        return tree;// .getParent();
    }

    private void process(CNode<LibElement> tree, int b) {
        //System.out.println("b" + b);
        int startingIndex = index;
        // if (index == 23) {
        // System.out.println("23: " + lib[23] + "," + lib[24]);
        // }
        // if ((b & 0b0010110) != 0) {
        // System.out.println("b: " + b + ", index: " + index);
        // }
        // int b 代表一个8bit的数据76543210

        // b(7) = 1 -> exist sibling
        // b(6) = 0 -> exist child
        // b(5) = 1 -> exist old comment
        // b(4) = 1 -> label is true(mark)
        // b(3) = 1 -> exist comment
        // b(2) = 1 -> start
        // b(1) = 1 -> no move
        // b(0) = 1 -> exist text(extension)

        if (((b >> 7) & 1) == 1) {
            // exist sibling
            // System.out.println("exist sibling");
            tree.addSibling(new CNode<>(new LibElement()));
        }
        if (((b >> 6) & 1) == 0) {
            // exist child
            // System.out.println("exist child");
            tree.addChild(new CNode<>(new LibElement()));
        }
        if (((b >> 4) & 1) == 1) {
            // exist label
            System.out.println("exist label at: " + index);
            tree.getData().setLabel(true);
        }

        if ((b & 0b1001) == 0b1000) {
            // only exist comment
            // comment 00 '00'
            // comment can be composed of two parts(line): comm1 08 comm2

            int start = index + 1;
//            while ((lib[++index]) != 0 || lib[index + 1] != 0) {
//                // comment[i]=(char) i;
//                if (lib[index] == 8) {
//                    lib[index] = 32;
//                }
//            }

            while (lib[++index]!=0) {
                if(lib[index]==8){
                    lib[index]=32;//'\n'
                }
            }

            String comment = toGBK(lib, start, index);
            tree.getData().setComment(comment);

            // System.out.println(b + ": Comment at " + index + " : " + comment);
        }
        if ((b & 0b1001) == 1) {
            // only exist text

            // 00 01 text 00 | 00
            if (lib[++index] != 0) {
                System.out.println("error1, index: " + index);
            }
            if (lib[++index] != 1) {
                System.out.println("error2, index: " + index +
                        ", " + lib[index - 1] + ", " + lib[index] + ", b = " + b);
            }

            int start = index + 1;
            while ((lib[++index]) != 0) {
                // text = text + (char) i;
            }
            String text = toGBK(lib, start, index);
            tree.getData().setText(text);
            // System.out.println("Text: " + text);
        }
        if ((b & 0b1001) == 0b1001) {
            // exist text and comment

            // 00 01 08 comment 00 text 00 | 00 version1
            // 00 01 comment 00 '00' text 00 | 00 version2

            if (lib[++index] != 0) {
                System.out.println("error3, index: " + index);
            }
            if (lib[++index] != 1) {
                System.out.println("error4, index: " + index);
            }

            // different version of renlib
            if (lib[index + 1] == 8) {
                // version 1
                System.out.println("version 1, b = " + lib[index - 2]);
                index += 1;
                int start = index + 1;
                while (lib[++index] != 0) {
                    //
                }
                String comment = toGBK(lib, start, index);
                tree.getData().setComment(comment);

                start = index + 1;

                while ((lib[++index]) != 0) {
                    // text = text + (char) i;
                }
                String text = toGBK(lib, start, index);
                tree.getData().setText(text);

            } else {
                // version 2
                System.out.println("version 2");
                int start = index + 1;
//                while ((lib[++index]) != 0 || lib[index + 1] != 0) {
//                    // comment = comment + (char) i;
//                }
                while((lib[++index]) != 0){

                }
                String comment = toGBK(lib, start, index);
                tree.getData().setComment(comment);

//                if (lib[++index] != 0) {
//                    System.out.println("error5, index: " + index);
//                }
                if(lib[index+1]==0){
                    index++;
                }
                index--;
                start = index + 1;

                while ((lib[++index]) != 0) {
                    // text = text + (char) i;
                }
                String text = toGBK(lib, start, index);
                tree.getData().setText(text);

                // System.out.println("exist text and comment: " + text + "\n" +
                // comment);
            }

        }

        // important note: if the index is not changed, then plus 1
        if (startingIndex == index) {
            // System.out.println(index);
            index++;
        }

        while (lib[index] == 0 && index < lib.length - 1) {
            index++;
        }
    }

    /**
     * serve for the method <code>"process"</code>
     *
     * @param lib
     * @param start
     * @param end
     * @return
     */
    private static String toGBK(byte[] lib, int start, int end) {
        String s = null;
        try {
            s = new String(Arrays.copyOfRange(lib, start, end), "gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public Map<Pos, String> getInfo(String dir, CData d) {
        LibTree lt = new LibTree(dir);
        tree0 = lt.readLib();
        for (Pos p : d.getData()) {
            boolean flag = true;
            for (CNode<LibElement> c : tree0.getChildren()) {

                if (flag && c.getData().getPoint().equals(p)) {
                    tree0 = c;
                    flag = false;
                }
            }
            //System.out.println("getInfo: " + p);
        }
        //System.out.println(tree.getData());
        Map<Pos, String> result = new HashMap<>();
        for (CNode<LibElement> t : tree0.getChildren()) {
            String text = t.getData().getText();
            if (text == null || text.equals("")) {
                result.put(t.getData().getPoint(), "x");
            } else {
                result.put(t.getData().getPoint(), text);
            }

        }

        return result;
    }

    public static Map<Pos, String> getInfo(CNode<LibElement> tree0, CData d) {

        for (Pos p : d.getData()) {
            boolean flag = true;
            for (CNode<LibElement> c : tree0.getChildren()) {

                if (flag && c.getData().getPoint().equals(p)) {
                    tree0 = c;
                    flag = false;
                }
            }
            //System.out.println("getInfo: " + p);
        }
        //System.out.println(tree.getData());
        Map<Pos, String> result = new HashMap<>();
        for (CNode<LibElement> t : tree0.getChildren()) {
            String text = t.getData().getText();
            if (text == null || text.equals("")) {
                result.put(t.getData().getPoint(), "x");
            } else {
                result.put(t.getData().getPoint(), text);
            }

        }

        return result;
    }

    public static void main(String[] args) {
        String dr = "/Users/mac/Dropbox/wzq/";
        String s = dr + "B10.lib";
        //LibTree lt = new LibTree(s);
        //byte[] bs = lt.lib;
        //System.out.println(Arrays.toStringR(bs));
        //CNode<LibElement> tree = lt.readLib();

        //Tool.writeObjectToFile(tree);

        CNode<LibElement> tr =(CNode<LibElement>) Tool.readObjectFromFile();


        //System.out.println(toGBK(bs, 49, 73));
     //System.out.println(tr.getChild(0).getChildren().size());
//         System.out.println(getInfo(s, new CData("00,77,68")));
//
//        System.out.println(tree.getChild(0).getChild(0).getData().getText());
//
//
//        System.out.println(bs[23]);
//        System.out.println(bs[24]);
//        System.out.println(bs[25]);
//        System.out.println(bs[26]);
//        System.out.println(bs[27]);
//        System.out.println(bs[28]);
//        System.out.println(bs[29]);
//        System.out.println(bs[30]);
//
//        System.out.println(toGBK(bs, 22, 32));

    }
}
