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
    <%--<link rel="stylesheet" href="css/style1.less" type="text/less"/>--%>
    <script src="js/processing.js"></script>
    <script src="js/chessBoard.js"></script>
    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="node_modules/dat.gui/build/dat.gui.js"></script>
    <script src="js/tool.js"></script>
    <%--<script src="//cdnjs.cloudflare.com/ajax/libs/less.js/2.5.3/less.min.js"></script>--%>

    <title>GreyStone</title>
</head>
<body>

<%--<div class='wrapper'>--%>
    <canvas id="canvas1" width="900" height="900"></canvas>
    <p id="data"></p>
    <button id="undo">123</button>

<%--</div>--%>


<script id="script1">
    // Attaching js code to the canvas by using a sketch object
    const sketch = new Processing.Sketch();

    sketch.attachFunction = function (p) {
        let data = [];//[{"x": 7, "y": 7}, {"x": 8, "y": 6}];
        let whoWin = 0;
        let engine = 0;


        function getMove(engine) {
            let xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status === 200) {

                    //when receive json data from servlet
                    let responseJson = JSON.parse(xhr.responseText);
                    data = responseJson.data;
                    whoWin = responseJson.whoWin;
                    //engine = responseJson.engine;
                    control.message = "";
                    p.redraw();
                }
            };
            xhr.open("POST", "Calc2Servlet", true);
            xhr.setRequestHeader("Content-type", "application/json");
            let json_data = {"data": data, "whoWin": whoWin, "engine": engine};
            xhr.send(JSON.stringify(json_data));
            control.message = "computing...";
        }

        //gui
        var datGui = function () {
            this.backgroundColor = [120, 120, 120]; // RGB array
            this.message = '';
            this.showLineNumber = 1;
            this.displayBoard = false;
            this.showSteps = true;
            this.engine1 = function () {
                getMove(1);
            };

            this.engine2 = function () {
                getMove(2);
            };

            this.undo = function () {
                data.pop();
                getMove(0);
            }
        };

        var control = new datGui();
        var gui = new dat.GUI();

        var f1 = gui.addFolder('board control');
        f1.addColor(control, 'backgroundColor');
        f1.add(control, 'showLineNumber', {None: 0, Normal: 1, AI: 2});
        f1.add(control, 'displayBoard');
        f1.add(control, 'showSteps');
        f1.open();

        var f2 = gui.addFolder('chess control');
        f2.add(control, 'message').listen();
        f2.add(control, 'engine1');
        f2.add(control, 'engine2');
        f2.add(control, 'undo');
        f2.open();


        //let data = "77,86,87,97,a8,88,a6,a5,75,76,66,57,79,78,58,a9,95,84,85,b7,c7";


        // button to undo
        $('#undo').click(function () {
            data.pop();
        });
        //let information = "";


        p.setup = function () {
            p.size(900, 900);
        };

        p.draw = function () {
            if (control.displayBoard) {
                drawBoardControl(p, data, control.backgroundColor, control.showSteps, control.showLineNumber);
            } else {
                draw0(p, data);
            }

            if (control.message !== 'computing...') {
                if (whoWin === 1) {
                    control.message = "black win";
                } else if (whoWin === 2) {
                    control.message = "white win";
                } else if (data.length % 2 === 1) {
                    control.message = "white turn";
                } else {
                    control.message = "black turn";
                }
            }


        };

        p.mousePressed = function () {
            if (p.mouseButton === p.LEFT) {
                let x = parseInt((p.mouseX - init_x + grid_size / 2) / grid_size);
                let y = parseInt((p.mouseY - init_y + grid_size / 2) / grid_size);

                if (!contains(data, {"x": x, "y": y}) && x < board_size && x >= 0 && y < board_size && y >= 0) {
                    data.push({"x": x, "y": y});
                    getMove(0);
                }
            } else if (p.mouseButton === p.RIGHT && data.length > 0) {
                //If you call pop() on an empty array,
                // it returns undefined. and pop nothing
                // So data.length > 0 above is unnecessary.
                data.pop();
                getMove(0);
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
