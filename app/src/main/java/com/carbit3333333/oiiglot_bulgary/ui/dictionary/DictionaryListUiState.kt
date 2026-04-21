package com.carbit3333333.oiiglot_bulgary.ui.dictionary

import com.carbit3333333.oiiglot_bulgary.model.dictionary.DictionaryWordListItem
import com.carbit3333333.oiiglot_bulgary.model.dictionary.WordGroup

data class DictionaryListUiState(
    val isLoading: Boolean = false,
    val query: String = "",
    val selectedGroupId: Long? = null,
    val words: List<DictionaryWordListItem> = emptyList(),
    val groups: List<WordGroup> = emptyList(),
    val errorMessage: String? = null
)
