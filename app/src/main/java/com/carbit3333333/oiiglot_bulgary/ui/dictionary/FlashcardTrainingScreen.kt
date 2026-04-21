package com.carbit3333333.oiiglot_bulgary.ui.dictionary

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carbit3333333.oiiglot_bulgary.model.dictionary.FlashcardItem
import com.carbit3333333.oiiglot_bulgary.ui.theme.OIiglot_BulgaryTheme
import com.carbit3333333.oiiglot_bulgary.viewmodel.FlashcardTrainingViewModel
import kotlin.math.roundToInt

@Composable
fun FlashcardTrainingScreen(
    onBackClick: () -> Unit,
    onFinishClick: () -> Unit,
    viewModel: FlashcardTrainingViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FlashcardTrainingScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onFinishClick = onFinishClick,
        onFlipCard = viewModel::flipCard,
        onKnowCard = viewModel::markKnown,
        onDontKnowCard = viewModel::markUnknown,
    )
}

@Composable
fun FlashcardTrainingScreenContent(
    uiState: FlashcardTrainingUiState,
    onBackClick: () -> Unit,
    onFinishClick: () -> Unit,
    onFlipCard: () -> Unit,
    onKnowCard: () -> Unit,
    onDontKnowCard: () -> Unit,
) {
    val pageBackground = Color(0xFFF4F7FC)
    val accentTint = Color(0xFFE7EEFF)
    val accentColor = Color(0xFF4164A9)
    val secondaryText = Color(0xFF66708A)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = pageBackground,
    ) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.systemBars),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = accentColor)
                }
            }

            uiState.currentCard == null -> {
                FlashcardTrainingSummary(
                    title = "Тренировка",
                    subtitle = "Слов для тренировки пока нет",
                    groupLabel = uiState.groupLabel,
                    knownCount = uiState.knownCount,
                    unknownCount = uiState.unknownCount,
                    buttonLabel = "Вернуться к словарю",
                    onBackClick = onBackClick,
                    onFinishClick = onFinishClick,
                )
            }

            uiState.isFinished -> {
                FlashcardTrainingSummary(
                    title = "Тренировка завершена",
                    subtitle = "Карточки закончились",
                    groupLabel = uiState.groupLabel,
                    knownCount = uiState.knownCount,
                    unknownCount = uiState.unknownCount,
                    buttonLabel = "Вернуться к словарю",
                    onBackClick = onBackClick,
                    onFinishClick = onFinishClick,
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.systemBars),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    item {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Назад",
                            )
                        }
                    }

                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "Тренировка",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color(0xFF20243A),
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = uiState.progressText,
                                style = MaterialTheme.typography.titleMedium,
                                color = secondaryText,
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                color = accentTint,
                                shape = MaterialTheme.shapes.large,
                            ) {
                                Text(
                                    text = uiState.groupLabel,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = accentColor,
                                )
                            }
                        }
                    }

                    item {
                        FlashcardSwipeCard(
                            card = uiState.currentCard,
                            face = uiState.currentCardFace,
                            onFlipCard = onFlipCard,
                            onKnowCard = onKnowCard,
                            onDontKnowCard = onDontKnowCard,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FlashcardSwipeCard(
    card: FlashcardItem,
    face: FlashcardFace,
    onFlipCard: () -> Unit,
    onKnowCard: () -> Unit,
    onDontKnowCard: () -> Unit,
) {
    val density = LocalDensity.current
    val swipeThresholdPx = remember(density) { with(density) { 96.dp.toPx() } }
    var dragOffsetY by remember(card.id) { mutableFloatStateOf(0f) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(420.dp)
            .offset { IntOffset(x = 0, y = dragOffsetY.roundToInt()) }
            .pointerInput(card.id) {
                detectVerticalDragGestures(
                    onVerticalDrag = { _, dragAmount ->
                        dragOffsetY += dragAmount
                    },
                    onDragEnd = {
                        when {
                            dragOffsetY <= -swipeThresholdPx -> onKnowCard()
                            dragOffsetY >= swipeThresholdPx -> onDontKnowCard()
                        }
                        dragOffsetY = 0f
                    },
                    onDragCancel = {
                        dragOffsetY = 0f
                    },
                )
            }
            .pointerInput(card.id, face) {
                detectTapGestures(
                    onTap = { onFlipCard() },
                )
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 28.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = if (face == FlashcardFace.Front) "Болгарский" else "Русский",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF66708A),
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = if (face == FlashcardFace.Front) card.bgWord else card.ruTranslation,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF20243A),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "Коснитесь, чтобы перевернуть",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF66708A),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Свайп вверх: знаю  •  свайп вниз: не знаю",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF8A94AE),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun FlashcardTrainingSummary(
    title: String,
    subtitle: String,
    groupLabel: String,
    knownCount: Int,
    unknownCount: Int,
    buttonLabel: String,
    onBackClick: () -> Unit,
    onFinishClick: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад",
                )
            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF20243A),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF66708A),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = Color(0xFFE7EEFF),
                    shape = MaterialTheme.shapes.large,
                ) {
                    Text(
                        text = groupLabel,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF4164A9),
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = "Знаю: $knownCount",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF20243A),
                    )
                    Text(
                        text = "Не знаю: $unknownCount",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF20243A),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onFinishClick,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(buttonLabel)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FlashcardTrainingScreenPreview() {
    OIiglot_BulgaryTheme {
        FlashcardTrainingScreenContent(
            uiState = FlashcardTrainingUiState(
                cards = listOf(
                    FlashcardItem(
                        id = 1L,
                        bgWord = "здравей",
                        ruTranslation = "привет",
                    ),
                ),
                groupName = "Путешествие",
            ),
            onBackClick = {},
            onFinishClick = {},
            onFlipCard = {},
            onKnowCard = {},
            onDontKnowCard = {},
        )
    }
}
