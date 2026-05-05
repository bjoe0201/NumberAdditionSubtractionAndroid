package com.example.numberadditionsubtractionandroid.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.numberadditionsubtractionandroid.data.ALL_ANIMALS
import com.example.numberadditionsubtractionandroid.data.Animal
import com.example.numberadditionsubtractionandroid.data.GameMode
import com.example.numberadditionsubtractionandroid.data.GameSettings
import com.example.numberadditionsubtractionandroid.data.VoiceMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.random.Random

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private lateinit var settings: GameSettings
    private var onSpeakRoundCount: ((Int, Animal) -> Unit)? = null
    private var onSpeakFeedback: ((Boolean) -> Unit)? = null

    fun init(
        gameSettings: GameSettings,
        speakRoundCount: (Int, Animal) -> Unit,
        speakFeedback: (Boolean) -> Unit
    ) {
        settings = gameSettings
        onSpeakRoundCount = speakRoundCount
        onSpeakFeedback = speakFeedback
        _uiState.value = GameUiState(totalRounds = gameSettings.rounds)
        startNextRound()
    }

    fun updateSpeechCallbacks(
        speakRoundCount: (Int, Animal) -> Unit,
        speakFeedback: (Boolean) -> Unit
    ) {
        onSpeakRoundCount = speakRoundCount
        onSpeakFeedback = speakFeedback
    }

    private fun startNextRound() {
        val state = _uiState.value
        if (state.currentRound >= state.totalRounds) {
            _uiState.value = state.copy(isGameOver = true)
            return
        }

        val operator = when (settings.gameMode) {
            GameMode.ADDITION -> GameMode.ADDITION
            GameMode.SUBTRACTION -> GameMode.SUBTRACTION
            GameMode.MIXED -> if (Random.nextBoolean()) GameMode.ADDITION else GameMode.SUBTRACTION
        }

        val animal = ALL_ANIMALS.random()
        val num1: Int
        val num2: Int
        val correctAnswer: Int

        when (operator) {
            GameMode.ADDITION -> {
                num1 = Random.nextInt(1, (settings.maxCount / 2).coerceAtLeast(2) + 1)
                num2 = Random.nextInt(1, (settings.maxCount - num1).coerceAtLeast(1) + 1)
                correctAnswer = num1 + num2
            }
            GameMode.SUBTRACTION -> {
                num1 = Random.nextInt(2, settings.maxCount.coerceAtLeast(3) + 1)
                num2 = Random.nextInt(1, num1)
                correctAnswer = num1 - num2
            }
            else -> throw IllegalStateException()
        }

        val animals1 = List(num1) { animal }
        val animals2 = if (operator == GameMode.ADDITION) List(num2) { animal } else emptyList()
        val answers = generateAnswers(correctAnswer, settings.maxCount)

        _uiState.value = state.copy(
            currentRound = state.currentRound + 1,
            round = RoundState(
                animal = animal,
                num1 = num1,
                num2 = num2,
                operator = operator,
                animals1 = animals1,
                animals2 = animals2,
                correctAnswer = correctAnswer,
                answers = answers
            ),
            showFeedback = false
        )
    }

    fun tapAnimalGroup1(index: Int) {
        val round = _uiState.value.round ?: return
        if (round.selectedAnswer != null) return
        if (index in round.tappedIndices1) {
            speakCurrentCount(currentAdditionCount(round), round.animal)
            return
        }
        val newTapped = round.tappedIndices1 + index
        val newRound = round.copy(tappedIndices1 = newTapped)
        _uiState.value = _uiState.value.copy(round = newRound)
        speakCurrentCount(newTapped.size + round.tappedIndices2.size, round.animal)
    }

    fun tapAnimalGroup2(index: Int) {
        val round = _uiState.value.round ?: return
        if (round.selectedAnswer != null) return
        if (index in round.tappedIndices2) {
            speakCurrentCount(currentAdditionCount(round), round.animal)
            return
        }
        val newTapped = round.tappedIndices2 + index
        val newRound = round.copy(tappedIndices2 = newTapped)
        _uiState.value = _uiState.value.copy(round = newRound)
        speakCurrentCount(round.tappedIndices1.size + newTapped.size, round.animal)
    }

    fun crossOutAnimal(index: Int) {
        val round = _uiState.value.round ?: return
        if (round.selectedAnswer != null) return
        if (index in round.crossedOutIndices) {
            speakCurrentCount(currentRemainingCount(round), round.animal)
            return
        }
        if (round.crossedOutIndices.size >= round.num2) return
        val newCrossed = round.crossedOutIndices + index
        val newRound = round.copy(crossedOutIndices = newCrossed)
        _uiState.value = _uiState.value.copy(round = newRound)
        speakCurrentCount(round.num1 - newCrossed.size, round.animal)
    }

    fun selectAnswer(answer: Int) {
        val state = _uiState.value
        val round = state.round ?: return
        if (round.selectedAnswer != null) return

        val isCorrect = answer == round.correctAnswer
        val newRound = round.copy(selectedAnswer = answer, isCorrect = isCorrect)
        val newCorrect = if (isCorrect) state.correctCount + 1 else state.correctCount
        val newWrong = if (!isCorrect) state.wrongCount + 1 else state.wrongCount
        val newScore = max(0, settings.rounds * settings.maxCount * (newCorrect - newWrong))

        _uiState.value = state.copy(
            round = newRound,
            correctCount = newCorrect,
            wrongCount = newWrong,
            score = newScore,
            showFeedback = true
        )

        viewModelScope.launch {
            if (settings.voiceMode != VoiceMode.NONE) {
                onSpeakRoundCount?.invoke(answer, round.animal)
                delay(900)
                onSpeakFeedback?.invoke(isCorrect)
            }
            delay(1500)
            startNextRound()
        }
    }

    private fun generateAnswers(correct: Int, maxCount: Int): List<Int> {
        val distractors = mutableSetOf<Int>()
        var attempts = 0
        while (distractors.size < 4 && attempts < 200) {
            attempts++
            val delta = if (maxCount <= 20) Random.nextInt(1, 6) else max(1, (maxCount * 0.1).toInt())
            val sign = if (Random.nextBoolean()) 1 else -1
            val candidate = correct + sign * delta
            if (candidate > 0 && candidate != correct && candidate <= maxCount + delta) {
                distractors.add(candidate)
            }
        }
        var fill = 1
        while (distractors.size < 4) {
            if (fill != correct && !distractors.contains(fill)) distractors.add(fill)
            fill++
        }
        return (distractors.take(4) + correct).shuffled()
    }

    private fun currentAdditionCount(round: RoundState): Int {
        return round.tappedIndices1.size + round.tappedIndices2.size
    }

    private fun currentRemainingCount(round: RoundState): Int {
        return round.num1 - round.crossedOutIndices.size
    }

    private fun speakCurrentCount(count: Int, animal: Animal) {
        if (settings.voiceMode == VoiceMode.NONE) return
        onSpeakRoundCount?.invoke(count, animal)
    }
}

