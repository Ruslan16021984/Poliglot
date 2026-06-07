package com.carbit3333333.oiiglot_bulgary.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.carbit3333333.oiiglot_bulgary.model.ExerciseResult
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise
import com.carbit3333333.oiiglot_bulgary.model.LessonSession
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
        val SESSION_VERSION = intPreferencesKey("session_version")
        val SESSION_JSON = stringPreferencesKey("session_json")
    }

    private val json = Json {
        ignoreUnknownKeys = true
    }

    suspend fun saveSession(lessonId: Int, state: LessonSessionUiState) {
        saveSavedSession(lessonId, state.toSavedLessonSession(lessonId))
    }

    suspend fun saveSessionSnapshot(lessonId: Int, state: LessonSessionUiState) {
        saveSavedSession(lessonId, state.toSavedLessonSession(lessonId))
    }

    private suspend fun saveSavedSession(lessonId: Int, saved: SavedLessonSession) {
        context.sessionDataStore.edit { prefs ->
            prefs[Keys.LESSON_ID] = lessonId
            prefs[Keys.SESSION_VERSION] = CURRENT_SESSION_VERSION
            prefs[Keys.SESSION_JSON] = json.encodeToString(saved)
        }
    }

    private fun LessonSessionUiState.toSavedLessonSession(lessonId: Int): SavedLessonSession {
        return SavedLessonSession(
            lessonId = lessonId,
            lessonTitle = lessonTitle,
            currentExerciseIndex = currentExerciseIndex,
            selectedWords = selectedWords,
            results = results.map { it.name },
            correctCount = correctCount,
            wrongCount = wrongCount,
            exercises = exercises.map {
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
    }

    suspend fun loadSession(lessonId: Int): LessonSessionUiState? {
        val prefs = context.sessionDataStore.data.first()
        val savedLessonId = prefs[Keys.LESSON_ID] ?: return null
        if (savedLessonId != lessonId) return null
        val savedSessionVersion = prefs[Keys.SESSION_VERSION] ?: return null
        if (savedSessionVersion != CURRENT_SESSION_VERSION) return null

        val sessionJson = prefs[Keys.SESSION_JSON] ?: return null
        val saved = runCatching {
            json.decodeFromString<SavedLessonSession>(sessionJson)
        }.getOrNull() ?: return null

        return saved.toUiState().takeIf(::isRestorableLessonSessionState)
    }

    suspend fun clearSession() {
        context.sessionDataStore.edit { prefs ->
            prefs.remove(Keys.LESSON_ID)
            prefs.remove(Keys.SESSION_VERSION)
            prefs.remove(Keys.SESSION_JSON)
        }
    }

    private fun SavedLessonSession.toUiState(): LessonSessionUiState {
        return LessonSessionUiState(
            lessonTitle = lessonTitle,
            exercises = exercises.map {
                LessonExercise(
                    id = it.id,
                    sourceText = it.sourceText,
                    instruction = it.instruction,
                    correctAnswerWords = it.correctAnswerWords,
                    availableWords = it.availableWords,
                    hint = it.hint
                )
            },
            currentExerciseIndex = currentExerciseIndex,
            // Start resumed exercises from a clean choice grid so the user always sees all 8 options first.
            selectedWords = emptyList(),
            results = results.map { ExerciseResult.valueOf(it) },
            correctCount = correctCount,
            wrongCount = wrongCount,
            currentResult = ExerciseResult.NONE,
            praiseText = null,
            isLessonFinished = false,
            lessonResult = null
        )
    }

    private companion object {
        const val CURRENT_SESSION_VERSION = 2
    }
}

internal fun isRestorableLessonSessionState(state: LessonSessionUiState): Boolean {
    if (state.lessonTitle.isBlank()) return false
    if (state.exercises.isEmpty()) return false
    if (state.isLessonFinished) return false
    if (state.lessonResult != null) return false
    if (state.currentExerciseIndex !in state.exercises.indices) return false
    if (state.results.size != state.exercises.size) return false

    return state.exercises.all { exercise ->
        exercise.sourceText.isNotBlank() &&
            exercise.instruction.isNotBlank() &&
            exercise.correctAnswerWords.isNotEmpty() &&
            exercise.availableWords.size == 8 &&
            exercise.correctAnswerWords.all { it in exercise.availableWords }
    }
}

internal fun relocalizeRestoredLessonSessionState(
    savedState: LessonSessionUiState,
    localizedSession: LessonSession,
): LessonSessionUiState {
    val localizedExercisesById = localizedSession.exercises.associateBy { it.id }
    return savedState.copy(
        lessonTitle = localizedSession.lessonTitle,
        exercises = savedState.exercises.map { savedExercise ->
            val localizedExercise = localizedExercisesById[savedExercise.id] ?: return@map savedExercise
            savedExercise.copy(
                sourceText = localizedExercise.sourceText,
                instruction = localizedExercise.instruction,
                hint = localizedExercise.hint,
            )
        },
    )
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
