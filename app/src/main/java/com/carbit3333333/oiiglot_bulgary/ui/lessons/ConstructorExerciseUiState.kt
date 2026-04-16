package com.carbit3333333.oiiglot_bulgary.ui.lessons

data class ConstructorExerciseUiState(
    val lessonTitle: String = "",
    val progressText: String = "",
    val sourceText: String = "",
    val instruction: String = "",
    val selectedWords: List<String> = emptyList(),
    val availableWords: List<WordOption> = emptyList(),
    val correctAnswersCount: Int = 0,
    val wrongAnswersCount: Int = 0
)

data class WordOption(
    val id: Int,
    val text: String,
    val isUsed: Boolean = false
)