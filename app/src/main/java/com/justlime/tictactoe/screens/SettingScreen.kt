package com.justlime.tictactoe.screens

import android.media.MediaPlayer
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.justlime.tictactoe.components.GameTitle
import com.justlime.tictactoe.game.SettingMenu
import com.justlime.tictactoe.game.isSoundChecked

@Composable
fun SettingScreen(navController: NavController){
    // Setting screen content
    val context = LocalContext.current
    val backClickSoundEffect =
        remember { MediaPlayer.create(context, com.justlime.tictactoe.R.raw.piece_put_sfx) }
    BackHandler {
        navController.navigate("home")

        // Handle back press action
        if(isSoundChecked.value) {
            backClickSoundEffect.seekTo(0)
            backClickSoundEffect.start()
        }
    }
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF230F3D))) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                GameTitle(text = "Setting")
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SettingMenu(navController = navController)
            }
        }
    }

@Preview
@Composable
private fun SettingScreenPreview() {
    SettingScreen(navController = NavController(LocalContext.current))
}