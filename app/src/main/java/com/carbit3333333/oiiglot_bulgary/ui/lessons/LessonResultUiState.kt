package com.carbit3333333.oiiglot_bulgary.ui.lessons

import com.carbit3333333.oiiglot_bulgary.model.LessonResult

data class LessonResultUiState(
    val result: LessonResult? = null,
    val hasNextLesson: Boolean = false,
    val nextLessonId: Int? = null
)