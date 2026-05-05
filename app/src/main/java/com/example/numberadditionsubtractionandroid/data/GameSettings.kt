package com.example.numberadditionsubtractionandroid.data

enum class GameMode { ADDITION, SUBTRACTION, MIXED }

enum class VoiceMode { NONE, NUMBER, NUMBER_WITH_ANIMAL }

data class GameSettings(
    val rounds: Int = 5,
    val maxCount: Int = 10,
    val voiceMode: VoiceMode = VoiceMode.NUMBER,
    val language: AppLanguage = AppLanguage.CHINESE,
    val gameMode: GameMode = GameMode.ADDITION
)

