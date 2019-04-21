package com.panxiantong.Test;

import com.panxiantong.gomoku.CData;
import com.panxiantong.gomoku.Calculate;
import com.panxiantong.gomoku.Engine;
import com.panxiantong.gomoku.Pos;

import java.util.*;
public class EngineTest {
    public static void main(String[] args) {


        Engine e = CData::getBestPosition;
        Engine e2 = d->random(d);
        System.out.println(Engine.chargewhodraw(e, e2));
    }

    public static Pos random(CData d){
        var v = Calculate.possiblePosition(d,2);
        return v.get(new Random().nextInt(v.size()));

    }


}
