<%--
  Created by IntelliJ IDEA.
  User: xiant
  Date: 2018/3/15
  Time: 21:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <script>
        function SitDownAt(n){
            location.href = "websocket_game.jsp?sit="+n;
        }


    </script>
    <title>Lobby</title>
</head>
<body>

<button onclick="SitDownAt(1)">sit 1</button>&nbsp
<button onclick="SitDownAt(2)">sit 2</button><br><br>

<button onclick="SitDownAt(3)">sit 3</button>&nbsp
<button onclick="SitDownAt(4)">sit 4</button><br><br>

<button onclick="SitDownAt(5)">sit 5</button>&nbsp
<button onclick="SitDownAt(6)">sit 6</button><br><br>

<button onclick="SitDownAt(7)">sit 7</button>&nbsp
<button onclick="SitDownAt(8)">sit 8</button><br><br>

</body>
</html>
