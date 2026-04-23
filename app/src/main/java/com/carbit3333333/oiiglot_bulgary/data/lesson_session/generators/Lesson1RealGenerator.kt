package com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson1ObjectAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson1SubjectAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson1TemplateAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson1VerbAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.support.LessonRealSentenceGenerator
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal object Lesson1RealGenerator {

    private data class SubjectEntry(
        val bg: String,
        val ru: String,
        val futureAuxRu: String,
        val haveRuPresent: String,
        val haveRuFuture: String,
    )

    private data class VerbEntry(
        val kind: String,
        val formsBg: Map<String, String>,
        val formsRu: Map<String, String>,
        val ruInfinitive: String,
        val objects: List<ObjectEntry>,
    )

    private data class ObjectEntry(
        val bg: String,
        val ru: String,
    )

    private data class TemplateEntry(
        val hint: String?,
        val sentenceTemplate: LessonRealSentenceGenerator.SentenceTemplate,
    )

    internal fun generateExercises(
        subjects: List<Lesson1SubjectAsset> = emptyList(),
        templates: List<Lesson1TemplateAsset> = emptyList(),
        verbs: List<Lesson1VerbAsset> = emptyList(),
    ): List<LessonExercise> {
        val subjectEntries = subjects.mapToSubjectEntries().ifEmpty { defaultSubjects() }
        val templateEntries = templates.mapToTemplateEntries().ifEmpty { defaultTemplates() }
        val verbEntries = verbs.mapToVerbEntries().ifEmpty { defaultVerbs() }

        return (1..100).map { id ->
            generateExercise(
                id = id,
                subjects = subjectEntries,
                templates = templateEntries,
                verbs = verbEntries,
            )
        }
    }

    private fun generateExercise(
        id: Int,
        subjects: List<SubjectEntry>,
        templates: List<TemplateEntry>,
        verbs: List<VerbEntry>,
    ): LessonExercise {
        val template = templates[(id - 1) % templates.size]
        val subject = subjects[((id - 1) / templates.size) % subjects.size]
        val verb = verbs[((id - 1) / (templates.size * subjects.size)) % verbs.size]
        val obj = verb.objects[((id - 1) / (templates.size * subjects.size * verbs.size)) % verb.objects.size]

        val lexicon = LessonRealSentenceGenerator.Lexicon(
            subject = LessonRealSentenceGenerator.SubjectForms(
                bg = subject.bg,
                ru = subject.ru,
            ),
            verb = LessonRealSentenceGenerator.VerbForms(
                bg = verb.formsBg.getValue(subject.bg),
                ru = verb.formsRu.getValue(subject.bg),
            ),
            objBg = obj.bg,
            objRu = obj.ru,
        )

        val sourceText = buildSourceText(
            subject = subject,
            verb = verb,
            obj = obj,
            template = template.sentenceTemplate,
        )

        return LessonRealSentenceGenerator.buildExercise(
            id = id,
            template = template.sentenceTemplate,
            lexicon = lexicon,
            sourceTextOverride = sourceText,
            distractorPool = buildDistractorPool(subjects, verbs),
            totalWords = 8,
            hint = template.hint,
        )
    }

    private fun buildSourceText(
        subject: SubjectEntry,
        verb: VerbEntry,
        obj: ObjectEntry,
        template: LessonRealSentenceGenerator.SentenceTemplate,
    ): String {
        if (verb.kind == "have") {
            return when (template.ruPattern) {
                "{subject} {verb} {object}" -> "${subject.haveRuPresent} ${obj.ru}"
                "{subject} не {verb} {object}" -> "${subject.haveRuPresent} нет ${obj.ru}"
                "{subject} {verb} {object}?" -> "${subject.haveRuPresent} ${obj.ru}?"
                "{subject} {futureAux} {infinitive} {object}" -> "${subject.haveRuFuture} ${obj.ru}"
                "{subject} не {futureAux} {infinitive} {object}" -> "${subject.haveRuFuture} не будет ${obj.ru}"
                "{subject} {futureAux} {infinitive} {object}?" -> "${subject.haveRuFuture} ${obj.ru}?"
                else -> "${subject.haveRuPresent} ${obj.ru}"
            }
        }

        return template.ruPattern
            .replace("{subject}", subject.ru)
            .replace("{verb}", verb.formsRu.getValue(subject.bg))
            .replace("{futureAux}", subject.futureAuxRu)
            .replace("{infinitive}", verb.ruInfinitive)
            .replace("{object}", obj.ru)
            .replace("  ", " ")
            .trim()
    }

    private fun buildDistractorPool(
        subjects: List<SubjectEntry>,
        verbs: List<VerbEntry>,
    ): List<String> {
        return buildList {
            addAll(subjects.map { it.bg })
            addAll(listOf("не", "ли", "ще", "няма", "да"))
            addAll(verbs.flatMap { it.formsBg.values })
            addAll(verbs.flatMap { it.objects.map { obj -> obj.bg } })
        }.distinct()
    }

    private fun List<Lesson1SubjectAsset>.mapToSubjectEntries(): List<SubjectEntry> {
        return map {
            SubjectEntry(
                bg = it.bg,
                ru = it.ru,
                futureAuxRu = it.futureAuxRu,
                haveRuPresent = it.haveRuPresent.ifBlank { defaultHavePresent(it.ru) },
                haveRuFuture = it.haveRuFuture.ifBlank { defaultHaveFuture(it.ru) },
            )
        }
    }

    private fun List<Lesson1TemplateAsset>.mapToTemplateEntries(): List<TemplateEntry> {
        return map {
            TemplateEntry(
                hint = it.hint,
                sentenceTemplate = LessonRealSentenceGenerator.SentenceTemplate(
                    ruPattern = it.ruPattern,
                    bgPattern = it.bgTokens.map(::tokenFromAsset),
                ),
            )
        }
    }

    private fun List<Lesson1VerbAsset>.mapToVerbEntries(): List<VerbEntry> {
        return map {
            VerbEntry(
                kind = it.kind,
                formsBg = it.formsBg,
                formsRu = it.formsRu,
                ruInfinitive = it.ruInfinitive,
                objects = it.objects.map(Lesson1ObjectAsset::toObjectEntry),
            )
        }
    }

    private fun Lesson1ObjectAsset.toObjectEntry(): ObjectEntry {
        return ObjectEntry(bg = bg, ru = ru)
    }

    private fun tokenFromAsset(raw: String): LessonRealSentenceGenerator.Token {
        return when (raw) {
            "{subject}" -> LessonRealSentenceGenerator.Token.SubjectBg
            "{verb}" -> LessonRealSentenceGenerator.Token.VerbBg
            "{object}" -> LessonRealSentenceGenerator.Token.ObjectBg
            else -> LessonRealSentenceGenerator.Token.Fixed(raw)
        }
    }

    private fun defaultHavePresent(subjectRu: String): String {
        return when (subjectRu) {
            "Я" -> "у меня есть"
            "Ты" -> "у тебя есть"
            "Он" -> "у него есть"
            "Она" -> "у неё есть"
            "Оно" -> "у него есть"
            "Мы" -> "у нас есть"
            "Вы" -> "у вас есть"
            "Они" -> "у них есть"
            else -> "есть"
        }
    }

    private fun defaultHaveFuture(subjectRu: String): String {
        return when (subjectRu) {
            "Я" -> "у меня будет"
            "Ты" -> "у тебя будет"
            "Он" -> "у него будет"
            "Она" -> "у неё будет"
            "Оно" -> "у него будет"
            "Мы" -> "у нас будет"
            "Вы" -> "у вас будет"
            "Они" -> "у них будет"
            else -> "будет"
        }
    }

    private fun defaultSubjects(): List<SubjectEntry> {
        return listOf(
            SubjectEntry("Аз", "Я", "буду", "у меня есть", "у меня будет"),
            SubjectEntry("Ти", "Ты", "будешь", "у тебя есть", "у тебя будет"),
            SubjectEntry("Той", "Он", "будет", "у него есть", "у него будет"),
            SubjectEntry("Тя", "Она", "будет", "у неё есть", "у неё будет"),
            SubjectEntry("То", "Оно", "будет", "у него есть", "у него будет"),
            SubjectEntry("Ние", "Мы", "будем", "у нас есть", "у нас будет"),
            SubjectEntry("Вие", "Вы", "будете", "у вас есть", "у вас будет"),
            SubjectEntry("Те", "Они", "будут", "у них есть", "у них будет"),
        )
    }

    private fun defaultTemplates(): List<TemplateEntry> {
        return listOf(
            TemplateEntry(
                hint = "💡 собирай полное предложение",
                sentenceTemplate = LessonRealSentenceGenerator.SentenceTemplate(
                    ruPattern = "{subject} {verb} {object}",
                    bgPattern = listOf(
                        LessonRealSentenceGenerator.Token.SubjectBg,
                        LessonRealSentenceGenerator.Token.VerbBg,
                        LessonRealSentenceGenerator.Token.ObjectBg,
                    ),
                ),
            ),
            TemplateEntry(
                hint = "💡 отрицание: не + глагол",
                sentenceTemplate = LessonRealSentenceGenerator.SentenceTemplate(
                    ruPattern = "{subject} не {verb} {object}",
                    bgPattern = listOf(
                        LessonRealSentenceGenerator.Token.SubjectBg,
                        LessonRealSentenceGenerator.Token.Fixed("не"),
                        LessonRealSentenceGenerator.Token.VerbBg,
                        LessonRealSentenceGenerator.Token.ObjectBg,
                    ),
                ),
            ),
            TemplateEntry(
                hint = "💡 вопрос: глагол + ли",
                sentenceTemplate = LessonRealSentenceGenerator.SentenceTemplate(
                    ruPattern = "{subject} {verb} {object}?",
                    bgPattern = listOf(
                        LessonRealSentenceGenerator.Token.SubjectBg,
                        LessonRealSentenceGenerator.Token.VerbBg,
                        LessonRealSentenceGenerator.Token.Fixed("ли"),
                        LessonRealSentenceGenerator.Token.ObjectBg,
                    ),
                ),
            ),
            TemplateEntry(
                hint = "💡 будущее время: ще + глагол",
                sentenceTemplate = LessonRealSentenceGenerator.SentenceTemplate(
                    ruPattern = "{subject} {futureAux} {infinitive} {object}",
                    bgPattern = listOf(
                        LessonRealSentenceGenerator.Token.SubjectBg,
                        LessonRealSentenceGenerator.Token.Fixed("ще"),
                        LessonRealSentenceGenerator.Token.VerbBg,
                        LessonRealSentenceGenerator.Token.ObjectBg,
                    ),
                ),
            ),
            TemplateEntry(
                hint = "💡 отрицание в будущем: няма да + глагол",
                sentenceTemplate = LessonRealSentenceGenerator.SentenceTemplate(
                    ruPattern = "{subject} не {futureAux} {infinitive} {object}",
                    bgPattern = listOf(
                        LessonRealSentenceGenerator.Token.SubjectBg,
                        LessonRealSentenceGenerator.Token.Fixed("няма"),
                        LessonRealSentenceGenerator.Token.Fixed("да"),
                        LessonRealSentenceGenerator.Token.VerbBg,
                        LessonRealSentenceGenerator.Token.ObjectBg,
                    ),
                ),
            ),
            TemplateEntry(
                hint = "💡 вопрос в будущем: ще + глагол + ли",
                sentenceTemplate = LessonRealSentenceGenerator.SentenceTemplate(
                    ruPattern = "{subject} {futureAux} {infinitive} {object}?",
                    bgPattern = listOf(
                        LessonRealSentenceGenerator.Token.SubjectBg,
                        LessonRealSentenceGenerator.Token.Fixed("ще"),
                        LessonRealSentenceGenerator.Token.VerbBg,
                        LessonRealSentenceGenerator.Token.Fixed("ли"),
                        LessonRealSentenceGenerator.Token.ObjectBg,
                    ),
                ),
            ),
        )
    }

    private fun defaultVerbs(): List<VerbEntry> {
        return listOf(
            VerbEntry(
                kind = "default",
                formsBg = mapOf(
                    "Аз" to "гледам",
                    "Ти" to "гледаш",
                    "Той" to "гледа",
                    "Тя" to "гледа",
                    "То" to "гледа",
                    "Ние" to "гледаме",
                    "Вие" to "гледате",
                    "Те" to "гледат",
                ),
                formsRu = mapOf(
                    "Аз" to "смотрю",
                    "Ти" to "смотришь",
                    "Той" to "смотрит",
                    "Тя" to "смотрит",
                    "То" to "смотрит",
                    "Ние" to "смотрим",
                    "Вие" to "смотрите",
                    "Те" to "смотрят",
                ),
                ruInfinitive = "смотреть",
                objects = listOf(
                    ObjectEntry("телевизия", "телевизор"),
                    ObjectEntry("филм", "фильм"),
                ),
            ),
            VerbEntry(
                kind = "default",
                formsBg = mapOf(
                    "Аз" to "работя",
                    "Ти" to "работиш",
                    "Той" to "работи",
                    "Тя" to "работи",
                    "То" to "работи",
                    "Ние" to "работим",
                    "Вие" to "работите",
                    "Те" to "работят",
                ),
                formsRu = mapOf(
                    "Аз" to "работаю",
                    "Ти" to "работаешь",
                    "Той" to "работает",
                    "Тя" to "работает",
                    "То" to "работает",
                    "Ние" to "работаем",
                    "Вие" to "работаете",
                    "Те" to "работают",
                ),
                ruInfinitive = "работать",
                objects = listOf(
                    ObjectEntry("тук", "здесь"),
                    ObjectEntry("в града", "в городе"),
                    ObjectEntry("на работа", "на работе"),
                ),
            ),
            VerbEntry(
                kind = "default",
                formsBg = mapOf(
                    "Аз" to "уча",
                    "Ти" to "учиш",
                    "Той" to "учи",
                    "Тя" to "учи",
                    "То" to "учи",
                    "Ние" to "учим",
                    "Вие" to "учите",
                    "Те" to "учат",
                ),
                formsRu = mapOf(
                    "Аз" to "учусь",
                    "Ти" to "учишься",
                    "Той" to "учится",
                    "Тя" to "учится",
                    "То" to "учится",
                    "Ние" to "учимся",
                    "Вие" to "учитесь",
                    "Те" to "учатся",
                ),
                ruInfinitive = "учиться",
                objects = listOf(
                    ObjectEntry("в училище", "в школе"),
                    ObjectEntry("вкъщи", "дома"),
                ),
            ),
            VerbEntry(
                kind = "default",
                formsBg = mapOf(
                    "Аз" to "говоря",
                    "Ти" to "говориш",
                    "Той" to "говори",
                    "Тя" to "говори",
                    "То" to "говори",
                    "Ние" to "говорим",
                    "Вие" to "говорите",
                    "Те" to "говорят",
                ),
                formsRu = mapOf(
                    "Аз" to "говорю",
                    "Ти" to "говоришь",
                    "Той" to "говорит",
                    "Тя" to "говорит",
                    "То" to "говорит",
                    "Ние" to "говорим",
                    "Вие" to "говорите",
                    "Те" to "говорят",
                ),
                ruInfinitive = "говорить",
                objects = listOf(
                    ObjectEntry("български", "по-болгарски"),
                    ObjectEntry("бавно", "медленно"),
                ),
            ),
            VerbEntry(
                kind = "default",
                formsBg = mapOf(
                    "Аз" to "пия",
                    "Ти" to "пиеш",
                    "Той" to "пие",
                    "Тя" to "пие",
                    "То" to "пие",
                    "Ние" to "пием",
                    "Вие" to "пиете",
                    "Те" to "пият",
                ),
                formsRu = mapOf(
                    "Аз" to "пью",
                    "Ти" to "пьёшь",
                    "Той" to "пьёт",
                    "Тя" to "пьёт",
                    "То" to "пьёт",
                    "Ние" to "пьём",
                    "Вие" to "пьёте",
                    "Те" to "пьют",
                ),
                ruInfinitive = "пить",
                objects = listOf(
                    ObjectEntry("вода", "воду"),
                    ObjectEntry("кафе", "кофе"),
                    ObjectEntry("чай", "чай"),
                ),
            ),
            VerbEntry(
                kind = "default",
                formsBg = mapOf(
                    "Аз" to "обичам",
                    "Ти" to "обичаш",
                    "Той" to "обича",
                    "Тя" to "обича",
                    "То" to "обича",
                    "Ние" to "обичаме",
                    "Вие" to "обичате",
                    "Те" to "обичат",
                ),
                formsRu = mapOf(
                    "Аз" to "люблю",
                    "Ти" to "любишь",
                    "Той" to "любит",
                    "Тя" to "любит",
                    "То" to "любит",
                    "Ние" to "любим",
                    "Вие" to "любите",
                    "Те" to "любят",
                ),
                ruInfinitive = "любить",
                objects = listOf(
                    ObjectEntry("кафе", "кофе"),
                    ObjectEntry("чай", "чай"),
                    ObjectEntry("филм", "фильм"),
                    ObjectEntry("книга", "книгу"),
                ),
            ),
            VerbEntry(
                kind = "have",
                formsBg = mapOf(
                    "Аз" to "имам",
                    "Ти" to "имаш",
                    "Той" to "има",
                    "Тя" to "има",
                    "То" to "има",
                    "Ние" to "имаме",
                    "Вие" to "имате",
                    "Те" to "имат",
                ),
                formsRu = mapOf(
                    "Аз" to "есть",
                    "Ти" to "есть",
                    "Той" to "есть",
                    "Тя" to "есть",
                    "То" to "есть",
                    "Ние" to "есть",
                    "Вие" to "есть",
                    "Те" to "есть",
                ),
                ruInfinitive = "иметь",
                objects = listOf(
                    ObjectEntry("книга", "книга"),
                    ObjectEntry("време", "время"),
                    ObjectEntry("работа", "работа"),
                    ObjectEntry("телефон", "телефон"),
                ),
            ),
        )
    }
}
