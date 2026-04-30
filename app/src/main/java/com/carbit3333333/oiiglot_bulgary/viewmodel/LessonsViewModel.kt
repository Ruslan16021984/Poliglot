package com.carbit3333333.oiiglot_bulgary.viewmodel

import android.app.Application
import android.content.pm.ApplicationInfo
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.carbit3333333.oiiglot_bulgary.data.LessonProgressStore
import com.carbit3333333.oiiglot_bulgary.data.LessonRepository
import com.carbit3333333.oiiglot_bulgary.data.billing.PurchaseAccessStore
import com.carbit3333333.oiiglot_bulgary.data.settings.AppLanguage
import com.carbit3333333.oiiglot_bulgary.data.settings.AppSettingsStore
import com.carbit3333333.oiiglot_bulgary.data.settings.AppThemeMode
import com.carbit3333333.oiiglot_bulgary.ui.lessons.LessonsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class LessonsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val isDebugBuild =
        (application.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

    private val repository = LessonRepository(application)
    private val progressStore = LessonProgressStore(application)
    private val purchaseAccessStore = PurchaseAccessStore(application)
    private val settingsStore = AppSettingsStore(application)

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
                purchaseAccessStore.hasFullCourseAccessFlow,
                progressStore.getLessonResultsFlow(lessonIds),
                settingsStore.themeModeFlow,
                settingsStore.languageFlow,
            ) { openedLessonId, hasFullCourseAccess, savedResults, themeMode, appLanguage ->
                val lessons = repository.getLessons().map { lesson ->
                    val savedResult = savedResults[lesson.id]

                    lesson.copy(
                        isLocked = !hasFullCourseAccess && lesson.id > openedLessonId,
                        isCompleted = savedResult?.isPassed == true,
                        bestScore = savedResult?.bestScore,
                        currentScore = savedResult?.currentScore,
                        currentProgress = savedResult?.currentStep ?: 0,
                        totalProgress = savedResult?.totalSteps ?: 0
                    )
                }

                LessonsUiState(
                    isLoading = false,
                    lessons = lessons,
                    appThemeMode = themeMode,
                    appLanguage = appLanguage,
                    hasFullCourseAccess = hasFullCourseAccess,
                    showDeveloperActions = isDebugBuild,
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun updateThemeMode(themeMode: AppThemeMode) {
        viewModelScope.launch {
            settingsStore.saveThemeMode(themeMode)
        }
    }

    fun updateLanguage(appLanguage: AppLanguage) {
        viewModelScope.launch {
            settingsStore.saveLanguage(appLanguage)
        }
    }

    fun unlockAllLessons() {
        if (!isDebugBuild) return
        viewModelScope.launch {
            progressStore.unlockAllLessons(
                maxLessonId = repository.getLessons().maxOfOrNull { it.id } ?: 1
            )
        }
    }

    fun resetLessons() {
        if (!isDebugBuild) return
        viewModelScope.launch {
            progressStore.resetLessonUnlocks(
                maxLessonId = repository.getLessons().maxOfOrNull { it.id } ?: 1
            )
        }
    }
}
