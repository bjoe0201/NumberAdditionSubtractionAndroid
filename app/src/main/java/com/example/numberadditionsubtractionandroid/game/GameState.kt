package com.example.numberadditionsubtractionandroid.game

import com.example.numberadditionsubtractionandroid.data.Animal
import com.example.numberadditionsubtractionandroid.data.GameMode

data class RoundState(
    val animal: Animal,
    val num1: Int,
    val num2: Int,
    val operator: GameMode,
    val animals1: List<Animal>,
    val animals2: List<Animal>,
    val correctAnswer: Int,
    val answers: List<Int>,
    val tappedIndices1: Set<Int> = emptySet(),
    val tappedIndices2: Set<Int> = emptySet(),
    val crossedOutIndices: Set<Int> = emptySet(),
    val selectedAnswer: Int? = null,
    val isCorrect: Boolean? = null
)

data class GameUiState(
    val currentRound: Int = 0,
    val totalRounds: Int = 5,
    val round: RoundState? = null,
    val score: Int = 0,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val isGameOver: Boolean = false,
    val showFeedback: Boolean = false
)

