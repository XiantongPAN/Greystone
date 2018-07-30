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

        //let data = "77,86,87,97,a8,88,a6,a5,75,76,66,57,79,78,58,a9,95,84,85,b7,c7";
        //let s = data.split(",");
        //let arr0 = getArr(data, 0);


        //let information = "";


        p.setup = function () {
            p.size(900, 900);
        };

        p.draw = function () {
            drawBoard(p, data);
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

                p.text(children[i], grid_size * pos.x + init_x, grid_size * pos.y + init_y);
            }


        };


        p.mousePressed = function () {
            if (p.mouseButton === p.LEFT) {
                let x = parseInt((p.mouseX - init_x + grid_size / 2) / grid_size);
                let y = parseInt((p.mouseY - init_y + grid_size / 2) / grid_size);


                if (!contains(data, {"x": x, "y": y}) && x < board_size && x >= 0 && y < board_size && y >= 0) {

                    data.push({"x": x, "y": y});

                    let xhr = new XMLHttpRequest();
                    xhr.onreadystatechange = function () {
                        if (xhr.readyState === 4 && xhr.status === 200) {
                            let responseJson = JSON.parse(xhr.responseText);
                            data = responseJson.data;
                            children = responseJson.children;
                            comment = responseJson.comment;

                            p.redraw();
                        }
                    };
                    xhr.open("POST", "LibServlet", true);
                    xhr.setRequestHeader("Content-type", "application/json");
                    let json_data = {"data": data};
                    xhr.send(JSON.stringify(json_data));
                }
            } else {
                data.pop();
                p.redraw();
            }


        };
    };
    const canvas = document.getElementById("canvas1");
    // attaching the sketch to the canvas
    new Processing(canvas, sketch);

</script>


</body>
</html>
