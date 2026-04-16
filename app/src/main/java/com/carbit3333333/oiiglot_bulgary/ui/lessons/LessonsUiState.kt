package com.carbit3333333.oiiglot_bulgary.ui.lessons

import com.carbit3333333.oiiglot_bulgary.model.Lesson

data class LessonsUiState(
    val isLoading: Boolean = false,
    val lessons: List<Lesson> = emptyList(),
    val errorMessage: String? = null
)