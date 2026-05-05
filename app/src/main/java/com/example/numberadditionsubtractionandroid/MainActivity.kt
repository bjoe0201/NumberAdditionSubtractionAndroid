package com.example.numberadditionsubtractionandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.numberadditionsubtractionandroid.data.Animal
import com.example.numberadditionsubtractionandroid.data.AppLanguage
import com.example.numberadditionsubtractionandroid.data.AppPreferencesRepository
import com.example.numberadditionsubtractionandroid.data.GameMode
import com.example.numberadditionsubtractionandroid.data.GameSettings
import com.example.numberadditionsubtractionandroid.data.LeaderboardRepository
import com.example.numberadditionsubtractionandroid.data.VoiceMode
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

private val GameSettingsSaver = listSaver<GameSettings, String>(
    save = { settings ->
        listOf(
            settings.rounds.toString(),
            settings.maxCount.toString(),
            settings.voiceMode.name,
            settings.language.name,
            settings.gameMode.name
        )
    },
    restore = { saved ->
        val defaults = GameSettings()
        GameSettings(
            rounds = saved.getOrNull(0)?.toIntOrNull() ?: defaults.rounds,
            maxCount = saved.getOrNull(1)?.toIntOrNull() ?: defaults.maxCount,
            voiceMode = saved.getOrNull(2)?.let { runCatching { VoiceMode.valueOf(it) }.getOrNull() }
                ?: defaults.voiceMode,
            language = saved.getOrNull(3)?.let { runCatching { AppLanguage.valueOf(it) }.getOrNull() }
                ?: defaults.language,
            gameMode = saved.getOrNull(4)?.let { runCatching { GameMode.valueOf(it) }.getOrNull() }
                ?: defaults.gameMode
        )
    }
)

private val ResultGameUiStateSaver = listSaver<GameUiState, String>(
    save = { state ->
        listOf(
            state.currentRound.toString(),
            state.totalRounds.toString(),
            state.score.toString(),
            state.correctCount.toString(),
            state.wrongCount.toString(),
            state.isGameOver.toString(),
            state.showFeedback.toString()
        )
    },
    restore = { saved ->
        val defaults = GameUiState()
        GameUiState(
            currentRound = saved.getOrNull(0)?.toIntOrNull() ?: defaults.currentRound,
            totalRounds = saved.getOrNull(1)?.toIntOrNull() ?: defaults.totalRounds,
            score = saved.getOrNull(2)?.toIntOrNull() ?: defaults.score,
            correctCount = saved.getOrNull(3)?.toIntOrNull() ?: defaults.correctCount,
            wrongCount = saved.getOrNull(4)?.toIntOrNull() ?: defaults.wrongCount,
            isGameOver = saved.getOrNull(5)?.toBooleanStrictOrNull() ?: defaults.isGameOver,
            showFeedback = saved.getOrNull(6)?.toBooleanStrictOrNull() ?: defaults.showFeedback
        )
    }
)

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
                var screen by rememberSaveable { mutableStateOf(Screen.HOME) }
                var settings by rememberSaveable(stateSaver = GameSettingsSaver) { mutableStateOf(GameSettings()) }
                var lastGameState by rememberSaveable(stateSaver = ResultGameUiStateSaver) {
                    mutableStateOf(GameUiState())
                }
                var startupResolved by rememberSaveable { mutableStateOf(false) }
                val gameViewModel: GameViewModel = viewModel()
                val scope = rememberCoroutineScope()

                LaunchedEffect(Unit) {
                    if (startupResolved) return@LaunchedEffect

                    val hasSavedLanguage = appPreferencesRepository.hasSavedLanguage.first()
                    val savedLanguage = appPreferencesRepository.language.first()
                    settings = settings.copy(language = savedLanguage)
                    screen = if (hasSavedLanguage) Screen.HOME else Screen.LANGUAGE
                    startupResolved = true
                }

                LaunchedEffect(screen, settings.language, settings.voiceMode) {
                    if (screen == Screen.GAME) {
                        gameViewModel.updateSpeechCallbacks(
                            speakRoundCount = roundCountSpeaker(settings),
                            speakFeedback = feedbackSpeaker(settings)
                        )
                    }
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
                            gameViewModel.init(
                                gameSettings = gameSettings,
                                speakRoundCount = roundCountSpeaker(gameSettings),
                                speakFeedback = feedbackSpeaker(gameSettings)
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
                            gameViewModel.init(
                                gameSettings = settings,
                                speakRoundCount = roundCountSpeaker(settings),
                                speakFeedback = feedbackSpeaker(settings)
                            )
                            screen = Screen.GAME
                        },
                        onHome = { screen = Screen.HOME }
                    )
                }
            }
        }
    }

    private fun roundCountSpeaker(settings: GameSettings): (Int, Animal) -> Unit = { num, animal ->
        ttsManager.speakGameCount(num, animal, settings.language, settings.voiceMode)
    }

    private fun feedbackSpeaker(settings: GameSettings): (Boolean) -> Unit = { correct ->
        ttsManager.speakFeedback(correct, settings.language)
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsManager.shutdown()
    }
}