package com.carbit3333333.oiiglot_bulgary

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
import com.carbit3333333.oiiglot_bulgary.data.settings.AppLanguage
import com.carbit3333333.oiiglot_bulgary.data.settings.AppSettingsStore
import com.carbit3333333.oiiglot_bulgary.data.settings.AppThemeMode
import com.carbit3333333.oiiglot_bulgary.navigation.AppNavGraph
import com.carbit3333333.oiiglot_bulgary.ui.theme.OIiglot_BulgaryTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val appSettingsStore = AppSettingsStore(applicationContext)

        lifecycleScope.launch {
            applyLanguageIfNeeded(appSettingsStore.languageFlow.first())
        }

        setContent {
            val settingsStore = remember { appSettingsStore }
            val themeMode by settingsStore.themeModeFlow.collectAsState(initial = AppThemeMode.System)

            OIiglot_BulgaryTheme(appThemeMode = themeMode) {
                AppNavGraph()
            }
        }
    }

    private fun applyLanguageIfNeeded(appLanguage: AppLanguage) {
        val targetLocales = when (appLanguage) {
            AppLanguage.System -> LocaleListCompat.getEmptyLocaleList()
            AppLanguage.Russian -> LocaleListCompat.forLanguageTags(AppLanguage.Russian.tag)
            AppLanguage.Ukrainian -> LocaleListCompat.forLanguageTags(AppLanguage.Ukrainian.tag)
        }

        if (AppCompatDelegate.getApplicationLocales() != targetLocales) {
            AppCompatDelegate.setApplicationLocales(targetLocales)
        }
    }
}
