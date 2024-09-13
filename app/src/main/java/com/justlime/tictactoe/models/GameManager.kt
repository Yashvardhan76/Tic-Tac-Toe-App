package com.justlime.tictactoe.models

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.justlime.tictactoe.R

object GameManager {



    var currentPlayer = mutableStateOf(Symbol.X)
    var gameStatus = mutableStateOf(GameState.ONGOING)
    var currentMode = mutableStateOf(Mode.SINGLE)

    val gameFont = FontFamily(Font(resId = R.font.nunitoblack, weight = FontWeight.Black))


    fun switchPlayer() {
        currentPlayer.value = when (currentPlayer.value) {
            Symbol.X -> Symbol.O
            else -> Symbol.X
        }
    }

    fun reset() {
        currentPlayer.value = Symbol.X
        gameStatus.value = GameState.EMPTY
    }
}

enum class GameState {
    ONGOING, PLAYER_WIN, DRAW, PAUSE, EMPTY
}

enum class Symbol {
    X, O, EMPTY;
}

enum class Mode{
    SINGLE, MULTI
}
