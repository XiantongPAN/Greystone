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
    <script src="js/jquery-3.2.1.min.js"></script>
    <title>GreyStone</title>
</head>

<body>


<%
    String msg = request.getParameter("msg");
    if (msg == null || msg == "") {
        msg = "00";
    }
%>

<canvas id="canvas1" width="900" height="900"></canvas>

<script id="script1">
    //Attaching js code to the canvas by using a sketch object
    var sketch = new Processing.Sketch();

    sketch.attachFunction = function (p) {

        var init_x = 50;
        var init_y = 50;
        var grid_size = 50;
        var board_size = 15;

        var data = "<%=msg%>";
        var s = data.split(",");

        var whoWin = s[0].split("")[0];
        var engine = s[0].split("")[1];

        var arr1 = new Array(s.length - 1);


        var information = "";

        for (var i = 1; i < s.length; i++) {
            arr1[i - 1] = parseInt(s[i], 16);
        }

        p.setup = function () {
            p.size(900, 900);
        };

        p.draw = function () {
            p.fill(120, 120, 120);
            p.rect(0, 0, p.height, p.width);


            p.fill(0);
            // Draw chess board
            for (let i = 0; i < board_size; i++) {
                p.line(grid_size * i + init_x, init_y, grid_size * i + init_x, grid_size * (board_size - 1) + init_y);
                p.line(init_x, grid_size * i + init_y, grid_size * (board_size - 1) + init_x, grid_size * i + init_y);
            }


            //Draw line number
            for (var i = 0; i < board_size; i++) {
                p.textSize(20);
                p.textAlign(p.CENTER, p.CENTER);
                p.text((i + 1) + "", i * grid_size + init_x, grid_size * board_size + init_y * 0.5);

                p.text(String.fromCharCode(79 - i), board_size * grid_size + init_x * 0.5, grid_size * i + init_y);
            }

            // Draw point
            var arr = [[7, 7], [3, 3], [3, 11], [11, 3], [11, 11]];
            for (var i = 0; i < arr.length; i++) {
                p.ellipse(grid_size * arr[i][0] + init_x, grid_size * arr[i][1] + init_y, grid_size / 4, grid_size / 4);

            }


            // Draw chess
            for (var i = 0; i < arr1.length; i++) {
                if (i % 2 == 0) {
                    p.fill(0);
                } else {
                    p.fill(255);
                }
                var x = parseInt(arr1[i] / 16) * grid_size + init_x;
                var y = (arr1[i] % 16) * grid_size + init_y;

                p.ellipse(x, y, 40, 40);

                if (i % 2 == 1) {
                    p.fill(0);
                } else {
                    p.fill(255);
                }
                if (i < 9) {
                    p.textSize(30);
                } else if (i < 99) {
                    p.textSize(24);
                } else {
                    p.textSize(18);
                }
                p.textAlign(p.CENTER, p.CENTER);
                p.text(i + 1 + "", x, y);
            }

            //draw win information
            p.fill(200);
            p.rect(50, 800, 720, 900);

            p.fill(0);
            if (whoWin == "2") {
                p.fill(255);
                information = "white win, press to restart";
            } else if (whoWin == "1") {
                p.fill(0);
                information = "black win, press to restart";
            }
            p.textSize(50);
            p.textAlign(p.CENTER, p.CENTER);
            p.text(information, 400, 850);

        };

        function contains(arr, obj) {
            var i = arr.length;
            while (i--) {
                if (arr[i] === obj) {
                    return true;
                }
            }
            return false;
        }

        p.mousePressed = function () {
            if (whoWin != "0") { // restart
                if (p.mouseX > 50 && p.mouseX < 720 && p.mouseY > 800 && p.mouseY < 900) {
                    location.href = "index.html";
                }
            } else {
                var x = parseInt((p.mouseX - init_x + grid_size / 2) / grid_size);
                var y = parseInt((p.mouseY - init_y + grid_size / 2) / grid_size);

                var xy = x.toString(16) + y.toString(16);
                if (!contains(s, xy) && x < board_size && x >= 0 && y < board_size && y >= 0) {

                    arr1.push(16 * x + y);
                    information = "computing...";

                    p.redraw();
                    location.href = "CalcServlet?msg=" + data + "," + xy;

                }
            }
        }
    };

    var canvas = document.getElementById("canvas1");
    //attaching the sketch to the canvas
    new Processing(canvas, sketch);

</script>
</body>
</html>
