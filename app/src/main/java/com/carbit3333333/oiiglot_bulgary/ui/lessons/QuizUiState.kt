package com.carbit3333333.oiiglot_bulgary.ui.lessons

import com.carbit3333333.oiiglot_bulgary.model.Question

data class QuizUiState(
    val isLoading: Boolean = false,
    val lessonId: Int = 0,
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswer: String? = null,
    val correctAnswersCount: Int = 0,
    val isCompleted: Boolean = false,
    val errorMessage: String? = null
) {
    val currentQuestion: Question?
        get() = questions.getOrNull(currentQuestionIndex)
}