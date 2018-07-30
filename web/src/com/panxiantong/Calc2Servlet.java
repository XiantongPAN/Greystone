package com.panxiantong;

// the gson jar pack should be put into dir WEB-INF/lib/

import com.google.gson.*;
import com.panxiantong.alphabeta.AlphaBeta;
import com.panxiantong.gomoku.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "Calc2Servlet", urlPatterns = "/Calc2Servlet")
public class Calc2Servlet extends HttpServlet {

    private List<Pos> data;
    private int whoWin;
    private int engine;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("doPost in CalcServlet");


        // receive information from jsp
        JsonObject json = Tool.readJSONData(request);
        data = new ArrayList<>();
        for (JsonElement e : json.get("data").getAsJsonArray()) {
            int x = e.getAsJsonObject().get("x").getAsInt();
            int y = e.getAsJsonObject().get("y").getAsInt();
            data.add(new Pos(x, y));
        }
        whoWin = json.get("whoWin").getAsInt();
        engine = json.get("engine").getAsInt();

        CData d = new CData(data);
        if (Calculate.isWin(d)) {
            // the client pass a win data.

            // record it in database
            //greystone.DBC.ipAddressInsert(Upload.getIpAddress(request), "w" + s);

            // show end game information
            whoWin = d.getFinalSide();
            //out.print(d.getFinalSide() + s.substring(1, 2) + "," + d.toString());


        } else {

            // Some engine may not able to make the first step,
            // here it constrains the first step at (7 ,7).

            //System.out.println(d.length() + ": " + s);

            switch (engine) {
                case 0:
                    //do not turn
                    break;
                case 1:
                    // internal simple engine
                    // identifier = 0
                    //d.append(Calculate.findMax(Calculate.globalScore(d)));
                    if (d.length() == 0) {
                        d.append(7, 7);
                    } else {
                        d.append(d.getBestPosition());
                    }

                    break;
                // use wine engine
                // identifier = 1
                //String wine = Tool.runExe(Calculate.engine_wine, s);
                //d = new CData(wine);
                case 2:
                    // ab search
                    AlphaBeta ab = new AlphaBeta(new Search(d));
                    Pos p = (Pos) ab.analyzeDepth(4);
                    d.append(p);
                    break;
                default:
                    break;
                // CData step
                //d.append(new CData(s).step());
            }


            if (Calculate.isWin(d)) {

                // record it to database
                //greystone.DBC.ipAddressInsert(Upload.getIpAddress(request), "b" + s);

                // show end game information
                whoWin = d.getFinalSide();
                data = d.getData();
                //out.print(d.getFinalSide() + s.substring(1, 2) + "," + d.toString());
            } else {
                // not win yet
                // goWith(0, s, d, request, response);

                //System.out.println(0 + s.substring(1, 2) + "," + d.toString());
                //out.print(0 + s.substring(1, 2) + "," + d);
                //out.print(0 + s.substring(1, 2) + "," + d.toString());
                //PrintWriter out = response.getWriter();
                whoWin = 0;
                data = d.getData();


            }
        }
        System.out.println(new Gson().toJson(this));
        response.getWriter().println(new Gson().toJson(this));
        //out.flush();


    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("you should not see doGet");
    }


}
