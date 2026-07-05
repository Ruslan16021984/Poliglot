package com.carbit3333333.oiiglot_bulgary.ui.lessons

import com.carbit3333333.oiiglot_bulgary.model.ExerciseResult
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise
import com.carbit3333333.oiiglot_bulgary.model.LessonResult

data class LessonSessionUiState(
    val lessonTitle: String = "",
    val exercises: List<LessonExercise> = emptyList(),
    val currentExerciseIndex: Int = 0,
    val selectedWords: List<String> = emptyList(),
    val results: List<ExerciseResult> = emptyList(),
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val currentResult: ExerciseResult = ExerciseResult.NONE,
    val retryUsedForCurrentExercise: Boolean = false,
    val pendingRetryAfterWrong: Boolean = false,
    val praiseText: String? = null,
    val isLessonFinished: Boolean = false,
    val lessonResult: LessonResult? = null
) {
    val currentExercise: LessonExercise?
        get() = exercises.getOrNull(currentExerciseIndex)
}
