package com.panxiantong.lib;

import com.panxiantong.gomoku.CNode;
import com.panxiantong.gomoku.CData;
import com.panxiantong.gomoku.Pos;
import processing.core.PApplet;

public class ReadLibUsingProcessing extends PApplet {

    int init_x = 50;
    int init_y = 50;
    int grid_size = 50;
    int board_size = 15;
    float chess_size = 0.8f;// 0.8*grid_size
    CData d = new CData();

    CNode<LibElement> currentNode;

    boolean showWin = true;// false;

    public ReadLibUsingProcessing() {
        String dr = "/Users/mac/Dropbox/wzq/";
        String s = dr + "山口打点（科普版）.lib";
        //String lib2 = "E:\\五子棋\\lib\\第一届神龙杯.lib";
        currentNode = new LibTree(s).readLib();

        // d.append(currentNode.getPoint());

    }

    public static void main(String[] args) {
        String c = Thread.currentThread().getStackTrace()[1].getClassName();
        System.out.println(c);
        PApplet.main(c);
    }

    public void settings() {
        size(900, 900);
    }

    public void setup() {
        surface.setTitle("GreyStone");
        surface.setResizable(true);
    }

    public void draw() {
        if (showWin) {
            fill(0);
            text("You win", 300, 300);
        }
        // ellipse(width/2,height/2,second(),second());

        // Draw chess
        // if (mousePressed) {
        // int x = (mouseX - init_x + grid_size / 2) / grid_size;
        // int y = (mouseY - init_y + grid_size / 2) / grid_size;
        //
        // if (d.notContain(x, y) && x >= 0 && x < board_size && y >= 0 && y <
        // board_size) {
        // d.append(x, y);
        // simpleAI();
        // }
        //
        // }

        // if (mousePressed && mouseButton == RIGHT) {
        // d.drop();
        // ellipse(500, 500, 50, 50);
        // // redraw();
        //
        // }

        fill(100, 130, 120);
        rect(0, 0, height, width);

        // Draw chess board
        for (int i = 0; i < board_size; i++) {
            line(grid_size * i + init_x, init_y, grid_size * i + init_x, grid_size * (board_size - 1) + init_y);
            line(init_x, grid_size * i + init_y, grid_size * (board_size - 1) + init_x, grid_size * i + init_y);
        }

        // Draw point
        fill(0);
        for (int[] a : new int[][]{{7, 7}, {3, 3}, {3, 11}, {11, 3}, {11, 11}}) {
            ellipse(grid_size * a[0] + init_x, grid_size * a[1] + init_y, grid_size / 4, grid_size / 4);
        }

        // Draw chess
        for (int i = 0; i < d.length(); i++) {

            // fill(i % 2 * 255);
            if (i % 2 == 0) {
                fill(0);
            } else {
                fill(255);
            }
            int x = d.getData(i).getX() * grid_size + init_x;
            int y = d.getData(i).getY() * grid_size + init_y;

            ellipse(x, y, 40, 40);

            if (i % 2 == 1) {
                fill(0);
            } else {
                fill(255);
            }
            if (i < 9) {
                textSize(30);
            } else if (i < 99) {
                textSize(24);
            } else {
                textSize(18);
            }
            textAlign(CENTER, CENTER);
            text(i + 1 + "", x, y);
        }

        // Draw comment
        fill(0);
        textAlign(CENTER, CENTER);
        // for output Chinese
        textFont(createFont("simsun.ttc", 40));
        // if no Chinese
        // textSize(40);
        String comment = currentNode.getData().getComment();
        if (comment != null) {
            text(comment, 400, 800);
        }

        // Draw children

        if (!currentNode.isRoot()) {
            for (CNode<LibElement> node : currentNode.getChildren()) {
                int x = node.getData().getX() * grid_size + init_x;
                int y = node.getData().getY() * grid_size + init_y;
                fill(255);
                ellipse(x, y, 13, 13);

                // Draw text
                String t = node.getData().getText();
                if (t != null) {
                    fill(0);
                    textFont(createFont("simsun.ttc", 25));
                    textAlign(CENTER, CENTER);
                    text(t, x, y);
                }

            }
        }
    }

    public void mousePressed() {
        if (mouseButton == LEFT) {
            int x = (mouseX - init_x + grid_size / 2) / grid_size;
            int y = (mouseY - init_y + grid_size / 2) / grid_size;

            if (d.notContain(x, y) && x >= 0 && x < board_size && y >= 0 && y < board_size) {
                // d.append(x, y);
                // simpleAI();

                if (!currentNode.isRoot()) {
                    for (CNode<LibElement> node : currentNode.getChildren()) {
                        if (node.getData().getPoint().equals(new Pos(x, y))) {
                            d.append(x, y);
                            currentNode = node;
                        }
                    }
                }

            }
        } else if (mouseButton == RIGHT && d.length() > 0) {
            d.drop();
            currentNode = currentNode.getParent();
        }
        redraw();
    }

}
