package com.example.numberadditionsubtractionandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.numberadditionsubtractionandroid.data.AppPreferencesRepository
import com.example.numberadditionsubtractionandroid.data.GameSettings
import com.example.numberadditionsubtractionandroid.data.LeaderboardRepository
import com.example.numberadditionsubtractionandroid.game.GameUiState
import com.example.numberadditionsubtractionandroid.game.GameViewModel
import com.example.numberadditionsubtractionandroid.tts.TtsManager
import com.example.numberadditionsubtractionandroid.ui.screens.GameScreen
import com.example.numberadditionsubtractionandroid.ui.screens.HomeScreen
import com.example.numberadditionsubtractionandroid.ui.screens.LanguageScreen
import com.example.numberadditionsubtractionandroid.ui.screens.ResultScreen
import com.example.numberadditionsubtractionandroid.ui.screens.SettingsScreen
import com.example.numberadditionsubtractionandroid.ui.theme.NumberAdditionSubtractionAndroidTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

enum class Screen { LANGUAGE, HOME, SETTINGS, GAME, RESULT }

class MainActivity : ComponentActivity() {

    private lateinit var ttsManager: TtsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ttsManager = TtsManager(this)
        enableEdgeToEdge()

        val repository = LeaderboardRepository(this)
        val appPreferencesRepository = AppPreferencesRepository(this)

        setContent {
            NumberAdditionSubtractionAndroidTheme {
                var screen by remember { mutableStateOf(Screen.HOME) }
                var settings by remember { mutableStateOf(GameSettings()) }
                var lastGameState by remember { mutableStateOf(GameUiState()) }
                var startupResolved by remember { mutableStateOf(false) }
                val gameViewModel: GameViewModel = viewModel()
                val scope = rememberCoroutineScope()

                LaunchedEffect(Unit) {
                    val hasSavedLanguage = appPreferencesRepository.hasSavedLanguage.first()
                    val savedLanguage = appPreferencesRepository.language.first()
                    settings = settings.copy(language = savedLanguage)
                    screen = if (hasSavedLanguage) Screen.HOME else Screen.LANGUAGE
                    startupResolved = true
                }

                if (!startupResolved) return@NumberAdditionSubtractionAndroidTheme

                when (screen) {
                    Screen.LANGUAGE -> LanguageScreen { lang ->
                        settings = settings.copy(language = lang)
                        scope.launch {
                            appPreferencesRepository.saveLanguage(lang)
                        }
                        screen = Screen.HOME
                    }

                    Screen.HOME -> HomeScreen(
                        language = settings.language,
                        onSpeakAnimal = { animal, lang ->
                            ttsManager.speakAnimalName(animal, lang)
                        },
                        onStart = { gameMode ->
                            val gameSettings = settings.copy(gameMode = gameMode)
                            val gameLanguage = gameSettings.language
                            val voiceMode = gameSettings.voiceMode
                            gameViewModel.init(
                                gameSettings = gameSettings,
                                speakRoundCount = { num, animal ->
                                    ttsManager.speakGameCount(num, animal, gameLanguage, voiceMode)
                                },
                                speakFeedback = { correct ->
                                    ttsManager.speakFeedback(correct, gameLanguage)
                                }
                            )
                            screen = Screen.GAME
                        },
                        onSettings = { screen = Screen.SETTINGS },
                        onLeaderboard = { screen = Screen.RESULT },
                        onChangeLanguage = { screen = Screen.LANGUAGE }
                    )

                    Screen.SETTINGS -> SettingsScreen(
                        settings = settings,
                        onSettingsChanged = { updatedSettings ->
                            val previousLanguage = settings.language
                            settings = updatedSettings
                            if (previousLanguage != updatedSettings.language) {
                                scope.launch {
                                    appPreferencesRepository.saveLanguage(updatedSettings.language)
                                }
                            }
                        },
                        onBack = { screen = Screen.HOME },
                        leaderboardRepository = repository
                    )

                    Screen.GAME -> GameScreen(
                        viewModel = gameViewModel,
                        settings = settings,
                        onGameOver = {
                            lastGameState = gameViewModel.uiState.value
                            screen = Screen.RESULT
                        }
                    )

                    Screen.RESULT -> ResultScreen(
                        gameState = lastGameState,
                        language = settings.language,
                        repository = repository,
                        onPlayAgain = {
                            val gameLanguage = settings.language
                            val voiceMode = settings.voiceMode
                            gameViewModel.init(
                                gameSettings = settings,
                                speakRoundCount = { num, animal ->
                                    ttsManager.speakGameCount(num, animal, gameLanguage, voiceMode)
                                },
                                speakFeedback = { correct ->
                                    ttsManager.speakFeedback(correct, gameLanguage)
                                }
                            )
                            screen = Screen.GAME
                        },
                        onHome = { screen = Screen.HOME }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsManager.shutdown()
    }
}