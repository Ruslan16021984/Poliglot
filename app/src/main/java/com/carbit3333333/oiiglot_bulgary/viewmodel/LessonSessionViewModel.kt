package com.carbit3333333.oiiglot_bulgary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.carbit3333333.oiiglot_bulgary.R
import com.carbit3333333.oiiglot_bulgary.data.LessonProgressStore
import com.carbit3333333.oiiglot_bulgary.data.LessonSessionRepository
import com.carbit3333333.oiiglot_bulgary.data.LessonSessionStore
import com.carbit3333333.oiiglot_bulgary.data.relocalizeRestoredLessonSessionState
import com.carbit3333333.oiiglot_bulgary.model.ExerciseResult
import com.carbit3333333.oiiglot_bulgary.model.LessonResult
import com.carbit3333333.oiiglot_bulgary.ui.lessons.LessonSessionUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Locale

internal const val PASSING_SCORE = 4.5f

class LessonSessionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private var currentLessonId: Int = 0
    private val repository = LessonSessionRepository(application)
    private val progressStore = LessonProgressStore(application)
    private val sessionStore = LessonSessionStore(application)
    private val resources = application.resources
    private val persistenceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var resultSaved = false

    private val praises = listOf(
        R.string.lesson_session_praise_bravo,
        R.string.lesson_session_praise_super,
        R.string.lesson_session_praise_wonderful,
        R.string.lesson_session_praise_excellent,
        R.string.lesson_session_praise_talent,
    )

    private val _uiState = MutableStateFlow(LessonSessionUiState())
    val uiState: StateFlow<LessonSessionUiState> = _uiState.asStateFlow()

    fun loadLessonSession(lessonId: Int) {
        viewModelScope.launch {
            currentLessonId = lessonId
            resultSaved = false

            val session = repository.getLessonSession(lessonId)
            val savedState = sessionStore.loadSession(lessonId)
            if (savedState != null) {
                val localizedState = relocalizeRestoredLessonSessionState(
                    savedState = savedState,
                    localizedSession = session,
                )
                _uiState.value = localizedState
                saveCurrentSession()

                saveLessonProgress(
                    currentStep = localizedState.currentExerciseIndex,
                    totalSteps = localizedState.exercises.size,
                    correctCount = localizedState.correctCount
                )
                return@launch
            }

            val newState = LessonSessionUiState(
                lessonTitle = session.lessonTitle,
                exercises = session.exercises,
                results = List(session.exercises.size) { ExerciseResult.NONE }
            )

            _uiState.value = newState
            saveCurrentSession()
            saveLessonProgress(
                currentStep = 0,
                totalSteps = session.exercises.size,
                correctCount = 0
            )
        }
    }

    fun persistSessionSnapshot() {
        if (currentLessonId == 0) return

        val snapshot = _uiState.value
        if (snapshot.isLessonFinished || snapshot.lessonResult != null) {
            persistenceScope.launch {
                sessionStore.clearSession()
                progressStore.saveLessonProgressSnapshot(
                    lessonId = currentLessonId,
                    currentStep = snapshot.currentExerciseIndex,
                    totalSteps = snapshot.exercises.size,
                    currentScore = calculateCurrentScore(
                        correctCount = snapshot.correctCount,
                        totalSteps = snapshot.exercises.size
                    )
                )
            }
            return
        }

        runBlocking {
            withContext(Dispatchers.IO) {
                sessionStore.saveSessionSnapshot(currentLessonId, snapshot)
                progressStore.saveLessonProgressSnapshot(
                    lessonId = currentLessonId,
                    currentStep = snapshot.currentExerciseIndex,
                    totalSteps = snapshot.exercises.size,
                    currentScore = calculateCurrentScore(
                        correctCount = snapshot.correctCount,
                        totalSteps = snapshot.exercises.size
                    )
                )
            }
        }
    }

    fun selectWord(word: String) {
        val state = _uiState.value
        if (state.currentResult != ExerciseResult.NONE) return

        _uiState.value = state.copy(
            selectedWords = state.selectedWords + word
        )
        saveCurrentSession()
    }

    fun removeSelectedWord(word: String) {
        val state = _uiState.value
        if (state.currentResult != ExerciseResult.NONE) return

        val mutable = state.selectedWords.toMutableList()
        mutable.remove(word)

        _uiState.value = state.copy(selectedWords = mutable)
        saveCurrentSession()
    }

    fun applyRecognizedAnswer(recognizedText: String) {
        val state = _uiState.value
        val exercise = state.currentExercise ?: return
        if (state.currentResult != ExerciseResult.NONE) return

        val recognizedTokens = tokenizeRecognizedAnswer(recognizedText)
        if (recognizedTokens.isEmpty()) return

        val availableWords = exercise.availableWords.toMutableList()
        val matchedWords = mutableListOf<String>()

        recognizedTokens.forEach { recognizedToken ->
            val matchedIndex = availableWords.indexOfFirst { availableWord ->
                normalizeRecognizedToken(availableWord) == recognizedToken
            }

            if (matchedIndex >= 0) {
                matchedWords += availableWords.removeAt(matchedIndex)
            }
        }

        if (matchedWords.isEmpty()) return

        _uiState.value = state.copy(selectedWords = matchedWords)
        saveCurrentSession()
    }

    fun checkAnswer() {
        val state = _uiState.value
        val exercise = state.currentExercise ?: return

        val isCorrect = state.selectedWords == exercise.correctAnswerWords
        val newResults = state.results.toMutableList()

        if (isCorrect) {
            newResults[state.currentExerciseIndex] = ExerciseResult.CORRECT

            _uiState.value = state.copy(
                results = newResults,
                currentResult = ExerciseResult.CORRECT,
                correctCount = state.correctCount + 1,
                praiseText = resources.getString(praises.random())
            )
            saveCurrentSession()

            viewModelScope.launch {
                delay(1800)
                moveToNextExercise()
            }
        } else {
            newResults[state.currentExerciseIndex] = ExerciseResult.WRONG

            _uiState.value = state.copy(
                results = newResults,
                currentResult = ExerciseResult.WRONG,
                wrongCount = state.wrongCount + 1
            )
            saveCurrentSession()
        }
    }

    fun onWrongAnswerScreenTap() {
        val state = _uiState.value
        if (state.currentResult != ExerciseResult.WRONG) return
        moveToNextExercise()
    }

    private fun moveToNextExercise() {
        val state = _uiState.value
        val totalExercises = state.exercises.size
        val nextIndex = state.currentExerciseIndex + 1
        val correctCount = state.correctCount
        val wrongCount = state.wrongCount
        val score = calculateLessonScore(
            correctCount = correctCount,
            totalExercises = totalExercises
        )
        val isPassed = score >= PASSING_SCORE
        val isFinished = nextIndex >= totalExercises
        val shouldFinishNow = isFinished || isPassed

        if (shouldFinishNow) {
            val lessonResult = LessonResult(
                lessonId = currentLessonId,
                lessonTitle = state.lessonTitle,
                totalExercises = totalExercises,
                correctCount = correctCount,
                wrongCount = wrongCount,
                score = score,
                isPassed = isPassed
            )

            saveProgressOnce(
                lessonId = currentLessonId,
                correctCount = correctCount,
                wrongCount = wrongCount,
                score = score,
                isPassed = isPassed
            )

            _uiState.value = state.copy(
                selectedWords = emptyList(),
                currentResult = ExerciseResult.NONE,
                praiseText = null,
                isLessonFinished = true,
                lessonResult = lessonResult
            )

            saveLessonProgress(
                currentStep = if (isFinished) totalExercises else nextIndex,
                totalSteps = totalExercises,
                correctCount = correctCount
            )

            viewModelScope.launch {
                sessionStore.clearSession()
            }
        } else {
            _uiState.value = state.copy(
                currentExerciseIndex = nextIndex,
                selectedWords = emptyList(),
                currentResult = ExerciseResult.NONE,
                praiseText = null
            )
            saveCurrentSession()
            saveLessonProgress(
                currentStep = nextIndex,
                totalSteps = state.exercises.size,
                correctCount = state.correctCount
            )
        }
    }

    private fun saveProgressOnce(
        lessonId: Int,
        correctCount: Int,
        wrongCount: Int,
        score: Float,
        isPassed: Boolean
    ) {
        if (resultSaved) return
        resultSaved = true

        viewModelScope.launch {
            progressStore.saveLessonResult(
                lessonId = lessonId,
                correctCount = correctCount,
                wrongCount = wrongCount,
                score = score,
                isPassed = isPassed
            )

            if (isPassed) {
                progressStore.unlockNextLesson(lessonId + 1)
            }
        }
    }

    private fun saveLessonProgress(
        currentStep: Int,
        totalSteps: Int,
        correctCount: Int
    ) {
        if (currentLessonId == 0) return

        persistenceScope.launch {
            progressStore.saveLessonProgress(
                lessonId = currentLessonId,
                currentStep = currentStep,
                totalSteps = totalSteps,
                currentScore = calculateCurrentScore(
                    correctCount = correctCount,
                    totalSteps = totalSteps
                )
            )
        }
    }

    private fun saveCurrentSession() {
        if (currentLessonId == 0) return
        val snapshot = _uiState.value
        if (snapshot.isLessonFinished || snapshot.lessonResult != null) return

        persistenceScope.launch {
            sessionStore.saveSession(currentLessonId, snapshot)
        }
    }

    private fun tokenizeRecognizedAnswer(recognizedText: String): List<String> {
        return recognizedText
            .split(Regex("\\s+"))
            .map(::normalizeRecognizedToken)
            .filter { it.isNotBlank() }
    }

    private fun normalizeRecognizedToken(token: String): String {
        return token
            .lowercase(Locale.ROOT)
            .replace("ѝ", "и")
            .replace(Regex("[^\\p{L}\\p{Nd}]"), "")
    }
    private fun calculateCurrentScore(correctCount: Int, totalSteps: Int): Float? {
        if (totalSteps <= 0) return null
        return calculateLessonScore(
            correctCount = correctCount,
            totalExercises = totalSteps
        )
    }

    override fun onCleared() {
        persistenceScope.cancel()
        super.onCleared()
    }
}

internal fun calculateLessonScore(correctCount: Int, totalExercises: Int): Float {
    if (totalExercises <= 0) return 0f
    return (correctCount.toFloat() / totalExercises.toFloat()) * 5f
}
