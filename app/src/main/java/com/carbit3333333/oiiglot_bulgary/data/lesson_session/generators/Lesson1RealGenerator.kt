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
        val subject = subjects[((id - 1) * 3) % subjects.size]
        val verb = verbs[((id - 1) * 5) % verbs.size]
        val obj = verb.objects[((id - 1) / templates.size) % verb.objects.size]

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

        return LessonRealSentenceGenerator.buildExercise(
            id = id,
            template = template.sentenceTemplate,
            lexicon = lexicon,
            sourceTextOverride = buildSourceText(
                subject = subject,
                verb = verb,
                obj = obj,
                template = template.sentenceTemplate,
            ),
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
            val isFuture = template.ruPattern.contains("{futureAuxRu}") || template.ruPattern.contains("{futureAux}")
            val isNegative = template.ruPattern.contains(" не ")
            val isQuestion = template.ruPattern.trimEnd().endsWith("?")
            val base = if (isFuture) subject.haveRuFuture else subject.haveRuPresent

            val sentence = when {
                isFuture && isNegative -> "$base не будет ${obj.ru}"
                isNegative -> "$base нет ${obj.ru}"
                else -> "$base ${obj.ru}"
            }

            return if (isQuestion) "$sentence?" else sentence
        }

        return template.ruPattern
            .replace("{subject}", subject.ru)
            .replace("{verb}", verb.formsRu.getValue(subject.bg))
            .replace("{futureAuxRu}", subject.futureAuxRu)
            .replace("{futureAux}", subject.futureAuxRu)
            .replace("{ruInfinitive}", verb.ruInfinitive)
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
                objects = it.objects.map { item -> item.toObjectEntry() },
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
            template(
                hint = "Собери полное предложение",
                ruPattern = "{subject} {verb} {object}",
                bgTokens = listOf("{subject}", "{verb}", "{object}"),
            ),
            template(
                hint = "Отрицание: не + глагол",
                ruPattern = "{subject} не {verb} {object}",
                bgTokens = listOf("{subject}", "не", "{verb}", "{object}"),
            ),
            template(
                hint = "Вопрос: глагол + ли",
                ruPattern = "{subject} {verb} {object}?",
                bgTokens = listOf("{subject}", "{verb}", "ли", "{object}"),
            ),
            template(
                hint = "Будущее время: ще + глагол",
                ruPattern = "{subject} {futureAuxRu} {ruInfinitive} {object}",
                bgTokens = listOf("{subject}", "ще", "{verb}", "{object}"),
            ),
            template(
                hint = "Отрицание в будущем: няма да + глагол",
                ruPattern = "{subject} {futureAuxRu} не {ruInfinitive} {object}",
                bgTokens = listOf("{subject}", "няма", "да", "{verb}", "{object}"),
            ),
            template(
                hint = "Вопрос в будущем: ще + глагол + ли",
                ruPattern = "{subject} {futureAuxRu} {ruInfinitive} {object}?",
                bgTokens = listOf("{subject}", "ще", "{verb}", "ли", "{object}"),
            ),
        )
    }

    private fun defaultVerbs(): List<VerbEntry> {
        return listOf(
            verbEntry(
                kind = "default",
                ruInfinitive = "смотреть",
                formsBg = listOf("гледам", "гледаш", "гледа", "гледа", "гледа", "гледаме", "гледате", "гледат"),
                formsRu = listOf("смотрю", "смотришь", "смотрит", "смотрит", "смотрит", "смотрим", "смотрите", "смотрят"),
                objects = listOf(
                    ObjectEntry("телевизия", "телевизор"),
                    ObjectEntry("филм", "фильм"),
                    ObjectEntry("сериал", "сериал"),
                ),
            ),
            verbEntry(
                kind = "default",
                ruInfinitive = "работать",
                formsBg = listOf("работя", "работиш", "работи", "работи", "работи", "работим", "работите", "работят"),
                formsRu = listOf("работаю", "работаешь", "работает", "работает", "работает", "работаем", "работаете", "работают"),
                objects = listOf(
                    ObjectEntry("тук", "здесь"),
                    ObjectEntry("в града", "в городе"),
                    ObjectEntry("в офиса", "в офисе"),
                ),
            ),
            verbEntry(
                kind = "default",
                ruInfinitive = "учиться",
                formsBg = listOf("уча", "учиш", "учи", "учи", "учи", "учим", "учите", "учат"),
                formsRu = listOf("учусь", "учишься", "учится", "учится", "учится", "учимся", "учитесь", "учатся"),
                objects = listOf(
                    ObjectEntry("в училище", "в школе"),
                    ObjectEntry("вкъщи", "дома"),
                    ObjectEntry("в университета", "в университете"),
                ),
            ),
            verbEntry(
                kind = "default",
                ruInfinitive = "говорить",
                formsBg = listOf("говоря", "говориш", "говори", "говори", "говори", "говорим", "говорите", "говорят"),
                formsRu = listOf("говорю", "говоришь", "говорит", "говорит", "говорит", "говорим", "говорите", "говорят"),
                objects = listOf(
                    ObjectEntry("български", "по-болгарски"),
                    ObjectEntry("бавно", "медленно"),
                    ObjectEntry("с приятел", "с другом"),
                ),
            ),
            verbEntry(
                kind = "default",
                ruInfinitive = "пить",
                formsBg = listOf("пия", "пиеш", "пие", "пие", "пие", "пием", "пиете", "пият"),
                formsRu = listOf("пью", "пьёшь", "пьёт", "пьёт", "пьёт", "пьём", "пьёте", "пьют"),
                objects = listOf(
                    ObjectEntry("вода", "воду"),
                    ObjectEntry("кафе", "кофе"),
                    ObjectEntry("чай", "чай"),
                ),
            ),
            verbEntry(
                kind = "default",
                ruInfinitive = "любить",
                formsBg = listOf("обичам", "обичаш", "обича", "обича", "обича", "обичаме", "обичате", "обичат"),
                formsRu = listOf("люблю", "любишь", "любит", "любит", "любит", "любим", "любите", "любят"),
                objects = listOf(
                    ObjectEntry("кафе", "кофе"),
                    ObjectEntry("чай", "чай"),
                    ObjectEntry("филм", "фильм"),
                    ObjectEntry("книга", "книгу"),
                ),
            ),
            verbEntry(
                kind = "have",
                ruInfinitive = "иметь",
                formsBg = listOf("имам", "имаш", "има", "има", "има", "имаме", "имате", "имат"),
                formsRu = listOf("имею", "имеешь", "имеет", "имеет", "имеет", "имеем", "имеете", "имеют"),
                objects = listOf(
                    ObjectEntry("книга", "книга"),
                    ObjectEntry("време", "время"),
                    ObjectEntry("работа", "работа"),
                    ObjectEntry("телефон", "телефон"),
                ),
            ),
        )
    }

    private fun template(
        hint: String,
        ruPattern: String,
        bgTokens: List<String>,
    ): TemplateEntry {
        return TemplateEntry(
            hint = hint,
            sentenceTemplate = LessonRealSentenceGenerator.SentenceTemplate(
                ruPattern = ruPattern,
                bgPattern = bgTokens.map(::tokenFromAsset),
            ),
        )
    }

    private fun verbEntry(
        kind: String,
        ruInfinitive: String,
        formsBg: List<String>,
        formsRu: List<String>,
        objects: List<ObjectEntry>,
    ): VerbEntry {
        val subjects = listOf("Аз", "Ти", "Той", "Тя", "То", "Ние", "Вие", "Те")
        return VerbEntry(
            kind = kind,
            formsBg = subjects.zip(formsBg).toMap(),
            formsRu = subjects.zip(formsRu).toMap(),
            ruInfinitive = ruInfinitive,
            objects = objects,
        )
    }
}
