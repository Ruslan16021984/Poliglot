package com.carbit3333333.oiiglot_bulgary.data.dictionary

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.difficultWordsDataStore by preferencesDataStore(name = "difficult_words")

class DifficultWordsStore(
    context: Context,
) {
    private val appContext = context.applicationContext

    val difficultWordIdsFlow: Flow<Set<Long>> =
        appContext.difficultWordsDataStore.data.map { preferences ->
            preferences[Keys.DIFFICULT_WORD_IDS].orEmpty().mapNotNull { it.toLongOrNull() }.toSet()
        }

    suspend fun markKnown(wordId: Long) {
        appContext.difficultWordsDataStore.edit { preferences ->
            val difficultIds = preferences[Keys.DIFFICULT_WORD_IDS].orEmpty().toMutableSet()
            val wordKey = wordId.toString()
            if (wordKey !in difficultIds) {
                return@edit
            }

            val streaks = preferences[Keys.KNOWN_STREAKS].orEmpty().toMutableMap()
            val nextStreak = (streaks[wordKey]?.toIntOrNull() ?: 0) + 1
            if (nextStreak >= KNOWN_STREAK_TO_CLEAR) {
                difficultIds.remove(wordKey)
                streaks.remove(wordKey)
            } else {
                streaks[wordKey] = nextStreak.toString()
            }
            preferences[Keys.DIFFICULT_WORD_IDS] = difficultIds
            preferences[Keys.KNOWN_STREAKS] = streaks.entries.map { "${it.key}:${it.value}" }.toSet()
        }
    }

    suspend fun markUnknown(wordId: Long) {
        appContext.difficultWordsDataStore.edit { preferences ->
            val difficultIds = preferences[Keys.DIFFICULT_WORD_IDS].orEmpty().toMutableSet()
            val wordKey = wordId.toString()
            difficultIds.add(wordKey)

            val streaks = preferences[Keys.KNOWN_STREAKS].orEmpty().toMutableMap()
            streaks.remove(wordKey)

            preferences[Keys.DIFFICULT_WORD_IDS] = difficultIds
            preferences[Keys.KNOWN_STREAKS] = streaks.entries.map { "${it.key}:${it.value}" }.toSet()
        }
    }

    private fun Set<String>.toMutableMap(): MutableMap<String, String> {
        return mapNotNull { entry ->
            val separatorIndex = entry.indexOf(':')
            if (separatorIndex <= 0 || separatorIndex >= entry.lastIndex) {
                null
            } else {
                entry.substring(0, separatorIndex) to entry.substring(separatorIndex + 1)
            }
        }.toMap().toMutableMap()
    }

    private object Keys {
        val DIFFICULT_WORD_IDS = stringSetPreferencesKey("difficult_word_ids")
        val KNOWN_STREAKS = stringSetPreferencesKey("known_streaks")
    }

    private companion object {
        const val KNOWN_STREAK_TO_CLEAR = 3
    }
}
