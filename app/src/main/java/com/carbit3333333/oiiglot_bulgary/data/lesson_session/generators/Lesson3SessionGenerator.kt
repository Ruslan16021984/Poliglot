package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson3VerbAsset
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal enum class Lesson3SentenceType {
    PAST,
    PAST_NEGATIVE,
}

internal fun generateLesson3Exercises(
    subjectRuMap: Map<String, String>,
    verbs: List<Lesson3VerbAsset>,
    subjectUkMap: Map<String, String> = emptyMap(),
    exerciseLocale: LessonExerciseLocale = LessonExerciseLocale.Russian,
): List<LessonExercise> {
    return (1..100).map { id ->
        generateLesson3Exercise(
            id = id,
            subjectRuMap = subjectRuMap,
            subjectUkMap = subjectUkMap,
            verbs = verbs,
            exerciseLocale = exerciseLocale,
        )
    }
}

internal fun generateLesson3Exercise(
    id: Int,
    subjectRuMap: Map<String, String>,
    verbs: List<Lesson3VerbAsset>,
    subjectUkMap: Map<String, String> = emptyMap(),
    exerciseLocale: LessonExerciseLocale = LessonExerciseLocale.Russian,
): LessonExercise {
    val type = if (id % 4 == 0) {
        Lesson3SentenceType.PAST_NEGATIVE
    } else {
        Lesson3SentenceType.PAST
    }

    val subjects = subjectRuMap.keys.toList()
    val verb = verbs[(id - 1) % verbs.size]
    val subject = subjects[((id - 1) / verbs.size) % subjects.size]

    val bgVerb = verb.past.getValue(subject)
    val sourceSubject = if (exerciseLocale == LessonExerciseLocale.Ukrainian) {
        subjectUkMap[subject] ?: defaultUkrainianSubject(subjectRuMap.getValue(subject))
    } else {
        subjectRuMap.getValue(subject)
    }
    val sourceVerb = if (exerciseLocale == LessonExerciseLocale.Ukrainian) {
        verb.ukPast[subject] ?: defaultUkrainianPast(verb.ruPast.getValue(subject))
    } else {
        verb.ruPast.getValue(subject)
    }

    val correctWords = when (type) {
        Lesson3SentenceType.PAST -> listOf(subject, bgVerb)
        Lesson3SentenceType.PAST_NEGATIVE -> listOf(subject, "не", bgVerb)
    }

    val sourceText = when (type) {
        Lesson3SentenceType.PAST -> "$sourceSubject $sourceVerb"
        Lesson3SentenceType.PAST_NEGATIVE -> "$sourceSubject не $sourceVerb"
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
        hint = buildLesson3Hint(type, subject, exerciseLocale),
    )
}

private fun defaultUkrainianSubject(subjectRu: String): String {
    return when (subjectRu) {
        "Я" -> "Я"
        "Ты" -> "Ти"
        "Он" -> "Він"
        "Она" -> "Вона"
        "Оно" -> "Воно"
        "Мы" -> "Ми"
        "Вы" -> "Ви"
        "Они" -> "Вони"
        else -> subjectRu
    }
}

private fun defaultUkrainianPast(ruPast: String): String {
    return when (ruPast) {
        "делал(а)" -> "робив(ла)"
        "делал" -> "робив"
        "делала" -> "робила"
        "делало" -> "робило"
        "делали" -> "робили"
        "смотрел(а)" -> "дивився(лася)"
        "смотрел" -> "дивився"
        "смотрела" -> "дивилася"
        "смотрело" -> "дивилося"
        "смотрели" -> "дивилися"
        "ходил(а)" -> "ходив(ла)"
        "ходил" -> "ходив"
        "ходила" -> "ходила"
        "ходило" -> "ходило"
        "ходили" -> "ходили"
        "ел(а)" -> "їв(ла)"
        "ел" -> "їв"
        "ела" -> "їла"
        "ело" -> "їло"
        "ели" -> "їли"
        "пил(а)" -> "пив(ла)"
        "пил" -> "пив"
        "пила" -> "пила"
        "пило" -> "пило"
        "пили" -> "пили"
        "работал(а)" -> "працював(ла)"
        "работал" -> "працював"
        "работала" -> "працювала"
        "работало" -> "працювало"
        "работали" -> "працювали"
        "учился(ась)" -> "вчився(лася)"
        "учился" -> "вчився"
        "училась" -> "вчилася"
        "училось" -> "вчилося"
        "учились" -> "вчилися"
        "говорил(а)" -> "говорив(ла)"
        "говорил" -> "говорив"
        "говорила" -> "говорила"
        "говорило" -> "говорило"
        "говорили" -> "говорили"
        "видел(а)" -> "бачив(ла)"
        "видел" -> "бачив"
        "видела" -> "бачила"
        "видело" -> "бачило"
        "видели" -> "бачили"
        "хотел(а)" -> "хотів(ла)"
        "хотел" -> "хотів"
        "хотела" -> "хотіла"
        "хотело" -> "хотіло"
        "хотели" -> "хотіли"
        "был(а)" -> "був(ла)"
        "был" -> "був"
        "была" -> "була"
        "было" -> "було"
        "были" -> "були"
        "имел(а)" -> "мав(ла)"
        "имел" -> "мав"
        "имела" -> "мала"
        "имело" -> "мало"
        "имели" -> "мали"
        else -> ruPast
    }
}

private fun buildLesson3Hint(
    sentenceType: Lesson3SentenceType,
    subject: String,
    exerciseLocale: LessonExerciseLocale,
): String {
    if (sentenceType == Lesson3SentenceType.PAST_NEGATIVE) {
        return if (exerciseLocale == LessonExerciseLocale.Ukrainian) {
            "💡 \"не\" ставиться перед формою минулого часу"
        } else {
            "💡 \"не\" ставится перед формой прошедшего времени"
        }
    }

    return when (subject) {
        "Аз" -> if (exerciseLocale == LessonExerciseLocale.Ukrainian) {
            "💡 Минулий час: форма для \"аз\" часто закінчується на -х"
        } else {
            "💡 Прошедшее время: форма для \"аз\" часто заканчивается на -х"
        }

        "Ние" -> if (exerciseLocale == LessonExerciseLocale.Ukrainian) {
            "💡 Минулий час: форма для \"ние\" часто закінчується на -хме"
        } else {
            "💡 Прошедшее время: форма для \"ние\" часто заканчивается на -хме"
        }

        "Вие" -> if (exerciseLocale == LessonExerciseLocale.Ukrainian) {
            "💡 Минулий час: форма для \"вие\" часто закінчується на -хте"
        } else {
            "💡 Прошедшее время: форма для \"вие\" часто заканчивается на -хте"
        }

        "Те" -> if (exerciseLocale == LessonExerciseLocale.Ukrainian) {
            "💡 Минулий час: форма для \"те\" часто закінчується на -ха"
        } else {
            "💡 Прошедшее время: форма для \"те\" часто заканчивается на -ха"
        }

        else -> if (exerciseLocale == LessonExerciseLocale.Ukrainian) {
            "💡 Форми для \"ти\" і \"той\" часто краще запам'ятовувати цілком"
        } else {
            "💡 Формы для \"ти\" и \"той\" часто лучше запоминать целиком"
        }
    }
}
