package com.justlime.tictactoe.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.justlime.tictactoe.models.GameManager.gameFont

@Composable
fun GameTitle(text: String,fontSize: TextUnit = 47.sp) {
    Text(
        text = text.uppercase(),
        color = Color(0xFF82CCD6),
        fontFamily = gameFont,
        fontSize = fontSize, // Font size
        letterSpacing = 3.sp, // Letter spacing
        style = TextStyle(
            shadow = Shadow(
                color = Color.Black,
                blurRadius = 10f
            )
        )
    )
}

@Preview( showBackground = true)
@Composable
private fun GameTitlePreview() {
        GameTitle(text = "Tic Tac Toe")
}