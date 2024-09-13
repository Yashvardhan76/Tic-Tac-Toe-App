package com.justlime.tictactoe.models

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.justlime.tictactoe.game.resetBoardState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// DataStore extension to create a singleton instance
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

// Preference keys
val IS_SOUND_CHECKED = booleanPreferencesKey("is_sound_checked")
val IS_MUSIC_CHECKED = booleanPreferencesKey("is_music_checked")
val IS_INFINITY_CHECKED = booleanPreferencesKey("is_infinity_checked")

// ViewModel for managing settings
class SettingsViewModel(private val dataStore: DataStore<Preferences>) : ViewModel() {

    // Function to save settings
    fun saveSettings(
        isSoundChecked: Boolean,
        isMusicChecked: Boolean,
        isInfinityChecked: Boolean
    ) {
        viewModelScope.launch {
            dataStore.edit { settings ->
                settings[IS_SOUND_CHECKED] = isSoundChecked
                settings[IS_MUSIC_CHECKED] = isMusicChecked
                settings[IS_INFINITY_CHECKED] = isInfinityChecked
            }
        }
    }

    // Function to get settings as Flow
    fun getSettings(): Flow<Preferences> {
        return dataStore.data
    }
}


// ViewModelFactory for SettingsViewModel
class SettingsViewModelFactory(private val dataStore: DataStore<Preferences>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class HomeViewModel(private val dataStore: DataStore<Preferences>) : ViewModel() {

    // StateFlow to expose settings data to the UI
    private val _isSoundChecked = MutableStateFlow(true)
    val isSoundChecked: StateFlow<Boolean> = _isSoundChecked

    private val _isMusicChecked = MutableStateFlow(false)
    val isMusicChecked: StateFlow<Boolean> = _isMusicChecked

    private val _isInfinityChecked = MutableStateFlow(true)
    val isInfinityChecked: StateFlow<Boolean> = _isInfinityChecked

    init {
        // Load the saved settings when the ViewModel is initialized
        loadSettings()
    }

    // Function to load the settings from DataStore
    private fun loadSettings() {
        viewModelScope.launch {
            dataStore.data.collect { settings ->
                _isSoundChecked.value = settings[IS_SOUND_CHECKED] ?: true
                _isMusicChecked.value = settings[IS_MUSIC_CHECKED] ?: false
                _isInfinityChecked.value = settings[IS_INFINITY_CHECKED] ?: true
            }
        }
    }
}
class HomeViewModelFactory(private val dataStore: DataStore<Preferences>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class GameViewModel : ViewModel() {
    var winColorBox = mutableStateListOf(-1)
        private set

    var boardState = mutableStateListOf<Symbol?>().apply { resetBoardState(this) }
        private set

    var storedMoves = mutableStateListOf<Int>()
        private set

    // Additional logic like resetting board, saving moves, etc.
}




