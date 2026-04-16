package com.carbit3333333.oiiglot_bulgary.ui.lessons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carbit3333333.oiiglot_bulgary.model.ExerciseResult
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise
import com.carbit3333333.oiiglot_bulgary.model.LessonResult
import com.carbit3333333.oiiglot_bulgary.ui.theme.OIiglot_BulgaryTheme
import com.carbit3333333.oiiglot_bulgary.utils.AppTextToSpeech
import com.carbit3333333.oiiglot_bulgary.viewmodel.LessonSessionViewModel

@Composable
fun LessonSessionScreen(
    lessonId: Int,
    onBackClick: () -> Unit,
    onLessonFinished: (correctCount: Int, wrongCount: Int) -> Unit = { _, _ -> },
    viewModel: LessonSessionViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val textToSpeech = remember { AppTextToSpeech(context) }

    LaunchedEffect(lessonId) {
        viewModel.loadLessonSession(lessonId)
    }

    LaunchedEffect(uiState.lessonResult) {
        val result = uiState.lessonResult ?: return@LaunchedEffect
        onLessonFinished(
            result.correctCount,
            result.wrongCount
        )
    }

    LaunchedEffect(uiState.currentExercise?.id, uiState.currentResult) {
        val currentExercise = uiState.currentExercise ?: return@LaunchedEffect

        if (
            uiState.currentResult == ExerciseResult.CORRECT ||
            uiState.currentResult == ExerciseResult.WRONG
        ) {
            val correctText = currentExercise.correctAnswerWords.joinToString(" ")
            textToSpeech.speak(correctText)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            textToSpeech.shutdown()
        }
    }

    LessonSessionScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onWordClick = viewModel::selectWord,
        onSelectedWordClick = viewModel::removeSelectedWord,
        onCheckClick = viewModel::checkAnswer,
        onWrongAnswerScreenTap = viewModel::onWrongAnswerScreenTap,
        onSpeakClick = { text ->
            textToSpeech.speak(text)
        }
    )
}

@Composable
fun LessonSessionScreenContent(
    uiState: LessonSessionUiState,
    onBackClick: () -> Unit,
    onWordClick: (String) -> Unit,
    onSelectedWordClick: (String) -> Unit,
    onCheckClick: () -> Unit,
    onWrongAnswerScreenTap: () -> Unit,
    onSpeakClick: (String) -> Unit
) {
    val currentExercise = uiState.currentExercise

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F3))
            .clickable(
                enabled = uiState.currentResult == ExerciseResult.WRONG,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onWrongAnswerScreenTap()
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            SessionTopBar(
                lessonTitle = uiState.lessonTitle,
                correctCount = uiState.correctCount,
                wrongCount = uiState.wrongCount,
                onBackClick = onBackClick
            )

            if (currentExercise != null) {
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = currentExercise.sourceText,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                when (uiState.currentResult) {
                    ExerciseResult.NONE -> {
                        InstructionBlock(
                            text = currentExercise.instruction
                        )
                    }

                    ExerciseResult.CORRECT -> {
                        CorrectAnswerBlock(
                            answerText = currentExercise.correctAnswerWords.joinToString(" "),
                            praiseText = uiState.praiseText,
                            onSpeakClick = onSpeakClick
                        )
                    }

                    ExerciseResult.WRONG -> {
                        WrongAnswerBlock(
                            selectedText = uiState.selectedWords.joinToString(" "),
                            correctText = currentExercise.correctAnswerWords.joinToString(" "),
                            onSpeakClick = onSpeakClick
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.currentResult == ExerciseResult.NONE) {
                    AnswerArea(
                        selectedWords = uiState.selectedWords,
                        onWordClick = onSelectedWordClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    WordGrid(
                        words = currentExercise.availableWords,
                        selectedWords = uiState.selectedWords,
                        onWordClick = onWordClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        when (uiState.currentResult) {
                            ExerciseResult.CORRECT -> {
                                Text(
                                    text = uiState.praiseText ?: "",
                                    style = MaterialTheme.typography.displayMedium,
                                    color = Color(0xFF4CAF50)
                                )
                            }

                            ExerciseResult.WRONG -> {
                                Text(
                                    text = "Нажмите на экран, чтобы перейти к следующему тесту.",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(horizontal = 24.dp)
                                )
                            }

                            ExerciseResult.NONE -> Unit
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Нет упражнений",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            if (uiState.currentResult == ExerciseResult.NONE && currentExercise != null) {
                Button(
                    onClick = onCheckClick,
                    enabled = uiState.selectedWords.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2E7D32)
                    )
                ) {
                    Text("Проверить")
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            ProgressStrip(
                results = uiState.results,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun SessionTopBar(
    lessonTitle: String,
    correctCount: Int,
    wrongCount: Int,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF4CAF50))
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Назад",
            tint = Color.White,
            modifier = Modifier.clickable(onClick = onBackClick)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = lessonTitle,
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )

        CounterItem(
            label = correctCount.toString(),
            circleColor = Color.White,
            textColor = Color(0xFF2E7D32)
        )

        Spacer(modifier = Modifier.width(10.dp))

        CounterItem(
            label = wrongCount.toString(),
            circleColor = Color.White,
            textColor = Color(0xFFC62828)
        )
    }
}

@Composable
private fun CounterItem(
    label: String,
    circleColor: Color,
    textColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(circleColor, CircleShape)
                .padding(horizontal = 8.dp, vertical = 3.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = textColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun InstructionBlock(
    text: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFD9EBD7))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            color = Color(0xFF4B6A4B),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun WrongAnswerBlock(
    selectedText: String,
    correctText: String,
    onSpeakClick: (String) -> Unit
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF6D7D7))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = if (selectedText.isBlank()) " " else selectedText,
                color = Color(0xFFC62828),
                style = MaterialTheme.typography.titleLarge
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD9EBD7))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = correctText,
                color = Color(0xFF2E7D32),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "🔊",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.clickable {
                    onSpeakClick(correctText)
                }
            )
        }
    }
}

@Composable
private fun CorrectAnswerBlock(
    answerText: String,
    praiseText: String?,
    onSpeakClick: (String) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD9EBD7))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = answerText,
                color = Color(0xFF2E7D32),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "🔊",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.clickable {
                    onSpeakClick(answerText)
                }
            )
        }

        if (!praiseText.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun AnswerArea(
    selectedWords: List<String>,
    onWordClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        if (selectedWords.isEmpty()) {
            Text(
                text = "Выберите слова",
                modifier = Modifier.padding(16.dp),
                color = Color.Gray,
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            FlowRow(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selectedWords.forEach { word ->
                    SelectedWordChip(
                        text = word,
                        onClick = { onWordClick(word) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectedWordChip(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = Color(0xFFE8F5E9),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun WordGrid(
    words: List<String>,
    selectedWords: List<String>,
    onWordClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val visibleWords = words.filterNot { selectedWords.contains(it) }

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        visibleWords.forEach { word ->
            WordButton(
                text = word,
                onClick = { onWordClick(word) }
            )
        }
    }
}

@Composable
private fun WordButton(
    text: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(88.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color(0xFF666666),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Composable
private fun ProgressStrip(
    results: List<ExerciseResult>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        results.forEach { result ->
            val color = when (result) {
                ExerciseResult.NONE -> Color(0xFFBDBDBD)
                ExerciseResult.CORRECT -> Color(0xFF4CAF50)
                ExerciseResult.WRONG -> Color(0xFFE53935)
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .background(color)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LessonSessionScreenPreview() {
    OIiglot_BulgaryTheme {
        Surface {
            LessonSessionScreenContent(
                uiState = LessonSessionUiState(
                    lessonTitle = "Урок 1",
                    exercises = listOf(
                        LessonExercise(
                            id = 1,
                            sourceText = "Ты будешь есть?",
                            instruction = "Переведите предложение",
                            correctAnswerWords = listOf("Ти", "ще", "ядеш", "ли"),
                            availableWords = listOf("Ти", "ще", "ядеш", "ли", "Аз", "не", "правя", "да")
                        )
                    ),
                    currentExerciseIndex = 0,
                    selectedWords = listOf("Ти", "ще"),
                    results = listOf(
                        ExerciseResult.CORRECT,
                        ExerciseResult.WRONG,
                        ExerciseResult.NONE,
                        ExerciseResult.NONE,
                        ExerciseResult.NONE,
                        ExerciseResult.NONE,
                        ExerciseResult.NONE,
                        ExerciseResult.NONE,
                        ExerciseResult.NONE,
                        ExerciseResult.NONE
                    ),
                    currentResult = ExerciseResult.NONE,
                    correctCount = 1,
                    wrongCount = 1,
                    lessonResult = null
                ),
                onBackClick = {},
                onWordClick = {},
                onSelectedWordClick = {},
                onCheckClick = {},
                onWrongAnswerScreenTap = {},
                onSpeakClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LessonSessionWrongPreview() {
    OIiglot_BulgaryTheme {
        Surface {
            LessonSessionScreenContent(
                uiState = LessonSessionUiState(
                    lessonTitle = "Урок 1",
                    exercises = listOf(
                        LessonExercise(
                            id = 1,
                            sourceText = "Ты не будешь есть",
                            instruction = "Переведите предложение",
                            correctAnswerWords = listOf("Ти", "няма", "да", "ядеш"),
                            availableWords = listOf("Ти", "няма", "да", "ядеш", "Аз", "ще", "ли", "правя")
                        )
                    ),
                    currentExerciseIndex = 0,
                    selectedWords = listOf("Ти", "ще", "ядеш"),
                    results = listOf(
                        ExerciseResult.CORRECT,
                        ExerciseResult.WRONG,
                        ExerciseResult.NONE
                    ),
                    currentResult = ExerciseResult.WRONG,
                    correctCount = 1,
                    wrongCount = 1,
                    lessonResult = null
                ),
                onBackClick = {},
                onWordClick = {},
                onSelectedWordClick = {},
                onCheckClick = {},
                onWrongAnswerScreenTap = {},
                onSpeakClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LessonSessionCorrectPreview() {
    OIiglot_BulgaryTheme {
        Surface {
            LessonSessionScreenContent(
                uiState = LessonSessionUiState(
                    lessonTitle = "Урок 1",
                    exercises = listOf(
                        LessonExercise(
                            id = 1,
                            sourceText = "Ты не будешь есть",
                            instruction = "Переведите предложение",
                            correctAnswerWords = listOf("Ти", "няма", "да", "ядеш"),
                            availableWords = listOf("Ти", "няма", "да", "ядеш", "Аз", "ще", "ли", "правя")
                        )
                    ),
                    currentExerciseIndex = 0,
                    selectedWords = listOf("Ти", "няма", "да", "ядеш"),
                    results = listOf(
                        ExerciseResult.CORRECT,
                        ExerciseResult.CORRECT,
                        ExerciseResult.NONE
                    ),
                    currentResult = ExerciseResult.CORRECT,
                    praiseText = "Талант!",
                    correctCount = 2,
                    wrongCount = 0,
                    lessonResult = LessonResult(
                        lessonId = 1,
                        lessonTitle = "Урок 1",
                        totalExercises = 3,
                        correctCount = 2,
                        wrongCount = 0,
                        score = 6.7f,
                        isPassed = true
                    )
                ),
                onBackClick = {},
                onWordClick = {},
                onSelectedWordClick = {},
                onCheckClick = {},
                onWrongAnswerScreenTap = {},
                onSpeakClick = {}
            )
        }
    }
}