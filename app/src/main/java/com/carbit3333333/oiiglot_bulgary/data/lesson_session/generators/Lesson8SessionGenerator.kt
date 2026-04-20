package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal fun generateLesson8Exercises(): List<LessonExercise> {
    return (1..60).map { id ->
        generateLesson8Exercise(id)
    }
}

internal fun generateLesson8Exercise(id: Int): LessonExercise {
    val template = lesson8Templates.random()

    val hint = when {
        template.correctWords.any { it.startsWith("по-") } ->
            "💡 сравнение: по- + прилагательное + от"

        template.correctWords.any { it.startsWith("най-") } ->
            "💡 превосходная степень: най- + прилагательное"

        "от" in template.correctWords ->
            "💡 \"от\" означает «чем»"

        else -> null
    }

    return buildTranslationExercise(
        id = id,
        sourceText = template.ru,
        correctWords = template.correctWords,
        distractorPool = lesson8WordPool,
        hint = hint
    )
}
