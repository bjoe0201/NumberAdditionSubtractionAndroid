package com.example.numberadditionsubtractionandroid.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class AppPreferencesRepository(private val context: Context) {

    private val LANGUAGE_KEY = stringPreferencesKey("language")

    val hasSavedLanguage: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[LANGUAGE_KEY] != null
    }

    val language: Flow<AppLanguage> = context.dataStore.data.map { preferences ->
        val raw = preferences[LANGUAGE_KEY]
        raw?.let { value ->
            AppLanguage.entries.find { it.name == value }
        } ?: AppLanguage.CHINESE
    }

    suspend fun saveLanguage(language: AppLanguage) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language.name
        }
    }
}

