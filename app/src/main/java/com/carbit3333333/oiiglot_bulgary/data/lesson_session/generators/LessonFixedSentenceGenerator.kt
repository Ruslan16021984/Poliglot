package com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonExerciseLocale
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonFixedSentenceAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.buildTranslationExercise
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal fun generateFixedSentenceExercises(
    fixedSentences: List<LessonFixedSentenceAsset>,
    exerciseLocale: LessonExerciseLocale = LessonExerciseLocale.Russian,
    totalExercises: Int = 100,
): List<LessonExercise> {
    require(fixedSentences.isNotEmpty()) { "Fixed sentence list must not be empty." }

    val distractorPool = buildList {
        addAll(fixedSentences.flatMap { it.correctWords })
        addAll(listOf("не", "ли", "Това", "е"))
    }.distinct()

    return (1..totalExercises).map { id ->
        val item = fixedSentences[(id - 1) % fixedSentences.size]
        buildTranslationExercise(
            id = id,
            sourceText = when (exerciseLocale) {
                LessonExerciseLocale.Ukrainian -> item.uk ?: item.ru
                LessonExerciseLocale.Russian -> item.ru
            },
            correctWords = item.correctWords,
            distractorPool = distractorPool,
            hint = item.hint,
        )
    }
}
