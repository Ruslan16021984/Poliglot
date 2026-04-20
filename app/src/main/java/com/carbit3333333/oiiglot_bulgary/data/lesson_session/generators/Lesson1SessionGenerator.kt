package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal fun generateLesson1Exercises(): List<LessonExercise> {
    return (1..100).map { id ->
        generateLesson1Exercise(id)
    }
}

internal fun generateLesson1Exercise(id: Int): LessonExercise {
    val sentenceType = randomLesson1SentenceType()
    val verb = verbs.random()

    val subject = when (sentenceType) {
        Lesson1SentenceType.PRESENT,
        Lesson1SentenceType.PRESENT_NEGATIVE,
        Lesson1SentenceType.FUTURE,
        Lesson1SentenceType.FUTURE_NEGATIVE -> subjects.random()

        Lesson1SentenceType.PRESENT_QUESTION,
        Lesson1SentenceType.FUTURE_QUESTION -> questionSubjects.random()
    }

    val bgVerb = verb.present.getValue(subject)
    val ruSubject = subjectRu.getValue(subject)
    val ruVerb = verb.ruPresent.getValue(subject)
    val ruFutureVerb = ruFuture.getValue(subject)

    val correctWords = when (sentenceType) {
        Lesson1SentenceType.PRESENT -> listOf(subject, bgVerb)
        Lesson1SentenceType.PRESENT_QUESTION -> listOf(subject, bgVerb, "ли")
        Lesson1SentenceType.PRESENT_NEGATIVE -> listOf(subject, "не", bgVerb)
        Lesson1SentenceType.FUTURE -> listOf(subject, "ще", bgVerb)
        Lesson1SentenceType.FUTURE_QUESTION -> listOf(subject, "ще", bgVerb, "ли")
        Lesson1SentenceType.FUTURE_NEGATIVE -> listOf(subject, "няма", "да", bgVerb)
    }

    val sourceText = when (sentenceType) {
        Lesson1SentenceType.PRESENT ->
            "$ruSubject $ruVerb"

        Lesson1SentenceType.PRESENT_QUESTION ->
            "$ruSubject $ruVerb?"

        Lesson1SentenceType.PRESENT_NEGATIVE ->
            "$ruSubject не $ruVerb"

        Lesson1SentenceType.FUTURE ->
            "$ruSubject $ruFutureVerb ${toRussianInfinitive(verb.infinitive)}"

        Lesson1SentenceType.FUTURE_QUESTION ->
            "$ruSubject $ruFutureVerb ${toRussianInfinitive(verb.infinitive)}?"

        Lesson1SentenceType.FUTURE_NEGATIVE ->
            "$ruSubject не $ruFutureVerb ${toRussianInfinitive(verb.infinitive)}"
    }

    val distractors = buildLesson1Distractors(correctWords)

    return buildTranslationExercise(
        id = id,
        sourceText = sourceText,
        correctWords = correctWords,
        distractorPool = distractors,
        hint = buildHint(correctWords)
    )
}

internal fun randomLesson1SentenceType(): Lesson1SentenceType {
    return when ((1..100).random()) {
        in 1..20 -> Lesson1SentenceType.PRESENT
        in 21..35 -> Lesson1SentenceType.PRESENT_QUESTION
        in 36..50 -> Lesson1SentenceType.PRESENT_NEGATIVE
        in 51..70 -> Lesson1SentenceType.FUTURE
        in 71..85 -> Lesson1SentenceType.FUTURE_QUESTION
        else -> Lesson1SentenceType.FUTURE_NEGATIVE
    }
}

internal fun buildLesson1Distractors(correctWords: List<String>): List<String> {
    val pool = (
            subjects +
                    listOf("ли", "не", "ще", "няма", "да") +
                    verbs.flatMap { it.present.values }
            ).distinct()

    val distractors = pool
        .filterNot { it in correctWords }
        .distinct()
        .shuffled()

    require(distractors.size >= 6) {
        "Not enough distractors! Got ${distractors.size}, need at least 6"
    }

    return distractors
}
