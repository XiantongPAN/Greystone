package com.panxiantong.Test;

import com.google.gson.*;
import com.panxiantong.gomoku.CData;
import com.panxiantong.gomoku.CNode;
import com.panxiantong.gomoku.Pos;
import com.panxiantong.lib.LibElement;
import com.panxiantong.lib.LibTree;

import java.util.*;

public class Json {
    private CData d;
    private Map<Pos, String> children;

    public Json(CData d, Map<Pos, String> p) {
        this.d = d;
        children = p;
    }


    public static void main(String[] args) {
        CData d = new CData();
        d.append(d.getBestPosition());
        System.out.println(d.getData());
//        String j = "{\"data\":[{\"x\":7,\"y\":7},{\"x\":8,\"y\":6}],\"whoWin\":0}";
//        JsonParser parser = new JsonParser();
//        JsonObject json = (JsonObject) parser.parse(j);
//        //System.out.println(json.get("data").getAsJsonArray().);
//        JsonArray array = json.get("data").getAsJsonArray();
//        List<Pos> data = new ArrayList<>();
//        for (JsonElement e : array) {
//            int x = ((JsonObject) e).get("x").getAsInt();
//            int y = ((JsonObject) e).get("y").getAsInt();
//            data.add(new Pos(x, y));
//        }
//        int whoWin = json.get("whoWin").getAsInt();
//        System.out.println(data);
//  String dr = "/Users/mac/Dropbox/wzq/";
//        String s = dr + "山口打点（科普版）.lib";
//        LibTree lt = new LibTree(s);
//        CNode<LibElement> tree = lt.readLib();
//
//        // receive information from jsp: CData d
//        CData dd = new CData("00,77,78");
//
//        // children position
//        Json t = new Json(dd, LibTree.getInfo(s, dd));
//
//
//        JsonParser parser = new JsonParser();
//        Gson gson = new Gson();
//        String ss = gson.toJson(t);
////        int[] k = new int[]{1,2,3};
////        JsonObject json=(JsonObject)parser.parse("{\"x\":10,\"y\":\"120\"}");
////        int s = json.get("y").getAsInt();
//        System.out.println(ss);

    }

}
