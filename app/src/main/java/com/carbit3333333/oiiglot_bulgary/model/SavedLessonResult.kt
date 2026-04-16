package com.carbit3333333.oiiglot_bulgary.model

data class SavedLessonResult(
    val lessonId: Int,
    val bestCorrectCount: Int,
    val bestWrongCount: Int,
    val bestScore: Float,
    val isPassed: Boolean
)