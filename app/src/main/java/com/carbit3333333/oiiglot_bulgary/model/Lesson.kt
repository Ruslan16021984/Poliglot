package com.carbit3333333.oiiglot_bulgary.model

data class Lesson(
    val id: Int,
    val title: String,
    val subtitle: String,
    val theory: List<TheoryBlock> = emptyList(),
    val phrases: List<Phrase>,
    val questions: List<Question>,
    val isCompleted: Boolean = false,
    val isLocked: Boolean = false,
    val bestScore: Float? = null,
)

data class Phrase(
    val bulgarian: String,
    val russian: String
)

data class Question(
    val id: Int,
    val text: String,
    val options: List<String>,
    val correctAnswer: String
)