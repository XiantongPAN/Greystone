const init_x = 50;
const init_y = 50;
const grid_size = 50;
const board_size = 15;

function contains(arr, obj) {
    let i = arr.length;
    while (i--) {
        if (arr[i] === obj) {
            return true;
        }
    }
    return false;
}

function getArr(s) {
    let arr = new Array(s.length-1);
    for (let i = 1; i < s.length; i++) {
        arr[i-1] = parseInt(s[i], 16);
    }
    return arr;
}



function drawBoard(p, arr1) {
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

}