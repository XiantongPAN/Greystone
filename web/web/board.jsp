<%--
  Created by IntelliJ IDEA.
  User: xiant
  Date: 2018/2/26
  Time: 21:09
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


<canvas id="canvas1" width="900" height="900"></canvas>
<p id="data">data</p>


<script id="script1">
    // Attaching js code to the canvas by using a sketch object
    const sketch = new Processing.Sketch();

    sketch.attachFunction = function (p) {


        let data = "77,86,87,97,a8,88,a6,a5,75,76,66,57,79,78,58,a9,95,84,85,b7,c7";
        //let s = data.split(",");
        let arr0 = getArr(data, 0);


        //let information = "";


        p.setup = function () {
            p.size(900, 900);
        };

        p.draw = function () {
            drawBoard(p, arr0);

        };


        p.mousePressed = function () {
            if (p.mouseButton === p.LEFT) {
                let x = parseInt((p.mouseX - init_x + grid_size / 2) / grid_size);
                let y = parseInt((p.mouseY - init_y + grid_size / 2) / grid_size);

                if (!contains(arr0, 16 * x + y) && x < board_size && x >= 0 && y < board_size && y >= 0) {
                    data = data + "," + x.toString(16) + y.toString(16);
                    arr0.push(16 * x + y);
                }
            } else if (p.mouseButton === p.RIGHT && arr0.length > 0) {
                data = data.substring(0, data.length - 3);
                arr0.pop();
            }
            document.getElementById("data").innerHTML = data;
            p.redraw();

            //
        }
    };
    const canvas = document.getElementById("canvas1");
    // attaching the sketch to the canvas
    new Processing(canvas, sketch);

</script>


</body>
</html>
