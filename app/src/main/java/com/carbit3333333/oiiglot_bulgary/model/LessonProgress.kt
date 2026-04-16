package com.carbit3333333.oiiglot_bulgary.model

data class LessonProgress(
    val lessonId: Int,
    val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false,
    val bestScore: Int = 0
)