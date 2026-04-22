package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import com.carbit3333333.oiiglot_bulgary.model.Lesson4Item
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal fun generateLesson4Exercises(items: List<Lesson4Item>): List<LessonExercise> {
    return (1..40).map { id ->
        generateLesson4Exercise(
            id = id,
            items = items,
        )
    }
}

internal fun generateLesson4Exercise(
    id: Int,
    items: List<Lesson4Item>,
): LessonExercise {
    val item = selectLesson4Item(
        id = id,
        items = items,
    )
    val correctWords = item.correctWords

    val distractorPool = (
        items.flatMap { it.correctWords } +
            listOf("Ти", "Те", "не")
        ).distinct()

    val hint = when {
        "да" in correctWords ->
            "💡 После искам / обичам действие идёт с \"да\"."

        hasDefiniteObject(correctWords) ->
            "💡 Если речь о конкретном предмете, используй форму с артиклем."

        else -> null
    }

    return buildTranslationExercise(
        id = id,
        sourceText = item.ru,
        correctWords = correctWords,
        distractorPool = distractorPool,
        hint = hint,
    )
}

private fun selectLesson4Item(
    id: Int,
    items: List<Lesson4Item>,
): Lesson4Item {
    val combinedPhrases = orderCombinedLesson4Items(items.filter(::isCombinedLesson4Item))
    return combinedPhrases[(id - 1) % combinedPhrases.size]
}

private fun isBasicNounItem(item: Lesson4Item): Boolean {
    return item.type == Lesson4Item.Type.NOUN &&
        item.correctWords.size == 1 &&
        item.correctWords.none { it.endsWith("та") || it.endsWith("то") }
}

private fun isDefiniteNounItem(item: Lesson4Item): Boolean {
    return item.type == Lesson4Item.Type.NOUN &&
        item.correctWords.size == 1 &&
        item.correctWords.any { it.endsWith("та") || it.endsWith("то") }
}

private fun isBareVerbItem(item: Lesson4Item): Boolean {
    return item.type == Lesson4Item.Type.VERB &&
        item.correctWords.size == 2 &&
        item.correctWords.firstOrNull() == "да"
}

private fun isCombinedLesson4Item(item: Lesson4Item): Boolean {
    return !isBasicNounItem(item) &&
        !isDefiniteNounItem(item) &&
        !isBareVerbItem(item)
}

private fun hasDefiniteObject(correctWords: List<String>): Boolean {
    return correctWords.any { it.endsWith("та") || it.endsWith("то") }
}

private fun orderCombinedLesson4Items(items: List<Lesson4Item>): List<Lesson4Item> {
    return items
}
