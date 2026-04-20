package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal fun generateLesson2Exercises(): List<LessonExercise> {
    return (1..100).map { id ->
        generateLesson2Exercise(id)
    }
}

internal fun generateLesson2Exercise(id: Int): LessonExercise {
    val type = when ((1..100).random()) {
        in 1..25 -> Lesson2SentenceType.PRESENT
        in 26..45 -> Lesson2SentenceType.QUESTION
        in 46..60 -> Lesson2SentenceType.NEGATIVE
        in 61..78 -> Lesson2SentenceType.THIS_IS
        in 79..90 -> Lesson2SentenceType.THIS_IS_QUESTION
        else -> Lesson2SentenceType.THIS_IS_NEGATIVE
    }

    val subject = subjects.random()
    val verb = sumForms.getValue(subject)
    val ruSubject = subjectRu.getValue(subject)

    val complementBg = complementsBg.random()
    val complementRu = complementsRu.getValue(complementBg)

    val nounBg = objectNounsBg.random()
    val nounRu = objectNounsRu.getValue(nounBg)

    val correctWords = when (type) {
        Lesson2SentenceType.PRESENT ->
            listOf(subject, verb, complementBg)

        Lesson2SentenceType.QUESTION ->
            listOf(subject, complementBg, "ли", verb)

        Lesson2SentenceType.NEGATIVE ->
            listOf(subject, "не", verb, complementBg)

        Lesson2SentenceType.THIS_IS ->
            listOf("Това", "е", nounBg)

        Lesson2SentenceType.THIS_IS_QUESTION ->
            listOf("Това", nounBg, "ли", "е")

        Lesson2SentenceType.THIS_IS_NEGATIVE ->
            listOf("Това", "не", "е", nounBg)
    }

    val sourceText = when (type) {
        Lesson2SentenceType.PRESENT ->
            "$ruSubject $complementRu"

        Lesson2SentenceType.QUESTION ->
            "$ruSubject $complementRu?"

        Lesson2SentenceType.NEGATIVE ->
            "$ruSubject не $complementRu"

        Lesson2SentenceType.THIS_IS ->
            "Это $nounRu"

        Lesson2SentenceType.THIS_IS_QUESTION ->
            "Это $nounRu?"

        Lesson2SentenceType.THIS_IS_NEGATIVE ->
            "Это не $nounRu"
    }

    val distractors = buildLesson2Distractors(
        subject = subject,
        correctVerb = verb,
        correctComplement = complementBg,
        type = type,
        correctNoun = nounBg
    )

    return buildTranslationExercise(
        id = id,
        sourceText = sourceText,
        correctWords = correctWords,
        distractorPool = distractors,
        hint = when (type) {
            Lesson2SentenceType.QUESTION ->
                "💡 с \"съм\" вопрос часто строится так: слово + ли + съм"

            Lesson2SentenceType.THIS_IS ->
                "💡 это → Това е ..."

            Lesson2SentenceType.THIS_IS_QUESTION ->
                "💡 вопрос: Това + слово + ли + е"

            Lesson2SentenceType.THIS_IS_NEGATIVE ->
                "💡 это не → Това не е ..."

            else -> buildHint(correctWords)
        }
    )
}

internal fun buildLesson2Distractors(
    subject: String,
    correctVerb: String,
    correctComplement: String,
    type: Lesson2SentenceType,
    correctNoun: String
): List<String> {
    val correctWords = when (type) {
        Lesson2SentenceType.PRESENT ->
            listOf(subject, correctVerb, correctComplement)

        Lesson2SentenceType.QUESTION ->
            listOf(subject, correctComplement, "ли", correctVerb)

        Lesson2SentenceType.NEGATIVE ->
            listOf(subject, "не", correctVerb, correctComplement)

        Lesson2SentenceType.THIS_IS ->
            listOf("Това", "е", correctNoun)

        Lesson2SentenceType.THIS_IS_QUESTION ->
            listOf("Това", correctNoun, "ли", "е")

        Lesson2SentenceType.THIS_IS_NEGATIVE ->
            listOf("Това", "не", "е", correctNoun)
    }

    val subjectDistractors = subjects.filterNot { it == subject }

    val verbDistractors = sumForms.values
        .filterNot { it == correctVerb }
        .distinct()

    val complementDistractors = complementsBg.filterNot { it == correctComplement }
    val nounDistractors = objectNounsBg.filterNot { it == correctNoun }

    val grammarDistractors = listOf("не", "ли", "Това")

    return (
            subjectDistractors +
                    verbDistractors +
                    complementDistractors +
                    nounDistractors +
                    grammarDistractors
            )
        .distinct()
        .filterNot { it in correctWords }
        .shuffled()
}
