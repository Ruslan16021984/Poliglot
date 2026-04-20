package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal fun generateLesson7Exercises(): List<LessonExercise> {
    return (1..60).map { id ->
        generateLesson7Exercise(id)
    }
}

internal fun generateLesson7Exercise(id: Int): LessonExercise {
    val template = lesson7Templates.random()

    val hint = when {
        template.correctWords.any { it in listOf("моята", "моят", "нашето", "нашите") } ->
            "💡 притяжательное местоимение согласуется с существительным"

        template.correctWords.any { it.endsWith("та") || it.endsWith("то") || it.endsWith("те") } ->
            "💡 форма с артиклем делает предмет конкретным"

        "ти" in template.correctWords ->
            "💡 \"ти\" здесь значит «тебе»"

        else -> null
    }

    return buildTranslationExercise(
        id = id,
        sourceText = template.ru,
        correctWords = template.correctWords,
        distractorPool = lesson7WordPool,
        hint = hint
    )
}
