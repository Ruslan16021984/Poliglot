package com.carbit3333333.oiiglot_bulgary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.carbit3333333.oiiglot_bulgary.data.dictionary.PersonalDictionaryRepository
import com.carbit3333333.oiiglot_bulgary.ui.dictionary.FlashcardFace
import com.carbit3333333.oiiglot_bulgary.ui.dictionary.FlashcardTrainingUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FlashcardTrainingViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {

    private val repository = PersonalDictionaryRepository(application)
    private val selectedGroupId = savedStateHandle.get<Long>("groupId")?.takeIf { it > 0L }
    private val selectedGroupName = savedStateHandle.get<String>("groupName")?.takeIf { it.isNotBlank() }

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

    private fun loadCards() {
        viewModelScope.launch {
            val cards = runCatching {
                if (selectedGroupId == null) {
                    repository.loadFlashcardsForAllWords()
                } else {
                    repository.loadFlashcardsForOneGroup(selectedGroupId)
                }
            }.getOrElse { emptyList() }

            _uiState.value = FlashcardTrainingUiState(
                isLoading = false,
                cards = cards,
                isFinished = cards.isEmpty(),
                groupName = selectedGroupName,
            )
        }
    }

    private fun advance(isKnown: Boolean) {
        val state = _uiState.value
        if (state.isLoading || state.isFinished || state.currentCard == null) {
            return
        }

        val nextIndex = state.currentIndex + 1
        _uiState.value = state.copy(
            currentIndex = if (nextIndex < state.totalCount) nextIndex else state.currentIndex,
            currentCardFace = FlashcardFace.Front,
            knownCount = state.knownCount + if (isKnown) 1 else 0,
            unknownCount = state.unknownCount + if (isKnown) 0 else 1,
            isFinished = nextIndex >= state.totalCount,
        )
    }
}
