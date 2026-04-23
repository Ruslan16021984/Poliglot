package com.carbit3333333.oiiglot_bulgary.ui.lessons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carbit3333333.oiiglot_bulgary.R
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
    val colorScheme = MaterialTheme.colorScheme
    val isDarkTheme = isSystemInDarkTheme()
    val elevatedCardColor = if (isDarkTheme) colorScheme.surfaceContainer else colorScheme.surfaceContainerLow
    val wordCardColor = if (isDarkTheme) colorScheme.surfaceContainerHigh else colorScheme.surfaceContainerLow

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
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
                onBackClick = onBackClick,
                containerColor = colorScheme.primary,
                onPrimaryColor = colorScheme.onPrimary,
                correctAccent = colorScheme.tertiary,
                wrongAccent = colorScheme.error,
            )

            if (currentExercise != null) {
                Spacer(modifier = Modifier.height(14.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = elevatedCardColor
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Text(
                            text = currentExercise.sourceText,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = colorScheme.onSurface,
                        )

                        currentExercise.hint?.let {
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodySmall,
                                color = colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
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
                        containerColor = elevatedCardColor,
                        chipColor = if (isDarkTheme) colorScheme.secondaryContainer.copy(alpha = 0.92f) else colorScheme.secondaryContainer,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    WordGrid(
                        words = currentExercise.availableWords,
                        selectedWords = uiState.selectedWords,
                        onWordClick = onWordClick,
                        cardColor = wordCardColor,
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
                                    color = colorScheme.tertiary
                                )
                            }

                            ExerciseResult.WRONG -> {
                                Text(
                                    text = stringResource(R.string.lesson_session_wrong_continue),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = colorScheme.onSurfaceVariant,
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
                        text = stringResource(R.string.lesson_session_no_exercises),
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
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    )
                ) {
                    Text(stringResource(R.string.common_check))
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
    onBackClick: () -> Unit,
    containerColor: Color,
    onPrimaryColor: Color,
    correctAccent: Color,
    wrongAccent: Color,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(containerColor)
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.common_back),
            tint = onPrimaryColor,
            modifier = Modifier.clickable(onClick = onBackClick)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = lessonTitle,
            color = onPrimaryColor,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )

        CounterItem(
            label = correctCount.toString(),
            circleColor = onPrimaryColor,
            textColor = correctAccent
        )

        Spacer(modifier = Modifier.width(10.dp))

        CounterItem(
            label = wrongCount.toString(),
            circleColor = onPrimaryColor,
            textColor = wrongAccent
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
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFD9EBD7))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            color = colorScheme.onTertiaryContainer,
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
    val colorScheme = MaterialTheme.colorScheme
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.errorContainer)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = if (selectedText.isBlank()) " " else selectedText,
                color = colorScheme.onErrorContainer,
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
                color = colorScheme.onTertiaryContainer,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(12.dp))

            IconButton(onClick = { onSpeakClick(correctText) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = stringResource(R.string.common_listen),
                    tint = colorScheme.onTertiaryContainer,
                )
            }
        }
    }
}

@Composable
private fun CorrectAnswerBlock(
    answerText: String,
    praiseText: String?,
    onSpeakClick: (String) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
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
                color = colorScheme.onTertiaryContainer,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(12.dp))

            IconButton(onClick = { onSpeakClick(answerText) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = stringResource(R.string.common_listen),
                    tint = colorScheme.onTertiaryContainer,
                )
            }
        }

        if (!praiseText.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun AnswerArea(
    selectedWords: List<String>,
    onWordClick: (String) -> Unit,
    containerColor: Color,
    chipColor: Color,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        if (selectedWords.isEmpty()) {
            Text(
                text = stringResource(R.string.lesson_session_select_words),
                modifier = Modifier.padding(16.dp),
                color = colorScheme.onSurfaceVariant,
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
                        onClick = { onWordClick(word) },
                        containerColor = chipColor,
                        textColor = colorScheme.onSecondaryContainer,
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectedWordChip(
    text: String,
    onClick: () -> Unit,
    containerColor: Color,
    textColor: Color,
) {
    Box(
        modifier = Modifier
            .background(
                color = containerColor,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor,
        )
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun WordGrid(
    words: List<String>,
    selectedWords: List<String>,
    onWordClick: (String) -> Unit,
    cardColor: Color,
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
                onClick = { onWordClick(word) },
                containerColor = cardColor,
            )
        }
    }
}

@Composable
private fun WordButton(
    text: String,
    onClick: () -> Unit,
    containerColor: Color,
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(88.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
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
                ExerciseResult.NONE -> MaterialTheme.colorScheme.outlineVariant
                ExerciseResult.CORRECT -> Color(0xFF93F189)
                ExerciseResult.WRONG -> MaterialTheme.colorScheme.error
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
