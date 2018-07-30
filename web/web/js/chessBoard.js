const init_x = 50;
const init_y = 50;
const grid_size = 50;
const board_size = 15;

// function getArr(data, t) {
//     let s = data.split(",");
//     let arr = new Array(s.length - t);
//     for (let i = t; i < s.length; i++) {
//         arr[i - t] = parseInt(s[i], 16);
//     }
//     return arr;
// }

function draw0(p, arr) {
    p.fill(255);
    p.rect(0, 0, p.height, p.width);

    p.fill(0);
    p.line(grid_size * 7 + init_x, init_y, grid_size * 7 + init_x, grid_size * (board_size - 1) + init_y);
    p.line(init_x, grid_size * 7 + init_y, grid_size * (board_size - 1) + init_x, grid_size * 7 + init_y);


    // Draw chess
    for (let i = 0; i < arr.length; i++) {

        //let x = parseInt(arr[i] / 16) * grid_size + init_x;
        //let y = (arr[i] % 16) * grid_size + init_y;
        let x = arr[i].x * grid_size + init_x;
        let y = arr[i].y * grid_size + init_y;

        p.fill(0);
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



function drawBoard(p, arr) {
    p.fill(120, 120, 120);
    p.rect(0, 0, p.height, p.width);

    p.fill(0);
    // Draw chess board
    for (let i = 0; i < board_size; i++) {
        p.line(grid_size * i + init_x, init_y, grid_size * i + init_x, grid_size * (board_size - 1) + init_y);
        p.line(init_x, grid_size * i + init_y, grid_size * (board_size - 1) + init_x, grid_size * i + init_y);
    }

    //Draw line number
    for (let i = 0; i < board_size; i++) {
        p.textSize(20);
        p.textAlign(p.CENTER, p.CENTER);
        p.text((i + 1) + "", i * grid_size + init_x, grid_size * board_size + init_y * 0.5);

        p.text(String.fromCharCode(79 - i), board_size * grid_size + init_x * 0.5, grid_size * i + init_y);
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