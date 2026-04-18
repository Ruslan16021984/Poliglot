package com.carbit3333333.oiiglot_bulgary.ui.lessons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
    onBackClick: () -> Unit,
    onLessonClick: (Int) -> Unit = {},
    viewModel: LessonsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LessonsScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onLessonClick = onLessonClick,
        onUnlockAllClick = { viewModel.unlockAllLessons() },
        onResetLessonsClick = { viewModel.resetLessons() }
    )
}

@Composable
fun LessonsScreenContent(
    uiState: LessonsUiState,
    onBackClick: () -> Unit,
    onLessonClick: (Int) -> Unit,
    onUnlockAllClick: () -> Unit = {},
    onResetLessonsClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp, bottom = 56.dp, top = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBackClick) {
            Text(text = "Назад")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Список уроков",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onUnlockAllClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Открыть все уроки")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onResetLessonsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сбросить прогресс уроков")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            uiState.isLoading -> {
                Text(text = "Загрузка...")
            }

            uiState.errorMessage != null -> {
                Text(text = uiState.errorMessage ?: "Ошибка")
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
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
private fun LessonItem(
    lesson: Lesson,
    onClick: () -> Unit
) {
    val containerColor = if (lesson.isLocked) Color(0xFFF0F0F0) else Color.White
    val textColor = if (lesson.isLocked) Color(0xFF9E9E9E) else Color.Unspecified

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = !lesson.isLocked,
                onClick = onClick
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
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
                    color = textColor
                )

                Spacer(modifier = Modifier.height(8.dp))

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
                        phrases = emptyList(),
                        questions = emptyList(),
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
                        phrases = emptyList(),
                        questions = emptyList(),
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
                        phrases = emptyList(),
                        questions = emptyList(),
                        isLocked = true
                    )
                )
            ),
            onBackClick = {},
            onLessonClick = {},
            onUnlockAllClick = {},
            onResetLessonsClick = {}
        )
    }
}