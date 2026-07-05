package com.carbit3333333.oiiglot_bulgary.viewmodel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.carbit3333333.oiiglot_bulgary.BuildConfig
import com.carbit3333333.oiiglot_bulgary.data.billing.LocalBillingFacade
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
    val hasFullCourseAccess: Boolean = false,
    val isPurchaseFlowAvailable: Boolean = false,
    val showDeveloperActions: Boolean = false,
)

class AppSettingsViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val showTestingTools = BuildConfig.INTERNAL_TESTING_TOOLS_ENABLED

    private val settingsStore = AppSettingsStore(application)
    private val billingFacade = LocalBillingFacade(application)

    val uiState: StateFlow<AppSettingsUiState> =
        combine(
            settingsStore.themeModeFlow,
            settingsStore.languageFlow,
            billingFacade.hasFullCourseAccessFlow,
        ) { themeMode, language, hasFullCourseAccess ->
            AppSettingsUiState(
                themeMode = themeMode,
                language = language,
                hasFullCourseAccess = hasFullCourseAccess,
                isPurchaseFlowAvailable = billingFacade.isPurchaseFlowAvailable,
                showDeveloperActions = showTestingTools,
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

    suspend fun launchFullCoursePurchase(activity: Activity): Boolean {
        return billingFacade.launchFullCoursePurchase(activity)
    }

    fun restorePurchases() {
        viewModelScope.launch {
            billingFacade.restorePurchases()
        }
    }

    fun revokeFullCourseAccess() {
        if (!showTestingTools) return
        viewModelScope.launch {
            billingFacade.revokeFullCourseAccess()
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
