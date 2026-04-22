package com.carbit3333333.oiiglot_bulgary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.carbit3333333.oiiglot_bulgary.data.dictionary.PersonalDictionaryRepository
import com.carbit3333333.oiiglot_bulgary.model.dictionary.DictionaryWordListItem
import com.carbit3333333.oiiglot_bulgary.model.dictionary.WordGroup
import com.carbit3333333.oiiglot_bulgary.ui.dictionary.DictionaryListUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class DictionaryViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val repository = PersonalDictionaryRepository(application)

    private val queryState = MutableStateFlow("")
    private val selectedGroupIdState = MutableStateFlow<Long?>(null)
    private val errorMessageState = MutableStateFlow<String?>(null)

    private val filteredWords: StateFlow<List<DictionaryWordListItem>> =
        combine(
            queryState,
            selectedGroupIdState,
        ) { query, selectedGroupId ->
            query.trim() to selectedGroupId
        }
            .flatMapLatest { (query, selectedGroupId) ->
                repository.observeFilteredWords(
                    query = query,
                    groupId = selectedGroupId,
                ).catch {
                    errorMessageState.value = it.message ?: "Не удалось загрузить слова"
                    emit(emptyList())
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList(),
            )

    val groups: StateFlow<List<WordGroup>> =
        repository.observeGroupsWithCounts()
            .catch {
                errorMessageState.value = it.message ?: "Не удалось загрузить группы"
                emit(emptyList())
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList(),
            )

    val hasAnyWordsForTraining: StateFlow<Boolean> =
        repository.observeAllWords()
            .map { words -> words.isNotEmpty() }
            .catch {
                errorMessageState.value = it.message ?: "Не удалось загрузить слова"
                emit(false)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false,
            )

    val uiState: StateFlow<DictionaryListUiState> = combine(
        queryState,
        selectedGroupIdState,
        filteredWords,
        groups,
        errorMessageState,
    ) { query, selectedGroupId, words, groups, errorMessage ->
        DictionaryListUiState(
            isLoading = false,
            query = query,
            selectedGroupId = selectedGroupId,
            words = words,
            groups = groups,
            errorMessage = errorMessage,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DictionaryListUiState(isLoading = true),
    )

    fun updateQuery(query: String) {
        queryState.value = query
    }

    fun selectGroup(groupId: Long?) {
        selectedGroupIdState.value = if (selectedGroupIdState.value == groupId) null else groupId
    }

    fun clearError() {
        errorMessageState.value = null
    }

    fun deleteWord(wordId: Long) {
        viewModelScope.launch {
            runCatching {
                repository.deleteWord(wordId)
            }.onFailure { throwable ->
                errorMessageState.value = throwable.message ?: "Не удалось удалить слово"
            }
        }
    }
}
