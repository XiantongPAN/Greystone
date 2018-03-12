<%@ page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"
         trimDirectiveWhitespaces="true" import="java.util.List, com.panxiantong.Message" %>
<%--the page is not involved with session,
set it false to improve performance and memory--%>

<!DOCTYPE html>

<html lang="en">
<head>

    <script src="js/processing.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no"/>
    <meta name="description" content="Server Sent Events Java Chat Example with code"/>
    <title>Java SSE Chat Example</title>

</head>
<body>


<canvas id="canvas1" width="900" height="900"></canvas>
<script id="script1">


    function sendMsg(msg){
        // Init http object
        var http = false;
        if (typeof ActiveXObject != "undefined") {
            try {
                http = new ActiveXObject("Msxml2.XMLHTTP");
            } catch (ex) {
                try {
                    http = new ActiveXObject("Microsoft.XMLHTTP");
                } catch (ex2) {
                    http = false;
                }
            }
        } else if (window.XMLHttpRequest) {
            try {
                http = new XMLHttpRequest();
            } catch (ex) {
                http = false;
            }
        }

        if (!http) {
            alert("Unable to connect!");
            return;
        }

        // Prepare data
        var parameters = "msg=" + encodeURIComponent(msg);

        http.onreadystatechange = function () {
            if (http.readyState == 4 && http.status == 200) {
                if (typeof http.responseText != "undefined") {
                    //var result = http.responseText;
                    //form.msg.value = "";
                }
            }
        };

        http.open("POST", "/OnlineServlet", true);
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http.send(parameters);

    }


    // Attaching js code to the canvas by using a sketch object
    const sketch = new Processing.Sketch();

    sketch.attachFunction = function (p) {

        const init_x = 50;
        const init_y = 50;
        const grid_size = 50;
        const board_size = 15;
        <%
        //String msg = (String)request.getAttribute("msg");
        //System.out.println(msg.get(0).getMessage());

        %>
        let data = null;
        data = "77,86";
        if (data === null || data === "") {
            data = "77,86,87,97,a8,88,a6,a5,75,76,66,57,79,78,58,a9,95,84,85,b7,c7";
        }
        //data = "77,86,87,97,a8,88,a6,a5,75,76,66,57,79,78,58,a9,95,84,85,b7,c7";

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
                    //arr1.push(16 * x + y);



                }
            } else if (p.mouseButton === p.RIGHT && arr1.length > 0) {
                data = data.substring(0, data.length - 3);
                //arr1.pop();
            }
            sendMsg(data);
            document.getElementById("data").innerHTML = data;
            //p.redraw();

            //go to jsp
            // Init http object


        };

        // Check that browser supports EventSource
        if (!!window.EventSource) {
            // Subscribe to url to listen
            var source = new EventSource('/OnlineServlet');

            source.onmessage = function (e) {
                //alert(e.data);
                data = e.data;
                document.getElementById("data").innerHTML = data;
                s = data.split(",");
                arr1 = new Array(s.length);
                for (let i = 0; i < s.length; i++) {
                    arr1[i] = parseInt(s[i], 16);

                }
                p.redraw();
            }


            // Define what to do when server sent new event
//            source.addEventListener("message", function (e) {
//                //TODO:
//                data=e.data;
//                document.getElementById("data").innerHTML = data;
//                s = data.split(",");
//                let arr1 = new Array(s.length);
//                for (let i = 0; i < s.length; i++) {
//                    arr1[i] = parseInt(s[i], 16);
//
//                }
//                p.redraw();
//                //var el = document.getElementById("data");
//                //el.innerHTML = e.data;
//                //el.scrollTop += 50;
//            }, false);





        } else {
            alert("Your browser does not support EventSource!");
        }
    };
    const canvas = document.getElementById("canvas1");
    // attaching the sketch to the canvas
    new Processing(canvas, sketch);

</script>

<p id="data">data</p>



</body>
</html>
