package com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonExerciseLocale
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.support.LessonRealSentenceGenerator
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal object Lesson5RealGenerator {

    private val subjectRu = mapOf(
        "Аз" to "Я",
        "Ти" to "Ты",
        "Той" to "Он",
        "Тя" to "Она",
        "То" to "Оно",
        "Ние" to "Мы",
        "Вие" to "Вы",
        "Те" to "Они",
    )

    private val subjectUk = mapOf(
        "Аз" to "Я",
        "Ти" to "Ти",
        "Той" to "Він",
        "Тя" to "Вона",
        "То" to "Воно",
        "Ние" to "Ми",
        "Вие" to "Ви",
        "Те" to "Вони",
    )

    private data class Verb(
        val bg: Map<String, String>,
        val ru: String,
        val uk: String,
        val objects: List<Triple<String, String, String>>,
    )

    private val verbs = listOf(
        Verb(
            bg = mapOf(
                "Аз" to "гледам",
                "Ти" to "гледаш",
                "Той" to "гледа",
                "Тя" to "гледа",
                "То" to "гледа",
                "Ние" to "гледаме",
                "Вие" to "гледате",
                "Те" to "гледат",
            ),
            ru = "смотреть",
            uk = "дивитися",
            objects = listOf(
                Triple("филм", "фильм", "фільм"),
                Triple("телевизия", "телевизор", "телевізор"),
                Triple("сериал", "сериал", "серіал"),
            ),
        ),
        Verb(
            bg = mapOf(
                "Аз" to "пия",
                "Ти" to "пиеш",
                "Той" to "пие",
                "Тя" to "пие",
                "То" to "пие",
                "Ние" to "пием",
                "Вие" to "пиете",
                "Те" to "пият",
            ),
            ru = "пить",
            uk = "пити",
            objects = listOf(
                Triple("кафе", "кофе", "каву"),
                Triple("вода", "воду", "воду"),
                Triple("чай", "чай", "чай"),
            ),
        ),
        Verb(
            bg = mapOf(
                "Аз" to "работя",
                "Ти" to "работиш",
                "Той" to "работи",
                "Тя" to "работи",
                "То" to "работи",
                "Ние" to "работим",
                "Вие" to "работите",
                "Те" to "работят",
            ),
            ru = "работать",
            uk = "працювати",
            objects = listOf(
                Triple("тук", "здесь", "тут"),
                Triple("в града", "в городе", "в місті"),
                Triple("в офиса", "в офисе", "в офісі"),
                Triple("вкъщи", "дома", "вдома"),
            ),
        ),
        Verb(
            bg = mapOf(
                "Аз" to "уча",
                "Ти" to "учиш",
                "Той" to "учи",
                "Тя" to "учи",
                "То" to "учи",
                "Ние" to "учим",
                "Вие" to "учите",
                "Те" to "учат",
            ),
            ru = "учиться",
            uk = "вчитися",
            objects = listOf(
                Triple("в училище", "в школе", "в школі"),
                Triple("вкъщи", "дома", "вдома"),
                Triple("в университета", "в университете", "в університеті"),
            ),
        ),
        Verb(
            bg = mapOf(
                "Аз" to "чета",
                "Ти" to "четеш",
                "Той" to "чете",
                "Тя" to "чете",
                "То" to "чете",
                "Ние" to "четем",
                "Вие" to "четете",
                "Те" to "четат",
            ),
            ru = "читать",
            uk = "читати",
            objects = listOf(
                Triple("книга", "книгу", "книгу"),
                Triple("писмо", "письмо", "лист"),
            ),
        ),
        Verb(
            bg = mapOf(
                "Аз" to "отивам",
                "Ти" to "отиваш",
                "Той" to "отива",
                "Тя" to "отива",
                "То" to "отива",
                "Ние" to "отиваме",
                "Вие" to "отивате",
                "Те" to "отиват",
            ),
            ru = "идти",
            uk = "йти",
            objects = listOf(
                Triple("в магазина", "в магазин", "в магазин"),
                Triple("вкъщи", "домой", "додому"),
            ),
        ),
    )

    private data class ModalForms(
        val bg: Map<String, String>,
        val ru: Map<String, String>,
        val uk: Map<String, String>,
    )

    private val modalForms = listOf(
        ModalForms(
            bg = mapOf(
                "Аз" to "мога",
                "Ти" to "можеш",
                "Той" to "може",
                "Тя" to "може",
                "То" to "може",
                "Ние" to "можем",
                "Вие" to "можете",
                "Те" to "могат",
            ),
            ru = mapOf(
                "Аз" to "могу",
                "Ти" to "можешь",
                "Той" to "может",
                "Тя" to "может",
                "То" to "может",
                "Ние" to "можем",
                "Вие" to "можете",
                "Те" to "могут",
            ),
            uk = mapOf(
                "Аз" to "можу",
                "Ти" to "можеш",
                "Той" to "може",
                "Тя" to "може",
                "То" to "може",
                "Ние" to "можемо",
                "Вие" to "можете",
                "Те" to "можуть",
            ),
        ),
        ModalForms(
            bg = mapOf(
                "Аз" to "искам",
                "Ти" to "искаш",
                "Той" to "иска",
                "Тя" to "иска",
                "То" to "иска",
                "Ние" to "искаме",
                "Вие" to "искате",
                "Те" to "искат",
            ),
            ru = mapOf(
                "Аз" to "хочу",
                "Ти" to "хочешь",
                "Той" to "хочет",
                "Тя" to "хочет",
                "То" to "хочет",
                "Ние" to "хотим",
                "Вие" to "хотите",
                "Те" to "хотят",
            ),
            uk = mapOf(
                "Аз" to "хочу",
                "Ти" to "хочеш",
                "Той" to "хоче",
                "Тя" to "хоче",
                "То" to "хоче",
                "Ние" to "хочемо",
                "Вие" to "хочете",
                "Те" to "хочуть",
            ),
        ),
    )

    private val mustRu = mapOf(
        "Аз" to "мне нужно",
        "Ти" to "тебе нужно",
        "Той" to "ему нужно",
        "Тя" to "ей нужно",
        "То" to "ему нужно",
        "Ние" to "нам нужно",
        "Вие" to "вам нужно",
        "Те" to "им нужно",
    )

    private val mustUk = mapOf(
        "Аз" to "мені потрібно",
        "Ти" to "тобі потрібно",
        "Той" to "йому потрібно",
        "Тя" to "їй потрібно",
        "То" to "йому потрібно",
        "Ние" to "нам потрібно",
        "Вие" to "вам потрібно",
        "Те" to "їм потрібно",
    )

    fun generateExercises(
        exerciseLocale: LessonExerciseLocale = LessonExerciseLocale.Russian,
    ): List<LessonExercise> {
        return (1..100).map { generateExercise(it, exerciseLocale) }
    }

    private fun generateExercise(
        id: Int,
        exerciseLocale: LessonExerciseLocale,
    ): LessonExercise {
        val subjects = subjectRu.keys.toList()
        val subject = subjects[(id - 1) % subjects.size]
        val verb = verbs[((id - 1) / subjects.size) % verbs.size]
        val obj = verb.objects[((id - 1) / (subjects.size * verbs.size).coerceAtLeast(1)) % verb.objects.size]

        val useMust = id % 5 == 0
        val useQuestion = id % 3 == 0
        val useNegative = id % 4 == 0

        val modalSet = modalForms[((id - 1) / (subjects.size * verbs.size * verb.objects.size).coerceAtLeast(1)) % modalForms.size]
        val modalBg = if (useMust) "трябва" else modalSet.bg.getValue(subject)

        val sourceText = buildSourceText(
            subject = subject,
            modalRu = if (useMust) mustRu.getValue(subject) else modalSet.ru.getValue(subject),
            modalUk = if (useMust) mustUk.getValue(subject) else modalSet.uk.getValue(subject),
            verbRu = verb.ru,
            verbUk = verb.uk,
            objectRu = obj.second,
            objectUk = obj.third,
            useMust = useMust,
            useNegative = useNegative,
            useQuestion = useQuestion,
            exerciseLocale = exerciseLocale,
        )

        val correctWords = mutableListOf(subject)
        if (useNegative) correctWords.add("не")
        correctWords.add(modalBg)
        if (useQuestion) correctWords.add("ли")
        correctWords.add("да")
        correctWords.add(verb.bg.getValue(subject))
        correctWords.add(obj.first)

        val hint = when {
            useMust -> "💡 трябва + да + глагол"
            modalBg.contains("мог") -> "💡 мога + да"
            modalBg.contains("иска") -> "💡 искам + да"
            useQuestion -> "💡 \"ли\" после спрягаемой формы"
            useNegative -> "💡 \"не\" перед спрягаемой формой"
            else -> null
        }

        return LessonRealSentenceGenerator.buildExercise(
            id = id,
            template = LessonRealSentenceGenerator.SentenceTemplate(
                ruPattern = sourceText,
                bgPattern = correctWords.map { LessonRealSentenceGenerator.Token.Fixed(it) },
            ),
            lexicon = LessonRealSentenceGenerator.Lexicon(
                subject = LessonRealSentenceGenerator.SubjectForms(
                    bg = subject,
                    ru = if (exerciseLocale == LessonExerciseLocale.Ukrainian) {
                        subjectUk.getValue(subject)
                    } else {
                        subjectRu.getValue(subject)
                    },
                ),
            ),
            distractorPool = buildDistractorPool(),
            hint = hint,
        )
    }

    private fun buildSourceText(
        subject: String,
        modalRu: String,
        modalUk: String,
        verbRu: String,
        verbUk: String,
        objectRu: String,
        objectUk: String,
        useMust: Boolean,
        useNegative: Boolean,
        useQuestion: Boolean,
        exerciseLocale: LessonExerciseLocale,
    ): String {
        val subjectText = if (exerciseLocale == LessonExerciseLocale.Ukrainian) {
            subjectUk.getValue(subject)
        } else {
            subjectRu.getValue(subject)
        }

        val modalText = if (exerciseLocale == LessonExerciseLocale.Ukrainian) modalUk else modalRu
        val verbText = if (exerciseLocale == LessonExerciseLocale.Ukrainian) verbUk else verbRu
        val objectText = if (exerciseLocale == LessonExerciseLocale.Ukrainian) objectUk else objectRu

        val sentence = if (useMust) {
            val mustText = if (useNegative) {
                modalText.replace(" потрібно", " не потрібно").replace(" нужно", " не нужно")
            } else {
                modalText
            }
            "$mustText $verbText $objectText"
        } else {
            buildString {
                append(subjectText)
                append(" ")
                if (useNegative) append("не ")
                append(modalText)
                append(" ")
                append(verbText)
                append(" ")
                append(objectText)
            }
        }

        val normalized = sentence
            .replace(Regex("\\s+"), " ")
            .trim()
            .replaceFirstChar { char ->
                if (char.isLowerCase()) char.titlecase() else char.toString()
            }

        return if (useQuestion) "$normalized?" else normalized
    }

    private fun buildDistractorPool(): List<String> {
        return buildList {
            addAll(subjectRu.keys)
            addAll(listOf("не", "ли", "да", "трябва"))
            addAll(modalForms.flatMap { it.bg.values })
            addAll(verbs.flatMap { it.bg.values })
            addAll(verbs.flatMap { verb -> verb.objects.map { it.first } })
        }.distinct()
    }
}
