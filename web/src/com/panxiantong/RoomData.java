package com.panxiantong;

import java.util.*;
import java.util.concurrent.*;

public class RoomData {

    private List<WSServer> black = new CopyOnWriteArrayList<>();
    private List<WSServer> white = new CopyOnWriteArrayList<>();

    private String data = "";

    public RoomData() {

    }


    public RoomData addBlack(WSServer e) {
        black.add(e);
        return this;
    }

    public RoomData addWhite(WSServer e) {
        white.add(e);
        return this;
    }

    public WSServer getBlackMover() {
        if(black.size()>0){
            return black.get(0);
        }else{
            return null;
        }

    }

    public WSServer getWhiteMover() {
        if(white.size()>0){
            return white.get(0);
        }else {
            return null;
        }

    }

    public List<WSServer> getBlack() {
        return black;
    }

    public List<WSServer> getWhite() {
        return white;
    }

    public RoomData delete(WSServer s){
        black.remove(s);
        white.remove(s);

        return this;
    }


    public String getData() {
        return data;
    }

    public RoomData setData(String data){
        this.data = data;
        return this;
    }


}
