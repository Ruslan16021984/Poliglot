package com.carbit3333333.oiiglot_bulgary.model

data class Exercise(
    val id: Int,
    val type: ExerciseType,
    val sourceText: String,
    val instruction: String,
    val correctAnswerWords: List<String>,
    val availableWords: List<String>
)

enum class ExerciseType {
    MULTIPLE_CHOICE,
    WORD_CONSTRUCTOR
}