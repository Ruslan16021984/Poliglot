package com.carbit3333333.oiiglot_bulgary

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.carbit3333333.oiiglot_bulgary.data.dictionary.PersonalDictionaryDatabase
import com.carbit3333333.oiiglot_bulgary.data.dictionary.PersonalDictionaryRepository
import com.carbit3333333.oiiglot_bulgary.model.dictionary.WordCard
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PersonalDictionaryRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var context: Context
    private lateinit var database: PersonalDictionaryDatabase
    private lateinit var repository: PersonalDictionaryRepository

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        database = PersonalDictionaryDatabase.getInstance(context)
        database.clearAllTables()
        repository = PersonalDictionaryRepository(context)
    }

    @After
    fun tearDown() {
        database.clearAllTables()
    }

    @Test
    fun `save word without groups persists trimmed values`() = runBlocking {
        val wordId = repository.saveWord(
            WordCard(
                bgWord = "  здравей  ",
                ruTranslation = "  привет  ",
            )
        )

        val savedWord = repository.getWordById(wordId)

        assertNotNull(savedWord)
        assertEquals("здравей", savedWord?.bgWord)
        assertEquals("привет", savedWord?.ruTranslation)
        assertTrue(savedWord?.groupIds?.isEmpty() == true)
    }

    @Test
    fun `save word with multiple groups stores all selected groups`() = runBlocking {
        val travelGroupId = repository.createGroup("Путешествие")
        val foodGroupId = repository.createGroup("Еда")

        val wordId = repository.saveWord(
            WordCard(
                bgWord = "билет",
                ruTranslation = "билет",
                groupIds = listOf(foodGroupId, travelGroupId, foodGroupId),
            )
        )

        val savedWord = repository.getWordById(wordId)
        val groups = repository.observeGroupsWithCounts().first()

        assertEquals(listOf(travelGroupId, foodGroupId).sorted(), savedWord?.groupIds)
        assertEquals(
            mapOf(
                "Еда" to 1L,
                "Путешествие" to 1L,
            ),
            groups.associate { it.name to it.wordCount },
        )
    }

    @Test
    fun `edit word replaces selected groups`() = runBlocking {
        val travelGroupId = repository.createGroup("Путешествие")
        val foodGroupId = repository.createGroup("Еда")
        val phrasesGroupId = repository.createGroup("Фразы")
        val wordId = repository.saveWord(
            WordCard(
                bgWord = "искам",
                ruTranslation = "хочу",
                groupIds = listOf(travelGroupId, foodGroupId),
            )
        )

        repository.saveWord(
            WordCard(
                id = wordId,
                bgWord = "искам да",
                ruTranslation = "хочу",
                groupIds = listOf(phrasesGroupId),
            )
        )

        val savedWord = repository.getWordById(wordId)
        val groups = repository.observeGroupsWithCounts().first()

        assertEquals("искам да", savedWord?.bgWord)
        assertEquals(listOf(phrasesGroupId), savedWord?.groupIds)
        assertEquals(0L, groups.first { it.id == travelGroupId }.wordCount)
        assertEquals(0L, groups.first { it.id == foodGroupId }.wordCount)
        assertEquals(1L, groups.first { it.id == phrasesGroupId }.wordCount)
    }

    @Test
    fun `delete word removes it from repository outputs`() = runBlocking {
        val wordId = repository.saveWord(
            WordCard(
                bgWord = "вода",
                ruTranslation = "вода",
            )
        )

        repository.deleteWord(wordId)

        assertNull(repository.getWordById(wordId))
        assertTrue(repository.observeAllWords().first().isEmpty())
    }

    @Test
    fun `load flashcards for all words returns every saved word`() = runBlocking {
        repository.saveWord(
            WordCard(
                bgWord = "чай",
                ruTranslation = "чай",
            )
        )
        repository.saveWord(
            WordCard(
                bgWord = "кафе",
                ruTranslation = "кофе",
            )
        )

        val flashcards = repository.loadFlashcardsForAllWords()

        assertEquals(2, flashcards.size)
        assertEquals(setOf("чай", "кафе"), flashcards.map { it.bgWord }.toSet())
    }

    @Test
    fun `load flashcards for one group returns only grouped words`() = runBlocking {
        val travelGroupId = repository.createGroup("Путешествие")
        val foodGroupId = repository.createGroup("Еда")
        repository.saveWord(
            WordCard(
                bgWord = "гара",
                ruTranslation = "вокзал",
                groupIds = listOf(travelGroupId),
            )
        )
        repository.saveWord(
            WordCard(
                bgWord = "супа",
                ruTranslation = "суп",
                groupIds = listOf(foodGroupId),
            )
        )
        repository.saveWord(
            WordCard(
                bgWord = "карта",
                ruTranslation = "карта",
                groupIds = listOf(travelGroupId, foodGroupId),
            )
        )

        val flashcards = repository.loadFlashcardsForOneGroup(travelGroupId)

        assertEquals(2, flashcards.size)
        assertEquals(setOf("гара", "карта"), flashcards.map { it.bgWord }.toSet())
    }
}
