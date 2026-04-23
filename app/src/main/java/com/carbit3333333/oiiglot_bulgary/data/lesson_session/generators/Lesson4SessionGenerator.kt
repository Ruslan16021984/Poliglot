package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import com.carbit3333333.oiiglot_bulgary.model.Lesson4Item
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal fun generateLesson4Exercises(items: List<Lesson4Item>): List<LessonExercise> {
    return (1..100).map { id ->
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
    val subjectOrder = listOf("Аз", "Ние", "Той", "Тя", "То", "Вие")
    val groupedItems = subjectOrder.associateWith { subject ->
        items
            .filter { it.correctWords.firstOrNull() == subject }
            .sortedBy(::lesson4Priority)
    }
    val maxGroupSize = groupedItems.values.maxOfOrNull { it.size } ?: 0

    return buildList {
        for (index in 0 until maxGroupSize) {
            for (subject in subjectOrder) {
                groupedItems[subject]?.getOrNull(index)?.let(::add)
            }
        }
    }
}

private fun lesson4Priority(item: Lesson4Item): Int {
    val words = item.correctWords
    val mainVerb = words.getOrNull(1).orEmpty()

    return when {
        "да" !in words && "книгата" in words && mainVerb.startsWith("иск") -> 0
        "да" !in words && "книгата" in words && mainVerb.startsWith("обич") -> 1
        "да" in words && words.any { it.startsWith("чет") } -> 2
        "да" in words && words.any { it.startsWith("уч") } -> 3
        "да" !in words && "работата" in words && mainVerb.startsWith("иск") -> 4
        "да" in words && words.any { it.startsWith("яд") || it == "ям" } -> 5
        "да" !in words && "водата" in words -> 6
        "да" in words && words.contains("кафе") -> 7
        "да" !in words && "кафето" in words && mainVerb.startsWith("иск") -> 8
        "да" !in words && "работата" in words && mainVerb.startsWith("обич") -> 9
        "да" !in words && "кафето" in words && mainVerb.startsWith("обич") -> 10
        "да" in words && words.any { it.startsWith("работ") } -> 11
        "да" in words && words.any { it.startsWith("говор") } -> 12
        "да" in words && words.contains("вода") -> 13
        "вода" in words -> 14
        else -> 15
    }
}
