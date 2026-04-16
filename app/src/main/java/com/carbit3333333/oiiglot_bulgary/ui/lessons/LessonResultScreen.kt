package com.carbit3333333.oiiglot_bulgary.ui.lessons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carbit3333333.oiiglot_bulgary.model.LessonResult
import com.carbit3333333.oiiglot_bulgary.ui.theme.OIiglot_BulgaryTheme
import java.util.Locale

@Composable
fun LessonResultScreen(
    result: LessonResult,
    hasNextLesson: Boolean,
    onRetryClick: () -> Unit,
    onNextLessonClick: () -> Unit,
    onBackToLessonsClick: () -> Unit
) {
    val titleText = if (result.isPassed) {
        "Урок пройден"
    } else {
        "Урок не пройден"
    }

    val titleColor = if (result.isPassed) {
        Color(0xFF2E7D32)
    } else {
        Color(0xFFC62828)
    }

    val scoreText = String.format(Locale.US, "%.1f", result.score)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F3))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = result.lessonTitle,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = titleText,
            style = MaterialTheme.typography.headlineSmall,
            color = titleColor,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                ResultRow(
                    label = "Всего упражнений",
                    value = result.totalExercises.toString()
                )

                ResultRow(
                    label = "Правильных",
                    value = result.correctCount.toString(),
                    valueColor = Color(0xFF2E7D32)
                )

                ResultRow(
                    label = "Неправильных",
                    value = result.wrongCount.toString(),
                    valueColor = Color(0xFFC62828)
                )

                ResultRow(
                    label = "Балл",
                    value = scoreText
                )

                ResultRow(
                    label = "Проходной балл",
                    value = "4.5"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (result.isPassed) {
                if (hasNextLesson) {
                    "Следующий урок открыт."
                } else {
                    "Это последний доступный урок."
                }
            } else {
                "Нужно пройти урок не ниже чем на 4.5 балла."
            },
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF666666)
        )

        Spacer(modifier = Modifier.weight(1f))

        if (result.isPassed) {
            if (hasNextLesson) {
                Button(
                    onClick = onNextLessonClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2E7D32)
                    )
                ) {
                    Text("Следующий урок")
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            Button(
                onClick = onBackToLessonsClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF757575)
                )
            ) {
                Text("К списку уроков")
            }
        } else {
            Button(
                onClick = onRetryClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7D32)
                )
            ) {
                Text("Пройти заново")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onBackToLessonsClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF757575)
                )
            ) {
                Text("К списку уроков")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ResultRow(
    label: String,
    value: String,
    valueColor: Color = Color(0xFF222222)
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF666666)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = valueColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LessonResultPassedPreview() {
    OIiglot_BulgaryTheme {
        Surface {
            LessonResultScreen(
                result = LessonResult(
                    lessonId = 1,
                    lessonTitle = "Урок 1",
                    totalExercises = 100,
                    correctCount = 67,
                    wrongCount = 33,
                    score = 6.7f,
                    isPassed = true
                ),
                hasNextLesson = true,
                onRetryClick = {},
                onNextLessonClick = {},
                onBackToLessonsClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LessonResultFailedPreview() {
    OIiglot_BulgaryTheme {
        Surface {
            LessonResultScreen(
                result = LessonResult(
                    lessonId = 1,
                    lessonTitle = "Урок 1",
                    totalExercises = 100,
                    correctCount = 38,
                    wrongCount = 62,
                    score = 3.8f,
                    isPassed = false
                ),
                hasNextLesson = true,
                onRetryClick = {},
                onNextLessonClick = {},
                onBackToLessonsClick = {}
            )
        }
    }
}