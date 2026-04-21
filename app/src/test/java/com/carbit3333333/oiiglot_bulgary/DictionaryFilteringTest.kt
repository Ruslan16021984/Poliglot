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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DictionaryFilteringTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var context: Context
    private lateinit var database: PersonalDictionaryDatabase
    private lateinit var repository: PersonalDictionaryRepository

    @Before
    fun setUp() = runBlocking {
        context = ApplicationProvider.getApplicationContext()
        database = PersonalDictionaryDatabase.getInstance(context)
        database.clearAllTables()
        repository = PersonalDictionaryRepository(context)

        val travelGroupId = repository.createGroup("Путешествие")
        val foodGroupId = repository.createGroup("Еда")

        repository.saveWord(
            WordCard(
                bgWord = "здравей",
                ruTranslation = "привет",
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
                bgWord = "гара",
                ruTranslation = "вокзал",
                groupIds = listOf(travelGroupId),
            )
        )
    }

    @After
    fun tearDown() {
        database.clearAllTables()
    }

    @Test
    fun `query matches bulgarian word`() = runBlocking {
        val result = repository.observeFilteredWords(query = "здрав", groupId = null).first()

        assertEquals(listOf("здравей"), result.map { it.bgWord })
    }

    @Test
    fun `query matches russian translation`() = runBlocking {
        val result = repository.observeFilteredWords(query = "вокз", groupId = null).first()

        assertEquals(listOf("гара"), result.map { it.bgWord })
    }

    @Test
    fun `group filter narrows results`() = runBlocking {
        val travelGroupId = repository.observeGroupsWithCounts().first().first { it.name == "Путешествие" }.id

        val result = repository.observeFilteredWords(query = "", groupId = travelGroupId).first()

        assertEquals(setOf("здравей", "гара"), result.map { it.bgWord }.toSet())
    }
}
