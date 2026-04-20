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
    val item = selectLesson4Item(
        id = id,
        items = items
    )
    val correctWords = item.correctWords

    val distractorPool = (
        items.flatMap { it.correctWords } +
            listOf("Ти", "Той", "Вие", "Те", "не")
        ).distinct()

    val hint = when {
        isDefiniteNounItem(item) ->
            "💡 это конкретный предмет → добавь окончание"

        isBareVerbItem(item) ->
            "💡 действие → используй \"да\""

        "да" in correctWords ->
            "💡 после хочу / люблю действие идёт с \"да\""

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

private fun selectLesson4Item(
    id: Int,
    items: List<Lesson4Item>
): Lesson4Item {
    val basicNouns = items.filter(::isBasicNounItem)
    val definiteNouns = items.filter(::isDefiniteNounItem)
    val bareVerbs = items.filter(::isBareVerbItem)
    val combinedPhrases = orderCombinedLesson4Items(items.filter(::isCombinedLesson4Item))

    val phaseIndex = id - 1

    return when {
        phaseIndex < 8 -> basicNouns[phaseIndex % basicNouns.size]
        phaseIndex < 16 -> definiteNouns[(phaseIndex - 8) % definiteNouns.size]
        phaseIndex < 24 -> bareVerbs[(phaseIndex - 16) % bareVerbs.size]
        else -> combinedPhrases[(phaseIndex - 24) % combinedPhrases.size]
    }
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

private fun orderCombinedLesson4Items(items: List<Lesson4Item>): List<Lesson4Item> {
    val priority = listOf(
        "я хочу книгу",
        "я хочу эту книгу",
        "я хочу есть",
        "я хочу пить",
        "я хочу работать",
        "я хочу читать",
        "я люблю читать",
        "я люблю пить кофе",
        "мы хотим работать",
        "мы любим читать",
        "он хочет работать",
        "он хочет эту работу",
        "вы хотите читать",
        "мы любим кофе",
        "мы любим пить кофе",
        "мы хотим эту воду",
        "я люблю кофе"
    )

    val priorityMap = priority.withIndex().associate { (index, ru) -> ru to index }

    return items.sortedWith(
        compareBy<Lesson4Item> { priorityMap[it.ru] ?: Int.MAX_VALUE }
            .thenBy { it.ru }
    )
}
