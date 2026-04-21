package com.carbit3333333.oiiglot_bulgary.ui.dictionary

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carbit3333333.oiiglot_bulgary.model.dictionary.DictionaryWordListItem
import com.carbit3333333.oiiglot_bulgary.model.dictionary.WordGroup
import com.carbit3333333.oiiglot_bulgary.ui.theme.OIiglot_BulgaryTheme
import com.carbit3333333.oiiglot_bulgary.viewmodel.DictionaryViewModel

@Composable
fun DictionaryScreen(
    onBackClick: () -> Unit,
    onAddWordClick: () -> Unit,
    onTrainAllClick: () -> Unit,
    onTrainGroupClick: (WordGroup) -> Unit,
    onWordClick: (Long) -> Unit,
    viewModel: DictionaryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val hasAnyWordsForTraining by viewModel.hasAnyWordsForTraining.collectAsStateWithLifecycle()

    DictionaryScreenContent(
        uiState = uiState,
        hasAnyWordsForTraining = hasAnyWordsForTraining,
        onBackClick = onBackClick,
        onAddWordClick = onAddWordClick,
        onTrainAllClick = onTrainAllClick,
        onTrainGroupClick = onTrainGroupClick,
        onWordClick = onWordClick,
        onQueryChange = viewModel::updateQuery,
        onGroupSelect = viewModel::selectGroup,
        onDeleteWordClick = viewModel::deleteWord,
        onDismissError = viewModel::clearError
    )
}

@Composable
fun DictionaryScreenContent(
    uiState: DictionaryListUiState,
    hasAnyWordsForTraining: Boolean,
    onBackClick: () -> Unit,
    onAddWordClick: () -> Unit,
    onTrainAllClick: () -> Unit,
    onTrainGroupClick: (WordGroup) -> Unit,
    onWordClick: (Long) -> Unit,
    onQueryChange: (String) -> Unit,
    onGroupSelect: (Long?) -> Unit,
    onDeleteWordClick: (Long) -> Unit,
    onDismissError: () -> Unit
) {
    val pageBackground = Color(0xFFF4F7FC)
    val accentTint = Color(0xFFE7EEFF)
    val accentColor = Color(0xFF4164A9)
    val secondaryText = Color(0xFF66708A)
    var pendingDeleteWordId by rememberSaveable { mutableStateOf<Long?>(null) }
    var pendingDeleteWordLabel by rememberSaveable { mutableStateOf("") }

    pendingDeleteWordId?.let { wordId ->
        AlertDialog(
            onDismissRequest = {
                pendingDeleteWordId = null
                pendingDeleteWordLabel = ""
            },
            title = {
                Text("Удалить слово?")
            },
            text = {
                Text("Слово \"$pendingDeleteWordLabel\" будет удалено из личного словаря.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteWordClick(wordId)
                        pendingDeleteWordId = null
                        pendingDeleteWordLabel = ""
                    }
                ) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        pendingDeleteWordId = null
                        pendingDeleteWordLabel = ""
                    }
                ) {
                    Text("Отмена")
                }
            }
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = pageBackground
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(onClick = onBackClick) {
                        Text("Назад")
                    }

                    Button(onClick = onAddWordClick) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Добавить")
                    }
                }
            }

            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = accentTint,
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Box(
                                    modifier = Modifier.size(42.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = accentColor
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Мои слова",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color(0xFF20243A)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Собирай словарь, ищи нужные слова и запускай тренировку по всей базе или по группе.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = secondaryText
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        OutlinedTextField(
                            value = uiState.query,
                            onValueChange = onQueryChange,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null
                                )
                            },
                            placeholder = {
                                Text("Поиск по болгарскому или русскому слову")
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onTrainAllClick,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = hasAnyWordsForTraining
                        ) {
                            Text("Учить все слова")
                        }
                    }
                }
            }

            if (uiState.errorMessage != null) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFF4F4)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = uiState.errorMessage,
                                modifier = Modifier.weight(1f),
                                color = Color(0xFF8B3A3A),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            OutlinedButton(onClick = onDismissError) {
                                Text("Ок")
                            }
                        }
                    }
                }
            }

            item {
                GroupFilterSection(
                    groups = uiState.groups,
                    selectedGroupId = uiState.selectedGroupId,
                    onGroupSelect = onGroupSelect,
                    onTrainGroupClick = onTrainGroupClick
                )
            }

            if (uiState.isLoading) {
                item {
                    EmptyDictionaryBlock(
                        title = "Загрузка словаря",
                        message = "Сейчас подтянем слова и группы."
                    )
                }
            } else if (uiState.words.isEmpty()) {
                item {
                    EmptyDictionaryBlock(
                        title = "Пока нет слов",
                        message = if (uiState.query.isBlank() && uiState.selectedGroupId == null) {
                            "Добавь первое слово, чтобы собрать свой словарь."
                        } else {
                            "По текущему фильтру ничего не найдено. Попробуй другой запрос или группу."
                        }
                    )
                }
            } else {
                items(uiState.words, key = { it.id }) { word ->
                    DictionaryWordRow(
                        word = word,
                        onClick = { onWordClick(word.id) },
                        onDeleteClick = {
                            pendingDeleteWordId = word.id
                            pendingDeleteWordLabel = word.bgWord
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun GroupFilterSection(
    groups: List<WordGroup>,
    selectedGroupId: Long?,
    onGroupSelect: (Long?) -> Unit,
    onTrainGroupClick: (WordGroup) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Группы",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF20243A)
        )

        if (groups.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Пока нет групп",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color(0xFF20243A)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Группы появятся после добавления слов и распределения по темам.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF66708A)
                    )
                }
            }
            return
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedGroupId == null,
                    onClick = { onGroupSelect(null) },
                    label = { Text("Все") }
                )
            }

            items(groups, key = { it.id }) { group ->
                FilterChip(
                    selected = selectedGroupId == group.id,
                    onClick = { onGroupSelect(group.id) },
                    label = { Text(group.name) }
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            groups.forEach { group ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = group.name,
                                style = MaterialTheme.typography.titleSmall,
                                color = Color(0xFF20243A)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${group.wordCount} слов",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF66708A)
                            )
                        }

                        OutlinedButton(
                            onClick = { onTrainGroupClick(group) },
                            enabled = group.wordCount > 0
                        ) {
                            Text("Учить")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DictionaryWordRow(
    word: DictionaryWordListItem,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 4.dp, height = 42.dp)
                    .background(
                        color = Color(0xFFCFE0FF),
                        shape = MaterialTheme.shapes.small
                    )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = word.bgWord,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF20243A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = word.ruTranslation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF66708A),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить слово",
                    tint = Color(0xFF8A94AE)
                )
            }
        }
    }
}

@Composable
private fun EmptyDictionaryBlock(
    title: String,
    message: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF20243A)
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF66708A)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DictionaryScreenContentPreview() {
    OIiglot_BulgaryTheme {
        DictionaryScreenContent(
            uiState = DictionaryListUiState(
                query = "",
                selectedGroupId = 2L,
                words = listOf(
                    DictionaryWordListItem(
                        id = 1L,
                        bgWord = "здравей",
                        ruTranslation = "привет"
                    ),
                    DictionaryWordListItem(
                        id = 2L,
                        bgWord = "благодаря",
                        ruTranslation = "спасибо"
                    )
                ),
                groups = listOf(
                    WordGroup(id = 1L, name = "Еда", wordCount = 4),
                    WordGroup(id = 2L, name = "Поездка", wordCount = 7)
                )
            ),
            hasAnyWordsForTraining = true,
            onBackClick = {},
            onAddWordClick = {},
            onTrainAllClick = {},
            onTrainGroupClick = {},
            onWordClick = {},
            onQueryChange = {},
            onGroupSelect = {},
            onDeleteWordClick = {},
            onDismissError = {}
        )
    }
}
