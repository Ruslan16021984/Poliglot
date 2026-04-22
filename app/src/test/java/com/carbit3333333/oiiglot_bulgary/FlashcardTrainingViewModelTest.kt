package com.carbit3333333.oiiglot_bulgary

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.carbit3333333.oiiglot_bulgary.model.dictionary.FlashcardItem
import com.carbit3333333.oiiglot_bulgary.ui.dictionary.FlashcardFace
import com.carbit3333333.oiiglot_bulgary.viewmodel.FlashcardTrainingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FlashcardTrainingViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `load success and training actions update state through finish`() = runTest {
        val cards = listOf(
            FlashcardItem(id = 1L, bgWord = "zdravei", ruTranslation = "privet"),
            FlashcardItem(id = 2L, bgWord = "chai", ruTranslation = "tea"),
        )
        val viewModel = FlashcardTrainingViewModel(
            selectedGroupId = null,
            selectedGroupName = "Travel",
            flashcardLoader = { cards },
        )

        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(cards, viewModel.uiState.value.cards)
        assertEquals("1 / 2", viewModel.uiState.value.progressText)
        assertEquals(FlashcardFace.Front, viewModel.uiState.value.currentCardFace)
        assertNull(viewModel.uiState.value.errorMessage)

        viewModel.flipCard()

        assertEquals(FlashcardFace.Back, viewModel.uiState.value.currentCardFace)

        viewModel.markKnown()

        assertEquals(1, viewModel.uiState.value.currentIndex)
        assertEquals(1, viewModel.uiState.value.knownCount)
        assertEquals(0, viewModel.uiState.value.unknownCount)
        assertFalse(viewModel.uiState.value.isFinished)
        assertEquals(FlashcardFace.Front, viewModel.uiState.value.currentCardFace)
        assertEquals("2 / 2", viewModel.uiState.value.progressText)

        viewModel.flipCard()
        viewModel.markUnknown()

        assertTrue(viewModel.uiState.value.isFinished)
        assertEquals(1, viewModel.uiState.value.knownCount)
        assertEquals(1, viewModel.uiState.value.unknownCount)
        assertEquals(listOf(cards[1]), viewModel.uiState.value.unknownCards)
        assertEquals("2 / 2", viewModel.uiState.value.progressText)

        viewModel.retryUnknownCards()

        assertFalse(viewModel.uiState.value.isFinished)
        assertEquals(listOf(cards[1]), viewModel.uiState.value.cards)
        assertEquals(0, viewModel.uiState.value.knownCount)
        assertEquals(0, viewModel.uiState.value.unknownCount)
        assertTrue(viewModel.uiState.value.unknownCards.isEmpty())
        assertEquals("1 / 1", viewModel.uiState.value.progressText)
    }

    @Test
    fun `load failure exposes explicit error state`() = runTest {
        val viewModel = FlashcardTrainingViewModel(
            selectedGroupId = null,
            selectedGroupName = null,
            flashcardLoader = { throw IllegalStateException("boom") },
        )

        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.cards.isEmpty())
        assertNull(viewModel.uiState.value.currentCard)
        assertFalse(viewModel.uiState.value.isFinished)
        assertEquals(
            "Не удалось загрузить карточки. Попробуйте ещё раз.",
            viewModel.uiState.value.errorMessage,
        )
    }
}
