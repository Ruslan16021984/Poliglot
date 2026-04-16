package com.carbit3333333.oiiglot_bulgary.ui.lessons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carbit3333333.oiiglot_bulgary.model.Lesson
import com.carbit3333333.oiiglot_bulgary.model.Phrase
import com.carbit3333333.oiiglot_bulgary.model.Question
import com.carbit3333333.oiiglot_bulgary.model.TheoryBlock
import com.carbit3333333.oiiglot_bulgary.ui.common.HighlightedEndingText
import com.carbit3333333.oiiglot_bulgary.ui.theme.OIiglot_BulgaryTheme
import com.carbit3333333.oiiglot_bulgary.viewmodel.LessonViewModel

@Composable
fun LessonScreen(
    lessonId: Int,
    onBackClick: () -> Unit,
    onStartExerciseClick: (Int) -> Unit = {},
    viewModel: LessonViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(lessonId) {
        viewModel.loadLesson(lessonId)
    }

    LessonScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onStartExerciseClick = onStartExerciseClick
    )
}

@Composable
fun LessonScreenContent(
    uiState: LessonUiState,
    onBackClick: () -> Unit,
    onStartExerciseClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .navigationBarsPadding()
    ) {
        Button(onClick = onBackClick) {
            Text(text = "Назад")
        }

        Spacer(modifier = Modifier.height(24.dp))

        when {
            uiState.isLoading -> {
                Text(text = "Загрузка...")
            }

            uiState.errorMessage != null -> {
                Text(text = uiState.errorMessage ?: "Ошибка")
            }

            uiState.lesson != null -> {
                val lesson = uiState.lesson

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = lesson.title,
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = lesson.subtitle,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    item {
                        Text(
                            text = "Теория",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    items(lesson.theory) { theoryBlock ->
                        TheoryBlockItem(theoryBlock = theoryBlock)
                    }
                    item {
                        Text(
                            text = "Окончания глаголов",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    item {
                        VerbEndingExamples()
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onStartExerciseClick(lesson.id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Начать упражнения")
                }
            }
        }
    }
}

@Composable
private fun VerbEndingExamples() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Обрати внимание, как меняются окончания:",
                style = MaterialTheme.typography.bodyLarge
            )

            HighlightedEndingText(word = "правя", ending = "я")
            HighlightedEndingText(word = "правиш", ending = "иш")
            HighlightedEndingText(word = "прави", ending = "и")
            HighlightedEndingText(word = "правим", ending = "им")
            HighlightedEndingText(word = "правите", ending = "ите")
            HighlightedEndingText(word = "правят", ending = "ят")
        }
    }
}

@Composable
private fun TheoryBlockItem(
    theoryBlock: TheoryBlock
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            theoryBlock.title?.let { title ->
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            theoryBlock.text?.let { text ->
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LessonScreenContentPreview() {
    OIiglot_BulgaryTheme {
        LessonScreenContent(
            uiState = LessonUiState(
                lesson = Lesson(
                    id = 1,
                    title = "Урок 1",
                    subtitle = "Приветствие и базовые фразы",
                    theory = listOf(
                        TheoryBlock(
                            title = "Настоящее время",
                            text = "В болгарском языке, как и в русском, сначала идёт местоимение, затем глагол.\n\nАз правя\nТи правиш\nТой прави"
                        ),
                        TheoryBlock(
                            title = "Отрицание",
                            text = "Отрицание образуется с помощью частицы \"не\".\n\nАз не правя\nТи не гледаш"
                        )
                    ),
                    phrases = listOf(
                        Phrase("Добро утро", "Доброе утро"),
                        Phrase("Добър ден", "Добрый день"),
                        Phrase("Благодаря", "Спасибо")
                    ),
                    questions = listOf(
                        Question(
                            id = 1,
                            text = "Как переводится «Добро утро»?",
                            options = listOf("Доброе утро", "Спасибо"),
                            correctAnswer = "Доброе утро"
                        )
                    )
                )
            ),
            onBackClick = {},
            onStartExerciseClick = {}
        )
    }
}