package com.carbit3333333.oiiglot_bulgary.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.carbit3333333.oiiglot_bulgary.model.SavedLessonResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.lessonProgressDataStore by preferencesDataStore(name = "lesson_progress")

class LessonProgressStore(
    private val context: Context
) {

    companion object {
        private val OPENED_LESSON_ID = intPreferencesKey("opened_lesson_id")
    }

    val openedLessonIdFlow: Flow<Int> = context.lessonProgressDataStore.data.map { preferences ->
        preferences[OPENED_LESSON_ID] ?: 1
    }

    suspend fun unlockNextLesson(nextLessonId: Int) {
        context.lessonProgressDataStore.edit { preferences ->
            val currentOpened = preferences[OPENED_LESSON_ID] ?: 1
            if (nextLessonId > currentOpened) {
                preferences[OPENED_LESSON_ID] = nextLessonId
            }
        }
    }

    suspend fun saveLessonResult(
        lessonId: Int,
        correctCount: Int,
        wrongCount: Int,
        score: Float,
        isPassed: Boolean
    ) {
        val bestCorrectKey = intPreferencesKey("lesson_${lessonId}_best_correct")
        val bestWrongKey = intPreferencesKey("lesson_${lessonId}_best_wrong")
        val bestScoreKey = floatPreferencesKey("lesson_${lessonId}_best_score")
        val isPassedKey = booleanPreferencesKey("lesson_${lessonId}_is_passed")

        context.lessonProgressDataStore.edit { preferences ->
            val oldBestScore = preferences[bestScoreKey] ?: 0f

            if (score >= oldBestScore) {
                preferences[bestCorrectKey] = correctCount
                preferences[bestWrongKey] = wrongCount
                preferences[bestScoreKey] = score
            }

            if (isPassed) {
                preferences[isPassedKey] = true
            } else {
                val oldPassed = preferences[isPassedKey] ?: false
                if (!oldPassed) {
                    preferences[isPassedKey] = false
                }
            }
        }
    }

    fun getLessonResultFlow(lessonId: Int): Flow<SavedLessonResult?> {
        val bestCorrectKey = intPreferencesKey("lesson_${lessonId}_best_correct")
        val bestWrongKey = intPreferencesKey("lesson_${lessonId}_best_wrong")
        val bestScoreKey = floatPreferencesKey("lesson_${lessonId}_best_score")
        val isPassedKey = booleanPreferencesKey("lesson_${lessonId}_is_passed")

        return context.lessonProgressDataStore.data.map { preferences ->
            val bestScore = preferences[bestScoreKey] ?: return@map null

            SavedLessonResult(
                lessonId = lessonId,
                bestCorrectCount = preferences[bestCorrectKey] ?: 0,
                bestWrongCount = preferences[bestWrongKey] ?: 0,
                bestScore = bestScore,
                isPassed = preferences[isPassedKey] ?: false
            )
        }
    }
    fun getLessonResultsFlow(lessonIds: List<Int>): Flow<Map<Int, SavedLessonResult>> {
        return context.lessonProgressDataStore.data.map { preferences ->
            lessonIds.mapNotNull { lessonId ->
                val bestCorrectKey = intPreferencesKey("lesson_${lessonId}_best_correct")
                val bestWrongKey = intPreferencesKey("lesson_${lessonId}_best_wrong")
                val bestScoreKey = floatPreferencesKey("lesson_${lessonId}_best_score")
                val isPassedKey = booleanPreferencesKey("lesson_${lessonId}_is_passed")

                val bestScore = preferences[bestScoreKey] ?: return@mapNotNull null

                lessonId to SavedLessonResult(
                    lessonId = lessonId,
                    bestCorrectCount = preferences[bestCorrectKey] ?: 0,
                    bestWrongCount = preferences[bestWrongKey] ?: 0,
                    bestScore = bestScore,
                    isPassed = preferences[isPassedKey] ?: false
                )
            }.toMap()
        }
    }
}