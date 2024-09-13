package com.justlime.tictactoe.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.justlime.tictactoe.MainActivity
import com.justlime.tictactoe.components.GameButton
import com.justlime.tictactoe.components.GameSwitch
import com.justlime.tictactoe.models.GameManager.currentMode
import com.justlime.tictactoe.models.Mode
import com.justlime.tictactoe.models.SettingsViewModel
import com.justlime.tictactoe.models.SettingsViewModelFactory
import com.justlime.tictactoe.models.dataStore

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

val isSoundChecked =   mutableStateOf(true)
val isMusicChecked =  mutableStateOf(false)
val isInfinityChecked =   mutableStateOf(false)

@Composable
fun SettingMenu(navController: NavController) {
    val context = LocalContext.current
    val dataStore = (context as MainActivity).dataStore

    // Use the custom ViewModelFactory
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(dataStore)
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameSwitch(
            text = "MUSIC",
            isChecked = isMusicChecked.value,
            onCheckedChange = {
                isMusicChecked.value = it
                if (isMusicChecked.value) {
                    context.playMusic()
                } else {
                    context.pauseMusic()
                }
                settingsViewModel.saveSettings(
                    isSoundChecked = isSoundChecked.value,
                    isMusicChecked = isMusicChecked.value,
                    isInfinityChecked = isInfinityChecked.value
                )
            }
        )

        GameSwitch(
            text = "SOUND",
            isChecked = isSoundChecked.value,
            onCheckedChange = {
                isSoundChecked.value = it
                // Update sound state and save settings
                settingsViewModel.saveSettings(
                    isSoundChecked = isSoundChecked.value,
                    isMusicChecked = isMusicChecked.value,
                    isInfinityChecked = isInfinityChecked.value
                )
            }
        )

        GameSwitch(
            text = "INFINITY",
            isChecked = isInfinityChecked.value,
            onCheckedChange = {
                isInfinityChecked.value = it
                settingsViewModel.saveSettings(
                    isSoundChecked = isSoundChecked.value,
                    isMusicChecked = isMusicChecked.value,
                    isInfinityChecked = isInfinityChecked.value
                )
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        GameButton(onClick = { navController.navigate("home") }, "BACK", 20.dp)
    }
}


