package com.panxiantong.gomoku;

import java.util.*;

public interface Engine {


    Pos getMove(CData d);
//        if (d.length() == 0) {
//            return new Pos(7, 7);
//        } else {
//            return d.getBestPosition();
//        }


    public static Map<String,Integer> chargewhodraw(Engine e1, Engine e2){
        Map<String,Integer> result = new HashMap<>();
        CData d = new CData();
        while(!Calculate.isWin(d)){
            d.append(e1.getMove(d));
            if(Calculate.isWin(d)){
                result.put("winner",1);
                break;
            }else{
                d.append(e2.getMove(d));
            }
        }
        result.put("winner",2);
        result.put("data",d.length());
        return result;
    }
}
