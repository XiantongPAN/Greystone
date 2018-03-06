/**
 * 
 */

// Attaching js code to the canvas by using a sketch object
var sketch = new Processing.Sketch();

sketch.attachFunction = function(processing) {
	
	var init_x = 50;
	var init_y = 50;
	var grid_size = 50;
	var board_size = 15;
	
	var data = "<%=msg%>";
	var s = data.split(",");
	
	var whoWin = s[0].split("")[0];
	var engine = s[0].split("")[1];
	
	var arr1 = new Array(s.length-1);
	
	
	var information = "";
	
	for(var i = 1; i < s.length; i++){
		arr1[i-1] = parseInt(s[i],16);
		
	}
	
	processing.setup = function() {
		processing.size(900, 900);
	};
	
	processing.draw = function() {
	 	processing.fill(120, 120, 120);
	 	processing.rect(0, 0, processing.height, processing.width);
	 	
	 	
	 	processing.fill(0)
	 	// Draw chess board
		for (var i = 0; i < board_size; i++) {
			processing.line(grid_size * i + init_x, init_y, grid_size * i + init_x, grid_size * (board_size - 1) + init_y);
			processing.line(init_x, grid_size * i + init_y, grid_size * (board_size - 1) + init_x, grid_size * i + init_y);
		}
	 	
	 	
	 	
	 	// Draw line number
	 	for (var i = 0; i < board_size; i++) {
	 		processing.textSize(20);
	 		processing.textAlign(processing.CENTER, processing.CENTER);
	 		processing.text((i+1)+"",i*grid_size+init_x,grid_size* board_size+init_y*0.5);
	 		
	 		processing.text(String.fromCharCode(79-i),board_size*grid_size+init_x*0.5,grid_size* i+init_y);
	 	}
	 	
	 	// Draw point
		var arr = [[7,7],[3,3],[3,11],[11,3],[11,11]];
		for (var i=0; i< arr.length; i++) {
			processing.ellipse(grid_size * arr[i][0] + init_x, grid_size * arr[i][1] + init_y, grid_size / 4, grid_size / 4);
			
		}
		
		
		
		
		// Draw chess
		for (var i = 0; i < arr1.length; i++) {
			if (i % 2 == 0) {
				processing.fill(0);
			} else {
				processing.fill(255);
			}
			var x = parseInt(arr1[i]/16) * grid_size + init_x;
			var y = (arr1[i]%16) * grid_size + init_y;

			processing.ellipse(x, y, 40, 40);

			if (i % 2 == 1) {
				processing.fill(0);
			} else {
				processing.fill(255);
			}
			if (i < 9) {
				processing.textSize(30);
			} else if (i < 99) {
				processing.textSize(24);
			} else {
				processing.textSize(18);
			}
			processing.textAlign(processing.CENTER, processing.CENTER);
			processing.text(i + 1 + "", x, y);
		}
		
		// draw win information
		processing.fill(200);
		processing.rect(50,800,720,900);
		
		processing.fill(0);
		if(whoWin=="2"){
			processing.fill(255);
			
			information = "white win, press to restart";
			
		}else if(whoWin=="1"){
			processing.fill(0);
			information = "black win, press to restart";
			
		}
		processing.textSize(50);
		processing.textAlign(processing.CENTER, processing.CENTER);
		processing.text(information, 400, 850);
		
		
	 	
	}
	
	function contains(arr, obj) {
		var i = arr.length;
		while (i--) {
			if (arr[i] === obj) {
				return true;
			}
		}
		return false;
	}

	processing.mousePressed = function() {
		if(whoWin != "0") { // restart
			if(processing.mouseX > 50 && processing.mouseX < 720 && processing.mouseY > 800 && processing.mouseY < 900){
				location.href="index.html";
			}
		} else {
			var x = parseInt((processing.mouseX - init_x + grid_size / 2) / grid_size);
			var y = parseInt((processing.mouseY - init_y + grid_size / 2) / grid_size);
			
			var xy = x.toString(16)+y.toString(16);
			
			if(!contains(s, xy) && x < board_size && x >= 0 && y < board_size && y >= 0){
				arr1.push(16 * x + y);
				information="computing...";
				
				processing.redraw();
				location.href = "NewGameServlet?msg=" + data + "," + xy;
				// $.post("NewGameServlet", { msg: data+","+xy } );
				
				
			}
			
		}
		
		
		
	}
	
	
}
var canvas = document.getElementById("canvas1");
// attaching the sketch to the canvas
var p = new Processing(canvas, sketch);