package com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators


import com.carbit3333333.oiiglot_bulgary.data.lesson_session.support.LessonRealSentenceGenerator
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

object Lesson5RealGenerator {

    private val subjectRu = mapOf(
        "Аз" to "Я",
        "Ти" to "Ты",
        "Той" to "Он",
        "Тя" to "Она",
        "То" to "Оно",
        "Ние" to "Мы",
        "Вие" to "Вы",
        "Те" to "Они"
    )

    private data class Verb(
        val bg: Map<String, String>,
        val ru: String,
        val objects: List<Pair<String, String>>
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
                "Те" to "гледат"
            ),
            ru = "смотреть",
            objects = listOf(
                "филм" to "фильм",
                "телевизия" to "телевизор"
            )
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
                "Те" to "пият"
            ),
            ru = "пить",
            objects = listOf(
                "кафе" to "кофе",
                "вода" to "воду"
            )
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
                "Те" to "работят"
            ),
            ru = "работать",
            objects = listOf(
                "тук" to "здесь",
                "в града" to "в городе"
            )
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
                "Те" to "учат"
            ),
            ru = "учиться",
            objects = listOf(
                "в училище" to "в школе",
                "вкъщи" to "дома"
            )
        )
    )

    private val modalForms = listOf(
        // могу
        mapOf(
            "Аз" to "мога",
            "Ти" to "можеш",
            "Той" to "може",
            "Тя" to "може",
            "То" to "може",
            "Ние" to "можем",
            "Вие" to "можете",
            "Те" to "могат"
        ) to mapOf(
            "Аз" to "могу",
            "Ти" to "можешь",
            "Той" to "может",
            "Тя" to "может",
            "То" to "может",
            "Ние" to "можем",
            "Вие" to "можете",
            "Те" to "могут"
        ),

        // хочу
        mapOf(
            "Аз" to "искам",
            "Ти" to "искаш",
            "Той" to "иска",
            "Тя" to "иска",
            "То" to "иска",
            "Ние" to "искаме",
            "Вие" to "искате",
            "Те" to "искат"
        ) to mapOf(
            "Аз" to "хочу",
            "Ти" to "хочешь",
            "Той" to "хочет",
            "Тя" to "хочет",
            "То" to "хочет",
            "Ние" to "хотим",
            "Вие" to "хотите",
            "Те" to "хотят"
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
        "Те" to "им нужно"
    )

    private val distractorPool = listOf(
        "Аз","Ти","Той","Тя","То","Ние","Вие","Те",
        "не","ли","да","трябва",
        "мога","можеш","може","можем","можете","могат",
        "искам","искаш","иска","искаме","искате","искат",
        "гледам","гледаш","гледа",
        "пия","пиеш","пие",
        "работя","работиш","работи",
        "уча","учиш","учи",
        "филм","телевизия","кафе","вода","тук","в града","в училище","вкъщи"
    )

    fun generateExercises(): List<LessonExercise> {
        return (1..80).map { generateExercise(it) }
    }

    private fun generateExercise(id: Int): LessonExercise {
        val subjects = subjectRu.keys.toList()
        val subject = subjects[(id - 1) % subjects.size]
        val verb = verbs[((id - 1) / subjects.size) % verbs.size]
        val obj = verb.objects[((id - 1) / (subjects.size * verbs.size).coerceAtLeast(1)) % verb.objects.size]

        val useMust = id % 5 == 0
        val useQuestion = id % 3 == 0
        val useNegative = id % 4 == 0

        val (modalBgMap, modalRuMap) = modalForms[((id - 1) / (subjects.size * verbs.size * verb.objects.size).coerceAtLeast(1)) % modalForms.size]

        val modalBg = if (useMust) "трябва" else modalBgMap.getValue(subject)
        val modalRu = if (useMust) mustRu.getValue(subject) else modalRuMap.getValue(subject)

        val verbBg = verb.bg.getValue(subject)

        val correctWords = mutableListOf<String>()

        correctWords.add(subject)

        if (useNegative) correctWords.add("не")

        correctWords.add(modalBg)

        if (useQuestion) correctWords.add("ли")

        correctWords.add("да")
        correctWords.add(verbBg)
        correctWords.add(obj.first)

        val sourceText = buildRussianSourceText(
            subject = subject,
            modalRu = modalRu,
            verbRu = verb.ru,
            objectRu = obj.second,
            useMust = useMust,
            useNegative = useNegative,
            useQuestion = useQuestion,
        )

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
                bgPattern = correctWords.map { LessonRealSentenceGenerator.Token.Fixed(it) }
            ),
            lexicon = LessonRealSentenceGenerator.Lexicon(
                subject = LessonRealSentenceGenerator.SubjectForms(subject, subjectRu.getValue(subject))
            ),
            distractorPool = distractorPool,
            hint = hint
        )
    }

    private fun buildRussianSourceText(
        subject: String,
        modalRu: String,
        verbRu: String,
        objectRu: String,
        useMust: Boolean,
        useNegative: Boolean,
        useQuestion: Boolean,
    ): String {
        val sentence = if (useMust) {
            val normalizedModalRu = if (useNegative) {
                modalRu.replace(" нужно", " не нужно")
            } else {
                modalRu
            }
            buildString {
                append(normalizedModalRu)
                append(" ")
                append(verbRu)
                append(" ")
                append(objectRu)
            }
        } else {
            buildString {
                append(subjectRu.getValue(subject))
                append(" ")
                if (useNegative) append("не ")
                append(modalRu)
                append(" ")
                append(verbRu)
                append(" ")
                append(objectRu)
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
}
