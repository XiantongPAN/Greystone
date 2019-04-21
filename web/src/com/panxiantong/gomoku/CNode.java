package com.panxiantong.gomoku;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class CNode<T> implements Serializable {
    private CNode<T> parent;

    private T data;

    private List<CNode<T>> children;

    // public CNode() {
    // this.parent = null;
    // this.data = null;
    // this.children = new LinkedList<>();
    // }

    public CNode(T data) {
        this.parent = null;
        this.data = data;
        this.children = new LinkedList<>();
    }

    // public CNode(CNode<T> parent, T data, List<CNode<T>> children) {
    // super();
    // this.parent = parent;
    // this.data = data;
    // this.children = children;
    // }

    public CNode<T> getParent() {
        return parent;
    }

    // Be careful to use
    // public void setParent(CNode<T> parent) {
    // this.parent = parent;
    // }

    public T getData() {
        return data;
    }

    public CNode<T> setData(T data) {
        this.data = data;
        return this;
    }

    public List<CNode<T>> getChildren() {
        return children;
    }

    public CNode<T> getChild(int i) {
        int length = children.size();
        if (i >= 0 && i < length) {
            return children.get(i);
        } else if (i < 0 && i + length >= 0) {
            return children.get(length + i);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    // public void setChildren(List<CNode<T>> children) {
    // this.children = children;
    // }

    public void addChild(CNode<T> child) {
        children.add(child);
        child.parent = this;
    }

    public void addSibling(CNode<T> sibling) {
        if (parent == null) {
            throw new NullPointerException("the node has no parent, addSibling() cannot work");
        }
        if (parent.children == null) {
            throw new NullPointerException("the children should be always initialized by list");
        }
        parent.children.add(sibling);
        sibling.parent = this.parent;
    }

    public boolean isRoot() {
        // System.out.println("xxx" + this.getChildren().size());
        return this.getChildren().size() == 0;
    }

    // Get depth of recent node
    public int getDepth() {
        int depth = 0;
        CNode<T> p = parent;// Start with parent
        while (p != null) {
            depth++;
            p = p.parent;
        }
        return depth;
    }

}
