package com.carbit3333333.oiiglot_bulgary.ui.dictionary

import com.carbit3333333.oiiglot_bulgary.model.dictionary.WordGroup

data class WordEditorUiState(
    val isLoading: Boolean = false,
    val wordId: Long? = null,
    val bgWord: String = "",
    val ruTranslation: String = "",
    val selectedGroupIds: Set<Long> = emptySet(),
    val availableGroups: List<WordGroup> = emptyList(),
    val isNewGroupDialogVisible: Boolean = false,
    val newGroupName: String = "",
    val showValidationErrors: Boolean = false,
    val isSaving: Boolean = false,
    val isSaveEnabled: Boolean = true,
    val isSaved: Boolean = false,
    val errorMessage: String? = null,
) {
    val isEditMode: Boolean
        get() = wordId != null

    val primaryButtonText: String
        get() = if (isEditMode) "Сохранить изменения" else "Добавить слово"

    val bgWordError: String?
        get() = if (showValidationErrors && bgWord.trim().isEmpty()) "Введите слово на болгарском" else null

    val ruTranslationError: String?
        get() = if (showValidationErrors && ruTranslation.trim().isEmpty()) "Введите перевод на русском" else null

    val isBackEnabled: Boolean
        get() = !isSaving

    val isEditorInteractive: Boolean
        get() = !isSaving
}
