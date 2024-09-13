package com.justlime.tictactoe.game

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.justlime.tictactoe.models.GameManager.currentMode
import com.justlime.tictactoe.models.GameManager.currentPlayer
import com.justlime.tictactoe.models.GameManager.gameFont
import com.justlime.tictactoe.models.GameManager.gameStatus
import com.justlime.tictactoe.models.GameManager.switchPlayer
import com.justlime.tictactoe.models.GameState
import com.justlime.tictactoe.models.GameViewModel
import com.justlime.tictactoe.models.Mode
import com.justlime.tictactoe.models.Symbol
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun CreateBoard(paddingValues: Dp = 20.dp, space: Dp = 10.dp,gameViewModel: GameViewModel = viewModel()) {
    val context = LocalContext.current
    val piecePutSoundEffect =
        remember { MediaPlayer.create(context, com.justlime.tictactoe.R.raw.piece_put_sfx) }
    val coroutineScope = rememberCoroutineScope()

    // Release when composable is no longer used
    DisposableEffect(Unit) {

        onDispose { piecePutSoundEffect.release() }
    }

    val winColorBox = gameViewModel.winColorBox
    val boardState = gameViewModel.boardState
    val storedMoves = gameViewModel.storedMoves

    piecePutSoundEffect.isLooping = false



    if (gameStatus.value == GameState.EMPTY) {
        resetBoardState(boardState)
        storedMoves.clear()
        winColorBox.clear()
        currentPlayer.value = Symbol.X
        Log.d("GameState: ", "BoardReset")
        gameStatus.value = GameState.ONGOING
    }
    // Create a LazyVerticalGrid
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(space),
        horizontalArrangement = Arrangement.spacedBy(space),
        contentPadding = PaddingValues(paddingValues),
    ) {
        items(boardState.size) { index ->
            PlayerBox(
                symbol = boardState[index],
                font = gameFont,
                symbolColor = { if (boardState[index] == Symbol.X) Color.White else Color(0xffbda41e) },
                isFadingOut = storedMoves.isNotEmpty() && storedMoves[0] == index && storedMoves.size >= 6,
                bgColor = {
                    if (winColorBox.contains(index)) if (boardState[index] == Symbol.X) Color(
                        0xff288b05
                    ) else Color(0xFFCE1D1D) else Color(0xFF4D1561)
                })
            {
                // Check game status
                if (gameStatus.value == GameState.ONGOING && boardState[index] == Symbol.EMPTY) {
                    handlePlayerMove(
                        index,
                        boardState,
                        storedMoves,
                        winColorBox,
                        piecePutSoundEffect,
                        coroutineScope
                    )
                }
            }
        }
    }
}


fun resetBoardState(boardState: MutableList<Symbol?>) {
    boardState.clear()
    boardState.addAll(List(9){Symbol.EMPTY})
//    val initialSequence = listOf(Symbol.EMPTY, Symbol.O, Symbol.X, Symbol.X, Symbol.X, Symbol.O, Symbol.EMPTY, Symbol.EMPTY, Symbol.O)
//    boardState.addAll(initialSequence)
}


private fun handlePlayerMove(
    index: Int,
    boardState: MutableList<Symbol?>,
    storedMoves: MutableList<Int>,
    winColorBox: MutableList<Int>,
    piecePutSoundEffect: MediaPlayer,
    context: CoroutineScope
) {
    boardState[index] = currentPlayer.value
    // print the list of stored Moves
    fun printList() {
        Log.d("storedMoves", "------------")
        Log.d(
            "storedMoves",
            storedMoves.joinToString(separator = " | ", prefix = "[", postfix = "]")
        )
        Log.d("storedMoves", "------------")
    }

    if (isInfinityChecked.value) {
        //append index to storedMoves
        storedMoves.add(index)
        Log.d("storedMoves", "ADDED")
        printList()
        if (storedMoves.size >= 7) {
            val oldestMoveIndex = storedMoves.removeAt(0)
            boardState[oldestMoveIndex] = Symbol.EMPTY
        }
    }
    if (isSoundChecked.value) {
        piecePutSoundEffect.seekTo(0)
        piecePutSoundEffect.start()
    }

    // Common checks for both SINGLE and MULTI modes


    if (checkWin(boardState, winColorBox)) {
        gameStatus.value = GameState.PLAYER_WIN

        return
    } else if (boardState.none { it == Symbol.EMPTY }) {
        gameStatus.value = GameState.DRAW

        return
    }

    // Switch player and perform AI move if in SINGLE mode
    switchPlayer()
    if (currentMode.value == Mode.SINGLE && gameStatus.value == GameState.ONGOING && currentPlayer.value == Symbol.O) {
        // Delay for AI move
        context.launch {
            delay(500)
            // Perform AI move
            if (isInfinityChecked.value) {
                if (storedMoves.size >= 5) {
                    val aiIndex = makeAIMoveForInfinityMode(boardState, storedMoves)
                    //append index to storedMoves
                    storedMoves.add(aiIndex)
                    Log.d("storedMoves", "ADDED BY AI")
                    printList()
                } else {
                    val aiIndex = makeAIMove(boardState)
                    storedMoves.add(aiIndex)
                }
                if (storedMoves.size >= 7) {
                    val oldestMoveIndex = storedMoves.removeAt(0)
                    boardState[oldestMoveIndex] = Symbol.EMPTY
                }
            } else makeAIMove(boardState)

            if (isSoundChecked.value) {
                piecePutSoundEffect.seekTo(0)
                piecePutSoundEffect.start()
            }

            if (checkWin(boardState, winColorBox)) {
                gameStatus.value = GameState.PLAYER_WIN

            } else if (boardState.none { it == Symbol.EMPTY }) {
                gameStatus.value = GameState.DRAW

            } else {
                switchPlayer()
            }
        }
    }
}


private fun checkWin(boardState: MutableList<Symbol?>, winColorBox: MutableList<Int>): Boolean {
    // Vertically
    for (i in 0..6 step 3) {
        if (boardState[i] == boardState[i + 1] && boardState[i] == boardState[i + 2] && boardState[i] != Symbol.EMPTY) {
            winColorBox.clear()
            winColorBox.addAll(listOf(i, i + 1, i + 2))
            return true
        }
    }
    // Horizontally
    for (i in 0..2) {
        if (boardState[i] == boardState[i + 3] && boardState[i] == boardState[i + 6] && boardState[i] != Symbol.EMPTY) {
            winColorBox.clear()
            winColorBox.addAll(listOf(i, i + 3, i + 6))
            return true
        }
    }
    // Diagonally
    if (boardState[0] == boardState[4] && boardState[0] == boardState[8] && boardState[0] != Symbol.EMPTY) {
        winColorBox.clear()
        winColorBox.addAll(listOf(0, 4, 8))
        return true
    }
    if (boardState[2] == boardState[4] && boardState[2] == boardState[6] && boardState[2] != Symbol.EMPTY) {
        winColorBox.clear()
        winColorBox.addAll(listOf(2, 4, 6))
        return true
    }
    return false
}

private fun makeAIMove(boardState: MutableList<Symbol?>): Int {
    val emptyIndicesCount = boardState.count { it == Symbol.EMPTY }
    val emptyIndices = boardState.withIndex().filter { it.value == Symbol.EMPTY }.map { it.index }
    if (emptyIndicesCount in 7..8) {
        val randomIndex = emptyIndices.random()
        boardState[randomIndex] = Symbol.O
        return randomIndex
    } else {
        //AI WIN Logic And Defend Logic
        for (index in emptyIndices) {
            //check if Ai is Winning
            val boardStateCopy = boardState.toMutableList()
            boardStateCopy[index] = Symbol.O
            if (checkWin(boardStateCopy, mutableListOf())) {
                Log.d("Win?", "AI is Winning || index: $index")
                boardState[index] = Symbol.O
                return index
            }
        }
        for (index in emptyIndices) {
            //check if player is Winning
            val boardStateCopy = boardState.toMutableList()
            boardStateCopy[index] = Symbol.X
            if (checkWin(boardStateCopy, mutableListOf())) {
                Log.d("Win?", "Player is Winning || index: $index")
                boardState[index] = Symbol.O
                return index
            }
        }
        //AI Logic if can't win or block
        val randomIndex = emptyIndices.random()
        boardState[randomIndex] = Symbol.O
        return randomIndex
    }
}

private fun makeAIMoveForInfinityMode(
    boardState: MutableList<Symbol?>,
    storedMoves: MutableList<Int>
): Int {
    //List of Empty Slices Index
    val emptyIndices = boardState.withIndex().filter { it.value == Symbol.EMPTY }.map { it.index }
    if (emptyIndices.count() in 7..8) {
        val randomIndex = emptyIndices.random()
        boardState[randomIndex] = Symbol.O
        return randomIndex
    } else {
        //AI WIN Logic And Defend Logic

        for (index in emptyIndices) {
            //check if Ai is Winning
            val boardStateCopy = boardState.toMutableList()
            val storedMovesCopy = storedMoves.toMutableList()
            boardStateCopy[index] = Symbol.O
            storedMovesCopy.add(index)
            boardStateCopy[storedMovesCopy[0]] = Symbol.EMPTY
            if (checkWin(boardStateCopy, mutableListOf())) {
                Log.d("Win?", "AI is Winning || index: $index")
                boardState[index] = Symbol.O
                return index
            }
        }
        for (index in emptyIndices) {
            //check if player is Winning
            val boardStateCopy = boardState.toMutableList()
            val storedMovesCopy = storedMoves.toMutableList()
            boardStateCopy[index] = Symbol.X
            //log stored values
//            Log.d("storedMoves", storedMovesCopy.joinToString(separator = " || ", prefix = "Pre Stored Moves: [", postfix = "]"))

            boardStateCopy[storedMovesCopy[0]] = Symbol.EMPTY
            storedMovesCopy.add(index)
            storedMovesCopy.removeAt(0)
            boardStateCopy[storedMovesCopy[0]] = Symbol.EMPTY
            //log stored values
//            Log.d("storedMoves", storedMovesCopy.joinToString(separator = " || ", prefix = "New Stored Moves: [", postfix = "]"))

            if (checkWin(boardStateCopy, mutableListOf())) {
                Log.d("Win?", "Player is Winning || index: $index")
                boardState[index] = Symbol.O
                return index
            }
        }
        //AI Logic if can't win or block
        val randomIndex = emptyIndices.random()
        boardState[randomIndex] = Symbol.O
        return randomIndex
    }
}

@Preview
@Composable
private fun CreateBoardPreview() {
    CreateBoard()
}