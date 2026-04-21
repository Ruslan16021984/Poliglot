package com.carbit3333333.oiiglot_bulgary.ui.dictionary

import com.carbit3333333.oiiglot_bulgary.model.dictionary.FlashcardItem

enum class FlashcardFace {
    Front,
    Back,
}

data class FlashcardTrainingUiState(
    val isLoading: Boolean = false,
    val cards: List<FlashcardItem> = emptyList(),
    val currentIndex: Int = 0,
    val currentCardFace: FlashcardFace = FlashcardFace.Front,
    val knownCount: Int = 0,
    val unknownCount: Int = 0,
    val isFinished: Boolean = false,
    val groupName: String? = null,
) {
    val currentCard: FlashcardItem?
        get() = cards.getOrNull(currentIndex)

    val totalCount: Int
        get() = cards.size

    val progressText: String
        get() = when {
            totalCount == 0 -> "0 / 0"
            isFinished -> "$totalCount / $totalCount"
            else -> "${currentIndex + 1} / $totalCount"
        }

    val groupLabel: String
        get() = groupName ?: "Все слова"
}
