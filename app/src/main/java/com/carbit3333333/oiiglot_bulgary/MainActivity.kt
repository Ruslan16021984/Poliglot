package com.carbit3333333.oiiglot_bulgary

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.os.LocaleListCompat
import com.carbit3333333.oiiglot_bulgary.data.settings.AppLanguage
import com.carbit3333333.oiiglot_bulgary.data.settings.AppSettingsStore
import com.carbit3333333.oiiglot_bulgary.data.settings.AppThemeMode
import com.carbit3333333.oiiglot_bulgary.navigation.AppNavGraph
import com.carbit3333333.oiiglot_bulgary.ui.theme.OIiglot_BulgaryTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsStore = remember { AppSettingsStore(this) }
            val themeMode by settingsStore.themeModeFlow.collectAsState(initial = AppThemeMode.System)
            val appLanguage by settingsStore.languageFlow.collectAsState(initial = AppLanguage.System)

            LaunchedEffect(appLanguage) {
                val locales = when (appLanguage) {
                    AppLanguage.System -> LocaleListCompat.getEmptyLocaleList()
                    AppLanguage.Russian -> LocaleListCompat.forLanguageTags(AppLanguage.Russian.tag)
                    AppLanguage.Ukrainian -> LocaleListCompat.forLanguageTags(AppLanguage.Ukrainian.tag)
                }
                AppCompatDelegate.setApplicationLocales(locales)
            }

            OIiglot_BulgaryTheme(appThemeMode = themeMode) {
                AppNavGraph()
            }
        }
    }
}
