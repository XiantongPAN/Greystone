package com.panxiantong;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import com.google.gson.*;

import com.panxiantong.gomoku.CData;
import com.panxiantong.gomoku.Pos;
import com.panxiantong.gomoku.Tool;
import com.panxiantong.lib.*;
import com.panxiantong.gomoku.CNode;

@WebServlet(name = "LibServlet", urlPatterns = "/LibServlet")
public class LibServlet extends HttpServlet {

    private List<Pos> data;
    private Map<Pos, String> children;
    private String comment;


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doPost in LibServlet");

        JsonObject json = Tool.readJSONData(request);

        data = new ArrayList<>();
        for (JsonElement e : json.get("data").getAsJsonArray()) {
            int x = e.getAsJsonObject().get("x").getAsInt();
            int y = e.getAsJsonObject().get("y").getAsInt();
            data.add(new Pos(x, y));
        }
        //String msg = request.getParameter("msg");
        CData d = new CData(data);


        String dr = "/Users/mac/Dropbox/wzq/";
        //String s = dr + "山口打点（科普版）.lib";
        String s = dr +"puyue.lib";
        LibTree lt = new LibTree(s);
        CNode<LibElement> tree = lt.readLib();


        // children position
        children = LibTree.getInfo(s, d);

        // if comment has no content, json data will not include it.
        comment = tree.getData().getComment();
        if(comment == null || comment.equals("")){
            comment = "no comment";
        }


        System.out.println(new Gson().toJson(this));

        // set charset to send chinese
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.getWriter().println(new Gson().toJson(this));

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doGet in LibServlet");
    }


}
