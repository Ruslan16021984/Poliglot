package com.carbit3333333.oiiglot_bulgary

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.carbit3333333.oiiglot_bulgary.data.dictionary.CourseDictionaryWordsRepository
import com.carbit3333333.oiiglot_bulgary.data.dictionary.PersonalDictionaryDatabase
import com.carbit3333333.oiiglot_bulgary.data.dictionary.PersonalDictionaryRepository
import com.carbit3333333.oiiglot_bulgary.model.dictionary.WordCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DictionaryFilteringTest {

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

            val travelGroupId = repository.createGroup("Travel")
            val foodGroupId = repository.createGroup("Food")

            repository.saveWord(
                WordCard(
                    bgWord = "zdravei",
                    ruTranslation = "privet",
                    groupIds = listOf(travelGroupId),
                )
            )
            repository.saveWord(
                WordCard(
                    bgWord = "supa",
                    ruTranslation = "sup",
                    groupIds = listOf(foodGroupId),
                )
            )
            repository.saveWord(
                WordCard(
                    bgWord = "gara",
                    ruTranslation = "vokzal",
                    groupIds = listOf(travelGroupId),
                )
            )
        }
    }

    @After
    fun tearDown() {
        runBlocking(Dispatchers.IO) {
            resetDatabase()
        }
    }

    @Test
    fun `query matches bulgarian word`() {
        runBlocking(Dispatchers.IO) {
            val result = repository.observeFilteredWords(query = "zdrav", groupId = null).first()

            assertEquals(listOf("zdravei"), result.map { it.bgWord })
        }
    }

    @Test
    fun `query matches russian translation`() {
        runBlocking(Dispatchers.IO) {
            val result = repository.observeFilteredWords(query = "vokz", groupId = null).first()

            assertEquals(listOf("gara"), result.map { it.bgWord })
        }
    }

    @Test
    fun `query also finds built in course words`() {
        runBlocking(Dispatchers.IO) {
            val result = repository.observeFilteredWords(query = "прив", groupId = null).first()

            assertTrue(result.any { it.bgWord == "здравей" && it.isBuiltIn })
        }
    }

    @Test
    fun `group filter narrows results`() {
        runBlocking(Dispatchers.IO) {
            val travelGroupId = repository.observeGroupsWithCounts().first()
                .first { it.name == "Travel" }
                .id

            val result = repository.observeFilteredWords(query = "", groupId = travelGroupId).first()

            assertEquals(setOf("zdravei", "gara"), result.map { it.bgWord }.toSet())
        }
    }

    @Test
    fun `built in course group shows only course words`() {
        runBlocking(Dispatchers.IO) {
            val courseGroupId = repository.observeGroupsWithCounts().first()
                .first { it.id == CourseDictionaryWordsRepository.COURSE_GROUP_ID }
                .id

            val result = repository.observeFilteredWords(query = "", groupId = courseGroupId).first()

            assertTrue(result.isNotEmpty())
            assertTrue(result.all { it.isBuiltIn })
            assertTrue(result.any { it.bgWord == "здравей" })
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
