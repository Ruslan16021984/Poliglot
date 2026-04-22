package com.carbit3333333.oiiglot_bulgary.ui.dictionary

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import kotlin.math.abs
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
        onRetryLoad = viewModel::retryLoad,
        onRetryUnknownCards = viewModel::retryUnknownCards,
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
    onRetryLoad: () -> Unit,
    onRetryUnknownCards: () -> Unit,
) {
    val pageBackground = Color(0xFFF5F7FB)
    val surfaceColor = Color.White
    val accentColor = Color(0xFF1F6FE5)
    val accentTint = Color(0xFFEAF2FF)
    val titleColor = Color(0xFF2D333C)

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

            uiState.errorMessage != null -> {
                FlashcardTrainingSummary(
                    title = "Не удалось открыть тренировку",
                    subtitle = uiState.errorMessage,
                    groupLabel = uiState.groupLabel,
                    knownCount = uiState.knownCount,
                    unknownCount = uiState.unknownCount,
                    buttonLabel = "Повторить",
                    onBackClick = onBackClick,
                    onFinishClick = onRetryLoad,
                    onRetryUnknownCards = null,
                )
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
                    onRetryUnknownCards = null,
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
                    onRetryUnknownCards = if (uiState.hasUnknownCards) onRetryUnknownCards else null,
                )
            }

            else -> {
                val currentCard = requireNotNull(uiState.currentCard)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.systemBars),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IconButton(
                                onClick = onBackClick,
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.extraLarge)
                                    .background(surfaceColor),
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Назад",
                                    tint = titleColor,
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Тренировка",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = titleColor,
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = uiState.progressText,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = titleColor,
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Surface(
                                color = accentTint,
                                shape = MaterialTheme.shapes.extraLarge,
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
                            card = currentCard,
                            face = uiState.currentCardFace,
                            onFlipCard = onFlipCard,
                            onKnowCard = onKnowCard,
                            onDontKnowCard = onDontKnowCard,
                        )
                    }

                    item {
                        SwipeHint(
                            text = "Свайп вверх: знаю",
                            accentColor = Color(0xFF2AA35E),
                            tint = Color(0xFFE8F8EE),
                            arrow = "↑",
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        SwipeHint(
                            text = "Свайп вниз: не знаю",
                            accentColor = Color(0xFFD94B42),
                            tint = Color(0xFFFCECEC),
                            arrow = "↓",
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
    val swipeThresholdPx = remember(density) { with(density) { 104.dp.toPx() } }
    var dragOffsetY by remember(card.id) { mutableFloatStateOf(0f) }
    val cardRotationY by animateFloatAsState(
        targetValue = if (face == FlashcardFace.Front) 0f else 180f,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing,
        ),
        label = "flashcard_flip",
    )
    val absoluteTilt = abs(dragOffsetY / swipeThresholdPx).coerceIn(0f, 1f)
    val shownFace = if (cardRotationY <= 90f) FlashcardFace.Front else FlashcardFace.Back

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(470.dp)
            .offset { IntOffset(x = 0, y = dragOffsetY.roundToInt()) }
            .graphicsLayer {
                rotationY = cardRotationY
                rotationZ = dragOffsetY / 40f
                scaleX = 1f - (absoluteTilt * 0.02f)
                scaleY = 1f - (absoluteTilt * 0.02f)
                cameraDistance = 12f * density.density * 72f
            }
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
            .pointerInput(card.id) {
                detectTapGestures(onTap = { onFlipCard() })
            },
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 26.dp, vertical = 30.dp)
                .graphicsLayer {
                    rotationY = if (shownFace == FlashcardFace.Back) 180f else 0f
                },
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = if (shownFace == FlashcardFace.Front) "Болгарский" else "Русский",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF727B8C),
                )
                Spacer(modifier = Modifier.height(26.dp))
                Text(
                    text = if (shownFace == FlashcardFace.Front) card.bgWord else card.ruTranslation,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2D333C),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(34.dp))
                Text(
                    text = if (shownFace == FlashcardFace.Front) {
                        "Коснитесь, чтобы перевернуть"
                    } else {
                        "Свайп вверх или вниз"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF727B8C),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun SwipeHint(
    text: String,
    accentColor: Color,
    tint: Color,
    arrow: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            color = tint,
            shape = MaterialTheme.shapes.extraLarge,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .padding(4.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = arrow,
                    color = accentColor,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = text,
            color = accentColor,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
        )
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
    onRetryUnknownCards: (() -> Unit)?,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад",
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Тренировка",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2D333C),
                )
            }
        }

        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Surface(
                    color = Color(0xFFEAF2FF),
                    shape = MaterialTheme.shapes.extraLarge,
                ) {
                    Text(
                        text = groupLabel,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF1F6FE5),
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 28.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color(0xFF2D333C),
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF727B8C),
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Знаю: $knownCount",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFF2D333C),
                        )
                        Text(
                            text = "Не знаю: $unknownCount",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFF2D333C),
                        )
                        if (onRetryUnknownCards != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "В повтор войдут только слова, отмеченные как «не знаю».",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF727B8C),
                                textAlign = TextAlign.Center,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Button(
                                onClick = onRetryUnknownCards,
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.large,
                            ) {
                                Text("Повторить трудные слова")
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = onFinishClick,
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.large,
                        ) {
                            Text(buttonLabel)
                        }
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
                        bgWord = "zdravei",
                        ruTranslation = "привет",
                    ),
                ),
                groupName = "Путешествие",
                unknownCount = 1,
                unknownCards = listOf(
                    FlashcardItem(
                        id = 1L,
                        bgWord = "zdravei",
                        ruTranslation = "привет",
                    )
                ),
                isFinished = true,
            ),
            onBackClick = {},
            onFinishClick = {},
            onFlipCard = {},
            onKnowCard = {},
            onDontKnowCard = {},
            onRetryLoad = {},
            onRetryUnknownCards = {},
        )
    }
}
