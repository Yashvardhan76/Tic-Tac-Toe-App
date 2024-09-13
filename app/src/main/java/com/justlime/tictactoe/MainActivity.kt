package com.justlime.tictactoe

import android.app.Activity
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.justlime.tictactoe.game.isMusicChecked
import com.justlime.tictactoe.models.GameManager.gameStatus
import com.justlime.tictactoe.models.GameState
import com.justlime.tictactoe.models.HomeViewModel
import com.justlime.tictactoe.models.HomeViewModelFactory
import com.justlime.tictactoe.models.IS_MUSIC_CHECKED
import com.justlime.tictactoe.models.dataStore
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
            Scaffold{
                LaunchedEffect(isMusicChecked.value) {
                    if (isMusicChecked.value) {
                        playMusic()
                    } else {
                        pauseMusic()
                    }
                }
                App(it)
                SetStatusBarColor(darkIcons = false)
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
fun SetStatusBarColor(color: Int = 0, darkIcons: Boolean = true) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val window = (context as Activity).window
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        window.statusBarColor = color  // Set the status bar color

        // Control the appearance of the status bar icons (light or dark)
        insetsController.isAppearanceLightStatusBars = darkIcons

        onDispose {}
    }
}

@Composable
fun App(paddingValues: PaddingValues) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val dataStore = (context as MainActivity).dataStore

    // Define NavHost with routes
    NavHost(navController = navController, startDestination = "home") {
        composable(route = "home") {
            // Initialize the HomeViewModel using the factory
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(dataStore)
            )

            // Pass the HomeViewModel to HomeScreen
            HomeScreen(navController = navController, homeViewModel = homeViewModel)

            // Set the game status to empty when in the home screen
            gameStatus.value = GameState.EMPTY
        }
        composable(route = "game") {
            // Game screen logic
            GameScreen(navController = navController)

            // Set the game status to ongoing when in the game screen
            gameStatus.value = GameState.ONGOING
        }
        composable(route = "setting") {
            // Settings screen logic
            SettingScreen(navController = navController)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AppPreview() {
    App(paddingValues = PaddingValues(0.dp))
}
