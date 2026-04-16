package com.carbit3333333.oiiglot_bulgary.ui.lessons

import com.carbit3333333.oiiglot_bulgary.model.Lesson

data class LessonUiState(
    val isLoading: Boolean = false,
    val lesson: Lesson? = null,
    val errorMessage: String? = null
)