package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import com.carbit3333333.oiiglot_bulgary.model.Lesson4Item
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal fun generateLesson4Exercises(items: List<Lesson4Item>): List<LessonExercise> {
    return (1..40).map { id ->
        generateLesson4Exercise(
            id = id,
            items = items
        )
    }
}

internal fun generateLesson4Exercise(
    id: Int,
    items: List<Lesson4Item>
): LessonExercise {
    val item = items.random()
    val correctWords = item.correctWords

    val distractorPool = listOf(
        "Аз", "Ти", "Той",
        "да", "не",
        "искам", "обичам",
        "книга", "книгата",
        "жена", "жената",
        "дете", "детето",
        "ям", "пия", "работя"
    )

    val hint = when {
        item.type == Lesson4Item.Type.NOUN && correctWords.any { it.endsWith("та") || it.endsWith("то") } ->
            "💡 это конкретный предмет → добавь окончание"

        "да" in correctWords ->
            "💡 действие → используй \"да\""

        else -> null
    }

    return buildTranslationExercise(
        id = id,
        sourceText = item.ru,
        correctWords = correctWords,
        distractorPool = distractorPool,
        hint = hint
    )
}
