package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import com.carbit3333333.oiiglot_bulgary.model.Lesson4Item
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal fun generateLesson4Exercises(
    items: List<Lesson4Item>,
    exerciseLocale: LessonExerciseLocale = LessonExerciseLocale.Russian,
): List<LessonExercise> {
    return (1..100).map { id ->
        generateLesson4Exercise(
            id = id,
            items = items,
            exerciseLocale = exerciseLocale,
        )
    }
}

internal fun generateLesson4Exercise(
    id: Int,
    items: List<Lesson4Item>,
    exerciseLocale: LessonExerciseLocale = LessonExerciseLocale.Russian,
): LessonExercise {
    val item = selectLesson4Item(id = id, items = items)
    val correctWords = item.correctWords
    val distractorPool = (items.flatMap { it.correctWords } + listOf("Ти", "Те", "не")).distinct()

    return buildTranslationExercise(
        id = id,
        sourceText = when (exerciseLocale) {
            LessonExerciseLocale.Ukrainian -> item.uk ?: item.ru
            LessonExerciseLocale.Russian -> item.ru
        },
        correctWords = correctWords,
        distractorPool = distractorPool,
        hint = buildLesson4Hint(correctWords, exerciseLocale),
    )
}

private fun buildLesson4Hint(
    correctWords: List<String>,
    exerciseLocale: LessonExerciseLocale,
): String? {
    val mainVerb = correctWords.getOrNull(1).orEmpty()

    return when {
        "да" in correctWords && exerciseLocale == LessonExerciseLocale.Ukrainian ->
            "Після искам / обичам дія йде з \"да\"."

        "да" in correctWords ->
            "После искам / обичам действие идёт с \"да\"."

        mainVerb.startsWith("харес") && exerciseLocale == LessonExerciseLocale.Ukrainian ->
            "Після харесвам тут іде предмет, а не дія."

        mainVerb.startsWith("харес") ->
            "После харесвам здесь идёт предмет, а не действие."

        hasDefiniteObject(correctWords) && exerciseLocale == LessonExerciseLocale.Ukrainian ->
            "Якщо йдеться про конкретний предмет, використовуй форму з артиклем."

        hasDefiniteObject(correctWords) ->
            "Если речь о конкретном предмете, используй форму с артиклем."

        else -> null
    }
}

private fun selectLesson4Item(
    id: Int,
    items: List<Lesson4Item>,
): Lesson4Item {
    val combinedPhrases = orderCombinedLesson4Items(items.filter(::isCombinedLesson4Item))
    check(combinedPhrases.isNotEmpty()) { "Lesson 4 has no combined phrases to generate exercises." }
    return combinedPhrases[(id - 1) % combinedPhrases.size]
}

private fun isBasicNounItem(item: Lesson4Item): Boolean {
    return item.type == Lesson4Item.Type.NOUN &&
        item.correctWords.size == 1 &&
        item.correctWords.none(::isDefiniteObjectWord)
}

private fun isDefiniteNounItem(item: Lesson4Item): Boolean {
    return item.type == Lesson4Item.Type.NOUN &&
        item.correctWords.size == 1 &&
        item.correctWords.any(::isDefiniteObjectWord)
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
    return correctWords.any(::isDefiniteObjectWord)
}

private fun isDefiniteObjectWord(word: String): Boolean {
    return word.endsWith("та") || word.endsWith("то")
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
        "да" !in words && "книгата" in words && mainVerb.startsWith("харес") -> 2
        "да" in words && words.any { it.startsWith("чет") } -> 3
        "да" in words && words.any { it.startsWith("уч") } -> 4
        "да" !in words && "работата" in words && mainVerb.startsWith("иск") -> 5
        "да" !in words && "работата" in words && mainVerb.startsWith("обич") -> 6
        "да" !in words && "работата" in words && mainVerb.startsWith("харес") -> 7
        "да" in words && words.any { it.startsWith("яд") || it == "ям" } -> 8
        "да" !in words && "водата" in words && mainVerb.startsWith("иск") -> 9
        "да" !in words && "водата" in words && mainVerb.startsWith("харес") -> 10
        "да" !in words && "водата" in words -> 11
        "да" in words && words.contains("кафе") -> 12
        "да" !in words && "кафето" in words && mainVerb.startsWith("иск") -> 13
        "да" !in words && "кафето" in words && mainVerb.startsWith("обич") -> 14
        "да" !in words && "кафето" in words && mainVerb.startsWith("харес") -> 15
        "да" in words && words.any { it.startsWith("работ") } -> 16
        "да" in words && words.any { it.startsWith("говор") } -> 17
        else -> 18
    }
}
