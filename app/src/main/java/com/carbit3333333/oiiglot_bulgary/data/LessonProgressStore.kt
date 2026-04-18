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

    suspend fun unlockAllLessons(maxLessonId: Int = 20) {
        context.lessonProgressDataStore.edit { preferences ->
            preferences[OPENED_LESSON_ID] = maxLessonId
        }
    }

    suspend fun resetLessonUnlocks(maxLessonId: Int = 20) {
        context.lessonProgressDataStore.edit { preferences ->
            preferences[OPENED_LESSON_ID] = 1

            for (lessonId in 1..maxLessonId) {
                val bestCorrectKey = intPreferencesKey("lesson_${lessonId}_best_correct")
                val bestWrongKey = intPreferencesKey("lesson_${lessonId}_best_wrong")
                val bestScoreKey = floatPreferencesKey("lesson_${lessonId}_best_score")
                val isPassedKey = booleanPreferencesKey("lesson_${lessonId}_is_passed")
                val currentStepKey = intPreferencesKey("lesson_${lessonId}_current_step")
                val totalStepsKey = intPreferencesKey("lesson_${lessonId}_total_steps")

                preferences.remove(bestCorrectKey)
                preferences.remove(bestWrongKey)
                preferences.remove(bestScoreKey)
                preferences.remove(isPassedKey)
                preferences.remove(currentStepKey)
                preferences.remove(totalStepsKey)
            }
        }
    }

    suspend fun saveLessonProgress(
        lessonId: Int,
        currentStep: Int,
        totalSteps: Int
    ) {
        val currentStepKey = intPreferencesKey("lesson_${lessonId}_current_step")
        val totalStepsKey = intPreferencesKey("lesson_${lessonId}_total_steps")

        context.lessonProgressDataStore.edit { preferences ->
            preferences[currentStepKey] = currentStep
            preferences[totalStepsKey] = totalSteps
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

    fun getLessonResultsFlow(lessonIds: List<Int>): Flow<Map<Int, SavedLessonResult>> {
        return context.lessonProgressDataStore.data.map { preferences ->
            lessonIds.mapNotNull { lessonId ->
                val bestCorrectKey = intPreferencesKey("lesson_${lessonId}_best_correct")
                val bestWrongKey = intPreferencesKey("lesson_${lessonId}_best_wrong")
                val bestScoreKey = floatPreferencesKey("lesson_${lessonId}_best_score")
                val isPassedKey = booleanPreferencesKey("lesson_${lessonId}_is_passed")
                val currentStepKey = intPreferencesKey("lesson_${lessonId}_current_step")
                val totalStepsKey = intPreferencesKey("lesson_${lessonId}_total_steps")

                val bestScore = preferences[bestScoreKey]
                val currentStep = preferences[currentStepKey] ?: 0
                val totalSteps = preferences[totalStepsKey] ?: 0
                val isPassed = preferences[isPassedKey] ?: false

                val hasAnyData =
                    bestScore != null || currentStep > 0 || totalSteps > 0 || isPassed

                if (!hasAnyData) {
                    return@mapNotNull null
                }

                lessonId to SavedLessonResult(
                    lessonId = lessonId,
                    bestCorrectCount = preferences[bestCorrectKey] ?: 0,
                    bestWrongCount = preferences[bestWrongKey] ?: 0,
                    bestScore = bestScore ?: 0f,
                    isPassed = isPassed,
                    currentStep = currentStep,
                    totalSteps = totalSteps
                )
            }.toMap()
        }
    }
}