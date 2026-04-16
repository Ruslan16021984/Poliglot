package com.carbit3333333.oiiglot_bulgary.viewmodel

import androidx.lifecycle.ViewModel
import com.carbit3333333.oiiglot_bulgary.data.LessonRepository
import com.carbit3333333.oiiglot_bulgary.ui.lessons.LessonUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LessonViewModel : ViewModel() {

    private val repository = LessonRepository()

    private val _uiState = MutableStateFlow(LessonUiState())
    val uiState: StateFlow<LessonUiState> = _uiState.asStateFlow()

    fun loadLesson(lessonId: Int) {
        val lesson = repository.getLessonById(lessonId)

        _uiState.value = if (lesson != null) {
            LessonUiState(lesson = lesson)
        } else {
            LessonUiState(errorMessage = "Урок не найден")
        }
    }
}