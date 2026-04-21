package com.carbit3333333.oiiglot_bulgary.ui.lessons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carbit3333333.oiiglot_bulgary.model.Lesson
import com.carbit3333333.oiiglot_bulgary.ui.theme.OIiglot_BulgaryTheme
import com.carbit3333333.oiiglot_bulgary.viewmodel.LessonsViewModel
import java.util.Locale

@Composable
fun LessonsScreen(
    onBackClick: (() -> Unit)? = null,
    onLessonClick: (Int) -> Unit = {},
    onDictionaryClick: () -> Unit = {},
    viewModel: LessonsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LessonsScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onLessonClick = onLessonClick,
        onDictionaryClick = onDictionaryClick,
        onUnlockAllClick = { viewModel.unlockAllLessons() },
        onResetLessonsClick = { viewModel.resetLessons() }
    )
}

@Composable
fun LessonsScreenContent(
    uiState: LessonsUiState,
    onBackClick: (() -> Unit)?,
    onLessonClick: (Int) -> Unit,
    onDictionaryClick: () -> Unit,
    onUnlockAllClick: () -> Unit = {},
    onResetLessonsClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        if (onBackClick != null) {
            Button(onClick = onBackClick) {
                Text(text = "Назад")
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        LessonsHeader(uiState = uiState)

        Spacer(modifier = Modifier.height(16.dp))

        LessonActionsRow(
            onDictionaryClick = onDictionaryClick,
            onUnlockAllClick = onUnlockAllClick,
            onResetLessonsClick = onResetLessonsClick
        )

        Spacer(modifier = Modifier.height(18.dp))

        when {
            uiState.isLoading -> {
                Text(text = "Загрузка...")
            }

            uiState.errorMessage != null -> {
                Text(text = uiState.errorMessage ?: "Ошибка")
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(uiState.lessons) { lesson ->
                        LessonItem(
                            lesson = lesson,
                            onClick = {
                                if (!lesson.isLocked) {
                                    onLessonClick(lesson.id)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonsHeader(
    uiState: LessonsUiState
) {
    val totalLessons = uiState.lessons.size
    val completedLessons = uiState.lessons.count { it.isCompleted }
    val availableLessons = uiState.lessons.count { !it.isLocked }
    val inProgressLessons = uiState.lessons.count { !it.isLocked && it.currentProgress > 0 && !it.isCompleted }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color(0xFFF7F8FC)
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
                    color = Color(0xFFE5ECFF),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = totalLessons.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF39538D)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Список уроков",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFF20243A)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Продолжай обучение с того места, где остановился",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6A728F)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HeaderStat(
                    modifier = Modifier.weight(1f),
                    label = "Доступно",
                    value = "$availableLessons/$totalLessons",
                    accent = Color(0xFF5C74AF)
                )
                HeaderStat(
                    modifier = Modifier.weight(1f),
                    label = "В процессе",
                    value = inProgressLessons.toString(),
                    accent = Color(0xFF2E7DCE)
                )
                HeaderStat(
                    modifier = Modifier.weight(1f),
                    label = "Завершено",
                    value = completedLessons.toString(),
                    accent = Color(0xFF2E8B57)
                )
            }
        }
    }
}

@Composable
private fun HeaderStat(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    accent: Color
) {
    Surface(
        modifier = modifier,
        color = Color.White,
        shape = MaterialTheme.shapes.large,
        tonalElevation = 1.dp,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = accent
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF6A728F)
            )
        }
    }
}

@Composable
private fun LessonActionsRow(
    onDictionaryClick: () -> Unit,
    onUnlockAllClick: () -> Unit,
    onResetLessonsClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onDictionaryClick),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFEAF1FF)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Мои слова",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF20243A)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Открой личный словарь и запусти тренировку по своим словам.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF5C6783)
                    )
                }

                OutlinedButton(onClick = onDictionaryClick) {
                    Text("Открыть")
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
        OutlinedButton(
            onClick = onUnlockAllClick,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 14.dp)
        ) {
            Text("Открыть все")
        }

        OutlinedButton(
            onClick = onResetLessonsClick,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 14.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Сбросить")
        }
    }
}
}

@Composable
private fun LessonItem(
    lesson: Lesson,
    onClick: () -> Unit
) {
    val containerColor = if (lesson.isLocked) Color(0xFFF3F4F7) else Color.White
    val textColor = if (lesson.isLocked) Color(0xFF9E9E9E) else Color(0xFF20243A)
    val subtitleColor = if (lesson.isLocked) Color(0xFFAAAAAA) else Color(0xFF4F566F)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = !lesson.isLocked,
                onClick = onClick
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = lesson.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = lesson.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = subtitleColor
                )

                Spacer(modifier = Modifier.height(12.dp))

                val progressText = when {
                    lesson.isLocked -> "Прогресс: недоступно"
                    lesson.totalProgress > 0 -> {
                        "Прогресс: ${lesson.currentProgress}/${lesson.totalProgress} (${lesson.progressPercent}%)"
                    }
                    else -> "Прогресс: 0%"
                }

                val progressColor = when {
                    lesson.isLocked -> Color(0xFF9E9E9E)
                    lesson.progressPercent in 1..99 -> Color(0xFF1565C0)
                    lesson.progressPercent >= 100 -> Color(0xFF2E7D32)
                    else -> Color(0xFF666666)
                }

                Text(
                    text = progressText,
                    style = MaterialTheme.typography.bodySmall,
                    color = progressColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                val resultText = when {
                    lesson.isLocked -> "Откроется после прохождения предыдущего урока"
                    lesson.isCompleted -> "Результат: пройден"
                    lesson.bestScore != null -> "Результат: ${String.format(Locale.US, "%.1f", lesson.bestScore)}"
                    lesson.currentProgress > 0 -> "Результат: ещё не завершён"
                    else -> "Результат: ещё не проходили"
                }

                val resultColor = when {
                    lesson.isLocked -> Color(0xFF9E9E9E)
                    lesson.isCompleted -> Color(0xFF2E7D32)
                    lesson.bestScore != null -> Color(0xFF666666)
                    lesson.currentProgress > 0 -> Color(0xFF1565C0)
                    else -> Color(0xFF666666)
                }

                Text(
                    text = resultText,
                    style = MaterialTheme.typography.bodySmall,
                    color = resultColor
                )
            }

            if (lesson.isLocked) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = Color(0xFF9E9E9E)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LessonsScreenContentPreview() {
    OIiglot_BulgaryTheme {
        LessonsScreenContent(
            uiState = LessonsUiState(
                lessons = listOf(
                    Lesson(
                        id = 1,
                        title = "Урок 1",
                        subtitle = "Приветствие и базовые фразы",
                        theory = emptyList(),
                        isCompleted = false,
                        isLocked = false,
                        bestScore = null,
                        currentProgress = 3,
                        totalProgress = 100
                    ),
                    Lesson(
                        id = 2,
                        title = "Урок 2",
                        subtitle = "Знакомство",
                        theory = emptyList(),
                        isCompleted = true,
                        isLocked = false,
                        bestScore = 8.4f,
                        currentProgress = 100,
                        totalProgress = 100
                    ),
                    Lesson(
                        id = 3,
                        title = "Урок 3",
                        subtitle = "Прошедшее время",
                        theory = emptyList(),
                        isLocked = true
                    )
                )
            ),
            onBackClick = {},
            onLessonClick = {},
            onDictionaryClick = {},
            onUnlockAllClick = {},
            onResetLessonsClick = {}
        )
    }
}
