package com.carbit3333333.oiiglot_bulgary.data


import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.carbit3333333.oiiglot_bulgary.model.LessonProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProgressRepository(private val context: Context) {

    fun getLessonProgress(lessonId: Int): Flow<LessonProgress> {
        return context.progressDataStore.data.map { prefs ->
            LessonProgress(
                lessonId = lessonId,
                isUnlocked = prefs[isUnlockedKey(lessonId)] ?: (lessonId == 1),
                isCompleted = prefs[isCompletedKey(lessonId)] ?: false,
                bestScore = prefs[bestScoreKey(lessonId)] ?: 0
            )
        }
    }

    fun getAllLessonsProgress(totalLessons: Int): Flow<List<LessonProgress>> {
        return context.progressDataStore.data.map { prefs ->
            (1..totalLessons).map { lessonId ->
                LessonProgress(
                    lessonId = lessonId,
                    isUnlocked = prefs[isUnlockedKey(lessonId)] ?: (lessonId == 1),
                    isCompleted = prefs[isCompletedKey(lessonId)] ?: false,
                    bestScore = prefs[bestScoreKey(lessonId)] ?: 0
                )
            }
        }
    }

    suspend fun completeLesson(lessonId: Int, score: Int, totalLessons: Int) {
        context.progressDataStore.edit { prefs ->
            val currentBest = prefs[bestScoreKey(lessonId)] ?: 0

            prefs[isCompletedKey(lessonId)] = true

            if (score > currentBest) {
                prefs[bestScoreKey(lessonId)] = score
            }

            val nextLessonId = lessonId + 1
            if (nextLessonId <= totalLessons) {
                prefs[isUnlockedKey(nextLessonId)] = true
            }
        }
    }

    suspend fun unlockLesson(lessonId: Int) {
        context.progressDataStore.edit { prefs ->
            prefs[isUnlockedKey(lessonId)] = true
        }
    }

    suspend fun resetAllProgress(totalLessons: Int) {
        context.progressDataStore.edit { prefs ->
            for (lessonId in 1..totalLessons) {
                prefs.remove(isUnlockedKey(lessonId))
                prefs.remove(isCompletedKey(lessonId))
                prefs.remove(bestScoreKey(lessonId))
            }
        }
    }

    companion object {
        private fun isUnlockedKey(lessonId: Int) =
            booleanPreferencesKey("lesson_${lessonId}_unlocked")

        private fun isCompletedKey(lessonId: Int) =
            booleanPreferencesKey("lesson_${lessonId}_completed")

        private fun bestScoreKey(lessonId: Int) =
            intPreferencesKey("lesson_${lessonId}_best_score")
    }
}