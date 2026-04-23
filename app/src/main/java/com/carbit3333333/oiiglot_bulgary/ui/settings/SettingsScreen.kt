package com.carbit3333333.oiiglot_bulgary.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carbit3333333.oiiglot_bulgary.R
import com.carbit3333333.oiiglot_bulgary.data.settings.AppLanguage
import com.carbit3333333.oiiglot_bulgary.data.settings.AppThemeMode
import com.carbit3333333.oiiglot_bulgary.ui.dictionary.rememberDictionaryPalette
import com.carbit3333333.oiiglot_bulgary.viewmodel.AppSettingsUiState
import com.carbit3333333.oiiglot_bulgary.viewmodel.AppSettingsViewModel

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    viewModel: AppSettingsViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onThemeModeSelect = viewModel::updateThemeMode,
        onLanguageSelect = viewModel::updateLanguage,
    )
}

@Composable
fun SettingsScreenContent(
    uiState: AppSettingsUiState,
    onBackClick: () -> Unit,
    onThemeModeSelect: (AppThemeMode) -> Unit,
    onLanguageSelect: (AppLanguage) -> Unit,
) {
    val palette = rememberDictionaryPalette()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = palette.pageBackground,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .clip(CircleShape),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.common_back),
                            tint = palette.title,
                        )
                    }

                    Column {
                        Text(
                            text = stringResource(R.string.settings_title),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = palette.title,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.settings_summary),
                            style = MaterialTheme.typography.bodyMedium,
                            color = palette.body,
                        )
                    }
                }
            }

            item {
                SettingsSectionCard(
                    title = stringResource(R.string.settings_theme_title),
                    description = stringResource(R.string.settings_theme_description),
                    surfaceColor = palette.surface,
                    borderColor = palette.border,
                    titleColor = palette.title,
                    bodyColor = palette.body,
                ) {
                    SettingOptionRow(
                        title = stringResource(R.string.settings_theme_system),
                        selected = uiState.themeMode == AppThemeMode.System,
                        onClick = { onThemeModeSelect(AppThemeMode.System) },
                        accent = palette.accent,
                        titleColor = palette.title,
                    )
                    SettingOptionRow(
                        title = stringResource(R.string.settings_theme_light),
                        selected = uiState.themeMode == AppThemeMode.Light,
                        onClick = { onThemeModeSelect(AppThemeMode.Light) },
                        accent = palette.accent,
                        titleColor = palette.title,
                    )
                    SettingOptionRow(
                        title = stringResource(R.string.settings_theme_dark),
                        selected = uiState.themeMode == AppThemeMode.Dark,
                        onClick = { onThemeModeSelect(AppThemeMode.Dark) },
                        accent = palette.accent,
                        titleColor = palette.title,
                    )
                }
            }

            item {
                SettingsSectionCard(
                    title = stringResource(R.string.settings_language_title),
                    description = stringResource(R.string.settings_language_description),
                    surfaceColor = palette.surface,
                    borderColor = palette.border,
                    titleColor = palette.title,
                    bodyColor = palette.body,
                ) {
                    SettingOptionRow(
                        title = stringResource(R.string.settings_language_system),
                        selected = uiState.language == AppLanguage.System,
                        onClick = { onLanguageSelect(AppLanguage.System) },
                        accent = palette.accent,
                        titleColor = palette.title,
                    )
                    SettingOptionRow(
                        title = stringResource(R.string.settings_language_russian),
                        selected = uiState.language == AppLanguage.Russian,
                        onClick = { onLanguageSelect(AppLanguage.Russian) },
                        accent = palette.accent,
                        titleColor = palette.title,
                    )
                    SettingOptionRow(
                        title = stringResource(R.string.settings_language_ukrainian),
                        selected = uiState.language == AppLanguage.Ukrainian,
                        onClick = { onLanguageSelect(AppLanguage.Ukrainian) },
                        accent = palette.accent,
                        titleColor = palette.title,
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsSectionCard(
    title: String,
    description: String,
    surfaceColor: Color,
    borderColor: Color,
    titleColor: Color,
    bodyColor: Color,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        border = BorderStroke(1.dp, borderColor.copy(alpha = 0.65f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = titleColor,
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = bodyColor,
                )
            }
            content()
        }
    }
}

@Composable
private fun SettingOptionRow(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    accent: Color,
    titleColor: Color,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0f),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = titleColor,
            )
            RadioButton(
                selected = selected,
                onClick = onClick,
            )
        }
    }
}
