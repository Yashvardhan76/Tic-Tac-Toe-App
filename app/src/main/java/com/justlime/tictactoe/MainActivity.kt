package com.justlime.tictactoe

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.justlime.tictactoe.models.GameManager.gameStatus
import com.justlime.tictactoe.models.GameState
import com.justlime.tictactoe.screens.GameScreen
import com.justlime.tictactoe.screens.HomeScreen
import com.justlime.tictactoe.screens.SettingScreen

class MainActivity : ComponentActivity() {

    private lateinit var music: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        music = MediaPlayer.create(this, R.raw.music).apply {
                isLooping = true
                setVolume(0.1f, 0.1f)
            }


        setContent {
            Surface {
                App()
            }
        }
    }

    fun playMusic() {
        if (!music.isPlaying) {
            music.start()
        }
    }

    fun pauseMusic() {
        if (music.isPlaying) {
            music.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        music.release()
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable(route = "home") {
            HomeScreen(navController)
            gameStatus.value = GameState.EMPTY
        }
        composable(route = "game") {
            GameScreen(navController)
            gameStatus.value = GameState.ONGOING
        }
        composable(route = "setting") {
            SettingScreen(navController = navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    App()
}
