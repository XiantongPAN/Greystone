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


<%--<%--%>
<%--String msg = request.getParameter("msg");--%>

<%--// if this page is accessed without msg--%>
<%--if (msg == null || msg.trim().isEmpty()) {--%>
<%--msg = "00";--%>
<%--}--%>
<%--%>--%>
<p align="center">
    <canvas id="canvas1" width="900" height="900"></canvas>
</p>

<script id="script1">

    //Attaching js code to the canvas by using a sketch object
    const sketch = new Processing.Sketch();

    sketch.attachFunction = function (p) {

        let data = [{"x": 7, "y": 7}];
        let whoWin = 0;
        let engine = 2;

        let information = "";

        p.setup = function () {
            p.size(900, 900);
        };

        p.draw = function () {

            draw0(p, data);

            //draw win information
            p.fill(200);
            p.rect(50, 800, 720, 900);

            p.fill(0);
            if (whoWin === 2) {
                p.fill(255);
                information = "white win, press to restart";
            } else if (whoWin === 1) {
                p.fill(0);
                information = "black win, press to restart";
            }
            p.textSize(50);
            p.textAlign(p.CENTER, p.CENTER);
            p.text(information, 400, 850);

        };


        p.mousePressed = function () {
            if (whoWin !== 0) { // restart
                if (p.mouseX > 50 && p.mouseX < 720 && p.mouseY > 800 && p.mouseY < 900) {
                    location.href = "index.jsp";
                }
            } else {
                let x = parseInt((p.mouseX - init_x + grid_size / 2) / grid_size);
                let y = parseInt((p.mouseY - init_y + grid_size / 2) / grid_size);


                if (!contains(data, {"x": x, "y": y}) && x < board_size && x >= 0 && y < board_size && y >= 0) {

                    data.push({"x": x, "y": y});
                    information = "computing...";

                    p.redraw();
                    let xhr = new XMLHttpRequest();
                    xhr.onreadystatechange = function () {
                        if (xhr.readyState === 4 && xhr.status === 200) {

                            //when receive json data from servlet
                            let responseJson = JSON.parse(xhr.responseText);
                            data = responseJson.data;
                            whoWin = responseJson.whoWin;
                            engine = responseJson.engine;
                            information = "It's your turn";
                            p.redraw();
                        }
                    };
                    //let xy = x.toString(16) + y.toString(16);
                    //xmlhttp.open("GET", "CalcServlet?msg=" + data + "," + xy, true);
                    xhr.open("POST", "Calc2Servlet", true);
                    //xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                    //xmlhttp.send("msg=" + data + "," + xy);
                    xhr.setRequestHeader("Content-type", "application/json");
                    let json_data = {"data": data, "whoWin": whoWin, "engine": engine};
                    xhr.send(JSON.stringify(json_data));
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
