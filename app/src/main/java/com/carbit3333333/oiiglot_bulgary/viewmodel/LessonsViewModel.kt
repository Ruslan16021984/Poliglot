package com.carbit3333333.oiiglot_bulgary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.carbit3333333.oiiglot_bulgary.data.LessonProgressStore
import com.carbit3333333.oiiglot_bulgary.data.LessonRepository
import com.carbit3333333.oiiglot_bulgary.ui.lessons.LessonsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class LessonsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = LessonRepository()
    private val progressStore = LessonProgressStore(application)

    private val _uiState = MutableStateFlow(LessonsUiState(isLoading = true))
    val uiState: StateFlow<LessonsUiState> = _uiState.asStateFlow()

    init {
        observeLessons()
    }

    private fun observeLessons() {
        viewModelScope.launch {
            val lessonIds = repository.getLessons().map { it.id }

            combine(
                progressStore.openedLessonIdFlow,
                progressStore.getLessonResultsFlow(lessonIds)
            ) { openedLessonId, savedResults ->
                repository.getLessons().map { lesson ->
                    val savedResult = savedResults[lesson.id]

                    lesson.copy(
                        isLocked = lesson.id > openedLessonId,
                        isCompleted = savedResult?.isPassed == true,
                        bestScore = savedResult?.bestScore,
                        currentProgress = savedResult?.currentStep ?: 0,
                        totalProgress = savedResult?.totalSteps ?: 0
                    )
                }
            }.collect { lessons ->
                _uiState.value = LessonsUiState(
                    isLoading = false,
                    lessons = lessons
                )
            }
        }
    }

    fun unlockAllLessons() {
        viewModelScope.launch {
            progressStore.unlockAllLessons(
                maxLessonId = repository.getLessons().maxOfOrNull { it.id } ?: 1
            )
        }
    }

    fun resetLessons() {
        viewModelScope.launch {
            progressStore.resetLessonUnlocks(
                maxLessonId = repository.getLessons().maxOfOrNull { it.id } ?: 1
            )
        }
    }
}