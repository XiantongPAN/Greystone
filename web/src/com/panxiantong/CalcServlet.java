package com.panxiantong;

import com.panxiantong.gomoku.CData;
import com.panxiantong.gomoku.Calculate;
import com.panxiantong.gomoku.Tool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CalcServlet", urlPatterns = "/CalcServlet")
public class CalcServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("dopost");

        PrintWriter out = response.getWriter();
        // s is the string chess data for transmitting.
        String s = request.getParameter("msg");
        //System.out.println("s=" + s);
        if (s == null || s.trim().isEmpty()) {
            s = "00";
        }

        CData d = new CData(s);// d = parse s to ChessData object
        //System.out.println("is win:" + Calculate.isWin(d));
        if (Calculate.isWin(d)) {
            // the client pass a win data.

            // record it to database
            //greystone.DBC.ipAddressInsert(Upload.getIpAddress(request), "w" + s);

            // show end game information
            //goWith(d.getFinalSide(), s, d, request, response);
            out.print(d.getFinalSide() + s.substring(1, 2) + "," + d.toString());

        } else {

            // Some engine may not able to make the first step,
            // here it constrains the first step at (7 ,7).
            if (d.length() == 0) {
                d.append(7, 7);
            } else {
                System.out.println(d.length() + ": " + s);

                if (s.charAt(1) == '0') {
                    // internal simple engine
                    // identifier = 0
                    //d.append(Calculate.findMax(Calculate.globalScore(d)));
                    d.append(d.getBestPosition());
                } else if (s.charAt(1) == '1') {
                    // use wine engine
                    // identifier = 1
                    String wine = Tool.runExe(Calculate.engine_wine, s);
                    d = new CData(wine);
                } else if (s.charAt(1) == '2') {
                    // CData step
                    d.append(new CData(s).step());
                }

            }
            if (Calculate.isWin(d)) {

                // record it to database
                //greystone.DBC.ipAddressInsert(Upload.getIpAddress(request), "b" + s);

                // show end game information
                //goWith(d.getFinalSide(), s, d, request, response);
                out.print(d.getFinalSide() + s.substring(1, 2) + "," + d.toString());
            } else {
                // not win yet
                // goWith(0, s, d, request, response);

                //System.out.println(0 + s.substring(1, 2) + "," + d.toString());
                //out.print(0 + s.substring(1, 2) + "," + d);
                out.print(0 + s.substring(1, 2) + "," + d.toString());
            }
        }
        //out.flush();


    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("you should not see doGet");
    }

}
