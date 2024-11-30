package com.example.arimagame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ArimaaGame()
            }
        }
    }
}


@Composable
fun ArimaaGame() {
    var boardState by remember { mutableStateOf(initialBoardState()) }
    var currentPlayer by remember { mutableStateOf("Gold") }
    var selectedPiece by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var validMoves by remember { mutableStateOf(emptyList<Pair<Int, Int>>()) }
    var errorMessage by remember { mutableStateOf("") }
    var moveHistory by remember { mutableStateOf(mutableSetOf<List<List<String>>>()) }
    var turnMoves by remember { mutableStateOf(0) }
    var currentTurnHistory by remember { mutableStateOf(mutableListOf<MutableList<MutableList<String>>>()) }
    var winner by remember { mutableStateOf<String?>(null) } // Track the winner

    val trapSquares = setOf(Pair(2, 2), Pair(2, 5), Pair(5, 2), Pair(5, 5))

    if (winner != null) {
        // Show winner and disable further moves
        Column(modifier = Modifier.fillMaxSize().padding(8.dp).wrapContentHeight()) {
            Text(
                text = "$winner wins!",
                fontSize = 30.sp,
                color = Color.Green,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(8.dp)
            )

            Button(
                onClick = {
                    // Reset the game after a win
                    boardState = initialBoardState()
                    moveHistory.clear()
                    currentTurnHistory.clear()
                    turnMoves = 0
                    currentPlayer = "Gold"
                    errorMessage = ""
                    winner = null
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Restart Game")
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            // Display Current Player
            Text(
                text = "Current Player: $currentPlayer",
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(8.dp)
            )

            // Display Error Messages
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    fontSize = 16.sp,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(8.dp)
                )
            }

            // Render Game Board
            for (row in 0 until 8) {
                Row {
                    for (col in 0 until 8) {
                        val piece = boardState[row][col]
                        val isTrap = trapSquares.contains(Pair(row, col))
                        val isValidMove = validMoves.contains(Pair(row, col))

                        // Define Tile Color
                        val tileColor = when {
                            isTrap -> Color.Red
                            isValidMove -> Color.Green
                            (row + col) % 2 == 0 -> Color.Cyan
                            else -> Color.Black
                        }

                        SquareTile(
                            color = tileColor,
                            modifier = Modifier.weight(1f).aspectRatio(1f).clickable {
                                handleTileClick(
                                    row, col, selectedPiece, currentPlayer, boardState, turnMoves,
                                    moveHistory, currentTurnHistory, validMoves, trapSquares,
                                    onPieceSelected = { piecePos, moves ->
                                        selectedPiece = piecePos
                                        validMoves = moves
                                    },
                                    onPieceMoved = { updatedBoard, moves ->
                                        currentTurnHistory.add(boardState.map { it.toMutableList() }.toMutableList())
                                        boardState = updatedBoard
                                        selectedPiece = null
                                        validMoves = emptyList()
                                        turnMoves += moves
                                        errorMessage = ""
                                    },
                                    onError = { errorMessage = it },
                                    onGameWon = { winnerText -> winner = winnerText }
                                )
                            }
                        ) {
                            if (piece.isNotEmpty()) {
                                val iconRes = getIconForPiece(piece)
                                if (iconRes != null) {
                                    Image(
                                        painter = painterResource(id = iconRes),
                                        contentDescription = piece,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Restart Turn Button
//            Row(modifier = Modifier.align(Alignment.CenterHorizontally).padding(8.dp)) {
//                Button(onClick = {
//                    if (currentTurnHistory.isNotEmpty()) {
//                        boardState = currentTurnHistory.first()
//                        currentTurnHistory.clear()
//                        turnMoves = 0
//                        errorMessage = ""
//                    } else {
//                        errorMessage = "No moves made to restart!"
//                    }
//                }) {
//                    Text("Undo")
//                }
//
//                Spacer(modifier = Modifier.width(16.dp))
//
//                // End Turn Button
//                Button(onClick = {
//                    if (turnMoves > 0 && !moveHistory.contains(boardState)) {
//                        moveHistory.add(boardState.map { it.toMutableList() })
//                        currentTurnHistory.clear()
//                        turnMoves = 0
//                        currentPlayer = if (currentPlayer == "Gold") "Silver" else "Gold"
//                    } else {
//                        errorMessage = "Cannot end turn: no valid moves made or board state repeated!"
//                    }
//                }) {
//                    Text("Finish Turn")
//                }
//            }

            // Auto-switch after every 4 turns
//            if (turnMoves >= 4) {
//                currentPlayer = if (currentPlayer == "Gold") "Silver" else "Gold"
//                turnMoves = 0  // Reset turn count for the next player
//            }

            // Reset Game Button
//            Row(modifier = Modifier.align(Alignment.CenterHorizontally).padding(8.dp)) {
//                Button(onClick = {
//                    boardState = initialBoardState()
//                    moveHistory.clear()
//                    currentTurnHistory.clear()
//                    turnMoves = 0
//                    currentPlayer = "Gold"
//                    errorMessage = ""
//                    winner = null
//                }) {
//                    Text("Reset Game")
//                }
//            }
        }
    }
}


// Map pieces to drawable resources
fun getIconForPiece(piece: String): Int? {
    return when (piece) {
//        "GoldRabbit" -> R.drawable.rabbit_gold
//        "GoldDog" -> R.drawable.dog_gold
//        "GoldHorse" -> R.drawable.horse_gold
//        "GoldCamel" -> R.drawable.camel_gold
//        "GoldElephant" -> R.drawable.elephants_gold
//        "GoldCat" -> R.drawable.cat_gold
//        "SilverRabbit" -> R.drawable.ic_rabbit // Optional: Use a different icon for Silver pieces
//        "SilverDog" -> R.drawable.ic_dog
//        "SilverHorse" -> R.drawable.ic_horse
//        "SilverCamel" -> R.drawable.ic_camel
//        "SilverElephant" -> R.drawable.ic_elephants
//        "SilverCat" -> R.drawable.ic_cat
        else -> null
    }
}

// Helper functions
fun initialBoardState(): MutableList<MutableList<String>> {
    val board = MutableList(8) { MutableList(8) { "" } }
    board[7] = mutableListOf("GoldRabbit", "GoldRabbit", "GoldRabbit", "GoldRabbit", "GoldRabbit", "GoldRabbit", "GoldRabbit", "GoldRabbit")
    board[6] = mutableListOf("GoldCat", "GoldDog", "GoldHorse", "GoldCamel", "GoldElephant", "GoldHorse", "GoldDog", "GoldCat")
    board[0] = mutableListOf("SilverRabbit", "SilverRabbit", "SilverRabbit", "SilverRabbit", "SilverRabbit", "SilverRabbit", "SilverRabbit", "SilverRabbit")
    board[1] = mutableListOf("SilverCat", "SilverDog", "SilverHorse", "SilverCamel", "SilverElephant", "SilverHorse", "SilverDog", "SilverCat")
    return board
}

fun handleTileClick(
    row: Int,
    col: Int,
    selectedPiece: Pair<Int, Int>?,
    currentPlayer: String,
    boardState: MutableList<MutableList<String>>,
    turnMoves: Int,
    moveHistory: MutableSet<List<List<String>>>,
    currentTurnHistory: List<MutableList<MutableList<String>>>,
    validMoves: List<Pair<Int, Int>>,
    trapSquares: Set<Pair<Int, Int>>,
    onPieceSelected: (Pair<Int, Int>, List<Pair<Int, Int>>) -> Unit,
    onPieceMoved: (MutableList<MutableList<String>>, Int) -> Unit,
    onError: (String) -> Unit,
    onGameWon: (String) -> Unit // Callback to handle game win
) {
    if (selectedPiece == null) {
        if (boardState[row][col].startsWith(currentPlayer)) {
            val validMoves = calculateValidMoves(row, col, boardState, currentPlayer)
            onPieceSelected(Pair(row, col), validMoves)
        } else {
            onError("Select a valid piece to move.")
        }
    } else {
        val (sr, sc) = selectedPiece
        if (validMoves.contains(Pair(row, col))) {
            val updatedBoard = boardState.map { it.toMutableList() }.toMutableList()
            updatedBoard[row][col] = updatedBoard[sr][sc]
            updatedBoard[sr][sc] = ""

            // Check for winner after the move
            val winner = checkForWinner(updatedBoard, currentPlayer)
            if (winner != null) {
                onGameWon(winner)
            } else {
                onPieceMoved(updatedBoard, 1)
            }
        } else {
            onError("Invalid move!")
        }
    }
}


fun calculateValidMoves(
    row: Int, col: Int,
    boardState: MutableList<MutableList<String>>,
    currentPlayer: String
): List<Pair<Int, Int>> {
    val directions = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))
    val piece = boardState[row][col]
    return directions.map { Pair(row + it.first, col + it.second) }
        .filter { (r, c) ->
            r in 0..7 && c in 0..7 &&
                    (boardState[r][c].isEmpty() || boardState[r][c].startsWith(currentPlayer))
        }
        .filter { !(piece.contains("Rabbit") && it.first < row) } // Rabbits can't move backward
}


fun isValidMove(sr: Int, sc: Int, dr: Int, dc: Int, boardState: MutableList<MutableList<String>>): Boolean {
    if (dr !in 0..7 || dc !in 0..7) return false
    if (boardState[dr][dc].isNotEmpty()) return false
    if (Math.abs(sr - dr) + Math.abs(sc - dc) != 1) return false
    return true
}

fun checkTrapLogic(boardState: MutableList<MutableList<String>>, traps: Set<Pair<Int, Int>>) {
    traps.forEach { (r, c) ->
        val piece = boardState[r][c]
        if (piece.isNotEmpty()) {
            val neighbors = listOf(
                Pair(r - 1, c), Pair(r + 1, c),
                Pair(r, c - 1), Pair(r, c + 1)
            )
            val hasFriendlyNeighbor = neighbors.any {
                val (nr, nc) = it
                nr in 0..7 && nc in 0..7 && boardState[nr][nc].startsWith(piece.substring(0, 4))
            }
            if (!hasFriendlyNeighbor) {
                boardState[r][c] = ""
            }
        }
    }
}

fun checkForWinner(boardState: MutableList<MutableList<String>>, currentPlayer: String): String? {
    val targetRow = if (currentPlayer == "Gold") 0 else 7
    for (col in 0..7) {
        val piece = boardState[targetRow][col]
        if (piece == "${currentPlayer}Rabbit") {
            return "$currentPlayer wins!"
        }
    }
    return null
}


fun handleImmobilization(boardState: MutableList<MutableList<String>>) {
    val sizeHierarchy = listOf("Rabbit", "Cat", "Dog", "Horse", "Camel", "Elephant")
    for (row in 0 until 8) {
        for (col in 0 until 8) {
            val piece = boardState[row][col]
            if (piece.isNotEmpty()) {
                val pieceType = piece.substring(4) // Extract type (e.g., Rabbit, Dog)
                val neighbors = listOf(
                    Pair(row - 1, col), Pair(row + 1, col),
                    Pair(row, col - 1), Pair(row, col + 1)
                )
                val largerNeighbors = neighbors.filter { (nr, nc) ->
                    nr in 0..7 && nc in 0..7 && boardState[nr][nc].isNotEmpty() &&
                            sizeHierarchy.indexOf(boardState[nr][nc].substring(4)) >
                            sizeHierarchy.indexOf(pieceType)
                }
                if (largerNeighbors.isNotEmpty()) {
                    boardState[row][col] = "$piece-Immobilized"
                } else if (piece.endsWith("-Immobilized")) {
                    boardState[row][col] = piece.removeSuffix("-Immobilized")
                }
            }
        }
    }
}

// Handle pushing mechanics
fun handlePush(
    boardState: MutableList<MutableList<String>>,
    from: Pair<Int, Int>,
    to: Pair<Int, Int>,
    onError: (String) -> Unit,
    onPushComplete: (MutableList<MutableList<String>>) -> Unit
) {
    val (fx, fy) = from
    val (tx, ty) = to
    val movingPiece = boardState[fx][fy]
    val pushedPiece = boardState[tx][ty]

    // Ensure the move is valid
    if (pushedPiece.isEmpty()) {
        onError("No piece to push!")
        return
    }

    val sizeHierarchy = listOf("Rabbit", "Cat", "Dog", "Horse", "Camel", "Elephant")
    if (sizeHierarchy.indexOf(movingPiece.substring(4)) <= sizeHierarchy.indexOf(pushedPiece.substring(4))) {
        onError("Cannot push a piece of equal or larger size!")
        return
    }

    // Determine the direction of the push
    val pushDirection = Pair(tx - fx, ty - fy)
    val pushedTo = Pair(tx + pushDirection.first, ty + pushDirection.second)

    // Ensure the pushed piece lands within bounds and on an empty square
    if (pushedTo.first !in 0..7 || pushedTo.second !in 0..7 || boardState[pushedTo.first][pushedTo.second].isNotEmpty()) {
        onError("Invalid push direction!")
        return
    }

    // Perform the push
    val updatedBoard = boardState.map { it.toMutableList() }.toMutableList()
    updatedBoard[pushedTo.first][pushedTo.second] = pushedPiece
    updatedBoard[tx][ty] = movingPiece
    updatedBoard[fx][fy] = ""

    onPushComplete(updatedBoard)
}

