package com.carbit3333333.oiiglot_bulgary.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.carbit3333333.oiiglot_bulgary.model.ExerciseResult
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise
import com.carbit3333333.oiiglot_bulgary.ui.lessons.LessonSessionUiState
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.sessionDataStore by preferencesDataStore(name = "lesson_session_store")

class LessonSessionStore(private val context: Context) {

    private object Keys {
        val LESSON_ID = intPreferencesKey("lesson_id")
        val SESSION_JSON = stringPreferencesKey("session_json")
    }

    private val json = Json {
        ignoreUnknownKeys = true
    }

    suspend fun saveSession(lessonId: Int, state: LessonSessionUiState) {
        val saved = SavedLessonSession(
            lessonId = lessonId,
            lessonTitle = state.lessonTitle,
            currentExerciseIndex = state.currentExerciseIndex,
            selectedWords = state.selectedWords,
            results = state.results.map { it.name },
            correctCount = state.correctCount,
            wrongCount = state.wrongCount,
            exercises = state.exercises.map {
                SavedLessonExercise(
                    id = it.id,
                    sourceText = it.sourceText,
                    instruction = it.instruction,
                    correctAnswerWords = it.correctAnswerWords,
                    availableWords = it.availableWords,
                    hint = it.hint
                )
            }
        )

        context.sessionDataStore.edit { prefs ->
            prefs[Keys.LESSON_ID] = lessonId
            prefs[Keys.SESSION_JSON] = json.encodeToString(saved)
        }
    }

    suspend fun loadSession(lessonId: Int): LessonSessionUiState? {
        val prefs = context.sessionDataStore.data.first()
        val savedLessonId = prefs[Keys.LESSON_ID] ?: return null
        if (savedLessonId != lessonId) return null

        val sessionJson = prefs[Keys.SESSION_JSON] ?: return null
        val saved = json.decodeFromString<SavedLessonSession>(sessionJson)

        return LessonSessionUiState(
            lessonTitle = saved.lessonTitle,
            exercises = saved.exercises.map {
                LessonExercise(
                    id = it.id,
                    sourceText = it.sourceText,
                    instruction = it.instruction,
                    correctAnswerWords = it.correctAnswerWords,
                    availableWords = it.availableWords,
                    hint = it.hint
                )
            },
            currentExerciseIndex = saved.currentExerciseIndex,
            selectedWords = saved.selectedWords,
            results = saved.results.map { ExerciseResult.valueOf(it) },
            correctCount = saved.correctCount,
            wrongCount = saved.wrongCount
        )
    }

    suspend fun clearSession() {
        context.sessionDataStore.edit { prefs ->
            prefs.remove(Keys.LESSON_ID)
            prefs.remove(Keys.SESSION_JSON)
        }
    }
}

@Serializable
data class SavedLessonSession(
    val lessonId: Int,
    val lessonTitle: String,
    val currentExerciseIndex: Int,
    val selectedWords: List<String>,
    val results: List<String>,
    val correctCount: Int,
    val wrongCount: Int,
    val exercises: List<SavedLessonExercise>
)

@Serializable
data class SavedLessonExercise(
    val id: Int,
    val sourceText: String,
    val instruction: String,
    val correctAnswerWords: List<String>,
    val availableWords: List<String>,
    val hint: String? = null
)