<%@ page language="java" session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" import="java.util.List, com.panxiantong.Message"%>
<!DOCTYPE html>

<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no"/>
<meta name="description" content="Server Sent Events Java Chat Example with code"/>
<title>Java SSE Chat Example</title>


<script type="text/javascript">


// Check that browser supports EventSource 
if (!!window.EventSource) {
	// Subscribe to url to listen
	var source = new EventSource('/chat');

	// Define what to do when server sent new event
	source.addEventListener("message", function(e) {
		var el = document.getElementById("chat"); 
		el.innerHTML += e.data + "<br/>";
		el.scrollTop += 50;
	}, false);
} else {
	alert("Your browser does not support EventSource!");
}


// Scroll chat to the bottom if not done and set focus to the msg on load
window.onload = function() {
	document.getElementById("chat").scrollTop += 50 * 100;
	document.getElementById("msg").focus();
};


// Send message to the server using AJAX call
function sendMsg(form) {

	if (form.msg.value.trim() == "") {
		alert("Empty message!");
	}
	
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
	var parameters = "msg=" + encodeURIComponent(form.msg.value.trim());

	http.onreadystatechange = function () {
		if (http.readyState == 4 && http.status == 200) {
			if (typeof http.responseText != "undefined") {
				var result = http.responseText;
				form.msg.value = "";
			}
		}
	};

	http.open("POST", form.action, true);
	http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	http.send(parameters);

	return false;
}

</script>

<style media="screen" type="text/css">

#container {
	max-width: 500px;
	margin: 10px auto;
}

#chat {
	width: 100%;
	height: 200px;
	border: 1px solid silver;
	overflow-y: scroll;
}

#msg {
	width: 99%;
	margin-top: 10px;
}

h1 {
	text-align: center;
	font-size: 150%;
}

#desc {
	max-width: 1020px;
	margin: 10px auto;
}

#desc p {
	text-align: justify;
	text-indent: 20px;
}

.center {
	text-align: center;
}

#download {
	margin: 50px auto;
	text-align: center;
}

#download A {
	display: inline-block;
	margin: 5px 10px;
	padding: 10px 25px;
	background: #F1592A;
	color: white;
	text-decoration: none;
	font-size: 20px;
	border-radius: 5px 5px;
}

#download A:hover {
	text-decoration: underline;
	background: #FF592A;
}

</style>

</head>
<body>

<h1>Java Server Sent Events Chat Example</h1>

<div id="container">

<div id="chat">
<%
	// Quick and dirty way to print existing messages - better use JSTL
	@SuppressWarnings("unchecked")
	List<Message> messages = (List<Message>)request.getAttribute("messages");
	for(Message msg : messages) { %>
		<%= msg.getMessage() %><br/>
<%	}
%>
</div>

<form id="msgForm" action="/chat" method="post" onsubmit="return sendMsg(this);">
	<input type="text" name="msg" id="msg" placeholder="Enter message here"/>
</form>

<p>Enter message in the field above and press Enter to send it.</p>

</div>

<jsp:include page="include.html" />

</body>
</html>
