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
    <script src="js/jquery-3.2.1.min.js"></script>
    <title>GreyStone</title>
</head>
<body>



<canvas id="canvas1" width="900" height="900"></canvas>
<p id="data">data</p>


<script id="script1">
    // Attaching js code to the canvas by using a sketch object
    const sketch = new Processing.Sketch();

    sketch.attachFunction = function (p) {

        const init_x = 50;
        const init_y = 50;
        const grid_size = 50;
        const board_size = 15;

        let data = "77,76,86,87,98,68,a8,97,b8,88,a6,a7,b6,58";
        let s = data.split(",");
        let arr1 = new Array(s.length);


        //let information = "";

        for (let i = 0; i < s.length; i++) {
            arr1[i] = parseInt(s[i], 16);

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


            // Draw line number
            for (let i = 0; i < board_size; i++) {
                p.textSize(20);
                p.textAlign(p.CENTER, p.CENTER);
                p.text(i + "", i * grid_size + init_x, grid_size * board_size + init_y * 0.5);

                p.text(i, board_size * grid_size + init_x * 0.5, grid_size * i + init_y);
            }

            // Draw point
            const arr = [[7, 7], [3, 3], [3, 11], [11, 3], [11, 11]];
            for (let i = 0; i < arr.length; i++) {
                p.ellipse(grid_size * arr[i][0] + init_x, grid_size * arr[i][1] + init_y, grid_size / 4, grid_size / 4);

            }


            // Draw chess
            for (let i = 0; i < arr1.length; i++) {
                if (i % 2 === 0) {
                    p.fill(0);
                } else {
                    p.fill(255);
                }
                let x = parseInt(arr1[i] / 16) * grid_size + init_x;
                let y = (arr1[i] % 16) * grid_size + init_y;

                p.ellipse(x, y, 40, 40);

                if (i % 2 === 1) {
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


        };

        function contains(arr, obj) {
            let i = arr.length;
            while (i--) {
                if (arr[i] === obj) {
                    return true;
                }
            }
            return false;
        }

        p.mousePressed = function () {
            if (p.mouseButton === p.LEFT) {
                let x = parseInt((p.mouseX - init_x + grid_size / 2) / grid_size);
                let y = parseInt((p.mouseY - init_y + grid_size / 2) / grid_size);

                if (!contains(arr1, 16 * x + y) && x < board_size && x >= 0 && y < board_size && y >= 0) {
                    data = data + "," + x.toString(16) + y.toString(16);
                    arr1.push(16 * x + y);
                }
            } else if (p.mouseButton === p.RIGHT && arr1.length > 0) {
                data = data.substring(0, data.length - 3);
                arr1.pop();
            }
            document.getElementById("data").innerHTML = data;
            p.redraw();
        }
    };
    const canvas = document.getElementById("canvas1");
    // attaching the sketch to the canvas
    new Processing(canvas, sketch);

</script>


</body>
</html>
