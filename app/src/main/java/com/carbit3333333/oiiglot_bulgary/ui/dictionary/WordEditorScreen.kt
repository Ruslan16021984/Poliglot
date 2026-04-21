package com.carbit3333333.oiiglot_bulgary.ui.dictionary

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
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
            onBackClick()
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
        onSpeechInputClick = { target ->
            if (!uiState.isEditorInteractive) {
                return@WordEditorScreenContent
            }

            speechTarget = target
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
                )
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
    onSpeechInputClick: (WordEditorViewModel.SpeechTarget) -> Unit,
) {
    val pageBackground = Color(0xFFF4F7FC)
    val accentTint = Color(0xFFE7EEFF)
    val accentColor = Color(0xFF4164A9)
    val primaryText = Color(0xFF20243A)
    val secondaryText = Color(0xFF66708A)

    if (uiState.isNewGroupDialogVisible) {
        AlertDialog(
            onDismissRequest = {
                if (uiState.isEditorInteractive) {
                    onDismissNewGroupDialog()
                }
            },
            title = {
                Text("Новая группа")
            },
            text = {
                OutlinedTextField(
                    value = uiState.newGroupName,
                    onValueChange = onNewGroupNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = uiState.isEditorInteractive,
                    label = {
                        Text("Название группы")
                    },
                    placeholder = {
                        Text("Например, Путешествие")
                    },
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
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedButton(
                        onClick = onBackClick,
                        enabled = uiState.isBackEnabled,
                    ) {
                        Text("Назад")
                    }

                    Button(
                        onClick = onSaveClick,
                        enabled = uiState.isSaveEnabled,
                    ) {
                        Text(uiState.primaryButtonText)
                    }
                }
            }

            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = accentTint,
                                shape = MaterialTheme.shapes.medium,
                            ) {
                                Box(
                                    modifier = Modifier.size(42.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        painter = painterResource(id = android.R.drawable.ic_menu_edit),
                                        contentDescription = null,
                                        tint = accentColor,
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = if (uiState.isEditMode) "Редактирование слова" else "Новое слово",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = primaryText,
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Сохраняй болгарское слово, русский перевод и сразу прикрепляй его к нужным группам.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = secondaryText,
                                )
                            }
                        }

                        if (uiState.isLoading) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            WordInputField(
                                value = uiState.bgWord,
                                label = "Болгарский",
                                placeholder = "Например, благодаря",
                                supportingText = uiState.bgWordError,
                                enabled = uiState.isEditorInteractive,
                                onValueChange = onBgWordChange,
                                onSpeechInputClick = {
                                    onSpeechInputClick(WordEditorViewModel.SpeechTarget.Bulgarian)
                                },
                            )

                            WordInputField(
                                value = uiState.ruTranslation,
                                label = "Русский перевод",
                                placeholder = "Например, спасибо",
                                supportingText = uiState.ruTranslationError,
                                enabled = uiState.isEditorInteractive,
                                onValueChange = onRuTranslationChange,
                                onSpeechInputClick = {
                                    onSpeechInputClick(WordEditorViewModel.SpeechTarget.Russian)
                                },
                            )
                        }
                    }
                }
            }

            if (uiState.errorMessage != null) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF4F4)),
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
                                color = Color(0xFF8B3A3A),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            OutlinedButton(onClick = onDismissError) {
                                Text("Ок")
                            }
                        }
                    }
                }
            }

            if (uiState.isSaving) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F6FF)),
                    ) {
                        Text(
                            text = "Сохраняем слово. Пожалуйста, дождитесь завершения.",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = primaryText,
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        Text(
                            text = "Группы",
                            style = MaterialTheme.typography.titleMedium,
                            color = primaryText,
                        )
                        Text(
                            text = "Можно выбрать несколько групп для одного слова.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = secondaryText,
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            uiState.availableGroups.forEach { group ->
                                FilterChip(
                                    selected = group.id in uiState.selectedGroupIds,
                                    onClick = { onToggleGroup(group.id) },
                                    enabled = uiState.isEditorInteractive,
                                    label = {
                                        Text(group.name)
                                    },
                                )
                            }

                            OutlinedButton(
                                onClick = onShowNewGroupDialog,
                                enabled = uiState.isEditorInteractive,
                            ) {
                                Text("+ Новая группа")
                            }
                        }

                        if (uiState.availableGroups.isEmpty()) {
                            Text(
                                text = "Пока нет групп. Создай первую и она сразу появится в выборе.",
                                style = MaterialTheme.typography.bodySmall,
                                color = secondaryText,
                            )
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            text = "Дополнительно",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = primaryText,
                        )
                        Text(
                            text = "Этот блок оставлен под следующие шаги MVP.",
                            style = MaterialTheme.typography.bodySmall,
                            color = secondaryText,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WordInputField(
    value: String,
    label: String,
    placeholder: String,
    supportingText: String?,
    enabled: Boolean,
    onValueChange: (String) -> Unit,
    onSpeechInputClick: () -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        readOnly = !enabled,
        label = {
            Text(label)
        },
        placeholder = {
            Text(placeholder)
        },
        trailingIcon = {
            IconButton(
                onClick = onSpeechInputClick,
                enabled = enabled,
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_btn_speak_now),
                    contentDescription = "Голосовой ввод",
                )
            }
        },
        isError = supportingText != null,
        supportingText = if (supportingText == null) {
            null
        } else {
            {
                Text(supportingText)
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun WordEditorScreenPreview() {
    OIiglot_BulgaryTheme {
        WordEditorScreenContent(
            uiState = WordEditorUiState(
                bgWord = "здравей",
                ruTranslation = "привет",
                selectedGroupIds = setOf(2L),
                availableGroups = listOf(
                    WordGroup(id = 1L, name = "Еда", wordCount = 4),
                    WordGroup(id = 2L, name = "Путешествие", wordCount = 7),
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
            onSpeechInputClick = {},
        )
    }
}
