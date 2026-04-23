package com.carbit3333333.oiiglot_bulgary

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.carbit3333333.oiiglot_bulgary.data.dictionary.PersonalDictionaryDatabase
import com.carbit3333333.oiiglot_bulgary.data.dictionary.PersonalDictionaryRepository
import com.carbit3333333.oiiglot_bulgary.model.dictionary.WordCard
import kotlinx.coroutines.Dispatchers
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
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class PersonalDictionaryRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var context: Context
    private lateinit var database: PersonalDictionaryDatabase
    private lateinit var repository: PersonalDictionaryRepository

    @Before
    fun setUp() {
        runBlocking(Dispatchers.IO) {
            context = ApplicationProvider.getApplicationContext()
            resetDatabase()
            database = PersonalDictionaryDatabase.getInstance(context)
            repository = PersonalDictionaryRepository(context)
        }
    }

    @After
    fun tearDown() {
        runBlocking(Dispatchers.IO) {
            resetDatabase()
        }
    }

    @Test
    fun `save word without groups persists trimmed values`() {
        runBlocking(Dispatchers.IO) {
            val wordId = repository.saveWord(
                WordCard(
                    bgWord = "  zdravei  ",
                    ruTranslation = "  privet  ",
                )
            )

            val savedWord = repository.getWordById(wordId)

            assertNotNull(savedWord)
            assertEquals("zdravei", savedWord?.bgWord)
            assertEquals("privet", savedWord?.ruTranslation)
            assertTrue(savedWord?.groupIds?.isEmpty() == true)
        }
    }

    @Test
    fun `save word with multiple groups stores all selected groups`() {
        runBlocking(Dispatchers.IO) {
            val travelGroupId = repository.createGroup("Travel")
            val foodGroupId = repository.createGroup("Food")

            val wordId = repository.saveWord(
                WordCard(
                    bgWord = "bilet",
                    ruTranslation = "ticket",
                    groupIds = listOf(foodGroupId, travelGroupId, foodGroupId),
                )
            )

            val savedWord = repository.getWordById(wordId)
            val groups = repository.observeGroupsWithCounts().first()

            assertEquals(listOf(travelGroupId, foodGroupId).sorted(), savedWord?.groupIds)
            assertEquals(1L, groups.first { it.name == "Food" }.wordCount)
            assertEquals(1L, groups.first { it.name == "Travel" }.wordCount)
        }
    }

    @Test
    fun `edit word replaces selected groups`() {
        runBlocking(Dispatchers.IO) {
            val travelGroupId = repository.createGroup("Travel")
            val foodGroupId = repository.createGroup("Food")
            val phrasesGroupId = repository.createGroup("Phrases")
            val wordId = repository.saveWord(
                WordCard(
                    bgWord = "iskam",
                    ruTranslation = "want",
                    groupIds = listOf(travelGroupId, foodGroupId),
                )
            )

            repository.saveWord(
                WordCard(
                    id = wordId,
                    bgWord = "iskam da",
                    ruTranslation = "want",
                    groupIds = listOf(phrasesGroupId),
                )
            )

            val savedWord = repository.getWordById(wordId)
            val groups = repository.observeGroupsWithCounts().first()

            assertEquals("iskam da", savedWord?.bgWord)
            assertEquals(listOf(phrasesGroupId), savedWord?.groupIds)
            assertEquals(0L, groups.first { it.id == travelGroupId }.wordCount)
            assertEquals(0L, groups.first { it.id == foodGroupId }.wordCount)
            assertEquals(1L, groups.first { it.id == phrasesGroupId }.wordCount)
        }
    }

    @Test
    fun `delete word removes it from repository outputs`() {
        runBlocking(Dispatchers.IO) {
            val wordId = repository.saveWord(
                WordCard(
                    bgWord = "test_remove_only_user_word",
                    ruTranslation = "test remove only user word",
                )
            )

            repository.deleteWord(wordId)

            assertNull(repository.getWordById(wordId))
            assertTrue(repository.observeAllWords().first().none { it.bgWord == "test_remove_only_user_word" })
        }
    }

    @Test
    fun `load flashcards for all words returns saved words together with course words`() {
        runBlocking(Dispatchers.IO) {
            repository.saveWord(
                WordCard(
                    bgWord = "custom_chai",
                    ruTranslation = "custom tea",
                )
            )
            repository.saveWord(
                WordCard(
                    bgWord = "custom_kafe",
                    ruTranslation = "custom coffee",
                )
            )

            val flashcards = repository.loadFlashcardsForAllWords()

            assertTrue(flashcards.map { it.bgWord }.containsAll(listOf("custom_chai", "custom_kafe")))
            assertTrue(flashcards.any { it.bgWord == "гледам" })
        }
    }

    @Test
    fun `repository exposes separate built in course group and protects built in words`() {
        runBlocking(Dispatchers.IO) {
            val groups = repository.observeGroupsWithCounts().first()
            val courseGroup = groups.firstOrNull { it.id < 0L }

            assertNotNull(courseGroup)
            assertTrue(courseGroup!!.wordCount > 0L)

            val allWords = repository.observeAllWords().first()
            val builtInWord = allWords.firstOrNull { it.isBuiltIn }

            assertNotNull(builtInWord)
            repository.deleteWord(requireNotNull(builtInWord).id)

            val afterDelete = repository.observeAllWords().first()
            assertTrue(afterDelete.any { it.id == builtInWord.id && it.isBuiltIn })
        }
    }

    @Test
    fun `built in course words keep lesson source metadata`() {
        runBlocking(Dispatchers.IO) {
            val builtInWord = repository.observeAllWords().first()
                .firstOrNull { it.isBuiltIn && it.bgWord == "гледам" }

            assertNotNull(builtInWord)
            assertEquals(1, builtInWord?.sourceLessonNumber)
        }
    }

    @Test
    fun `load flashcards for one group returns only grouped words`() {
        runBlocking(Dispatchers.IO) {
            val travelGroupId = repository.createGroup("Travel")
            val foodGroupId = repository.createGroup("Food")
            repository.saveWord(
                WordCard(
                    bgWord = "gara",
                    ruTranslation = "station",
                    groupIds = listOf(travelGroupId),
                )
            )
            repository.saveWord(
                WordCard(
                    bgWord = "supa",
                    ruTranslation = "soup",
                    groupIds = listOf(foodGroupId),
                )
            )
            repository.saveWord(
                WordCard(
                    bgWord = "karta",
                    ruTranslation = "map",
                    groupIds = listOf(travelGroupId, foodGroupId),
                )
            )

            val flashcards = repository.loadFlashcardsForOneGroup(travelGroupId)

            assertEquals(2, flashcards.size)
            assertEquals(setOf("gara", "karta"), flashcards.map { it.bgWord }.toSet())
        }
    }

    private fun resetDatabase() {
        runCatching { database.close() }
        val instanceField = PersonalDictionaryDatabase::class.java.getDeclaredField("INSTANCE")
        instanceField.isAccessible = true
        instanceField.set(null, null)
        context.deleteDatabase("personal_dictionary.db")
    }
}
