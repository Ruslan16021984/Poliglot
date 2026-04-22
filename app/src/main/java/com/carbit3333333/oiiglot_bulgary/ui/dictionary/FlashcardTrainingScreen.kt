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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
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
import com.carbit3333333.oiiglot_bulgary.utils.AppTextToSpeech
import com.carbit3333333.oiiglot_bulgary.viewmodel.FlashcardTrainingViewModel
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun FlashcardTrainingScreen(
    onBackClick: () -> Unit,
    onFinishClick: () -> Unit,
    viewModel: FlashcardTrainingViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val textToSpeech = remember { AppTextToSpeech(context) }

    DisposableEffect(Unit) {
        onDispose {
            textToSpeech.shutdown()
        }
    }

    FlashcardTrainingScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onFinishClick = onFinishClick,
        onFlipCard = viewModel::flipCard,
        onToggleDirection = viewModel::toggleDirection,
        onKnowCard = viewModel::markKnown,
        onDontKnowCard = viewModel::markUnknown,
        onRetryLoad = viewModel::retryLoad,
        onRetryUnknownCards = viewModel::retryUnknownCards,
        onSpeakBulgarian = textToSpeech::speakBulgarian,
        onSpeakRussian = textToSpeech::speakRussian,
    )
}

@Composable
fun FlashcardTrainingScreenContent(
    uiState: FlashcardTrainingUiState,
    onBackClick: () -> Unit,
    onFinishClick: () -> Unit,
    onFlipCard: () -> Unit,
    onToggleDirection: () -> Unit,
    onKnowCard: () -> Unit,
    onDontKnowCard: () -> Unit,
    onRetryLoad: () -> Unit,
    onRetryUnknownCards: () -> Unit,
    onSpeakBulgarian: (String) -> Unit,
    onSpeakRussian: (String) -> Unit,
) {
    val palette = rememberDictionaryPalette()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = palette.pageBackground,
    ) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.systemBars),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = palette.accent)
                }
            }

            uiState.errorMessage != null -> {
                FlashcardTrainingSummary(
                    title = "Не удалось открыть тренировку",
                    subtitle = uiState.errorMessage,
                    groupLabel = uiState.groupLabel,
                    directionLabel = uiState.directionLabel,
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
                    directionLabel = uiState.directionLabel,
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
                    directionLabel = uiState.directionLabel,
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
                                    .background(palette.surface),
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Назад",
                                    tint = palette.title,
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Тренировка",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = palette.title,
                            )
                        }
                    }

                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = uiState.progressText,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = palette.title,
                                )
                                Spacer(modifier = Modifier.width(14.dp))
                                Surface(
                                    color = palette.accentSurface,
                                    shape = MaterialTheme.shapes.extraLarge,
                                ) {
                                    Text(
                                        text = uiState.groupLabel,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = palette.accent,
                                    )
                                }
                            }

                            OutlinedButton(
                                onClick = onToggleDirection,
                                shape = MaterialTheme.shapes.large,
                            ) {
                                Text(uiState.directionLabel)
                            }
                        }
                    }

                    item {
                        key(currentCard.id, uiState.direction) {
                            FlashcardSwipeCard(
                                card = currentCard,
                                face = uiState.currentCardFace,
                                direction = uiState.direction,
                                palette = palette,
                                onFlipCard = onFlipCard,
                                onKnowCard = onKnowCard,
                                onDontKnowCard = onDontKnowCard,
                                onSpeakBulgarian = onSpeakBulgarian,
                                onSpeakRussian = onSpeakRussian,
                            )
                        }
                    }

                    item {
                        SwipeHint(
                            text = "Свайп вверх: знаю",
                            accentColor = palette.hintPositiveText,
                            tint = palette.hintPositiveSurface,
                            arrow = "↑",
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        SwipeHint(
                            text = "Свайп вниз: не знаю",
                            accentColor = palette.hintNegativeText,
                            tint = palette.hintNegativeSurface,
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
    direction: FlashcardDirection,
    palette: DictionaryPalette,
    onFlipCard: () -> Unit,
    onKnowCard: () -> Unit,
    onDontKnowCard: () -> Unit,
    onSpeakBulgarian: (String) -> Unit,
    onSpeakRussian: (String) -> Unit,
) {
    val density = LocalDensity.current
    val swipeThresholdPx = remember(density) { with(density) { 104.dp.toPx() } }
    val dismissTravelPx = remember(density) { with(density) { 280.dp.toPx() } }
    var dragOffsetY by remember(card.id, direction) { mutableFloatStateOf(0f) }
    var dismissDirection by remember(card.id, direction) { mutableStateOf<SwipeDismissDirection?>(null) }
    val cardRotationY by animateFloatAsState(
        targetValue = if (face == FlashcardFace.Front) 0f else 180f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "flashcard_flip",
    )
    val animatedOffsetY by animateFloatAsState(
        targetValue = when (dismissDirection) {
            SwipeDismissDirection.Up -> -dismissTravelPx
            SwipeDismissDirection.Down -> dismissTravelPx
            null -> dragOffsetY
        },
        animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing),
        label = "flashcard_swipe_offset",
    )
    val cardScale by animateFloatAsState(
        targetValue = if (dismissDirection == null) 1f else 0.82f,
        animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing),
        label = "flashcard_swipe_scale",
    )
    val absoluteTilt = abs(animatedOffsetY / swipeThresholdPx).coerceIn(0f, 1f)
    val shownFace = if (cardRotationY <= 90f) FlashcardFace.Front else FlashcardFace.Back

    val frontLanguage = when (direction) {
        FlashcardDirection.BgToRu -> "Болгарский"
        FlashcardDirection.RuToBg -> "Русский"
    }
    val backLanguage = when (direction) {
        FlashcardDirection.BgToRu -> "Русский"
        FlashcardDirection.RuToBg -> "Болгарский"
    }
    val frontText = when (direction) {
        FlashcardDirection.BgToRu -> card.bgWord
        FlashcardDirection.RuToBg -> card.ruTranslation
    }
    val backText = when (direction) {
        FlashcardDirection.BgToRu -> card.ruTranslation
        FlashcardDirection.RuToBg -> card.bgWord
    }
    val speakAction: () -> Unit = when (shownFace) {
        FlashcardFace.Front -> {
            when (direction) {
                FlashcardDirection.BgToRu -> ({ onSpeakBulgarian(frontText) })
                FlashcardDirection.RuToBg -> ({ onSpeakRussian(frontText) })
            }
        }
        FlashcardFace.Back -> {
            when (direction) {
                FlashcardDirection.BgToRu -> ({ onSpeakRussian(backText) })
                FlashcardDirection.RuToBg -> ({ onSpeakBulgarian(backText) })
            }
        }
    }
    val speakLabel = when (shownFace) {
        FlashcardFace.Front -> frontLanguage
        FlashcardFace.Back -> backLanguage
    }

    LaunchedEffect(card.id, dismissDirection) {
        when (dismissDirection) {
            SwipeDismissDirection.Up -> {
                delay(180)
                onKnowCard()
            }
            SwipeDismissDirection.Down -> {
                delay(180)
                onDontKnowCard()
            }
            null -> Unit
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(470.dp)
            .offset { IntOffset(x = 0, y = animatedOffsetY.roundToInt()) }
            .graphicsLayer {
                rotationY = cardRotationY
                rotationZ = animatedOffsetY / 34f
                scaleX = cardScale - (absoluteTilt * 0.02f)
                scaleY = cardScale - (absoluteTilt * 0.02f)
                cameraDistance = 12f * density.density * 72f
                alpha = if (dismissDirection == null) 1f else 0.96f
            }
            .pointerInput(card.id, direction) {
                detectVerticalDragGestures(
                    onVerticalDrag = { _, dragAmount ->
                        if (dismissDirection == null) {
                            dragOffsetY += dragAmount
                        }
                    },
                    onDragEnd = {
                        when {
                            dragOffsetY <= -swipeThresholdPx -> dismissDirection = SwipeDismissDirection.Up
                            dragOffsetY >= swipeThresholdPx -> dismissDirection = SwipeDismissDirection.Down
                            else -> dragOffsetY = 0f
                        }
                    },
                    onDragCancel = {
                        dragOffsetY = 0f
                    },
                )
            }
            .pointerInput(card.id, direction) {
                detectTapGestures(
                    onTap = {
                        if (dismissDirection == null) {
                            onFlipCard()
                        }
                    },
                )
            },
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = palette.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(palette.surface)
                .padding(horizontal = 26.dp, vertical = 30.dp)
                .graphicsLayer {
                    rotationY = if (shownFace == FlashcardFace.Back) 180f else 0f
                },
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clip(MaterialTheme.shapes.large),
                color = palette.accentSurface,
                shape = MaterialTheme.shapes.large,
            ) {
                TextButton(
                    onClick = speakAction,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Произнести: $speakLabel",
                        tint = palette.accent,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Слушать",
                        color = palette.accent,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = if (shownFace == FlashcardFace.Front) frontLanguage else backLanguage,
                    style = MaterialTheme.typography.titleMedium,
                    color = palette.body,
                )
                Spacer(modifier = Modifier.height(26.dp))
                Text(
                    text = if (shownFace == FlashcardFace.Front) frontText else backText,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = palette.title,
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
                    color = palette.body,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

private enum class SwipeDismissDirection {
    Up,
    Down,
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
    directionLabel: String,
    knownCount: Int,
    unknownCount: Int,
    buttonLabel: String,
    onBackClick: () -> Unit,
    onFinishClick: () -> Unit,
    onRetryUnknownCards: (() -> Unit)?,
) {
    val palette = rememberDictionaryPalette()

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
                        tint = palette.title,
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Тренировка",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = palette.title,
                )
            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Surface(
                    color = palette.accentSurface,
                    shape = MaterialTheme.shapes.extraLarge,
                ) {
                    Text(
                        text = groupLabel,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = palette.accent,
                    )
                }
                Text(
                    text = directionLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    color = palette.body,
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(containerColor = palette.surface),
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
                            color = palette.title,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodyLarge,
                            color = palette.body,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Знаю: $knownCount",
                            style = MaterialTheme.typography.titleLarge,
                            color = palette.title,
                        )
                        Text(
                            text = "Не знаю: $unknownCount",
                            style = MaterialTheme.typography.titleLarge,
                            color = palette.title,
                        )
                        if (onRetryUnknownCards != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "В повтор войдут только слова, отмеченные как «не знаю».",
                                style = MaterialTheme.typography.bodyMedium,
                                color = palette.body,
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
                direction = FlashcardDirection.RuToBg,
                groupName = "Путешествие",
            ),
            onBackClick = {},
            onFinishClick = {},
            onFlipCard = {},
            onToggleDirection = {},
            onKnowCard = {},
            onDontKnowCard = {},
            onRetryLoad = {},
            onRetryUnknownCards = {},
            onSpeakBulgarian = {},
            onSpeakRussian = {},
        )
    }
}
