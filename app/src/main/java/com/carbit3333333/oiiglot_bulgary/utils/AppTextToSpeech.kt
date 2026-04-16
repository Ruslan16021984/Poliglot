package com.carbit3333333.oiiglot_bulgary.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class AppTextToSpeech(context: Context) {

    private var textToSpeech: TextToSpeech? = null
    private var isReady: Boolean = false

    init {
        textToSpeech = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale("bg", "BG"))

                isReady = result != TextToSpeech.LANG_MISSING_DATA &&
                        result != TextToSpeech.LANG_NOT_SUPPORTED

                if (isReady) {
                    textToSpeech?.setSpeechRate(0.9f)
                }
            } else {
                isReady = false
            }
        }
    }

    fun speak(text: String) {
        if (!isReady || text.isBlank()) return

        textToSpeech?.stop()
        textToSpeech?.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "lesson_tts_${System.currentTimeMillis()}"
        )
    }

    fun shutdown() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
    }
}