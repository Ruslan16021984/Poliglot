package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal fun generateLesson5Exercises(): List<LessonExercise> {
    return (1..60).map { id ->
        generateLesson5Exercise(id)
    }
}

internal fun generateLesson5Exercise(id: Int): LessonExercise {
    val sentenceType = randomLesson5SentenceType()
    val modalType = randomLesson5ModalType()

    val subject = lesson5Subjects.random()
    val subjectRuText = lesson5SubjectRu.getValue(subject)

    val verb = verbs.random()
    val actionBg = verb.present.getValue(subject)
    val actionRu = toRussianInfinitive(verb.infinitive)

    val possibleObjects = lesson5ObjectsByInfinitive[verb.infinitive].orEmpty()
    val objectPair = if (possibleObjects.isNotEmpty() && (1..100).random() <= 55) {
        possibleObjects.random()
    } else {
        null
    }
    val objectBg = objectPair?.first
    val objectRu = objectPair?.second

    val modalBg = when (modalType) {
        Lesson5ModalType.CAN -> canForms.getValue(subject)
        Lesson5ModalType.WANT -> wantForms.getValue(subject)
        Lesson5ModalType.MUST -> mustForm
    }

    val modalRu = when (modalType) {
        Lesson5ModalType.CAN -> canRuForms.getValue(subject)
        Lesson5ModalType.WANT -> wantRuForms.getValue(subject)
        Lesson5ModalType.MUST -> mustRuForms.getValue(subject)
    }

    val correctWords = buildList {
        add(subject)

        if (sentenceType == Lesson5SentenceType.NEGATIVE) {
            add("не")
        }

        add(modalBg)

        if (sentenceType == Lesson5SentenceType.QUESTION) {
            add("ли")
        }

        add("да")
        add(actionBg)

        if (objectBg != null) {
            add(objectBg)
        }
    }

    val sourceText = buildString {
        append(subjectRuText)
        append(" ")

        if (sentenceType == Lesson5SentenceType.NEGATIVE) {
            append("не ")
        }

        append(modalRu)
        append(" ")
        append(actionRu)

        if (objectRu != null) {
            append(" ")
            append(objectRu)
        }

        if (sentenceType == Lesson5SentenceType.QUESTION) {
            append("?")
        }
    }

    val distractorPool = (
            lesson5Subjects +
                    listOf("не", "да", "ли") +
                    canForms.values +
                    wantForms.values +
                    listOf(mustForm) +
                    verbs.flatMap { it.present.values } +
                    lesson5ObjectsByInfinitive.values.flatten().map { it.first }
            ).distinct()

    val hint = when {
        "ли" in correctWords ->
            "💡 вопрос → \"ли\" ставится после глагола"

        modalType == Lesson5ModalType.CAN ->
            "💡 могу → форма на \"мога\" + да"

        modalType == Lesson5ModalType.WANT ->
            "💡 хочу → форма на \"искам\" + да"

        modalType == Lesson5ModalType.MUST ->
            "💡 нужно → трябва + да"

        "не" in correctWords ->
            "💡 \"не\" ставится перед глаголом"

        else -> null
    }

    return buildTranslationExercise(
        id = id,
        sourceText = sourceText,
        correctWords = correctWords,
        distractorPool = distractorPool,
        hint = hint
    )
}


internal fun randomLesson5SentenceType(): Lesson5SentenceType {
    return when ((1..100).random()) {
        in 1..40 -> Lesson5SentenceType.POSITIVE
        in 41..70 -> Lesson5SentenceType.NEGATIVE
        else -> Lesson5SentenceType.QUESTION
    }
}

internal fun randomLesson5ModalType(): Lesson5ModalType {
    return when ((1..100).random()) {
        in 1..40 -> Lesson5ModalType.CAN
        in 41..75 -> Lesson5ModalType.WANT
        else -> Lesson5ModalType.MUST
    }
}
