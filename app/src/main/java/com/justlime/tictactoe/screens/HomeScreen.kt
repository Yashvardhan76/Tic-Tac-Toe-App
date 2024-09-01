package com.justlime.tictactoe.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.justlime.tictactoe.components.GameTitle
import com.justlime.tictactoe.game.HomeMenu
import com.justlime.tictactoe.models.GameManager.reset

@Composable
fun HomeScreen(navController: NavController) {
    val activity = LocalContext.current as? Activity
    BackHandler {
        reset()
        activity?.finish()
    }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF230F3D))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.2f),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    GameTitle(text = "Tic Tac Toe")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.8f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    HomeMenu(navController)
                }
            }
}

@Preview(widthDp = 320, heightDp = 480)
@Composable
fun HomeScreenPreview() {

    val navController = NavController(LocalContext.current)
    HomeScreen(
        navController
    )
}