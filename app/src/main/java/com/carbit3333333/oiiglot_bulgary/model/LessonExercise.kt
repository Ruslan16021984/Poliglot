package com.carbit3333333.oiiglot_bulgary.model


data class LessonExercise(
    val id: Int,
    val sourceText: String,
    val instruction: String,
    val correctAnswerWords: List<String>,
    val availableWords: List<String>
)
