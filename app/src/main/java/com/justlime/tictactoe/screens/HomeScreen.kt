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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.justlime.tictactoe.components.GameTitle
import com.justlime.tictactoe.game.HomeMenu
import com.justlime.tictactoe.game.isInfinityChecked
import com.justlime.tictactoe.game.isMusicChecked
import com.justlime.tictactoe.game.isSoundChecked
import com.justlime.tictactoe.models.GameManager.reset
import com.justlime.tictactoe.models.HomeViewModel

@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel) {
    val activity = LocalContext.current as? Activity
    isSoundChecked.value = homeViewModel.isSoundChecked.collectAsState().value
    isMusicChecked.value = homeViewModel.isMusicChecked.collectAsState().value
    isInfinityChecked.value = homeViewModel.isInfinityChecked.collectAsState().value


    // Handle back press to reset the game and finish the activity
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