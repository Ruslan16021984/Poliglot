package com.carbit3333333.oiiglot_bulgary.data.dictionary

import android.content.Context
import androidx.room.withTransaction
import com.carbit3333333.oiiglot_bulgary.model.dictionary.DictionaryWordListItem
import com.carbit3333333.oiiglot_bulgary.model.dictionary.FlashcardItem
import com.carbit3333333.oiiglot_bulgary.model.dictionary.WordCard
import com.carbit3333333.oiiglot_bulgary.model.dictionary.WordGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PersonalDictionaryRepository(
    context: Context,
) {

    private val appContext = context.applicationContext
    private val database = PersonalDictionaryDatabase.getInstance(appContext)
    private val wordCardDao = database.wordCardDao()
    private val wordGroupDao = database.wordGroupDao()

    fun observeAllWords(): Flow<List<DictionaryWordListItem>> {
        return observeFilteredWords(query = "", groupId = null)
    }

    fun observeFilteredWords(
        query: String,
        groupId: Long? = null,
    ): Flow<List<DictionaryWordListItem>> {
        return wordCardDao.observeWords(query = query, groupId = groupId)
            .map { words -> words.map { it.toListItem() } }
    }

    fun observeGroupsWithCounts(): Flow<List<WordGroup>> {
        return wordGroupDao.observeAllGroupsWithCounts()
            .map { groups -> groups.map { it.toDomain() } }
    }

    suspend fun getWordById(wordId: Long): WordCard? {
        return wordCardDao.getWordWithGroupsById(wordId)?.toDomain()
    }

    suspend fun saveWord(word: WordCard): Long {
        val normalizedBgWord = word.bgWord.trim()
        val normalizedRuTranslation = word.ruTranslation.trim()
        require(normalizedBgWord.isNotEmpty()) { "bgWord must not be blank" }
        require(normalizedRuTranslation.isNotEmpty()) { "ruTranslation must not be blank" }

        return database.withTransaction {
            val existingWord = if (word.id > 0) {
                wordCardDao.getWordWithGroupsById(word.id)
            } else {
                null
            }

            if (word.id > 0 && existingWord == null) {
                throw IllegalStateException("Cannot update missing word id ${word.id}")
            }

            val now = System.currentTimeMillis()
            val savedWord = WordCardEntity(
                id = existingWord?.wordCard?.id ?: 0,
                bgWord = normalizedBgWord,
                ruTranslation = normalizedRuTranslation,
                createdAt = existingWord?.wordCard?.createdAt ?: now,
                updatedAt = now,
            )

            val savedWordId = if (existingWord == null) {
                wordCardDao.insertWord(savedWord)
            } else {
                wordCardDao.updateWord(savedWord)
                savedWord.id
            }

            wordCardDao.deleteGroupCrossRefsForWord(savedWordId)
            word.groupIds
                .distinct()
                .filter { it > 0 }
                .forEach { groupId ->
                    wordCardDao.insertGroupCrossRef(
                        WordCardGroupCrossRef(
                            wordCardId = savedWordId,
                            wordGroupId = groupId,
                        )
                    )
                }

            savedWordId
        }
    }

    suspend fun createGroup(name: String): Long {
        val normalizedName = name.trim()
        if (normalizedName.isEmpty()) {
            return 0L
        }
        return wordGroupDao.createGroup(
            WordGroupEntity(
                name = normalizedName,
            )
        )
    }

    suspend fun deleteWord(wordId: Long) {
        database.withTransaction {
            val word = wordCardDao.getWordWithGroupsById(wordId)?.wordCard ?: return@withTransaction
            wordCardDao.deleteWord(word)
        }
    }

    suspend fun loadFlashcardsForAllWords(): List<FlashcardItem> {
        return loadFlashcards(query = "", groupId = null)
    }

    suspend fun loadFlashcardsForOneGroup(groupId: Long): List<FlashcardItem> {
        return loadFlashcards(query = "", groupId = groupId)
    }

    private suspend fun loadFlashcards(
        query: String,
        groupId: Long?,
    ): List<FlashcardItem> {
        return wordCardDao.observeWords(query = query, groupId = groupId)
            .first()
            .map { it.toFlashcardItem() }
    }

    private fun WordCardEntity.toListItem(): DictionaryWordListItem {
        return DictionaryWordListItem(
            id = id,
            bgWord = bgWord,
            ruTranslation = ruTranslation,
        )
    }

    private fun WordCardEntity.toFlashcardItem(): FlashcardItem {
        return FlashcardItem(
            id = id,
            bgWord = bgWord,
            ruTranslation = ruTranslation,
        )
    }

    private fun WordGroupWithCount.toDomain(): WordGroup {
        return WordGroup(
            id = id,
            name = name,
            wordCount = wordCount,
        )
    }

    private fun WordCardWithGroups.toDomain(): WordCard {
        return WordCard(
            id = wordCard.id,
            bgWord = wordCard.bgWord,
            ruTranslation = wordCard.ruTranslation,
            groupIds = groups.map { it.id }.sorted(),
        )
    }
}
