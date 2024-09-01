package com.justlime.tictactoe.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justlime.tictactoe.models.GameManager.currentMode
import com.justlime.tictactoe.models.GameManager.currentPlayer
import com.justlime.tictactoe.models.GameManager.gameStatus
import com.justlime.tictactoe.models.GameState
import com.justlime.tictactoe.models.Mode
import com.justlime.tictactoe.models.Symbol

@Composable
fun PlayerBox(
    symbol: Symbol?,
    font: FontFamily,
    isFadingOut: Boolean = false,
    symbolColor: () -> Color,
    bgColor: () -> Color,
    onClick: () -> Unit
) {
    val isClickable = isBoxClickable(symbol)

    Box(
        Modifier
            .background(
                color = bgColor(),
                shape = RoundedCornerShape(20)
            )
            .aspectRatio(1f)
            .size(50.dp)
            .clickable(enabled = isClickable) {
                if (isClickable) {
                    onClick()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (symbol == Symbol.EMPTY) "" else symbol.toString(),
            color = if (isFadingOut) Color.Gray else symbolColor(),
            fontSize = 70.sp,
            fontFamily = font
        )
    }
}

@Composable
fun isBoxClickable(symbol: Symbol?): Boolean {
    return when {
        symbol != Symbol.EMPTY -> false
        gameStatus.value != GameState.ONGOING -> false
        currentMode.value == Mode.SINGLE && currentPlayer.value != Symbol.X -> false
        else -> true
    }
}
