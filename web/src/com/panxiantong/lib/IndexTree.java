package com.panxiantong.lib;

import com.panxiantong.gomoku.CNode;
import com.panxiantong.gomoku.Tool;

import static com.panxiantong.lib.LibTree.getBeginningIndex;

public class IndexTree {
    public CNode<Integer> tree0;

    public IndexTree(CNode<Integer> tree) {
        tree0 = tree;
    }

    public static CNode<Integer> CreateIndexTree(String s) {
        byte[] lib = Tool.readFileByBytes(s);
        // initialize index.

        int index = getBeginningIndex(lib)+1;

        CNode<Integer> tree = new CNode<>(-1);
        //tree.addChild(new CNode<>(0));
        CNode<Integer> n = null;// tree.traverse();

        int temp = 0;

        while ((n = traverse(tree)) != null) {
            System.out.println("index: " + index);
            // temp = lib[index];
            // while (temp == 0) {// 1 or 2
            // temp = lib[++index];
            // }
            // n.setData(new LibElement(temp));

            n.setData(index);
            int b = lib[index];
            if (((b >> 7) & 1) == 1) {
                // exist sibling
                // System.out.println("exist sibling");
                n.addSibling(new CNode<>(-1));
            }
            if (((b >> 6) & 1) == 0) {
                // exist child
                // System.out.println("exist child");
                n.addChild(new CNode<>(-1));
            }
            index = findNext(index, lib);
        }
        return n;
        // tree.setParent(new ChessNode());
    }

    private static CNode<Integer> traverse(CNode<Integer> tree) {
        if (tree == null) {
            throw new NullPointerException("Please input an exist node to the 'traverse' method");
        }
        if (tree.getData() != -1) {
            if (tree.isRoot()) {
                return null;
            }
            for (CNode<Integer> node : tree.getChildren()) {
                CNode<Integer> n = traverse(node);
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

    private static int findNext(int index, byte[] lib) {
        int nextIndex = index;
        while (lib[++nextIndex] != 0) {
        }

        return nextIndex + 2;
    }

    public static void main(String[] args) {
        //byte[] lib = Tool.readFileByBytes("/Users/mac/Dropbox/wzq/huayue.lib");
        //int index = 0;
//        for (int i = 0; i < 30; i++) {
//            System.out.println(index=findNext(index, lib));
//        }

//        for (int i = 0; i < 50; i++) {
//            System.out.println("" + lib[i] % 16 + "," + lib[i] / 16);
//        }

        CNode<Integer> tree = CreateIndexTree("/Users/mac/Dropbox/wzq/huayue.lib");
        System.out.println(1);

    }

}
