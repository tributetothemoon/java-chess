const BLOCK_SIZE_PIXEL = 80;

const $chessBoard = document.getElementById('chess-board');
const $controlPanel = document.getElementById('control-panel');
const $startButton = document.getElementById('start-button');

const squareBuffer = new SquareBuffer();

addSelectionEventOnChessBoard();
addEventOnStartButton();

function processResponse(responseJsonBody, successScenarioFunction) {
    if (Math.floor(responseJsonBody.statusCode / 100) === 2) {
        successScenarioFunction();
    }
}

function SquareBuffer() {
    this.buffer = [];
    this.add = addAndRequestMove;
}

function addAndRequestMove(square) {
    this.buffer.push(square);
    if (this.buffer.length === 2) {
        const toSquare = this.buffer.pop();
        const fromSquare = this.buffer.pop();
        console.log(`request [POST]/move, body: from: ${fromSquare.id}, \nto: ${toSquare.id}\n`);

        try {
            fetch("/move", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    from: fromSquare.id,
                    to: toSquare.id,
                }),
            })
                .then(res => res.json())
                .then(res => processResponse(res, () => updateBoard(res.item.chessBoard)));
        } catch
            (error) {
            console.error(error.messages);
        }
    }
}

async function addSelectionEventOnChessBoard() {
    $chessBoard.addEventListener('click', event => {
        const selectedSquare = event.target.closest('div');
        squareBuffer.add(selectedSquare);
    })
}

async function addEventOnStartButton() {
    await $startButton.addEventListener('click', event => {
        try {
            fetch('/newgame')
                .then(res => res.json())
                .then(res => processResponse(res, () => updateBoard(res.item.chessBoard)));
            $startButton.style.display = 'none';
        } catch (error) {
            console.error(error.messages);
        }
    });
}

function updateBoard(piecesMap) {
    for (const square of $chessBoard.querySelectorAll('div')) {
        square.innerHTML = '';
    }
    for (const [position, piece] of Object.entries(piecesMap)) {
        updateSqaure(position, piece);
    }
}

async function updateSqaure(position, piece) {
    const square = document.getElementById(position);
    square.appendChild(makeImage(piece.color + '-' + piece.notation));
}

function makeImage(imageName) {
    const img = document.createElement('img');
    img.setAttribute('src', '/images/' + imageName + '.png');
    img.height = BLOCK_SIZE_PIXEL;
    return img;
}
