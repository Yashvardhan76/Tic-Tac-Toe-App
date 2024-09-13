package com.justlime.tictactoe.screens

import android.media.MediaPlayer
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.justlime.tictactoe.components.GameButton
import com.justlime.tictactoe.components.GameTitle
import com.justlime.tictactoe.game.CreateBoard
import com.justlime.tictactoe.game.isSoundChecked
import com.justlime.tictactoe.models.GameManager.currentMode
import com.justlime.tictactoe.models.GameManager.currentPlayer
import com.justlime.tictactoe.models.GameManager.gameFont
import com.justlime.tictactoe.models.GameManager.gameStatus
import com.justlime.tictactoe.models.GameManager.reset
import com.justlime.tictactoe.models.GameState
import com.justlime.tictactoe.models.Mode
import com.justlime.tictactoe.models.Symbol
import com.justlime.tictactoe.models.WindowInfo
import com.justlime.tictactoe.models.rememberWindowInfo


@Composable
fun GameScreen(navController: NavController) {
    val context = LocalContext.current
    val winSoundEffect =
        remember { MediaPlayer.create(context, com.justlime.tictactoe.R.raw.game_win_sfx) }
    val defeatSoundEffect =
        remember { MediaPlayer.create(context, com.justlime.tictactoe.R.raw.game_defeat_sfx) }
    val drawSoundEffect =
        remember { MediaPlayer.create(context, com.justlime.tictactoe.R.raw.game_draw_sfx) }
    val backClickSoundEffect =
        remember { MediaPlayer.create(context, com.justlime.tictactoe.R.raw.piece_put_sfx) }
    val windowInfo = rememberWindowInfo()
    BackHandler {
        navController.navigate("home")

        // Handle back press action
        if(isSoundChecked.value) {
            backClickSoundEffect.seekTo(0)
            backClickSoundEffect.start()
        }
    }

    // Game status message
    val message = remember {
        derivedStateOf {
            when (gameStatus.value) {
                GameState.ONGOING -> "Player ${currentPlayer.value}'s Turn"
                GameState.PLAYER_WIN -> {
                    if (currentMode.value == Mode.SINGLE) {
                        if (currentPlayer.value == Symbol.X) {
                            // Play win sound effect here (once)
                            if (isSoundChecked.value) {
                                winSoundEffect.seekTo(0)
                                winSoundEffect.start()
                            }
                            "Victory"
                        } else {
                            // Play defeat sound effect here (once)
                            if (isSoundChecked.value) {
                                defeatSoundEffect.seekTo(0)
                                defeatSoundEffect.start()
                            }
                            "Defeat!"
                        }
                    } else {
                        // Play win sound effect here (once)
                        if (isSoundChecked.value) {
                            winSoundEffect.seekTo(0)
                            winSoundEffect.start()
                        }
                        "Player ${currentPlayer.value} Wins!"
                    }
                }

                GameState.DRAW -> {
                    // Play draw sound effect here (once)
                    if(isSoundChecked.value) {
                        drawSoundEffect.seekTo(0)
                        drawSoundEffect.start()
                    }
                    "TIE!"
                }

                GameState.PAUSE -> "Game Paused"
                else -> " "
            }
        }
    }

    // Game screen content
    //window size based
    Box(
        Modifier
            .fillMaxSize(),
    ) {
        if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF230F3D)),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Title
                Row(
                    modifier = Modifier
                        .weight(0.2f),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    GameTitle(text = "Tic Tac Toe")
                }

                // Game board
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.7f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CreateBoard()
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.4f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier)
                    Row(modifier = Modifier.padding(PaddingValues(10.dp))) {
                        if (gameStatus.value == GameState.PLAYER_WIN || gameStatus.value == GameState.DRAW) {
                            GameButton(onClick = { reset() }, text = "New Game")
                        }
                    }
                    // Message
                    Row(
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = message.value,
                            fontFamily = gameFont,
                            fontSize = 30.sp,
                            color = Color.White
                        )
                    }
                }

            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF230F3D)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Game board
                Column(
                    modifier = Modifier
                        .width(320.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    CreateBoard()
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.8f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Title
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        GameTitle(text = "Tic Tac Toe", 36.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    // Message
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = message.value,
                            fontFamily = gameFont,
                            fontSize = 26.sp,
                            color = Color.White
                        )
                    }

                    //New Game Button
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier)
                        Row(modifier = Modifier.padding(PaddingValues(10.dp))) {
                            if (gameStatus.value == GameState.PLAYER_WIN || gameStatus.value == GameState.DRAW) {
                                GameButton(onClick = { reset() }, text = "New Game")
                            }

                        }
                    }
                }


            }
        }
    }
//    if (gameStatus.value==GameState.PAUSE||gameStatus.value==GameState.PLAYER_WIN||gameStatus.value==GameState.DRAW)
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(color = Color(0x61270303))
//    ) {}
}

@Preview
@Composable
fun GameScreenPreview() {
    val navController = NavController(LocalContext.current)
    //window size based

    GameScreen(
        navController = navController
    )
}