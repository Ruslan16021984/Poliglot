package com.carbit3333333.oiiglot_bulgary.ui.lessons

import com.carbit3333333.oiiglot_bulgary.data.settings.AppLanguage
import com.carbit3333333.oiiglot_bulgary.data.settings.AppThemeMode
import com.carbit3333333.oiiglot_bulgary.model.Lesson

data class LessonsUiState(
    val isLoading: Boolean = false,
    val lessons: List<Lesson> = emptyList(),
    val errorMessage: String? = null,
    val appThemeMode: AppThemeMode = AppThemeMode.System,
    val appLanguage: AppLanguage = AppLanguage.System,
)
