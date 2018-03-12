<%--
  Created by IntelliJ IDEA.
  User: xiant
  Date: 2018/2/26
  Time: 20:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>

<head>
    <script src="js/processing.js"></script>
    <script src="js/chessBoard.js"></script>
    <title>GreyStone</title>
</head>

<body>


<%
    String msg = request.getParameter("msg");
    if (msg == null || msg == "") {
        msg = "00";
    }
%>
<p align="center">
    <canvas id="canvas1" width="900" height="900"></canvas>
</p>

<script id="script1">


    //Attaching js code to the canvas by using a sketch object
    const sketch = new Processing.Sketch();

    sketch.attachFunction = function (p) {


        let data = "<%=msg%>";

        let s = data.split(",");
        let arr1 = getArr(s);

        let whoWin = data.charAt(0);
        let engine = data.charAt(1);
        let information = "";

        p.setup = function () {
            p.size(900, 900);
        };

        p.draw = function () {

            drawBoard(p, arr1);

            //draw win information
            p.fill(200);
            p.rect(50, 800, 720, 900);

            p.fill(0);
            if (whoWin === "2") {
                p.fill(255);
                information = "white win, press to restart";
            } else if (whoWin === "1") {
                p.fill(0);
                information = "black win, press to restart";
            }
            p.textSize(50);
            p.textAlign(p.CENTER, p.CENTER);
            p.text(information, 400, 850);

        };


        p.mousePressed = function () {
            if (whoWin !== "0") { // restart
                if (p.mouseX > 50 && p.mouseX < 720 && p.mouseY > 800 && p.mouseY < 900) {
                    location.href = "index.jsp";
                }
            } else {
                let x = parseInt((p.mouseX - init_x + grid_size / 2) / grid_size);
                let y = parseInt((p.mouseY - init_y + grid_size / 2) / grid_size);

                let xy = x.toString(16) + y.toString(16);
                if (!contains(s, xy) && x < board_size && x >= 0 && y < board_size && y >= 0) {

                    arr1.push(16 * x + y);
                    information = "computing...";

                    p.redraw();
                    //location.href = "CalcServlet?msg=" + data + "," + xy;
                    let xmlhttp = new XMLHttpRequest();
                    xmlhttp.onreadystatechange = function () {
                        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {

                            data = xmlhttp.responseText;

                            s = data.split(",");
                            arr1 = getArr(s);
                            whoWin = data.charAt(0);
                            information = "It's your turn";
                            p.redraw();
                        }
                    };
                    xmlhttp.open("GET", "CalcServlet?msg=" + data + "," + xy, true);
                    xmlhttp.send();
                }
            }
        }
    };

    const canvas = document.getElementById("canvas1");
    //attaching the sketch to the canvas
    new Processing(canvas, sketch);

</script>
</body>
</html>
