package com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.support.LessonRealSentenceGenerator
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

object Lesson1RealGenerator {

    private data class VerbLexeme(
        val formsBg: Map<String, String>,
        val formsRu: Map<String, String>,
        val objects: List<Pair<String, String>>
    )

    private val templates = listOf(
        LessonRealSentenceGenerator.SentenceTemplate(
            ruPattern = "{subject} {verb} {object}",
            bgPattern = listOf(
                LessonRealSentenceGenerator.Token.SubjectBg,
                LessonRealSentenceGenerator.Token.VerbBg,
                LessonRealSentenceGenerator.Token.ObjectBg
            )
        ),
        LessonRealSentenceGenerator.SentenceTemplate(
            ruPattern = "{subject} не {verb} {object}",
            bgPattern = listOf(
                LessonRealSentenceGenerator.Token.SubjectBg,
                LessonRealSentenceGenerator.Token.Fixed("не"),
                LessonRealSentenceGenerator.Token.VerbBg,
                LessonRealSentenceGenerator.Token.ObjectBg
            )
        ),
        LessonRealSentenceGenerator.SentenceTemplate(
            ruPattern = "{subject} {verb} {object}?",
            bgPattern = listOf(
                LessonRealSentenceGenerator.Token.SubjectBg,
                LessonRealSentenceGenerator.Token.VerbBg,
                LessonRealSentenceGenerator.Token.Fixed("ли"),
                LessonRealSentenceGenerator.Token.ObjectBg
            )
        ),
        LessonRealSentenceGenerator.SentenceTemplate(
            ruPattern = "{subject} будет {verb} {object}",
            bgPattern = listOf(
                LessonRealSentenceGenerator.Token.SubjectBg,
                LessonRealSentenceGenerator.Token.Fixed("ще"),
                LessonRealSentenceGenerator.Token.VerbBg,
                LessonRealSentenceGenerator.Token.ObjectBg
            )
        ),
        LessonRealSentenceGenerator.SentenceTemplate(
            ruPattern = "{subject} не будет {verb} {object}",
            bgPattern = listOf(
                LessonRealSentenceGenerator.Token.SubjectBg,
                LessonRealSentenceGenerator.Token.Fixed("няма"),
                LessonRealSentenceGenerator.Token.Fixed("да"),
                LessonRealSentenceGenerator.Token.VerbBg,
                LessonRealSentenceGenerator.Token.ObjectBg
            )
        ),
        LessonRealSentenceGenerator.SentenceTemplate(
            ruPattern = "{subject} будет {verb} {object}?",
            bgPattern = listOf(
                LessonRealSentenceGenerator.Token.SubjectBg,
                LessonRealSentenceGenerator.Token.Fixed("ще"),
                LessonRealSentenceGenerator.Token.VerbBg,
                LessonRealSentenceGenerator.Token.Fixed("ли"),
                LessonRealSentenceGenerator.Token.ObjectBg
            )
        )
    )

    private val subjectRu = mapOf(
        "Аз" to "Я",
        "Ти" to "Ты",
        "Той" to "Он",
        "Ние" to "Мы",
        "Вие" to "Вы",
        "Те" to "Они"
    )

    private val verbs = listOf(
        VerbLexeme(
            formsBg = mapOf(
                "Аз" to "гледам",
                "Ти" to "гледаш",
                "Той" to "гледа",
                "Ние" to "гледаме",
                "Вие" to "гледате",
                "Те" to "гледат"
            ),
            formsRu = mapOf(
                "Аз" to "смотрю",
                "Ти" to "смотришь",
                "Той" to "смотрит",
                "Ние" to "смотрим",
                "Вие" to "смотрите",
                "Те" to "смотрят"
            ),
            objects = listOf(
                "телевизия" to "телевизор",
                "филм" to "фильм"
            )
        ),
        VerbLexeme(
            formsBg = mapOf(
                "Аз" to "работя",
                "Ти" to "работиш",
                "Той" to "работи",
                "Ние" to "работим",
                "Вие" to "работите",
                "Те" to "работят"
            ),
            formsRu = mapOf(
                "Аз" to "работаю",
                "Ти" to "работаешь",
                "Той" to "работает",
                "Ние" to "работаем",
                "Вие" to "работаете",
                "Те" to "работают"
            ),
            objects = listOf(
                "тук" to "здесь",
                "в града" to "в городе",
                "на работа" to "на работе"
            )
        ),
        VerbLexeme(
            formsBg = mapOf(
                "Аз" to "уча",
                "Ти" to "учиш",
                "Той" to "учи",
                "Ние" to "учим",
                "Вие" to "учите",
                "Те" to "учат"
            ),
            formsRu = mapOf(
                "Аз" to "учусь",
                "Ти" to "учишься",
                "Той" to "учится",
                "Ние" to "учимся",
                "Вие" to "учитесь",
                "Те" to "учатся"
            ),
            objects = listOf(
                "в училище" to "в школе",
                "български" to "болгарский"
            )
        ),
        VerbLexeme(
            formsBg = mapOf(
                "Аз" to "говоря",
                "Ти" to "говориш",
                "Той" to "говори",
                "Ние" to "говорим",
                "Вие" to "говорите",
                "Те" to "говорят"
            ),
            formsRu = mapOf(
                "Аз" to "говорю",
                "Ти" to "говоришь",
                "Той" to "говорит",
                "Ние" to "говорим",
                "Вие" to "говорите",
                "Те" to "говорят"
            ),
            objects = listOf(
                "тук" to "здесь",
                "български" to "по-болгарски"
            )
        ),
        VerbLexeme(
            formsBg = mapOf(
                "Аз" to "пия",
                "Ти" to "пиеш",
                "Той" to "пие",
                "Ние" to "пием",
                "Вие" to "пиете",
                "Те" to "пият"
            ),
            formsRu = mapOf(
                "Аз" to "пью",
                "Ти" to "пьёшь",
                "Той" to "пьёт",
                "Ние" to "пьём",
                "Вие" to "пьёте",
                "Те" to "пьют"
            ),
            objects = listOf(
                "вода" to "воду",
                "кафе" to "кофе",
                "чай" to "чай"
            )
        )
    )

    private val distractorPool = listOf(
        "Аз", "Ти", "Той", "Ние", "Вие", "Те",
        "не", "ли", "ще", "няма", "да",
        "гледам", "гледаш", "гледа", "гледаме", "гледате", "гледат",
        "работя", "работиш", "работи", "работим", "работите", "работят",
        "уча", "учиш", "учи", "учим", "учите", "учат",
        "говоря", "говориш", "говори", "говорим", "говорите", "говорят",
        "пия", "пиеш", "пие", "пием", "пиете", "пият",
        "телевизия", "филм", "тук", "в града", "на работа", "в училище",
        "български", "вода", "кафе", "чай"
    )

    fun generateExercises(): List<LessonExercise> {
        return (1..100).map { id -> generateExercise(id) }
    }

    private fun generateExercise(id: Int): LessonExercise {
        val template = templates.random()
        val subject = subjectRu.keys.random()
        val verbLexeme = verbs.random()
        val objectPair = verbLexeme.objects.random()

        val lexicon = LessonRealSentenceGenerator.Lexicon(
            subject = LessonRealSentenceGenerator.SubjectForms(
                bg = subject,
                ru = subjectRu.getValue(subject)
            ),
            verb = LessonRealSentenceGenerator.VerbForms(
                bg = verbLexeme.formsBg.getValue(subject),
                ru = verbLexeme.formsRu.getValue(subject)
            ),
            objBg = objectPair.first,
            objRu = objectPair.second
        )

        val hint = when {
            template.bgPattern.any { it == LessonRealSentenceGenerator.Token.Fixed("няма") } ->
                "💡 отрицание в будущем: няма да + глагол"

            template.bgPattern.any { it == LessonRealSentenceGenerator.Token.Fixed("ще") } &&
                    template.bgPattern.any { it == LessonRealSentenceGenerator.Token.Fixed("ли") } ->
                "💡 вопрос в будущем: ще + глагол + ли"

            template.bgPattern.any { it == LessonRealSentenceGenerator.Token.Fixed("ще") } ->
                "💡 будущее время: ще + глагол"

            template.bgPattern.any { it == LessonRealSentenceGenerator.Token.Fixed("ли") } ->
                "💡 вопрос: глагол + ли"

            template.bgPattern.any { it == LessonRealSentenceGenerator.Token.Fixed("не") } ->
                "💡 отрицание: не + глагол"

            else -> "💡 собирай полное предложение"
        }

        return LessonRealSentenceGenerator.buildExercise(
            id = id,
            template = template,
            lexicon = lexicon,
            distractorPool = distractorPool,
            totalWords = 8,
            hint = hint
        )
    }
}
