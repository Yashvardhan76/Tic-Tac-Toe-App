package com.justlime.tictactoe.models

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.justlime.tictactoe.R

object GameManager {



    var currentPlayer = mutableStateOf(Symbol.X)
    var gameStatus = mutableStateOf(GameState.ONGOING)
    var currentMode = mutableStateOf(Mode.SINGLE)
    var soundState = mutableStateOf(Sound.ON)
    var gameType = mutableStateOf(Type.INFINITE)
    val isSoundChecked =  mutableStateOf(true)
    val isMusicChecked =  mutableStateOf(false)
    val isInfinityChecked = mutableStateOf(true)

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

enum class Sound {
    ON, OFF
}

enum class Mode{
    SINGLE, MULTI
}
enum class Type{
    SIMPLE, ULTIMATE, INFINITE
}
