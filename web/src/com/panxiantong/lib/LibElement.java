package com.panxiantong.lib;


import com.panxiantong.gomoku.Pos;

import java.io.Serializable;

public class LibElement implements Serializable {

    // Point
    private byte data;

    private String comment;
    private String text;
    private Boolean label;

    /**
     * tell the existence while in the tree
     */
    private Boolean exist = false;

    private LibElement(byte d, String c, String t, Boolean l) {
        data = d;
        comment = c;
        text = t;
        label = l;
    }

    // create an empty lib element, exist = false;
    public LibElement() {
        this((byte) 0, null, null, false);
    }

    public LibElement(int d) {
        this((byte) d, null, null, false);
        exist = true;
    }

    public LibElement(byte d) {
        this(d, null, null, false);
        exist = true;
    }

    /**
     * convert byte to Pos
     */
    public Pos getPoint() {
        int a = (int) data;
        if (a < 0) {
            a += 256;
        }
        // p.x = a / 16;
        // p.y = a % 16 - 1;
        return new Pos(a / 16, a % 16 - 1);
    }

    public int getX() {
        return getPoint().getX();
    }

    public int getY() {
        return getPoint().getY();
    }

    public byte getData() {
        return data;
    }

    public void setData(byte data) {
        this.data = data;
    }

    public String getComment() {
        return comment;
    }

    public LibElement setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getText() {
        return text;
    }

    public LibElement setText(String text) {
        this.text = text;
        return this;
    }

    public Boolean getLabel() {
        return label;
    }

    public LibElement setLabel(Boolean label) {
        this.label = label;
        return this;
    }

    public Boolean getExist() {
        return exist;
    }

    // public void setExist(Boolean exist) {
    // this.exist = exist;
    // }

    public String toString() {
        return getPoint().toString();

    }

}
