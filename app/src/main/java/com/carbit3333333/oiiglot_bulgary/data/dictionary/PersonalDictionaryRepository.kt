package com.carbit3333333.oiiglot_bulgary.data.dictionary

import android.content.Context
import androidx.room.withTransaction
import com.carbit3333333.oiiglot_bulgary.R
import com.carbit3333333.oiiglot_bulgary.model.dictionary.DictionaryWordListItem
import com.carbit3333333.oiiglot_bulgary.model.dictionary.FlashcardItem
import com.carbit3333333.oiiglot_bulgary.model.dictionary.WordCard
import com.carbit3333333.oiiglot_bulgary.model.dictionary.WordGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class PersonalDictionaryRepository(
    context: Context,
) {
    companion object {
        const val DIFFICULT_GROUP_ID: Long = -2L
        private const val COURSE_LESSON_GROUP_BASE: Long = -1_000L

        fun courseLessonGroupId(lessonNumber: Int): Long {
            return COURSE_LESSON_GROUP_BASE - lessonNumber
        }

        fun courseLessonNumberFromGroupId(groupId: Long): Int? {
            val lessonNumber = (COURSE_LESSON_GROUP_BASE - groupId).toInt()
            return lessonNumber.takeIf { it > 0 }
        }
    }

    private val appContext = context.applicationContext
    private val database = PersonalDictionaryDatabase.getInstance(appContext)
    private val wordCardDao = database.wordCardDao()
    private val wordGroupDao = database.wordGroupDao()
    private val courseWordsRepository = CourseDictionaryWordsRepository(appContext)
    private val difficultWordsStore = DifficultWordsStore(appContext)
    private val builtInWords = courseWordsRepository.loadWords()

    fun observeAllWords(): Flow<List<DictionaryWordListItem>> {
        return observeFilteredWords(query = "", groupId = null)
    }

    fun observeFilteredWords(
        query: String,
        groupId: Long? = null,
    ): Flow<List<DictionaryWordListItem>> {
        val normalizedQuery = query.trim()

        if (groupId == DIFFICULT_GROUP_ID) {
            return combine(
                observeFilteredWords(query = query, groupId = null),
                difficultWordsStore.difficultWordIdsFlow,
            ) { words, difficultIds ->
                words.filter { it.id in difficultIds }
            }
        }

        groupId?.let(::courseLessonNumberFromGroupId)?.let { lessonNumber ->
            return flowOf(
                builtInWords
                    .filter { it.sourceLessonNumber == lessonNumber }
                    .filter { it.matchesQuery(normalizedQuery) }
                    .sortedBy { it.bgWord.lowercase() }
            )
        }

        val effectiveGroupId = groupId.takeUnless { it == CourseDictionaryWordsRepository.COURSE_GROUP_ID }
        return wordCardDao.observeWords(query = normalizedQuery, groupId = effectiveGroupId)
            .map { words ->
                val userWords = if (groupId == CourseDictionaryWordsRepository.COURSE_GROUP_ID) {
                    emptyList()
                } else {
                    words.map { it.toListItem() }
                }
                val courseWords = if (groupId == null || groupId == CourseDictionaryWordsRepository.COURSE_GROUP_ID) {
                    builtInWords.filter { it.matchesQuery(normalizedQuery) }
                } else {
                    emptyList()
                }

                mergeWords(
                    builtIn = courseWords,
                    user = userWords,
                )
            }
    }

    fun observeGroupsWithCounts(): Flow<List<WordGroup>> {
        return combine(
            wordGroupDao.observeAllGroupsWithCounts(),
            difficultWordsStore.difficultWordIdsFlow,
            observeFilteredWords(query = "", groupId = null),
        ) { groups, difficultIds, allWords ->
            val difficultWordCount = allWords.count { it.id in difficultIds }.toLong()
            val lessonGroups = builtInWords
                .groupBy { it.sourceLessonNumber }
                .entries
                .filter { it.key != null }
                .sortedBy { it.key ?: Int.MAX_VALUE }
                .map { (lessonNumber, words) ->
                    WordGroup(
                        id = courseLessonGroupId(requireNotNull(lessonNumber)),
                        name = appContext.getString(
                            R.string.dictionary_course_lesson_badge,
                            lessonNumber,
                        ),
                        wordCount = words.size.toLong(),
                    )
                }

            listOf(
                WordGroup(
                    id = DIFFICULT_GROUP_ID,
                    name = appContext.getString(R.string.dictionary_difficult_group_name),
                    wordCount = difficultWordCount,
                ),
                WordGroup(
                    id = CourseDictionaryWordsRepository.COURSE_GROUP_ID,
                    name = appContext.getString(R.string.dictionary_course_group_name),
                    wordCount = builtInWords.size.toLong(),
                )
            ) + lessonGroups + groups.map { it.toDomain() }
        }
    }

    suspend fun getWordById(wordId: Long): WordCard? {
        if (wordId <= 0L) return null
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
        if (wordId <= 0L) return
        database.withTransaction {
            val word = wordCardDao.getWordWithGroupsById(wordId)?.wordCard ?: return@withTransaction
            wordCardDao.deleteWord(word)
        }
    }

    suspend fun loadFlashcardsForAllWords(): List<FlashcardItem> {
        val userWords = loadFlashcards(query = "", groupId = null)
        return mergeFlashcards(
            builtIn = builtInWords.map { it.toFlashcardItem() },
            user = userWords,
        )
    }

    suspend fun loadFlashcardsForOneGroup(groupId: Long): List<FlashcardItem> {
        if (groupId == DIFFICULT_GROUP_ID) {
            return loadFlashcardsForDifficultWords()
        }
        if (groupId == CourseDictionaryWordsRepository.COURSE_GROUP_ID) {
            return builtInWords.map { it.toFlashcardItem() }
        }
        courseLessonNumberFromGroupId(groupId)?.let { lessonNumber ->
            return builtInWords
                .filter { it.sourceLessonNumber == lessonNumber }
                .map { it.toFlashcardItem() }
        }
        return loadFlashcards(query = "", groupId = groupId)
    }

    suspend fun markFlashcardKnown(card: FlashcardItem) {
        difficultWordsStore.markKnown(card.id)
    }

    suspend fun markFlashcardUnknown(card: FlashcardItem) {
        difficultWordsStore.markUnknown(card.id)
    }

    private suspend fun loadFlashcardsForDifficultWords(): List<FlashcardItem> {
        val difficultIds = difficultWordsStore.difficultWordIdsFlow.first()
        return loadFlashcardsForAllWords().filter { it.id in difficultIds }
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

    private fun DictionaryWordListItem.matchesQuery(query: String): Boolean {
        if (query.isBlank()) return true
        val normalizedQuery = query.lowercase()
        return bgWord.lowercase().contains(normalizedQuery) ||
            ruTranslation.lowercase().contains(normalizedQuery)
    }

    private fun mergeWords(
        builtIn: List<DictionaryWordListItem>,
        user: List<DictionaryWordListItem>,
    ): List<DictionaryWordListItem> {
        val merged = LinkedHashMap<String, DictionaryWordListItem>()
        builtIn.forEach { word -> merged[word.mergeKey()] = word }
        user.forEach { word -> merged[word.mergeKey()] = word }
        return merged.values.sortedBy { it.bgWord.lowercase() }
    }

    private fun mergeFlashcards(
        builtIn: List<FlashcardItem>,
        user: List<FlashcardItem>,
    ): List<FlashcardItem> {
        val merged = LinkedHashMap<String, FlashcardItem>()
        builtIn.forEach { word -> merged[word.mergeKey()] = word }
        user.forEach { word -> merged[word.mergeKey()] = word }
        return merged.values.sortedBy { it.bgWord.lowercase() }
    }

    private fun DictionaryWordListItem.mergeKey(): String {
        return "${bgWord.lowercase()}|${ruTranslation.lowercase()}"
    }

    private fun FlashcardItem.mergeKey(): String {
        return "${bgWord.lowercase()}|${ruTranslation.lowercase()}"
    }

    private fun DictionaryWordListItem.toFlashcardItem(): FlashcardItem {
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
