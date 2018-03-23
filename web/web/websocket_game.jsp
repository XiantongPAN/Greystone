<%--
  Created by IntelliJ IDEA.
  User: xiant
  Date: 2018/3/14
  Time: 19:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

<head>
    <script src="js/processing.js"></script>
    <script src="js/chessBoard.js"></script>
    <title>Online game</title>
</head>

<%
    int sit = Integer.parseInt(request.getParameter("sit"));
%>

<body>
Sit <%=sit%><br/>
<p align="center">
    <canvas id="canvas1" width="900" height="900"></canvas>
</p>

<script id="script1">
    //Attaching js code to the canvas by using a sketch object
    const sketch = new Processing.Sketch();

    sketch.attachFunction = function (p) {


        let data = "<%=sit%>";

        //let s = data.split(",");
        let arr1 = getArr(data, 1);

        let whoWin = data.charAt(0);
        //let engine = data.charAt(1);
        let information = "";

        let websocket = null;
        //判断当前浏览器是否支持WebSocket
        if ('WebSocket' in window) {

            websocket = new WebSocket("ws://localhost:8080/WSServer");
            //websocket = new WebSocket("ws://panxiantong.com/WSServer");
            // do not do anything here

        }
        else {
            alert('Your browser does not support websocket')
        }

        //连接发生错误的回调方法
        websocket.onerror = function () {
            alert("WebSocket connection error");
        };

        //Connection success
        websocket.onopen = function () {
            alert("Connection success");

            // send sit number immediately after connection success
            websocket.send("<%=sit%>");

        };

        //receiving message
        websocket.onmessage = function (event) {
            data = "<%=sit%>" + event.data;
            arr1 = getArr(data, 1);
            p.redraw();
        };

        //连接关闭的回调方法
        websocket.onclose = function () {
            alert("WebSocket connection closed");
        };

        //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
        window.onbeforeunload = function () {
            websocket.close();
        };


        p.setup = function () {
            p.size(900, 900);
        };

        p.draw = function () {

            drawBoard(p, arr1);

            //draw win information
            p.fill(200);
            p.rect(50, 800, 720, 900);

            p.fill(0);
            if (whoWin === "2") {
                p.fill(255);
                information = "white win, press to restart";
            } else if (whoWin === "1") {
                p.fill(0);
                information = "black win, press to restart";
            }
            p.textSize(50);
            p.textAlign(p.CENTER, p.CENTER);
            p.text(information, 400, 850);

        };


        p.mousePressed = function () {
            if (false) { // restart
                if (p.mouseX > 50 && p.mouseX < 720 && p.mouseY > 800 && p.mouseY < 900) {
                    location.href = "index.jsp";
                }
            } else {
                let x = parseInt((p.mouseX - init_x + grid_size / 2) / grid_size);
                let y = parseInt((p.mouseY - init_y + grid_size / 2) / grid_size);


                if (!contains(arr1, 16 * x + y) && x < board_size && x >= 0 && y < board_size && y >= 0) {
                    let xy = x.toString(16) + y.toString(16);
                    //alert(data);
                    websocket.send(data + "," + xy);
                    //arr1.push(16 * x + y);
                    //information = "computing...";

                    //p.redraw();

//                    data = xmlhttp.responseText;
//                    arr1 = getArr(data, 1);
//                    whoWin = data.charAt(0);
//                    information = "It's your turn";
//                    p.redraw();


                    //xmlhttp.send("msg=" + data + "," + xy);
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
