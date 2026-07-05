package com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonExerciseLocale
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonExerciseStrings
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.TextbookLessonExerciseSetAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.buildAvailableWords
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal fun generateTextbookLessonExercises(
    exerciseSet: TextbookLessonExerciseSetAsset,
    exerciseLocale: LessonExerciseLocale = LessonExerciseLocale(languageCode = "ru"),
    totalExercises: Int = 100,
): List<LessonExercise> {
    require(exerciseSet.items.isNotEmpty()) { "Textbook lesson exercise list must not be empty." }

    val globalDistractorPool = buildList {
        addAll(exerciseSet.items.flatMap { it.correctWords })
        addAll(exerciseSet.items.flatMap { it.distractors })
        addAll(listOf("не", "ли", "Това", "е", "Аз", "Ти", "Той", "Тя", "То", "Ние", "Вие", "Те"))
    }.distinct()

    return (1..totalExercises).map { id ->
        val item = exerciseSet.items[(id - 1) % exerciseSet.items.size]
        LessonExercise(
            id = id,
            sourceText = item.resolveSourceText(
                languageCode = exerciseLocale.languageCode,
                fallbackLanguageCode = exerciseLocale.fallbackLanguageCode,
            ),
            instruction = LessonExerciseStrings.translationInstruction(
                languageCode = exerciseLocale.languageCode,
                fallbackLanguageCode = exerciseLocale.fallbackLanguageCode,
            ),
            correctAnswerWords = item.correctWords,
            availableWords = buildAvailableWords(
                correctWords = item.correctWords,
                distractorPool = item.distractors + globalDistractorPool,
                totalWords = 8,
            ),
            hint = item.resolveHint(
                languageCode = exerciseLocale.languageCode,
                fallbackLanguageCode = exerciseLocale.fallbackLanguageCode,
            ),
        )
    }
}
