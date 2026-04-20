package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal fun generateLesson3Exercises(): List<LessonExercise> {
    return (1..100).map { id ->
        generateLesson3Exercise(id)
    }
}

internal fun generateLesson3Exercise(id: Int): LessonExercise {
    val type = if ((1..100).random() <= 70) {
        Lesson3SentenceType.PAST
    } else {
        Lesson3SentenceType.PAST_NEGATIVE
    }

    val verb = verbs.random()
    val subject = subjects.random()

    val bgVerb = verb.past.getValue(subject)
    val ruSubject = subjectRu.getValue(subject)
    val ruVerb = verb.ruPast.getValue(subject)

    val correctWords = when (type) {
        Lesson3SentenceType.PAST ->
            listOf(subject, bgVerb)

        Lesson3SentenceType.PAST_NEGATIVE ->
            listOf(subject, "не", bgVerb)
    }

    val sourceText = when (type) {
        Lesson3SentenceType.PAST ->
            "$ruSubject $ruVerb"

        Lesson3SentenceType.PAST_NEGATIVE ->
            "$ruSubject не $ruVerb"
    }

    val distractorPool = (
            subjects +
                    listOf("не") +
                    verbs.flatMap { it.past.values }
            ).distinct()

    return buildTranslationExercise(
        id = id,
        sourceText = sourceText,
        correctWords = correctWords,
        distractorPool = distractorPool,
        hint = buildHint(correctWords)
    )
}
