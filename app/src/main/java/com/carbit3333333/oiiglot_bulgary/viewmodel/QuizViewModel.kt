package com.carbit3333333.oiiglot_bulgary.viewmodel

import androidx.lifecycle.ViewModel
import com.carbit3333333.oiiglot_bulgary.data.LessonRepository
import com.carbit3333333.oiiglot_bulgary.ui.lessons.QuizUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuizViewModel : ViewModel() {

    private val repository = LessonRepository()

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    fun loadQuiz(lessonId: Int) {
        val lesson = repository.getLessonById(lessonId)

        _uiState.value = if (lesson != null) {
            QuizUiState(
                lessonId = lessonId,
                questions = lesson.questions
            )
        } else {
            QuizUiState(errorMessage = "Урок не найден")
        }
    }

    fun selectAnswer(answer: String) {
        _uiState.value = _uiState.value.copy(selectedAnswer = answer)
    }

    fun submitAnswer() {
        val state = _uiState.value
        val currentQuestion = state.currentQuestion ?: return
        val selectedAnswer = state.selectedAnswer ?: return

        val isCorrect = selectedAnswer == currentQuestion.correctAnswer
        val newCorrectAnswersCount = if (isCorrect) {
            state.correctAnswersCount + 1
        } else {
            state.correctAnswersCount
        }

        val nextIndex = state.currentQuestionIndex + 1
        val isCompleted = nextIndex >= state.questions.size

        _uiState.value = if (isCompleted) {
            state.copy(
                correctAnswersCount = newCorrectAnswersCount,
                isCompleted = true,
                selectedAnswer = null
            )
        } else {
            state.copy(
                currentQuestionIndex = nextIndex,
                correctAnswersCount = newCorrectAnswersCount,
                selectedAnswer = null
            )
        }
    }

    fun restartQuiz() {
        val state = _uiState.value
        _uiState.value = state.copy(
            currentQuestionIndex = 0,
            selectedAnswer = null,
            correctAnswersCount = 0,
            isCompleted = false
        )
    }
}