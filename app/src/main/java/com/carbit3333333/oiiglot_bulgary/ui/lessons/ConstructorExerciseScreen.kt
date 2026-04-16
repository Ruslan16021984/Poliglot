package com.carbit3333333.oiiglot_bulgary.ui.lessons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carbit3333333.oiiglot_bulgary.ui.theme.OIiglot_BulgaryTheme

data class ConstructorWordUi(
    val id: Int,
    val text: String,
    val isUsed: Boolean = false
)

enum class AnswerStatus {
    NONE,
    CORRECT,
    WRONG
}

@Composable
fun ConstructorExerciseScreen(
    onBackClick: () -> Unit = {}
) {
    val correctAnswerWords = remember {
        listOf("Ще", "отиде", "ли", "той")
    }

    val words = remember {
        mutableStateListOf(
            ConstructorWordUi(id = 1, text = "Ще"),
            ConstructorWordUi(id = 2, text = "отиде"),
            ConstructorWordUi(id = 3, text = "ли"),
            ConstructorWordUi(id = 4, text = "той"),
            ConstructorWordUi(id = 5, text = "ние"),
            ConstructorWordUi(id = 6, text = "съм"),
            ConstructorWordUi(id = 7, text = "те"),
            ConstructorWordUi(id = 8, text = "правя")
        )
    }

    val selectedWords = remember { mutableStateListOf<ConstructorWordUi>() }
    var answerStatus by remember { mutableStateOf(AnswerStatus.NONE) }
    var correctCount by remember { mutableIntStateOf(0) }
    var wrongCount by remember { mutableIntStateOf(0) }

    ConstructorExerciseScreenContent(
        lessonTitle = "Урок 1",
        sourceText = "Он пойдет?",
        instruction = "Переведите предложение",
        currentStep = 1,
        totalSteps = 10,
        correctCount = correctCount,
        wrongCount = wrongCount,
        words = words,
        selectedWords = selectedWords,
        answerStatus = answerStatus,
        correctAnswerWords = correctAnswerWords,
        onBackClick = onBackClick,
        onWordClick = { word ->
            if (!word.isUsed && answerStatus == AnswerStatus.NONE) {
                val index = words.indexOfFirst { it.id == word.id }
                if (index != -1) {
                    words[index] = words[index].copy(isUsed = true)
                    selectedWords.add(word)
                }
            }
        },
        onSelectedWordClick = { word ->
            if (answerStatus != AnswerStatus.NONE) return@ConstructorExerciseScreenContent

            selectedWords.remove(word)

            val index = words.indexOfFirst { it.id == word.id }
            if (index != -1) {
                words[index] = words[index].copy(isUsed = false)
            }
        },
        onRemoveLastWordClick = {
            if (answerStatus != AnswerStatus.NONE) return@ConstructorExerciseScreenContent

            val lastWord = selectedWords.lastOrNull() ?: return@ConstructorExerciseScreenContent
            selectedWords.remove(lastWord)

            val index = words.indexOfFirst { it.id == lastWord.id }
            if (index != -1) {
                words[index] = words[index].copy(isUsed = false)
            }
        },
        onClearClick = {
            selectedWords.forEach { selected ->
                val index = words.indexOfFirst { it.id == selected.id }
                if (index != -1) {
                    words[index] = words[index].copy(isUsed = false)
                }
            }
            selectedWords.clear()
            answerStatus = AnswerStatus.NONE
        },
        onCheckClick = {
            val selectedText = selectedWords.map { it.text }
            answerStatus = if (selectedText == correctAnswerWords) {
                correctCount += 1
                AnswerStatus.CORRECT
            } else {
                wrongCount += 1
                AnswerStatus.WRONG
            }
        },
        onTryAgainClick = {
            selectedWords.forEach { selected ->
                val index = words.indexOfFirst { it.id == selected.id }
                if (index != -1) {
                    words[index] = words[index].copy(isUsed = false)
                }
            }
            selectedWords.clear()
            answerStatus = AnswerStatus.NONE
        }
    )
}

@Composable
fun ConstructorExerciseScreenContent(
    lessonTitle: String,
    sourceText: String,
    instruction: String,
    currentStep: Int,
    totalSteps: Int,
    correctCount: Int,
    wrongCount: Int,
    words: List<ConstructorWordUi>,
    selectedWords: List<ConstructorWordUi>,
    answerStatus: AnswerStatus,
    correctAnswerWords: List<String>,
    onBackClick: () -> Unit,
    onWordClick: (ConstructorWordUi) -> Unit,
    onSelectedWordClick: (ConstructorWordUi) -> Unit,
    onRemoveLastWordClick: () -> Unit,
    onClearClick: () -> Unit,
    onCheckClick: () -> Unit,
    onTryAgainClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F4F4))
            .statusBarsPadding()
            .systemBarsPadding()
    ) {
        ConstructorTopBar(
            lessonTitle = lessonTitle,
            currentStep = currentStep,
            totalSteps = totalSteps,
            correctCount = correctCount,
            wrongCount = wrongCount,
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = sourceText,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD7EAD5))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = instruction,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF4B6A4B)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnswerArea(
            selectedWords = selectedWords,
            isEditable = answerStatus == AnswerStatus.NONE,
            onWordClick = onSelectedWordClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        if (answerStatus != AnswerStatus.NONE) {
            Spacer(modifier = Modifier.height(12.dp))

            ResultBlock(
                answerStatus = answerStatus,
                correctAnswerWords = correctAnswerWords,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(words, key = { it.id }) { word ->
                WordCard(
                    text = word.text,
                    enabled = !word.isUsed && answerStatus == AnswerStatus.NONE,
                    onClick = { onWordClick(word) }
                )
            }
        }

        Divider()

        when (answerStatus) {
            AnswerStatus.NONE -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onRemoveLastWordClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Удалить")
                    }

                    OutlinedButton(
                        onClick = onClearClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Очистить")
                    }
                }

                Button(
                    onClick = onCheckClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    enabled = selectedWords.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2E7D32)
                    )
                ) {
                    Text("Проверить")
                }
            }

            AnswerStatus.CORRECT -> {
                Button(
                    onClick = onTryAgainClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2E7D32)
                    )
                ) {
                    Text("Собрать заново")
                }
            }

            AnswerStatus.WRONG -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onTryAgainClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Попробовать снова")
                    }

                    Button(
                        onClick = onClearClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2E7D32)
                        )
                    ) {
                        Text("Очистить")
                    }
                }
            }
        }
    }
}

@Composable
private fun ConstructorTopBar(
    lessonTitle: String,
    currentStep: Int,
    totalSteps: Int,
    correctCount: Int,
    wrongCount: Int,
    onBackClick: () -> Unit
) {
    val progress = if (totalSteps > 0) currentStep.toFloat() / totalSteps.toFloat() else 0f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF4CAF50))
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Назад",
                tint = Color.White,
                modifier = Modifier.clickable(onClick = onBackClick)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = lessonTitle,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )

            CounterBadge(
                text = "+$correctCount",
                backgroundColor = Color(0xFF2E7D32)
            )

            Spacer(modifier = Modifier.width(8.dp))

            CounterBadge(
                text = "-$wrongCount",
                backgroundColor = Color(0xFFD32F2F)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier.weight(1f),
                color = Color.White,
                trackColor = Color(0x66FFFFFF)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "$currentStep / $totalSteps",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun CounterBadge(
    text: String,
    backgroundColor: Color
) {
    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = CircleShape
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun AnswerArea(
    selectedWords: List<ConstructorWordUi>,
    isEditable: Boolean,
    onWordClick: (ConstructorWordUi) -> Unit,
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
                        text = word.text,
                        enabled = isEditable,
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
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = Color(0xFFE8F5E9),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun WordCard(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (enabled) Color.White else Color(0xFFE0E0E0)
    val textColor = if (enabled) Color(0xFF555555) else Color(0xFFAAAAAA)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .clickable(enabled = enabled, onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = textColor,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Composable
private fun ResultBlock(
    answerStatus: AnswerStatus,
    correctAnswerWords: List<String>,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (answerStatus == AnswerStatus.CORRECT) {
        Color(0xFFDFF3E2)
    } else {
        Color(0xFFF9E0E0)
    }

    val title = if (answerStatus == AnswerStatus.CORRECT) {
        "Правильно"
    } else {
        "Неправильно"
    }

    val titleColor = if (answerStatus == AnswerStatus.CORRECT) {
        Color(0xFF2E7D32)
    } else {
        Color(0xFFC62828)
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                color = titleColor,
                style = MaterialTheme.typography.titleMedium
            )

            if (answerStatus == AnswerStatus.WRONG) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Правильный ответ: ${correctAnswerWords.joinToString(" ")}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ConstructorExerciseScreenPreview() {
    OIiglot_BulgaryTheme {
        Surface {
            ConstructorExerciseScreenContent(
                lessonTitle = "Урок 1",
                sourceText = "Он пойдет?",
                instruction = "Переведите предложение",
                currentStep = 1,
                totalSteps = 10,
                correctCount = 0,
                wrongCount = 0,
                words = listOf(
                    ConstructorWordUi(id = 1, text = "Ще"),
                    ConstructorWordUi(id = 2, text = "отиде"),
                    ConstructorWordUi(id = 3, text = "ли"),
                    ConstructorWordUi(id = 4, text = "той"),
                    ConstructorWordUi(id = 5, text = "ние"),
                    ConstructorWordUi(id = 6, text = "съм"),
                    ConstructorWordUi(id = 7, text = "те"),
                    ConstructorWordUi(id = 8, text = "правя")
                ),
                selectedWords = listOf(
                    ConstructorWordUi(id = 1, text = "Ще"),
                    ConstructorWordUi(id = 2, text = "отиде")
                ),
                answerStatus = AnswerStatus.NONE,
                correctAnswerWords = listOf("Ще", "отиде", "ли", "той"),
                onBackClick = {},
                onWordClick = {},
                onSelectedWordClick = {},
                onRemoveLastWordClick = {},
                onClearClick = {},
                onCheckClick = {},
                onTryAgainClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConstructorExerciseCorrectPreview() {
    OIiglot_BulgaryTheme {
        Surface {
            ConstructorExerciseScreenContent(
                lessonTitle = "Урок 1",
                sourceText = "Он пойдет?",
                instruction = "Переведите предложение",
                currentStep = 3,
                totalSteps = 10,
                correctCount = 2,
                wrongCount = 0,
                words = listOf(
                    ConstructorWordUi(id = 1, text = "Ще", isUsed = true),
                    ConstructorWordUi(id = 2, text = "отиде", isUsed = true),
                    ConstructorWordUi(id = 3, text = "ли", isUsed = true),
                    ConstructorWordUi(id = 4, text = "той", isUsed = true)
                ),
                selectedWords = listOf(
                    ConstructorWordUi(id = 1, text = "Ще"),
                    ConstructorWordUi(id = 2, text = "отиде"),
                    ConstructorWordUi(id = 3, text = "ли"),
                    ConstructorWordUi(id = 4, text = "той")
                ),
                answerStatus = AnswerStatus.CORRECT,
                correctAnswerWords = listOf("Ще", "отиде", "ли", "той"),
                onBackClick = {},
                onWordClick = {},
                onSelectedWordClick = {},
                onRemoveLastWordClick = {},
                onClearClick = {},
                onCheckClick = {},
                onTryAgainClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConstructorExerciseWrongPreview() {
    OIiglot_BulgaryTheme {
        Surface {
            ConstructorExerciseScreenContent(
                lessonTitle = "Урок 1",
                sourceText = "Он пойдет?",
                instruction = "Переведите предложение",
                currentStep = 4,
                totalSteps = 10,
                correctCount = 2,
                wrongCount = 1,
                words = listOf(
                    ConstructorWordUi(id = 1, text = "Ще", isUsed = true),
                    ConstructorWordUi(id = 2, text = "отиде", isUsed = true),
                    ConstructorWordUi(id = 3, text = "ли", isUsed = false),
                    ConstructorWordUi(id = 4, text = "той", isUsed = false),
                    ConstructorWordUi(id = 5, text = "ние", isUsed = true)
                ),
                selectedWords = listOf(
                    ConstructorWordUi(id = 1, text = "Ще"),
                    ConstructorWordUi(id = 5, text = "ние"),
                    ConstructorWordUi(id = 2, text = "отиде")
                ),
                answerStatus = AnswerStatus.WRONG,
                correctAnswerWords = listOf("Ще", "отиде", "ли", "той"),
                onBackClick = {},
                onWordClick = {},
                onSelectedWordClick = {},
                onRemoveLastWordClick = {},
                onClearClick = {},
                onCheckClick = {},
                onTryAgainClick = {}
            )
        }
    }
}