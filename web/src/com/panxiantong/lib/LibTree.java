package com.panxiantong.lib;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import com.panxiantong.gomoku.CNode;
import com.panxiantong.gomoku.Tool;

public class LibTree {
    private byte[] lib;

    private int index;

    public LibTree(String s) {

        lib = Tool.readFileByBytes(s);
        // initialize index.

        index = getBeginningIndex(lib);
    }

    private static int getBeginningIndex(byte[] lib) {
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

        int startingIndex = index;
        // if (index == 23) {
        // System.out.println("23: " + lib[23] + "," + lib[24]);
        // }
        // if ((b & 0b0010110) != 0) {
        // System.out.println("b: " + b + ", index: " + index);
        // }
        // int b 代表一个8bit的数据76543210

        // b(7) = 1->exist sibling
        // b(6) = 1->exist child
        // b(5) = 0
        // b(4) = 1->exist label
        // b(3) = 1 -> exist comment
        // b(2) = b(1)=0
        // b(0) = 1->exist text

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

            // commnent can be composed of two parts: comm1 08 comm2

            int start = index + 1;
            while ((lib[++index]) != 0 || lib[index + 1] != 0) {
                // comment[i]=(char) i;
                if (lib[index] == 8) {
                    lib[index] = 32;
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
                System.out.println("error2, index: " + index);
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
            // 00 01 comment 00 00 text 00 | 00 version2

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
                while ((lib[++index]) != 0 || lib[index + 1] != 0) {
                    // comment = comment + (char) i;
                }
                String comment = toGBK(lib, start, index);
                tree.getData().setComment(comment);

                if (lib[++index] != 0) {
                    System.out.println("error5, index: " + index);
                }
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

    public static void main(String[] args) {
        String s = "C:\\Users\\xiant\\Dropbox\\wzq\\山口打点（科普版）.lib";
        String s2 = "E:\\五子棋\\lib\\第一届神龙杯.lib";
        // System.out.println(getBeginningIndex(Tool.readFileByBytes(s2)));

        LibTree lt = new LibTree(s2);
        byte[] bs = lt.lib;

        lt.readLib();

        System.out.println(bs[23]);
        System.out.println(bs[24]);
        System.out.println(bs[25]);
        System.out.println(bs[26]);
        System.out.println(bs[27]);
        System.out.println(bs[28]);
        System.out.println(bs[29]);
        System.out.println(bs[30]);
        // CNode<LibElement> tree = new LibTree(s2).readLib();
        System.out.println(toGBK(bs, 22, 32));
        // tree.getSize()
        // byte b[] = Tool.readFile(s);
        // System.out.println("," + tree.getData());
        // String c = null;
        // try {
        // c = new String(Arrays.copyOfRange(b, 6, 200), "gbk");
        // } catch (UnsupportedEncodingException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // System.out.println(c);
        // UsingProcessing.main(args);
        // PApplet.main(new UsingProcessing().getClass().getName());

    }
}
