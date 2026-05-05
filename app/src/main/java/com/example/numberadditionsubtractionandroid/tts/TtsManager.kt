package com.example.numberadditionsubtractionandroid.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.numberadditionsubtractionandroid.data.Animal
import com.example.numberadditionsubtractionandroid.data.AppLanguage
import com.example.numberadditionsubtractionandroid.data.VoiceMode
import com.example.numberadditionsubtractionandroid.data.localizedName
import java.util.Locale

class TtsManager(context: Context) {

    private var tts: TextToSpeech? = null
    private var ready = false

    init {
        tts = TextToSpeech(context) { status ->
            ready = status == TextToSpeech.SUCCESS
        }
    }

    fun speakGameCount(number: Int, animal: Animal, lang: AppLanguage, voiceMode: VoiceMode) {
        when (voiceMode) {
            VoiceMode.NONE -> return
            VoiceMode.NUMBER -> speakText(number.toString(), lang)
            VoiceMode.NUMBER_WITH_ANIMAL -> speakText(countPhrase(number, animal, lang), lang)
        }
    }

    fun speakAnimalName(animal: Animal, lang: AppLanguage) {
        speakText(animal.localizedName(lang), lang)
    }

    fun speakText(text: String, lang: AppLanguage) {
        if (!ready) return
        val locale = localeFor(lang)
        tts?.language = locale
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun speakFeedback(correct: Boolean, lang: AppLanguage) {
        val text = when (lang) {
            AppLanguage.CHINESE -> if (correct) "答對了" else "不對哦"
            AppLanguage.ENGLISH -> if (correct) "Correct!" else "Wrong!"
            AppLanguage.JAPANESE -> if (correct) "せいかい" else "ざんねん"
        }
        speakText(text, lang)
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        ready = false
    }

    private fun localeFor(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> Locale.TRADITIONAL_CHINESE
        AppLanguage.ENGLISH -> Locale.ENGLISH
        AppLanguage.JAPANESE -> Locale.JAPANESE
    }

    private fun countPhrase(number: Int, animal: Animal, lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "$number 隻${animal.localizedName(lang)}"
        AppLanguage.ENGLISH -> "$number ${animal.localizedName(lang)}"
        AppLanguage.JAPANESE -> "$number ${animal.localizedName(lang)}"
    }
}

