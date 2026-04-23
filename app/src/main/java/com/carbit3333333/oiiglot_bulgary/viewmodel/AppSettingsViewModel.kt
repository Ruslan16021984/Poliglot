package com.carbit3333333.oiiglot_bulgary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.carbit3333333.oiiglot_bulgary.data.settings.AppLanguage
import com.carbit3333333.oiiglot_bulgary.data.settings.AppSettingsStore
import com.carbit3333333.oiiglot_bulgary.data.settings.AppThemeMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AppSettingsUiState(
    val themeMode: AppThemeMode = AppThemeMode.System,
    val language: AppLanguage = AppLanguage.System,
)

class AppSettingsViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val settingsStore = AppSettingsStore(application)

    val uiState: StateFlow<AppSettingsUiState> =
        combine(
            settingsStore.themeModeFlow,
            settingsStore.languageFlow,
        ) { themeMode, language ->
            AppSettingsUiState(
                themeMode = themeMode,
                language = language,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AppSettingsUiState(),
        )

    fun updateThemeMode(themeMode: AppThemeMode) {
        viewModelScope.launch {
            settingsStore.saveThemeMode(themeMode)
        }
    }

    fun updateLanguage(language: AppLanguage) {
        viewModelScope.launch {
            settingsStore.saveLanguage(language)
        }
    }

    companion object {
        fun provideFactory(application: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AppSettingsViewModel(application) as T
                }
            }
    }
}
