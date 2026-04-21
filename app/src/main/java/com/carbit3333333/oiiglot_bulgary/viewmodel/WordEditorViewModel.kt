package com.carbit3333333.oiiglot_bulgary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.carbit3333333.oiiglot_bulgary.data.dictionary.PersonalDictionaryRepository
import com.carbit3333333.oiiglot_bulgary.model.dictionary.WordCard
import com.carbit3333333.oiiglot_bulgary.ui.dictionary.WordEditorUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WordEditorViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {

    enum class SpeechTarget {
        Bulgarian,
        Russian,
    }

    private val repository = PersonalDictionaryRepository(application)
    private val initialWordId = savedStateHandle.get<Long>("wordId")?.takeIf { it > 0L }

    private val editorState = MutableStateFlow(
        WordEditorUiState(
            isLoading = initialWordId != null,
            wordId = initialWordId,
        )
    )

    private val groupsState = repository.observeGroupsWithCounts()
        .catch { throwable ->
            editorState.value = editorState.value.copy(
                errorMessage = throwable.message ?: "Не удалось загрузить группы",
            )
            emit(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    val uiState: StateFlow<WordEditorUiState> = combine(
        editorState,
        groupsState,
    ) { state, groups ->
        state.copy(
            availableGroups = groups,
            isSaveEnabled = !state.isLoading && !state.isSaving,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = editorState.value,
    )

    init {
        initialWordId?.let(::loadWord)
    }

    fun updateBgWord(value: String) {
        editorState.value = editorState.value.copy(bgWord = value)
    }

    fun updateRuTranslation(value: String) {
        editorState.value = editorState.value.copy(ruTranslation = value)
    }

    fun toggleGroupSelection(groupId: Long) {
        val selectedIds = editorState.value.selectedGroupIds.toMutableSet()
        if (!selectedIds.add(groupId)) {
            selectedIds.remove(groupId)
        }
        editorState.value = editorState.value.copy(selectedGroupIds = selectedIds)
    }

    fun showNewGroupDialog() {
        editorState.value = editorState.value.copy(
            isNewGroupDialogVisible = true,
            newGroupName = "",
        )
    }

    fun dismissNewGroupDialog() {
        editorState.value = editorState.value.copy(
            isNewGroupDialogVisible = false,
            newGroupName = "",
        )
    }

    fun updateNewGroupName(name: String) {
        editorState.value = editorState.value.copy(newGroupName = name)
    }

    fun createGroup() {
        val groupName = editorState.value.newGroupName.trim()
        if (groupName.isEmpty()) {
            return
        }

        viewModelScope.launch {
            runCatching {
                repository.createGroup(groupName)
            }.onSuccess { groupId ->
                if (groupId > 0L) {
                    editorState.value = editorState.value.copy(
                        isNewGroupDialogVisible = false,
                        newGroupName = "",
                        selectedGroupIds = editorState.value.selectedGroupIds + groupId,
                    )
                } else {
                    editorState.value = editorState.value.copy(
                        errorMessage = "Группа с таким названием уже есть или не была создана",
                    )
                }
            }.onFailure { throwable ->
                editorState.value = editorState.value.copy(
                    errorMessage = throwable.message ?: "Не удалось создать группу",
                )
            }
        }
    }

    fun saveWord() {
        val state = editorState.value
        val trimmedBgWord = state.bgWord.trim()
        val trimmedRuTranslation = state.ruTranslation.trim()

        editorState.value = state.copy(showValidationErrors = true)
        if (trimmedBgWord.isEmpty() || trimmedRuTranslation.isEmpty()) {
            return
        }

        viewModelScope.launch {
            editorState.value = editorState.value.copy(
                isSaving = true,
                errorMessage = null,
            )

            runCatching {
                repository.saveWord(
                    WordCard(
                        id = state.wordId ?: 0L,
                        bgWord = trimmedBgWord,
                        ruTranslation = trimmedRuTranslation,
                        groupIds = state.selectedGroupIds.sorted(),
                    )
                )
            }.onSuccess { savedWordId ->
                editorState.value = editorState.value.copy(
                    isSaving = false,
                    isSaved = true,
                    wordId = savedWordId,
                    bgWord = trimmedBgWord,
                    ruTranslation = trimmedRuTranslation,
                )
            }.onFailure { throwable ->
                editorState.value = editorState.value.copy(
                    isSaving = false,
                    errorMessage = throwable.message ?: "Не удалось сохранить слово",
                )
            }
        }
    }

    fun appendRecognizedText(target: SpeechTarget, recognizedText: String) {
        val trimmedText = recognizedText.trim()
        if (trimmedText.isEmpty()) {
            return
        }

        val state = editorState.value
        editorState.value = when (target) {
            SpeechTarget.Bulgarian -> state.copy(
                bgWord = state.bgWord.appendRecognizedText(trimmedText),
            )

            SpeechTarget.Russian -> state.copy(
                ruTranslation = state.ruTranslation.appendRecognizedText(trimmedText),
            )
        }
    }

    fun onSpeechRecognitionUnavailable() {
        editorState.value = editorState.value.copy(
            errorMessage = "Голосовой ввод недоступен на этом устройстве",
        )
    }

    fun clearError() {
        editorState.value = editorState.value.copy(errorMessage = null)
    }

    fun acknowledgeSaveCompleted() {
        editorState.value = editorState.value.copy(isSaved = false)
    }

    private fun loadWord(wordId: Long) {
        viewModelScope.launch {
            runCatching {
                repository.getWordById(wordId)
            }.onSuccess { word ->
                editorState.value = if (word == null) {
                    WordEditorUiState(
                        isLoading = false,
                        errorMessage = "Слово не найдено. Можно сохранить его заново как новое.",
                    )
                } else {
                    editorState.value.copy(
                        isLoading = false,
                        wordId = word.id,
                        bgWord = word.bgWord,
                        ruTranslation = word.ruTranslation,
                        selectedGroupIds = word.groupIds.toSet(),
                        errorMessage = null,
                    )
                }
            }.onFailure { throwable ->
                editorState.value = editorState.value.copy(
                    isLoading = false,
                    errorMessage = throwable.message ?: "Не удалось загрузить слово",
                )
            }
        }
    }

    private fun String.appendRecognizedText(recognizedText: String): String {
        return if (isBlank()) {
            recognizedText
        } else {
            trimEnd() + " " + recognizedText
        }
    }
}
