package com.carbit3333333.oiiglot_bulgary.ui.dictionary

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carbit3333333.oiiglot_bulgary.model.dictionary.WordGroup
import com.carbit3333333.oiiglot_bulgary.ui.theme.OIiglot_BulgaryTheme
import com.carbit3333333.oiiglot_bulgary.viewmodel.WordEditorViewModel

@Composable
fun WordEditorScreen(
    onBackClick: () -> Unit,
    viewModel: WordEditorViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var speechTarget by remember { mutableStateOf<WordEditorViewModel.SpeechTarget?>(null) }

    BackHandler(enabled = uiState.isSaving) {
    }

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val recognizedText = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()

            if (!recognizedText.isNullOrBlank()) {
                speechTarget?.let { target ->
                    viewModel.appendRecognizedText(target, recognizedText)
                }
            }
        }
        speechTarget = null
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            viewModel.acknowledgeSaveCompleted()
            if (uiState.isEditMode) {
                onBackClick()
            }
        }
    }

    WordEditorScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onBgWordChange = viewModel::updateBgWord,
        onRuTranslationChange = viewModel::updateRuTranslation,
        onToggleGroup = viewModel::toggleGroupSelection,
        onShowNewGroupDialog = viewModel::showNewGroupDialog,
        onDismissNewGroupDialog = viewModel::dismissNewGroupDialog,
        onNewGroupNameChange = viewModel::updateNewGroupName,
        onCreateGroup = viewModel::createGroup,
        onSaveClick = viewModel::saveWord,
        onDismissError = viewModel::clearError,
        onDismissSuccess = viewModel::clearSuccess,
        onSpeechInputClick = { target ->
            if (!uiState.isEditorInteractive) {
                return@WordEditorScreenContent
            }

            speechTarget = target
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE,
                    if (target == WordEditorViewModel.SpeechTarget.Bulgarian) "bg-BG" else "ru-RU",
                )
                putExtra(
                    RecognizerIntent.EXTRA_PROMPT,
                    if (target == WordEditorViewModel.SpeechTarget.Bulgarian) {
                        "Скажите слово на болгарском"
                    } else {
                        "Скажите перевод на русском"
                    },
                )
            }

            runCatching {
                speechLauncher.launch(intent)
            }.onFailure {
                speechTarget = null
                viewModel.onSpeechRecognitionUnavailable()
            }
        },
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WordEditorScreenContent(
    uiState: WordEditorUiState,
    onBackClick: () -> Unit,
    onBgWordChange: (String) -> Unit,
    onRuTranslationChange: (String) -> Unit,
    onToggleGroup: (Long) -> Unit,
    onShowNewGroupDialog: () -> Unit,
    onDismissNewGroupDialog: () -> Unit,
    onNewGroupNameChange: (String) -> Unit,
    onCreateGroup: () -> Unit,
    onSaveClick: () -> Unit,
    onDismissError: () -> Unit,
    onDismissSuccess: () -> Unit,
    onSpeechInputClick: (WordEditorViewModel.SpeechTarget) -> Unit,
) {
    val pageBackground = Color(0xFFF5F7FB)
    val surfaceColor = Color.White
    val accentColor = Color(0xFF1F6FE5)
    val accentTint = Color(0xFFEAF2FF)
    val titleColor = Color(0xFF2D333C)
    val bodyColor = Color(0xFF727B8C)
    val borderColor = Color(0xFFD9E1EC)

    if (uiState.isNewGroupDialogVisible) {
        AlertDialog(
            onDismissRequest = {
                if (uiState.isEditorInteractive) {
                    onDismissNewGroupDialog()
                }
            },
            title = { Text("Новая группа") },
            text = {
                OutlinedTextField(
                    value = uiState.newGroupName,
                    onValueChange = onNewGroupNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = uiState.isEditorInteractive,
                    label = { Text("Название группы") },
                    placeholder = { Text("Например, Путешествие") },
                    shape = RoundedCornerShape(16.dp),
                )
            },
            confirmButton = {
                Button(
                    onClick = onCreateGroup,
                    enabled = uiState.isEditorInteractive && uiState.newGroupName.trim().isNotEmpty(),
                ) {
                    Text("Создать")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = onDismissNewGroupDialog,
                    enabled = uiState.isEditorInteractive,
                ) {
                    Text("Отмена")
                }
            },
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = pageBackground,
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
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            enabled = uiState.isBackEnabled,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(surfaceColor),
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Назад",
                                tint = titleColor,
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = uiState.screenTitle,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = titleColor,
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = surfaceColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                    ) {
                        if (uiState.isLoading) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            EditorFieldCard(
                                title = "Болгарский",
                                value = uiState.bgWord,
                                placeholder = "Введите слово на болгарском",
                                supportingText = uiState.bgWordError,
                                enabled = uiState.isEditorInteractive,
                                onValueChange = onBgWordChange,
                                onSpeechInputClick = {
                                    onSpeechInputClick(WordEditorViewModel.SpeechTarget.Bulgarian)
                                },
                                borderColor = borderColor,
                                bodyColor = bodyColor,
                            )

                            EditorFieldCard(
                                title = "Русский перевод",
                                value = uiState.ruTranslation,
                                placeholder = "Введите перевод",
                                supportingText = uiState.ruTranslationError,
                                enabled = uiState.isEditorInteractive,
                                onValueChange = onRuTranslationChange,
                                onSpeechInputClick = {
                                    onSpeechInputClick(WordEditorViewModel.SpeechTarget.Russian)
                                },
                                borderColor = borderColor,
                                bodyColor = bodyColor,
                            )
                        }
                    }
                }
            }

            if (uiState.errorMessage != null) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF4F1)),
                        shape = RoundedCornerShape(18.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = uiState.errorMessage,
                                modifier = Modifier.weight(1f),
                                color = Color(0xFF9C4038),
                            )
                            OutlinedButton(onClick = onDismissError) {
                                Text("Ок")
                            }
                        }
                    }
                }
            }

            if (uiState.successMessage != null) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF8EF)),
                        shape = RoundedCornerShape(18.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = uiState.successMessage,
                                modifier = Modifier.weight(1f),
                                color = Color(0xFF256C42),
                            )
                            OutlinedButton(onClick = onDismissSuccess) {
                                Text("Ок")
                            }
                        }
                    }
                }
            }

            if (uiState.isSaving) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F6FF)),
                        shape = RoundedCornerShape(18.dp),
                    ) {
                        Text(
                            text = "Сохраняем слово. Пожалуйста, дождитесь завершения.",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                            color = titleColor,
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = surfaceColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = "Когда всё заполнено, сохраните карточку в словарь.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = bodyColor,
                        )
                        Button(
                            onClick = onSaveClick,
                            enabled = uiState.isSaveEnabled,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            contentPadding = PaddingValues(vertical = 16.dp),
                        ) {
                            Text(uiState.primaryButtonText)
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = surfaceColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        Text(
                            text = "Группы",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = titleColor,
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            uiState.availableGroups.forEach { group ->
                                GroupSelectionChip(
                                    group = group,
                                    selected = group.id in uiState.selectedGroupIds,
                                    onClick = { onToggleGroup(group.id) },
                                    accentColor = accentColor,
                                    borderColor = borderColor,
                                    bodyColor = bodyColor,
                                    enabled = uiState.isEditorInteractive,
                                )
                            }

                            OutlinedButton(
                                onClick = onShowNewGroupDialog,
                                enabled = uiState.isEditorInteractive,
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.5.dp, accentColor.copy(alpha = 0.55f)),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = accentColor,
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Новая группа", color = accentColor)
                            }
                        }

                        if (uiState.availableGroups.isEmpty()) {
                            Text(
                                text = "Пока нет групп. Создай первую, и она сразу появится в выборе.",
                                style = MaterialTheme.typography.bodySmall,
                                color = bodyColor,
                            )
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = surfaceColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = titleColor,
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Дополнительно",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = titleColor,
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = bodyColor,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EditorFieldCard(
    title: String,
    value: String,
    placeholder: String,
    supportingText: String?,
    enabled: Boolean,
    onValueChange: (String) -> Unit,
    onSpeechInputClick: () -> Unit,
    borderColor: Color,
    bodyColor: Color,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2D333C),
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            readOnly = !enabled,
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            placeholder = { Text(placeholder, color = bodyColor) },
            trailingIcon = {
                IconButton(
                    onClick = onSpeechInputClick,
                    enabled = enabled,
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_btn_speak_now),
                        contentDescription = "Голосовой ввод",
                        tint = bodyColor,
                    )
                }
            },
            isError = supportingText != null,
            supportingText = if (supportingText == null) {
                null
            } else {
                { Text(supportingText) }
            },
        )
    }
}

@Composable
private fun GroupSelectionChip(
    group: WordGroup,
    selected: Boolean,
    onClick: () -> Unit,
    accentColor: Color,
    borderColor: Color,
    bodyColor: Color,
    enabled: Boolean,
) {
    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        color = if (selected) accentColor.copy(alpha = 0.08f) else Color.White,
        border = BorderStroke(1.dp, if (selected) accentColor else borderColor),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = groupIcon(group.name),
                contentDescription = null,
                tint = if (selected) accentColor else bodyColor,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = group.name,
                color = if (selected) accentColor else Color(0xFF2D333C),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

private fun groupIcon(name: String) = when {
    "еда" in name.lowercase() || "ресторан" in name.lowercase() -> Icons.Default.Add
    else -> Icons.Default.Star
}

@Preview(showBackground = true)
@Composable
private fun WordEditorScreenPreview() {
    OIiglot_BulgaryTheme {
        WordEditorScreenContent(
            uiState = WordEditorUiState(
                bgWord = "",
                ruTranslation = "",
                selectedGroupIds = setOf(2L),
                availableGroups = listOf(
                    WordGroup(id = 1L, name = "Путешествие", wordCount = 24),
                    WordGroup(id = 2L, name = "Еда", wordCount = 18),
                    WordGroup(id = 3L, name = "Быт", wordCount = 16),
                ),
            ),
            onBackClick = {},
            onBgWordChange = {},
            onRuTranslationChange = {},
            onToggleGroup = {},
            onShowNewGroupDialog = {},
            onDismissNewGroupDialog = {},
            onNewGroupNameChange = {},
            onCreateGroup = {},
            onSaveClick = {},
            onDismissError = {},
            onDismissSuccess = {},
            onSpeechInputClick = {},
        )
    }
}
