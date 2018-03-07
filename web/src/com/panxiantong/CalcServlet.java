package com.panxiantong;


import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.panxiantong.gomoku.*;

/**
 * Servlet implementation class CalcServlet
 */
@WebServlet(name = "CalcServlet", urlPatterns = "/CalcServlet")
public class CalcServlet extends HttpServlet {
    private static final long serialVersionUID = 13L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CalcServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // s is the string chess data for transmitting.
        String s = request.getParameter("msg");
        if (s == null || s.length() == 0) {
            s = "00";
        }

        CData d = new CData(s);// d = parse s to ChessData object

        if (Calculate.isWin(d)) {
            // the client pass a win data.

            // record it to database
            //greystone.DBC.ipAddressInsert(Upload.getIpAddress(request), "w" + s);

            // show end game information
            goWith(d.getFinalSide(), s, d, request, response);

        } else {

            // Some engine may not able to make the first step,
            // here it constrains the first step at (7 ,7).
            if (d.length() == 0) {
                d.append(7, 7);
            } else {
                System.out.println(d.length() + "," + s);

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
                goWith(d.getFinalSide(), s, d, request, response);
            } else {
                // not win yet
                goWith(0, s, d, request, response);
            }
        }

    }

    /**
     * @param i        : 0 for not win yet, 1 for black and 2 for white
     * @param data     : to extract char at 1, that's the engine info
     * @param d        : see ChessData d in doPost()
     * @param request  : the rest of param is the same in doPost()
     * @param response :
     * @throws ServletException
     * @throws IOException
     */
    private void goWith(int i, String data, CData d, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String msg = i + data.substring(1, 2) + "," + d.toString();
        RequestDispatcher rd = request.getRequestDispatcher("gomoku.jsp?msg=" + msg);
        // request.setAttribute("msg", i + data.substring(1, 2) + "," + d.toString());
        rd.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
