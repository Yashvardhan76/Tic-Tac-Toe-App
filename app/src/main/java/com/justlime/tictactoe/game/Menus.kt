package com.justlime.tictactoe.game

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.justlime.tictactoe.MainActivity
import com.justlime.tictactoe.components.GameButton
import com.justlime.tictactoe.components.GameSwitch
import com.justlime.tictactoe.models.GameManager.currentMode
import com.justlime.tictactoe.models.GameManager.gameType
import com.justlime.tictactoe.models.GameManager.isInfinityChecked
import com.justlime.tictactoe.models.GameManager.isMusicChecked
import com.justlime.tictactoe.models.GameManager.isSoundChecked
import com.justlime.tictactoe.models.GameManager.soundState
import com.justlime.tictactoe.models.Mode
import com.justlime.tictactoe.models.Sound
import com.justlime.tictactoe.models.Type

@Composable
fun HomeMenu(navController: NavController) {
    Column {
        GameButton(
            onClick = { navController.navigate("game"); currentMode.value = Mode.MULTI },
            "PLAY",
            20.dp
        )
        Spacer(modifier = Modifier.height(10.dp))
        GameButton(
            onClick = { navController.navigate("game"); currentMode.value = Mode.SINGLE },
            "PRACTICE",
            20.dp
        )
        Spacer(modifier = Modifier.height(10.dp))
        GameButton(onClick = { navController.navigate("setting") }, "SETTING", 20.dp)
    }
}

@Composable
fun SettingMenu(navController: NavController) {
    val context = LocalContext.current as MainActivity
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameSwitch(text = "MUSIC", isChecked = isMusicChecked, onCheckedChange ={
            isMusicChecked.value = it
            if (isMusicChecked.value) {
                context.playMusic()
            } else {
                context.pauseMusic()
            }
        },onToggle = {
        } )


        GameSwitch(
            text = "SOUND",
            isChecked = isSoundChecked,
            onCheckedChange = {
                isSoundChecked.value = it
                if (isSoundChecked.value) soundState.value = Sound.ON else soundState.value =
                    Sound.OFF
                Log.d("toggle", "Sound: ${soundState.value}")
            },
            onToggle = {
                // Additional action on toggle if needed

            })
        GameSwitch(
            text = "INFINITY",
            isChecked = isInfinityChecked,
            onCheckedChange = {
                isInfinityChecked.value = it
                if (isInfinityChecked.value) gameType.value = Type.INFINITE else gameType.value =
                    Type.SIMPLE
                Log.d("toggle", "Type: ${gameType.value}")
            },
            onToggle = {
                // Additional action on toggle if needed
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        GameButton(onClick = { navController.navigate("home") }, "BACK", 20.dp)

    }
}