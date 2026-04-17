package com.carbit3333333.oiiglot_bulgary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.carbit3333333.oiiglot_bulgary.data.LessonProgressStore
import com.carbit3333333.oiiglot_bulgary.data.LessonSessionRepository
import com.carbit3333333.oiiglot_bulgary.data.LessonSessionStore
import com.carbit3333333.oiiglot_bulgary.model.ExerciseResult
import com.carbit3333333.oiiglot_bulgary.model.LessonResult
import com.carbit3333333.oiiglot_bulgary.ui.lessons.LessonSessionUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LessonSessionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private var currentLessonId: Int = 0
    private val repository = LessonSessionRepository()
    private val progressStore = LessonProgressStore(application)
    private val sessionStore = LessonSessionStore(application)

    private var resultSaved = false

    private val praises = listOf(
        "Браво!",
        "Супер!",
        "Чудесно!",
        "Отлично!",
        "Талант!"
    )

    private val _uiState = MutableStateFlow(LessonSessionUiState())
    val uiState: StateFlow<LessonSessionUiState> = _uiState.asStateFlow()

    fun loadLessonSession(lessonId: Int) {
        viewModelScope.launch {
            currentLessonId = lessonId
            resultSaved = false

            val savedState = sessionStore.loadSession(lessonId)
            if (savedState != null) {
                _uiState.value = savedState
                return@launch
            }

            val session = repository.getLessonSession(lessonId)

            val newState = LessonSessionUiState(
                lessonTitle = session.lessonTitle,
                exercises = session.exercises,
                results = List(session.exercises.size) { ExerciseResult.NONE }
            )

            _uiState.value = newState
            saveCurrentSession()
        }
    }

    fun selectWord(word: String) {
        val state = _uiState.value
        if (state.currentResult != ExerciseResult.NONE) return

        _uiState.value = state.copy(
            selectedWords = state.selectedWords + word
        )
        saveCurrentSession()
    }

    fun removeSelectedWord(word: String) {
        val state = _uiState.value
        if (state.currentResult != ExerciseResult.NONE) return

        val mutable = state.selectedWords.toMutableList()
        mutable.remove(word)

        _uiState.value = state.copy(selectedWords = mutable)
        saveCurrentSession()
    }

    fun checkAnswer() {
        val state = _uiState.value
        val exercise = state.currentExercise ?: return

        val isCorrect = state.selectedWords == exercise.correctAnswerWords
        val newResults = state.results.toMutableList()

        if (isCorrect) {
            newResults[state.currentExerciseIndex] = ExerciseResult.CORRECT

            _uiState.value = state.copy(
                results = newResults,
                currentResult = ExerciseResult.CORRECT,
                correctCount = state.correctCount + 1,
                praiseText = praises.random()
            )
            saveCurrentSession()

            viewModelScope.launch {
                delay(1800)
                moveToNextExercise()
            }
        } else {
            newResults[state.currentExerciseIndex] = ExerciseResult.WRONG

            _uiState.value = state.copy(
                results = newResults,
                currentResult = ExerciseResult.WRONG,
                wrongCount = state.wrongCount + 1
            )
            saveCurrentSession()
        }
    }

    fun onWrongAnswerScreenTap() {
        val state = _uiState.value
        if (state.currentResult != ExerciseResult.WRONG) return
        moveToNextExercise()
    }

    private fun moveToNextExercise() {
        val state = _uiState.value
        val nextIndex = state.currentExerciseIndex + 1
        val isFinished = nextIndex >= state.exercises.size

        if (isFinished) {
            val totalExercises = state.exercises.size
            val correctCount = state.correctCount
            val wrongCount = state.wrongCount
            val score = if (totalExercises > 0) {
                (correctCount.toFloat() / totalExercises.toFloat()) * 10f
            } else {
                0f
            }
            val isPassed = score >= 4.5f

            val lessonResult = LessonResult(
                lessonId = currentLessonId,
                lessonTitle = state.lessonTitle,
                totalExercises = totalExercises,
                correctCount = correctCount,
                wrongCount = wrongCount,
                score = score,
                isPassed = isPassed
            )

            saveProgressOnce(
                lessonId = currentLessonId,
                correctCount = correctCount,
                wrongCount = wrongCount,
                score = score,
                isPassed = isPassed
            )

            _uiState.value = state.copy(
                selectedWords = emptyList(),
                currentResult = ExerciseResult.NONE,
                praiseText = null,
                isLessonFinished = true,
                lessonResult = lessonResult
            )

            viewModelScope.launch {
                sessionStore.clearSession()
            }
        } else {
            _uiState.value = state.copy(
                currentExerciseIndex = nextIndex,
                selectedWords = emptyList(),
                currentResult = ExerciseResult.NONE,
                praiseText = null
            )
            saveCurrentSession()
        }
    }

    private fun saveProgressOnce(
        lessonId: Int,
        correctCount: Int,
        wrongCount: Int,
        score: Float,
        isPassed: Boolean
    ) {
        if (resultSaved) return
        resultSaved = true

        viewModelScope.launch {
            progressStore.saveLessonResult(
                lessonId = lessonId,
                correctCount = correctCount,
                wrongCount = wrongCount,
                score = score,
                isPassed = isPassed
            )

            if (isPassed) {
                progressStore.unlockNextLesson(lessonId + 1)
            }
        }
    }

    private fun saveCurrentSession() {
        if (currentLessonId == 0) return

        viewModelScope.launch {
            sessionStore.saveSession(currentLessonId, _uiState.value)
        }
    }
}