package com.example.numberadditionsubtractionandroid.data

data class LeaderboardEntry(
    val playerName: String,
    val score: Int,
    val correctCount: Int,
    val wrongCount: Int,
    val date: String
)

