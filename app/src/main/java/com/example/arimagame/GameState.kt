package com.example.arimagame

data class GameState(
    val board: Array<Array<String>> = arrayOf(
        arrayOf("R", "R", "R", "R", "R", "R", "R", "R"),
        arrayOf("C", "D", "H", "E", "E", "H", "D", "C"),
        arrayOf("", "", "", "", "", "", "", ""),
        arrayOf("", "", "", "", "", "", "", ""),
        arrayOf("", "", "", "", "", "", "", ""),
        arrayOf("", "", "", "", "", "", "", ""),
        arrayOf("", "", "", "", "", "", "", ""),
        arrayOf("", "", "", "", "", "", "", "")
    ),
    val history: MutableList<Array<Array<String>>> = mutableListOf(board.copyOf())
) {
    fun isDuplicateState(): Boolean {
        return history.any { it.contentDeepEquals(board) }
    }

    fun movePiece(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int): GameState {
        val newBoard = board.copyOf() // Create a deep copy of the board
        newBoard[toRow][toCol] = newBoard[fromRow][fromCol]
        newBoard[fromRow][fromCol] = ""
        return GameState(newBoard, (history + listOf(newBoard)).toMutableList())
    }

    fun removePieceIfInTrap(row: Int, col: Int): GameState {
        if (isInTrap(row, col) && !hasFriendlyPieceAdjacent(row, col)) {
            val newBoard = board.copyOf()
            newBoard[row][col] = ""
            return GameState(newBoard, (history + listOf(newBoard)).toMutableList())
        }
        return this
    }

    fun undoMove(): GameState {
        return if (history.size > 1) {
            GameState(history[history.size - 2].copyOf(), history.dropLast(1).toMutableList())
        } else {
            this
        }
    }

    // Determines if a piece is in a trap
    private fun isInTrap(row: Int, col: Int): Boolean {
        return (row == 2 || row == 5) && (col == 2 || col == 5)
    }

    // Determines if a friendly piece is adjacent
    private fun hasFriendlyPieceAdjacent(row: Int, col: Int): Boolean {
        val piece = board[row][col]
        val adjacentPositions = listOf(
            row - 1 to col, row + 1 to col, row to col - 1, row to col + 1
        )
        return adjacentPositions.any { (r, c) ->
            r in 0..7 && c in 0..7 && board[r][c] == piece
        }
    }
}

// Custom extension function for deep copying a 2D array
fun Array<Array<String>>.copyOf(): Array<Array<String>> {
    return Array(size) { i -> this[i].clone() } // Deep copy each row
}
