package com.example.numberadditionsubtractionandroid.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "leaderboard")

class LeaderboardRepository(private val context: Context) {

    private val LEADERBOARD_KEY = stringPreferencesKey("leaderboard_entries")

    val entries: Flow<List<LeaderboardEntry>> = context.dataStore.data.map { prefs ->
        val raw = prefs[LEADERBOARD_KEY] ?: return@map emptyList()
        parseEntries(raw)
    }

    suspend fun clearAll() {
        context.dataStore.edit { prefs ->
            prefs[LEADERBOARD_KEY] = ""
        }
    }

    suspend fun addEntry(entry: LeaderboardEntry) {
        context.dataStore.edit { prefs ->
            val raw = prefs[LEADERBOARD_KEY] ?: ""
            val current = parseEntries(raw).toMutableList()
            current.add(entry)
            current.sortByDescending { it.score }
            val top10 = current.take(10)
            prefs[LEADERBOARD_KEY] = serializeEntries(top10)
        }
    }

    private fun serializeEntries(entries: List<LeaderboardEntry>): String {
        return entries.joinToString("|") { e ->
            "${e.playerName}~${e.score}~${e.correctCount}~${e.wrongCount}~${e.date}"
        }
    }

    private fun parseEntries(raw: String): List<LeaderboardEntry> {
        if (raw.isBlank()) return emptyList()
        return raw.split("|").mapNotNull { line ->
            val parts = line.split("~")
            if (parts.size == 5) {
                LeaderboardEntry(
                    playerName = parts[0],
                    score = parts[1].toIntOrNull() ?: 0,
                    correctCount = parts[2].toIntOrNull() ?: 0,
                    wrongCount = parts[3].toIntOrNull() ?: 0,
                    date = parts[4]
                )
            } else null
        }
    }
}

