<%--
  Created by IntelliJ IDEA.
  User: mac
  Date: 2018/7/22
  Time: 上午10:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>

    <script src="js/processing.js"></script>
    <script src="js/chessBoard.js"></script>
    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/tool.js"></script>
    <script src="node_modules/dat.gui/build/dat.gui.js"></script>
    <title>read lib</title>
</head>
<body>

<label id="test"></label>

<canvas id="canvas1" width="900" height="900"></canvas>
<p id="data">data</p>


<script id="script1">
    // Attaching js code to the canvas by using a sketch object
    const sketch = new Processing.Sketch();

    sketch.attachFunction = function (p) {
        let data = [{"x": 7, "y": 7}];
        let children = {};
        let comment = "";
        //let lib = "a";
        //let data = "77,86,87,97,a8,88,a6,a5,75,76,66,57,79,78,58,a9,95,84,85,b7,c7";
        //let s = data.split(",");
        //let arr0 = getArr(data, 0);

        function getLib() {
            let xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status === 200) {

                    let responseJson = JSON.parse(xhr.responseText);

                    if (responseJson.validLib) {
                        data = responseJson.data;
                        children = responseJson.children;
                        comment = responseJson.comment;
                        p.redraw();
                    } else {
                        alert("invalid lib");
                    }


                }
            };
            xhr.open("POST", "LibServlet", true);
            xhr.setRequestHeader("Content-type", "application/json");
            let json_data = {"data": data, "lib": control.chooseLib};
            xhr.send(JSON.stringify(json_data));
        }

        //gui
        var datGui = function () {
            this.backgroundColor = [120, 120, 120]; // RGB array
            this.chooseLib = 'sk';
            this.showLineNumber = 1;
            this.displayBoard = false;
            this.showSteps = true;

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
        f2.add(control, 'chooseLib').listen();
        f2.add(control, 'undo');
        f2.open();

        p.setup = function () {
            p.size(900, 900);

            //initialize, draw lib info
            getLib();
        };

        p.draw = function () {
            if (control.displayBoard) {
                drawBoardControl(p, data, control.backgroundColor,
                    control.showSteps, control.showLineNumber);
            } else {
                draw0(p, data);
            }

            //draw comment
            p.fill(0);

            // p.CERTER is not CENTER
            p.textAlign(p.CENTER, p.CENTER);
            // for output Chinese
            //p.textFont(p.createFont("simsun.ttc", 40));
            if (comment != null) {
                p.text(comment, 400, 800);
            }

            //draw children
            for (let i in children) {
                let pos = $.parseJSON(i);
                if (children[i] === "x") {
                    if(data.length%2===0){
                        p.fill(0);
                    }else{
                        p.fill(255);
                    }

                    p.ellipse(grid_size * pos.x + init_x,
                        grid_size * pos.y + init_y, grid_size / 4, grid_size / 4);
                } else {
                    p.text(children[i], grid_size * pos.x + init_x, grid_size * pos.y + init_y);
                }

            }


        };


        p.mousePressed = function () {
            if (p.mouseButton === p.LEFT) {
                let x = parseInt((p.mouseX - init_x + grid_size / 2) / grid_size);
                let y = parseInt((p.mouseY - init_y + grid_size / 2) / grid_size);


                if (!contains(data, {"x": x, "y": y}) && x < board_size && x >= 0 && y < board_size && y >= 0) {

                    data.push({"x": x, "y": y});

                    getLib();
                }
            } else {
                data.pop();
                getLib();
            }


        };
    };
    const canvas = document.getElementById("canvas1");
    // attaching the sketch to the canvas
    new Processing(canvas, sketch);

</script>


</body>
</html>
