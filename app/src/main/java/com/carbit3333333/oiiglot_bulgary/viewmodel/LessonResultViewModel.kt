package com.carbit3333333.oiiglot_bulgary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.carbit3333333.oiiglot_bulgary.data.LessonProgressStore
import com.carbit3333333.oiiglot_bulgary.data.LessonRepository
import com.carbit3333333.oiiglot_bulgary.data.LessonSessionRepository
import com.carbit3333333.oiiglot_bulgary.model.LessonResult
import com.carbit3333333.oiiglot_bulgary.ui.lessons.LessonResultUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LessonResultViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sessionRepository = LessonSessionRepository()
    private val lessonRepository = LessonRepository()
    private val progressStore = LessonProgressStore(application)

    private val _uiState = MutableStateFlow(LessonResultUiState())
    val uiState: StateFlow<LessonResultUiState> = _uiState.asStateFlow()

    fun loadLessonResult(
        lessonId: Int,
        correctCount: Int,
        wrongCount: Int
    ) {
        val session = sessionRepository.getLessonSession(lessonId)
        val totalExercises = session.exercises.size

        val score = if (totalExercises > 0) {
            (correctCount.toFloat() / totalExercises.toFloat()) * 10f
        } else {
            0f
        }

        val isPassed = score >= 4.5f

        val result = LessonResult(
            lessonId = lessonId,
            lessonTitle = session.lessonTitle,
            totalExercises = totalExercises,
            correctCount = correctCount,
            wrongCount = wrongCount,
            score = score,
            isPassed = isPassed
        )

        val nextLessonId = lessonRepository.getNextLessonId(lessonId)
        val hasNextLesson = nextLessonId != null

        _uiState.value = LessonResultUiState(
            result = result,
            hasNextLesson = hasNextLesson,
            nextLessonId = nextLessonId
        )

        viewModelScope.launch {
            progressStore.saveLessonResult(
                lessonId = lessonId,
                correctCount = correctCount,
                wrongCount = wrongCount,
                score = score,
                isPassed = isPassed
            )

            if (isPassed && nextLessonId != null) {
                progressStore.unlockNextLesson(nextLessonId)
            }
        }
    }
}