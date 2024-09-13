package com.justlime.tictactoe.components

import android.media.MediaPlayer
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justlime.tictactoe.models.GameManager.gameFont

@Composable
fun CustomSwitch(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    checkedTrackColor: Color = MaterialTheme.colorScheme.primary,
    uncheckedTrackColor: Color = Color.Gray,
    thumbColor: Color = Color.White,
    uncheckedThumbColor: Color = Color.White,
    thumbRadius: Dp = 27.dp,
    switchWidth: Dp = 90.dp,
    switchHeight: Dp = 40.dp,
    content: @Composable () -> Unit
) {
    val thumbOffset by animateDpAsState(
        targetValue = if (isChecked) switchWidth - thumbRadius * 2 else 0.dp,
        label = ""
    )
    val animatedThumbColor by animateColorAsState(
        targetValue = if (isChecked) thumbColor else uncheckedThumbColor,
        label = ""
    )

    Box(
        modifier = modifier
            .width(switchWidth)
            .height(switchHeight)
            .background(
                color = if (isChecked) checkedTrackColor else uncheckedTrackColor,
                shape = RoundedCornerShape(percent = 50)
            )
            .clickable(
                onClick = { onCheckedChange(!isChecked) },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Row(
            modifier = Modifier
                .size(thumbRadius * 2)
                .offset(x = thumbOffset)
                .background(
                    color = animatedThumbColor,
                    shape = CircleShape
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            content()
        }
    }
}

@Composable
fun GameSwitch(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onToggle: () -> Unit = {}
) {
    val context = LocalContext.current
    val toggleSound =
        remember { MediaPlayer.create(context, com.justlime.tictactoe.R.raw.piece_put_sfx) }

    DisposableEffect(Unit) {
        onDispose {
            toggleSound.stop()
            toggleSound.release()
        }
    }

    Row(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color(0xfff0bebe))
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.5f),
            ) {
                Text(text = text, color = Color.Black, fontFamily = gameFont, fontSize = 30.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                CustomSwitch(
                    thumbColor = Color(0xff9d8508),
                    uncheckedThumbColor = Color.Red,
                    checkedTrackColor = Color(0xff230f3d),
                    uncheckedTrackColor = Color(0xff230f3d),
                    isChecked = isChecked,
                    onCheckedChange = {
                        onCheckedChange(it)
                        onToggle() // Call the sound playback function
                    },
                ) {
                    if (isChecked) {
                        Text(text = "ON", color = Color.White, fontFamily = gameFont)
                    } else {
                        Text(text = "OFF", color = Color.White, fontFamily = gameFont)
                    }
                }
            }
        }
    }
}
