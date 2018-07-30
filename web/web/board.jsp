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
    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="node_modules/dat.gui/build/dat.gui.js"></script>
    <script src="js/tool.js"></script>

    <title>GreyStone</title>
</head>
<body>


<canvas id="canvas1" width="900" height="900"></canvas>
<p id="data"></p>
<button id ="undo">123</button>

<script id="script1">
    // Attaching js code to the canvas by using a sketch object
    const sketch = new Processing.Sketch();

    sketch.attachFunction = function (p) {


        //gui
        var FizzyText = function() {
            this.message = 'dat.gui';
            this.speed = 0.8;
            this.displayOutline = false;
            this.undo = function () {
                data.pop();
                p.redraw();
            }
        };

        var text = new FizzyText();
        var gui = new dat.GUI();
        gui.add(text, 'message');
        gui.add(text, 'speed', -5, 5);
        gui.add(text, 'displayOutline');
        gui.add(text, 'undo');





        //let data = "77,86,87,97,a8,88,a6,a5,75,76,66,57,79,78,58,a9,95,84,85,b7,c7";

        let data = [{"x": 7, "y": 7}, {"x": 8, "y": 6}];

        // button to undo
        $('#undo').click(function(){
            data.pop();
        });
        //let information = "";


        p.setup = function () {
            p.size(900, 900);
        };

        p.draw = function () {
            drawBoard(p, data);

        };

        p.mousePressed = function () {
            if (p.mouseButton === p.LEFT) {
                let x = parseInt((p.mouseX - init_x + grid_size / 2) / grid_size);
                let y = parseInt((p.mouseY - init_y + grid_size / 2) / grid_size);

                if (!contains(data, {"x": x, "y": y}) && x < board_size && x >= 0 && y < board_size && y >= 0) {
                    data.push({"x": x, "y": y});
                }
            } else if (p.mouseButton === p.RIGHT && data.length > 0) {
                //If you call pop() on an empty array,
                // it returns undefined. and pop nothing
                // So data.length > 0 above is unnecessary.
                data.pop();
            }

            // JSON is not Json
            document.getElementById("data").innerHTML = JSON.stringify(data);
            p.redraw();

        }
    };
    const canvas = document.getElementById("canvas1");
    // attaching the sketch to the canvas
    new Processing(canvas, sketch);

</script>


</body>
</html>
