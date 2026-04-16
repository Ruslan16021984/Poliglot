package com.carbit3333333.oiiglot_bulgary.ui.lessons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carbit3333333.oiiglot_bulgary.model.Question
import com.carbit3333333.oiiglot_bulgary.ui.theme.OIiglot_BulgaryTheme
import com.carbit3333333.oiiglot_bulgary.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    lessonId: Int,
    onBackClick: () -> Unit,
    viewModel: QuizViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(lessonId) {
        viewModel.loadQuiz(lessonId)
    }

    QuizScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onAnswerSelect = viewModel::selectAnswer,
        onSubmitClick = viewModel::submitAnswer,
        onRestartClick = viewModel::restartQuiz
    )
}

@Composable
fun QuizScreenContent(
    uiState: QuizUiState,
    onBackClick: () -> Unit,
    onAnswerSelect: (String) -> Unit,
    onSubmitClick: () -> Unit,
    onRestartClick: () -> Unit
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

        when {
            uiState.isLoading -> {
                Text(text = "Загрузка...")
            }

            uiState.errorMessage != null -> {
                Text(text = uiState.errorMessage ?: "Ошибка")
            }

            uiState.isCompleted -> {
                Text(
                    text = "Упражнения завершены",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Правильных ответов: ${uiState.correctAnswersCount} из ${uiState.questions.size}",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onRestartClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Пройти заново")
                }
            }

            uiState.currentQuestion != null -> {
                val question = uiState.currentQuestion

                Text(
                    text = "Вопрос ${uiState.currentQuestionIndex + 1} из ${uiState.questions.size}",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = question?.text.orEmpty(),
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    question?.options?.forEach { option ->
                        AnswerOption(
                            text = option,
                            selected = uiState.selectedAnswer == option,
                            onClick = { onAnswerSelect(option) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onSubmitClick,
                    enabled = uiState.selectedAnswer != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ответить")
                }
            }
        }
    }
}

@Composable
private fun AnswerOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            RadioButton(
                selected = selected,
                onClick = null
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun QuizScreenContentPreview() {
    OIiglot_BulgaryTheme {
        QuizScreenContent(
            uiState = QuizUiState(
                lessonId = 1,
                questions = listOf(
                    Question(
                        id = 1,
                        text = "Как переводится «Здравей»?",
                        options = listOf("Спасибо", "Привет", "Пока", "Нет"),
                        correctAnswer = "Привет"
                    )
                ),
                selectedAnswer = "Привет"
            ),
            onBackClick = {},
            onAnswerSelect = {},
            onSubmitClick = {},
            onRestartClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizResultContentPreview() {
    OIiglot_BulgaryTheme {
        QuizScreenContent(
            uiState = QuizUiState(
                lessonId = 1,
                questions = listOf(
                    Question(
                        id = 1,
                        text = "Как переводится «Здравей»?",
                        options = listOf("Спасибо", "Привет"),
                        correctAnswer = "Привет"
                    )
                ),
                correctAnswersCount = 1,
                isCompleted = true
            ),
            onBackClick = {},
            onAnswerSelect = {},
            onSubmitClick = {},
            onRestartClick = {}
        )
    }
}