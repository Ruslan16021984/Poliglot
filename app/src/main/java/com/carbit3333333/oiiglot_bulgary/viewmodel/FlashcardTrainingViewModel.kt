package com.carbit3333333.oiiglot_bulgary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.carbit3333333.oiiglot_bulgary.data.dictionary.PersonalDictionaryRepository
import com.carbit3333333.oiiglot_bulgary.model.dictionary.FlashcardItem
import com.carbit3333333.oiiglot_bulgary.ui.dictionary.FlashcardFace
import com.carbit3333333.oiiglot_bulgary.ui.dictionary.FlashcardTrainingUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FlashcardTrainingViewModel(
    private val selectedGroupId: Long?,
    private val selectedGroupName: String?,
    private val flashcardLoader: suspend (Long?) -> List<FlashcardItem>,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        FlashcardTrainingUiState(
            isLoading = true,
            groupName = selectedGroupName,
        )
    )
    val uiState: StateFlow<FlashcardTrainingUiState> = _uiState.asStateFlow()

    init {
        loadCards()
    }

    fun flipCard() {
        val state = _uiState.value
        if (state.isLoading || state.isFinished || state.currentCard == null) {
            return
        }

        _uiState.value = state.copy(
            currentCardFace = when (state.currentCardFace) {
                FlashcardFace.Front -> FlashcardFace.Back
                FlashcardFace.Back -> FlashcardFace.Front
            },
        )
    }

    fun markKnown() {
        advance(isKnown = true)
    }

    fun markUnknown() {
        advance(isKnown = false)
    }

    fun retryLoad() {
        loadCards()
    }

    fun retryUnknownCards() {
        val state = _uiState.value
        if (state.unknownCards.isEmpty()) {
            return
        }

        _uiState.value = FlashcardTrainingUiState(
            isLoading = false,
            cards = state.unknownCards,
            currentIndex = 0,
            currentCardFace = FlashcardFace.Front,
            knownCount = 0,
            unknownCount = 0,
            unknownCards = emptyList(),
            isFinished = false,
            groupName = if (selectedGroupName.isNullOrBlank()) {
                "Повтор трудных слов"
            } else {
                "$selectedGroupName • повтор"
            },
        )
    }

    private fun loadCards() {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            cards = emptyList(),
            currentIndex = 0,
            currentCardFace = FlashcardFace.Front,
            knownCount = 0,
            unknownCount = 0,
            unknownCards = emptyList(),
            isFinished = false,
            errorMessage = null,
        )

        viewModelScope.launch {
            runCatching {
                flashcardLoader(selectedGroupId)
            }.onSuccess { cards ->
                _uiState.value = FlashcardTrainingUiState(
                    isLoading = false,
                    cards = cards,
                    isFinished = cards.isEmpty(),
                    groupName = selectedGroupName,
                )
            }.onFailure {
                _uiState.value = FlashcardTrainingUiState(
                    isLoading = false,
                    groupName = selectedGroupName,
                    errorMessage = "Не удалось загрузить карточки. Попробуйте ещё раз.",
                )
            }
        }
    }

    private fun advance(isKnown: Boolean) {
        val state = _uiState.value
        val currentCard = state.currentCard ?: return
        if (state.isLoading || state.isFinished) {
            return
        }

        val nextIndex = state.currentIndex + 1
        _uiState.value = state.copy(
            currentIndex = if (nextIndex < state.totalCount) nextIndex else state.currentIndex,
            currentCardFace = FlashcardFace.Front,
            knownCount = state.knownCount + if (isKnown) 1 else 0,
            unknownCount = state.unknownCount + if (isKnown) 0 else 1,
            unknownCards = if (isKnown) state.unknownCards else state.unknownCards + currentCard,
            isFinished = nextIndex >= state.totalCount,
        )
    }

    companion object {
        fun provideFactory(
            repository: PersonalDictionaryRepository,
            groupId: Long?,
            groupName: String?,
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    require(modelClass.isAssignableFrom(FlashcardTrainingViewModel::class.java))
                    return FlashcardTrainingViewModel(
                        selectedGroupId = groupId?.takeIf { it > 0L },
                        selectedGroupName = groupName?.takeIf { it.isNotBlank() },
                        flashcardLoader = { requestedGroupId ->
                            if (requestedGroupId == null) {
                                repository.loadFlashcardsForAllWords()
                            } else {
                                repository.loadFlashcardsForOneGroup(requestedGroupId)
                            }
                        },
                    ) as T
                }
            }
        }
    }
}
