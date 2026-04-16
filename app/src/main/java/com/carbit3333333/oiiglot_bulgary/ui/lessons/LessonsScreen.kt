package com.carbit3333333.oiiglot_bulgary.ui.lessons

import androidx.compose.foundation.background
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
        onLessonClick = onLessonClick
    )
}

@Composable
fun LessonsScreenContent(
    uiState: LessonsUiState,
    onBackClick: () -> Unit,
    onLessonClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Button(onClick = onBackClick) {
            Text(text = "Назад")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Список уроков",
            style = MaterialTheme.typography.headlineSmall
        )

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

                if (lesson.isLocked) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Откроется после прохождения предыдущего урока",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF9E9E9E)
                    )
                } else {
                    lesson.bestScore?.let { score ->
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Балл: ${String.format(java.util.Locale.US, "%.1f", score)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (lesson.isCompleted) Color(0xFF2E7D32) else Color(0xFF666666)
                        )
                    }
                }
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
                        isCompleted = true,
                        isLocked = false,
                        bestScore = 6.7f
                    ),
                    Lesson(
                        id = 2,
                        title = "Урок 2",
                        subtitle = "Знакомство",
                        theory = emptyList(),
                        phrases = emptyList(),
                        questions = emptyList(),
                        isLocked = true
                    )
                )
            ),
            onBackClick = {},
            onLessonClick = {}
        )
    }
}