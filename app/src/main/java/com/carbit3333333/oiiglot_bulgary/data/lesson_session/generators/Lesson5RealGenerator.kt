package com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators


import com.carbit3333333.oiiglot_bulgary.data.lesson_session.support.LessonRealSentenceGenerator
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

object Lesson5RealGenerator {

    private val subjectRu = mapOf(
        "Аз" to "Я",
        "Ти" to "Ты",
        "Той" to "Он",
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
            "Ние" to "можем",
            "Вие" to "можете",
            "Те" to "могат"
        ) to mapOf(
            "Аз" to "могу",
            "Ти" to "можешь",
            "Той" to "может",
            "Ние" to "можем",
            "Вие" to "можете",
            "Те" to "могут"
        ),

        // хочу
        mapOf(
            "Аз" to "искам",
            "Ти" to "искаш",
            "Той" to "иска",
            "Ние" to "искаме",
            "Вие" to "искате",
            "Те" to "искат"
        ) to mapOf(
            "Аз" to "хочу",
            "Ти" to "хочешь",
            "Той" to "хочет",
            "Ние" to "хотим",
            "Вие" to "хотите",
            "Те" to "хотят"
        ),

    )

    private val mustRu = mapOf(
        "Аз" to "мне нужно",
        "Ти" to "тебе нужно",
        "Той" to "ему нужно",
        "Ние" to "нам нужно",
        "Вие" to "вам нужно",
        "Те" to "им нужно"
    )

    private val distractorPool = listOf(
        "Аз","Ти","Той","Ние","Вие","Те",
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

        val subject = subjectRu.keys.random()
        val verb = verbs.random()
        val obj = verb.objects.random()

        val useMust = (1..100).random() <= 20
        val useQuestion = (1..100).random() <= 30
        val useNegative = (1..100).random() <= 30

        val (modalBgMap, modalRuMap) = modalForms.random()

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

        val sourceText = buildString {
            append(subjectRu.getValue(subject))
            append(" ")

            if (useNegative) append("не ")

            append(modalRu)
            append(" ")
            append(verb.ru)
            append(" ")
            append(obj.second)

            if (useQuestion) append("?")
        }

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
}
