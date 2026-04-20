package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal const val TRANSLATION_INSTRUCTION = "Переведите предложение"

internal fun buildTranslationExercise(
    id: Int,
    sourceText: String,
    correctWords: List<String>,
    distractorPool: List<String>,
    hint: String? = null,
    totalWords: Int = 8
): LessonExercise {
    return LessonExercise(
        id = id,
        sourceText = sourceText,
        instruction = TRANSLATION_INSTRUCTION,
        correctAnswerWords = correctWords,
        availableWords = buildAvailableWords(
            correctWords = correctWords,
            distractorPool = distractorPool,
            totalWords = totalWords
        ),
        hint = hint
    )
}

internal fun buildAvailableWords(
    correctWords: List<String>,
    distractorPool: List<String>,
    totalWords: Int = 8
): List<String> {
    val uniqueCorrectWords = correctWords.distinct()

    require(uniqueCorrectWords.isNotEmpty()) {
        "Correct words must not be empty"
    }

    require(uniqueCorrectWords.size <= totalWords) {
        "Correct words count (${uniqueCorrectWords.size}) can't be greater than totalWords ($totalWords)"
    }

    val distractors = distractorPool
        .filterNot { it in uniqueCorrectWords }
        .distinct()
        .shuffled()
        .take(totalWords - uniqueCorrectWords.size)

    val result = (uniqueCorrectWords + distractors).shuffled()

    require(result.size == totalWords) {
        "Available words size must be $totalWords, but was ${result.size}. Correct=$uniqueCorrectWords, distractors=$distractors"
    }

    require(uniqueCorrectWords.all { it in result }) {
        "Not all correct words were added. Correct=$uniqueCorrectWords, result=$result"
    }

    return result
}

internal fun toRussianInfinitive(bgInfinitive: String): String {
    return when (bgInfinitive) {
        "правя" -> "делать"
        "гледам" -> "смотреть"
        "отивам" -> "идти"
        "ям" -> "есть"
        "пия" -> "пить"
        "работя" -> "работать"
        "уча" -> "учиться"
        "говоря" -> "говорить"
        "виждам" -> "видеть"
        "искам" -> "хотеть"
        else -> "делать"
    }
}

internal fun buildHint(correctWords: List<String>): String? {
    return when {
        "няма" in correctWords && "да" in correctWords ->
            "💡 няма + да + глагол"

        "ще" in correctWords ->
            "💡 ще + глагол"

        "ли" in correctWords ->
            "💡 \"ли\" ставится после глагола"

        "не" in correctWords ->
            "💡 \"не\" ставится перед глаголом"

        else -> null
    }
}
