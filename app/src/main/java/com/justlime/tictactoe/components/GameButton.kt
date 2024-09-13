package com.justlime.tictactoe.components

import android.media.MediaPlayer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justlime.tictactoe.R
import com.justlime.tictactoe.game.isSoundChecked
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun GameButton(onClick: () -> Unit, text: String, padding: Dp = 10.dp) {
    val context = LocalContext.current
    val buttonClickSound = remember { MediaPlayer.create(context, R.raw.button_click_sfx) }
    buttonClickSound.isLooping = false
    val gameFont = FontFamily(Font(resId = R.font.nunitoblack, weight = FontWeight.Black))
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        onDispose { buttonClickSound.release() } // Release when composable is no longer used
    }
    Button(
        onClick = {
            coroutineScope.launch {
                if (isSoundChecked.value) {
                    if (!buttonClickSound.isPlaying) {
                        buttonClickSound.seekTo(0)
                        buttonClickSound.start()
                    }
                    delay(350.milliseconds)
                    onClick()
                } else {
                    onClick()
                }
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2BFBF)),
        shape = RoundedCornerShape(bottomEnd = 4.dp, bottomStart = 4.dp),
        contentPadding = PaddingValues(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = padding, end = padding)
            .shadow(
                elevation = 100.dp,
                shape = RoundedCornerShape(0.dp),
                spotColor = Color(0xff000000),
                ambientColor = Color(0xff000000),
            )
        ,
    ) {
        Text(
            text = text,
            color = Color(0xFF3D0F0F),
            fontFamily = gameFont,
            fontSize = 30.sp,
        )
    }
}

@Composable
@Preview(widthDp = 320)
fun GameButtonPreview() {
    GameButton(onClick = { }, text = "PLAY")
}
