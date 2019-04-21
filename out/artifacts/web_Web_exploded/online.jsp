<%@ page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"
         trimDirectiveWhitespaces="true" %>
<%--the page is not involved with session,
set it false to improve performance and memory--%>

<!DOCTYPE html>

<html lang="en">
<head>

    <script src="js/processing.js"></script>
    <script src="js/chessBoard.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Java SSE online game</title>

</head>
<body>


<canvas id="canvas1" width="900" height="900"></canvas>
<script id="script1">


    function sendMsg(msg) {
        // Init http object
        let http = false;
        if (typeof ActiveXObject !== "undefined") {
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
        let parameters = "msg=" + encodeURIComponent(msg);

        http.onreadystatechange = function () {
            if (http.readyState === 4 && http.status === 200) {
                if (typeof http.responseText !== "undefined") {
                    //var result = http.responseText;
                    // result should be null

                    //TODO: do something after ajax uploading

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

        <%
        //String msg = (String)request.getAttribute("msg");
        //System.out.println(msg.get(0).getMessage());

        %>
        let data = null;
        data = "77,76";
        if (data === null || data === "") {
            data = "77,86,87,97,a8,88,a6,a5,75,76,66,57,79,78,58,a9,95,84,85,b7,c7";
        }
        //data = "77,86,87,97,a8,88,a6,a5,75,76,66,57,79,78,58,a9,95,84,85,b7,c7";

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
                    //arr1.push(16 * x + y);


                }
            } else if (p.mouseButton === p.RIGHT && arr0.length > 0) {
                data = data.substring(0, data.length - 3);
                //arr0.pop();
            }
            sendMsg(data);
            document.getElementById("data").innerHTML = data;
            //p.redraw();

            //go to jsp
            // Init http object


        };

        // Check that browser supports EventSource
        if (!!window.EventSource) {
            // Subscribe to url to listen, go to doGet() in OnlineServlet
            var source = new EventSource('/OnlineServlet?room=<%=request.getParameter("room")%>');
            // this is a stream. it can change

            source.onmessage = function (event) {

                data = event.data;
                document.getElementById("data").innerHTML = data;

                arr0 = getArr(data, 0);
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
