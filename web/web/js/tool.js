function contains(arr, obj) {

    let s = JSON.stringify(obj);
    for (let i = 0; i < arr.length; i++) {

        if (arr[i] === obj || JSON.stringify(arr[i]) === s) {

            return true;
        }
    }
    return false;
}


function drawBoardControl(p, arr, bg, showSteps, lineNumber) {
    p.fill(bg[0], bg[1], bg[2]);
    p.rect(0, 0, p.height - 1, p.width - 1);

    p.fill(0);
    // Draw chess board
    for (let i = 0; i < board_size; i++) {
        p.line(grid_size * i + init_x, init_y, grid_size * i + init_x, grid_size * (board_size - 1) + init_y);
        p.line(init_x, grid_size * i + init_y, grid_size * (board_size - 1) + init_x, grid_size * i + init_y);
    }

    //Draw line number
    if(lineNumber > 0){
        for (let i = 0; i < board_size; i++) {
            p.textSize(20);
            p.textAlign(p.CENTER, p.CENTER);
            if(lineNumber === 1){
                p.text((i + 1) + "", i * grid_size + init_x, grid_size * board_size + init_y * 0.5);
                p.text(String.fromCharCode(79 - i), board_size * grid_size + init_x * 0.5, grid_size * i + init_y);

            }else{
                p.text(i + "", i * grid_size + init_x, grid_size * board_size + init_y * 0.5);
                p.text(i + "", board_size * grid_size + init_x * 0.5, grid_size * i + init_y);

            }
        }

    }


    // Draw point
    const points = [[7, 7], [3, 3], [3, 11], [11, 3], [11, 11]];
    for (let i = 0; i < points.length; i++) {
        p.ellipse(grid_size * points[i][0] + init_x, grid_size * points[i][1] + init_y, grid_size / 4, grid_size / 4);

    }

    // Draw chess
    for (let i = 0; i < arr.length; i++) {
        if (i % 2 === 0) {
            p.fill(0);
        } else {
            p.fill(255);
        }
        //let x = parseInt(arr[i] / 16) * grid_size + init_x;
        //let y = (arr[i] % 16) * grid_size + init_y;
        let x = arr[i].x * grid_size + init_x;
        let y = arr[i].y * grid_size + init_y;

        p.ellipse(x, y, 40, 40);


        if(showSteps){
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

    }
}

function getMove(p, data, whoWin, engine) {
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            //when receive json data from servlet
            let responseJson = JSON.parse(xhr.responseText);
            data = responseJson.data;
            whoWin = responseJson.whoWin;
            engine = responseJson.engine;
            p.redraw();
        }
    };
    xhr.open("POST", "Calc2Servlet", true);
    xhr.setRequestHeader("Content-type", "application/json");
    let json_data = {"data": data, "whoWin": whoWin, "engine": engine};
    xhr.send(JSON.stringify(json_data));
}