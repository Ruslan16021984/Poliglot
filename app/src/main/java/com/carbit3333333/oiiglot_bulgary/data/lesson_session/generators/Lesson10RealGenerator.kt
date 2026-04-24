package com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson10IntervalAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson10PhraseAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson10TemplateAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonExerciseLocale
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.buildTranslationExercise
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal object Lesson10RealGenerator {

    fun generateExercises(
        timePhrases: List<Lesson10PhraseAsset>,
        routineActions: List<Lesson10PhraseAsset>,
        intervals: List<Lesson10IntervalAsset>,
        intervalActions: List<Lesson10PhraseAsset>,
        questionActions: List<Lesson10PhraseAsset>,
        templates: List<Lesson10TemplateAsset>,
        intervalTemplates: List<Lesson10TemplateAsset>,
        questionTemplates: List<Lesson10TemplateAsset>,
        exerciseLocale: LessonExerciseLocale = LessonExerciseLocale.Russian,
    ): List<LessonExercise> {
        val distractorPool = buildDistractorPool(
            timePhrases = timePhrases,
            routineActions = routineActions,
            intervals = intervals,
            intervalActions = intervalActions,
            questionActions = questionActions,
            questionTemplates = questionTemplates
        )

        val exercises = mutableListOf<LessonExercise>()
        var routineIndex = 0
        var intervalIndex = 0
        var questionIndex = 0

        for (id in 1..100) {
            val exercise = when {
                id % 4 == 0 -> generateQuestionExercise(
                    id = id,
                    sequenceIndex = questionIndex++,
                    actions = questionActions,
                    templates = questionTemplates,
                    distractorPool = distractorPool,
                    exerciseLocale = exerciseLocale,
                )

                id % 3 == 0 -> generateIntervalExercise(
                    id = id,
                    sequenceIndex = intervalIndex++,
                    intervals = intervals,
                    actions = intervalActions,
                    templates = intervalTemplates,
                    distractorPool = distractorPool,
                    exerciseLocale = exerciseLocale,
                )

                else -> generateRoutineExercise(
                    id = id,
                    sequenceIndex = routineIndex++,
                    timePhrases = timePhrases,
                    actions = routineActions,
                    templates = templates,
                    distractorPool = distractorPool,
                    exerciseLocale = exerciseLocale,
                )
            }

            exercises += exercise
        }

        return exercises
    }

    private fun generateRoutineExercise(
        id: Int,
        sequenceIndex: Int,
        timePhrases: List<Lesson10PhraseAsset>,
        actions: List<Lesson10PhraseAsset>,
        templates: List<Lesson10TemplateAsset>,
        distractorPool: List<String>,
        exerciseLocale: LessonExerciseLocale,
    ): LessonExercise {
        val timePhrase = timePhrases[sequenceIndex % timePhrases.size]
        val action = actions[(sequenceIndex * cycleStep(actions.size)) % actions.size]
        val template = templates[sequenceIndex % templates.size]

        val sourceTemplateTokens = when (exerciseLocale) {
            LessonExerciseLocale.Ukrainian -> fallbackUkrainianTemplateTokens(template.ukTokens)
            LessonExerciseLocale.Russian -> template.ruTokens
        }
        val sourceTimeTokens = when (exerciseLocale) {
            LessonExerciseLocale.Ukrainian -> fallbackUkrainianTimePhrase(timePhrase.ukTokens)
            LessonExerciseLocale.Russian -> timePhrase.ruTokens
        }
        val sourceActionTokens = when (exerciseLocale) {
            LessonExerciseLocale.Ukrainian -> fallbackUkrainianActionPhrase(action.ukTokens)
            LessonExerciseLocale.Russian -> action.ruTokens
        }

        val sourceText = capitalizeSentence(
            renderTokens(
            sourceTemplateTokens,
            mapOf(
                "{time}" to sourceTimeTokens,
                "{action}" to sourceActionTokens
            )
            )
        )

        val correctWords = capitalizeFirstToken(
            renderBgTokens(
            template.bgTokens,
            mapOf(
                "{time}" to timePhrase.bgTokens,
                "{action}" to action.bgTokens
            )
            )
        )

        return buildTranslationExercise(
            id = id,
            sourceText = sourceText,
            correctWords = correctWords,
            distractorPool = distractorPool,
            hint = template.hint
        )
    }

    private fun generateIntervalExercise(
        id: Int,
        sequenceIndex: Int,
        intervals: List<Lesson10IntervalAsset>,
        actions: List<Lesson10PhraseAsset>,
        templates: List<Lesson10TemplateAsset>,
        distractorPool: List<String>,
        exerciseLocale: LessonExerciseLocale,
    ): LessonExercise {
        val interval = intervals[sequenceIndex % intervals.size]
        val action = actions[(sequenceIndex / intervals.size) % actions.size]
        val template = templates[sequenceIndex % templates.size]

        val sourceTemplateTokens = when (exerciseLocale) {
            LessonExerciseLocale.Ukrainian -> fallbackUkrainianTemplateTokens(template.ukTokens)
            LessonExerciseLocale.Russian -> template.ruTokens
        }
        val sourceActionTokens = when (exerciseLocale) {
            LessonExerciseLocale.Ukrainian -> fallbackUkrainianActionPhrase(action.ukTokens)
            LessonExerciseLocale.Russian -> action.ruTokens
        }
        val sourceFromTokens = when (exerciseLocale) {
            LessonExerciseLocale.Ukrainian -> fallbackUkrainianIntervalTokens(interval.ukFromTokens)
            LessonExerciseLocale.Russian -> interval.ruFromTokens
        }
        val sourceToTokens = when (exerciseLocale) {
            LessonExerciseLocale.Ukrainian -> fallbackUkrainianIntervalTokens(interval.ukToTokens)
            LessonExerciseLocale.Russian -> interval.ruToTokens
        }

        val sourceText = capitalizeSentence(
            renderTokens(
            sourceTemplateTokens,
            mapOf(
                "{action}" to sourceActionTokens,
                "{from}" to sourceFromTokens,
                "{to}" to sourceToTokens
            )
            )
        )

        val correctWords = capitalizeFirstToken(
            renderBgTokens(
            template.bgTokens,
            mapOf(
                "{action}" to action.bgTokens,
                "{from}" to interval.bgFromTokens,
                "{to}" to interval.bgToTokens
            )
            )
        )

        return buildTranslationExercise(
            id = id,
            sourceText = sourceText,
            correctWords = correctWords,
            distractorPool = distractorPool,
            hint = template.hint
        )
    }

    private fun generateQuestionExercise(
        id: Int,
        sequenceIndex: Int,
        actions: List<Lesson10PhraseAsset>,
        templates: List<Lesson10TemplateAsset>,
        distractorPool: List<String>,
        exerciseLocale: LessonExerciseLocale,
    ): LessonExercise {
        val action = actions[sequenceIndex % actions.size]
        val template = templates[sequenceIndex % templates.size]

        val sourceTemplateTokens = when (exerciseLocale) {
            LessonExerciseLocale.Ukrainian -> fallbackUkrainianTemplateTokens(template.ukTokens)
            LessonExerciseLocale.Russian -> template.ruTokens
        }
        val sourceActionTokens = when (exerciseLocale) {
            LessonExerciseLocale.Ukrainian -> fallbackUkrainianQuestionAction(action.ukTokens)
            LessonExerciseLocale.Russian -> action.ruTokens
        }

        val sourceText = finalizeSourceText(
            capitalizeSentence(
                renderTokens(
                    sourceTemplateTokens,
                    mapOf("{action}" to sourceActionTokens)
                )
            ),
            template
        )

        val correctWords = capitalizeFirstToken(
            renderBgTokens(
                template.bgTokens,
                mapOf("{action}" to action.bgTokens)
            )
        )

        return buildTranslationExercise(
            id = id,
            sourceText = sourceText,
            correctWords = correctWords,
            distractorPool = distractorPool,
            hint = template.hint
        )
    }

    private fun renderTokens(
        templateTokens: List<String>,
        replacements: Map<String, List<String>>
    ): String {
        return templateTokens
            .flatMap { token -> replacements[token] ?: listOf(token) }
            .joinToString(" ")
            .replace(" ?", "?")
    }

    private fun renderBgTokens(
        templateTokens: List<String>,
        replacements: Map<String, List<String>>
    ): List<String> {
        return templateTokens.flatMap { token -> replacements[token] ?: listOf(token) }
    }

    private fun capitalizeSentence(text: String): String {
        return text.replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase() else char.toString()
        }
    }

    private fun fallbackUkrainianTemplateTokens(tokens: List<String>): List<String> {
        return when (tokens.joinToString(" ")) {
            "{time} {action}" -> listOf("{time}", "{action}")
            "{action} {time}" -> listOf("{action}", "{time}")
            "{action} {from} {to}" -> listOf("{action}", "{from}", "{to}")
            "{from} {to} {action}" -> listOf("{from}", "{to}", "{action}")
            "Когда {action}" -> listOf("Коли", "{action}")
            "Во сколько часов {action}" -> listOf("О", "котрій", "годині", "{action}")
            else -> fallbackUkrainianTokens(tokens)
        }
    }

    private fun fallbackUkrainianTimePhrase(tokens: List<String>): List<String> {
        return when (tokens.joinToString(" ")) {
            "В понедельник" -> listOf("У", "понеділок")
            "Во вторник" -> listOf("У", "вівторок")
            "В среду" -> listOf("У", "середу")
            "В четверг" -> listOf("У", "четвер")
            "В пятницу" -> listOf("У", "п'ятницю")
            "Утром" -> listOf("Вранці")
            "Днём" -> listOf("Вдень")
            "Вечером" -> listOf("Увечері")
            "Ночью" -> listOf("Вночі")
            "Сегодня" -> listOf("Сьогодні")
            "Завтра" -> listOf("Завтра")
            "После работы" -> listOf("Після", "роботи")
            "Перед обедом" -> listOf("Перед", "обідом")
            "В час дня" -> listOf("О", "першій", "годині", "дня")
            "В два часа" -> listOf("О", "другій", "годині")
            "В три часа" -> listOf("О", "третій", "годині")
            "В пять часов" -> listOf("О", "п'ятій", "годині")
            else -> fallbackUkrainianTokens(tokens)
        }
    }

    private fun fallbackUkrainianActionPhrase(tokens: List<String>): List<String> {
        return when (tokens.joinToString(" ")) {
            "работаю" -> listOf("працюю")
            "у меня урок" -> listOf("у", "мене", "урок")
            "учусь" -> listOf("навчаюся")
            "завтракаю" -> listOf("снідаю")
            "пью кофе" -> listOf("п'ю", "каву")
            "ужинаю" -> listOf("вечеряю")
            "отдыхаю" -> listOf("відпочиваю")
            "иду в магазин" -> listOf("йду", "до", "магазину")
            "возвращаюсь домой" -> listOf("повертаюся", "додому")
            "обедаю" -> listOf("обідаю")
            "читаю дома" -> listOf("читаю", "вдома")
            "иду домой" -> listOf("йду", "додому")
            "читаю" -> listOf("читаю")
            "сплю" -> listOf("сплю")
            else -> fallbackUkrainianTokens(tokens)
        }
    }

    private fun fallbackUkrainianQuestionAction(tokens: List<String>): List<String> {
        return when (tokens.joinToString(" ")) {
            "ты работаешь" -> listOf("ти", "працюєш")
            "у тебя урок" -> listOf("у", "тебе", "урок")
            "ты учишься" -> listOf("ти", "навчаєшся")
            "ты завтракаешь" -> listOf("ти", "снідаєш")
            "ты пьёшь кофе" -> listOf("ти", "п'єш", "каву")
            "ты ужинаешь" -> listOf("ти", "вечеряєш")
            "ты отдыхаешь" -> listOf("ти", "відпочиваєш")
            "ты идёшь в магазин" -> listOf("ти", "йдеш", "до", "магазину")
            "ты возвращаешься домой" -> listOf("ти", "повертаєшся", "додому")
            "ты обедаешь" -> listOf("ти", "обідаєш")
            "ты читаешь дома" -> listOf("ти", "читаєш", "вдома")
            "ты идёшь домой" -> listOf("ти", "йдеш", "додому")
            else -> fallbackUkrainianTokens(tokens)
        }
    }

    private fun fallbackUkrainianIntervalTokens(tokens: List<String>): List<String> {
        return when (tokens.joinToString(" ")) {
            "с девяти" -> listOf("з", "дев'ятої")
            "до пяти" -> listOf("до", "п'ятої")
            "с двух" -> listOf("з", "другої")
            "до трёх" -> listOf("до", "третьої")
            "с часа" -> listOf("з", "першої")
            "до двух" -> listOf("до", "другої")
            else -> fallbackUkrainianTokens(tokens)
        }
    }

    private fun fallbackUkrainianTokens(tokens: List<String>): List<String> {
        return tokens.map { token ->
            when (token) {
                "В" -> "У"
                "Во" -> "О"
                "понедельник" -> "понеділок"
                "вторник" -> "вівторок"
                "среду" -> "середу"
                "четверг" -> "четвер"
                "пятницу" -> "п'ятницю"
                "Утром" -> "Вранці"
                "Днём" -> "Вдень"
                "Вечером" -> "Увечері"
                "Ночью" -> "Вночі"
                "Сегодня" -> "Сьогодні"
                "Завтра" -> "Завтра"
                "После" -> "Після"
                "работы" -> "роботи"
                "Перед" -> "Перед"
                "обедом" -> "обідом"
                "час" -> "годину"
                "дня" -> "дня"
                "два" -> "два"
                "три" -> "три"
                "пять" -> "п'ять"
                "часа" -> "години"
                "часов" -> "годин"
                "работаю" -> "працюю"
                "у" -> "у"
                "меня" -> "мене"
                "урок" -> "урок"
                "учусь" -> "навчаюся"
                "завтракаю" -> "снідаю"
                "пью" -> "п'ю"
                "кофе" -> "каву"
                "ужинаю" -> "вечеряю"
                "отдыхаю" -> "відпочиваю"
                "иду" -> "йду"
                "в" -> "до"
                "магазин" -> "магазину"
                "возвращаюсь" -> "повертаюся"
                "домой" -> "додому"
                "обедаю" -> "обідаю"
                "читаю" -> "читаю"
                "дома" -> "вдома"
                "с" -> "з"
                "девяти" -> "дев'ятої"
                "до" -> "до"
                "пяти" -> "п'ятої"
                "двух" -> "другої"
                "трёх" -> "третьої"
                "сплю" -> "сплю"
                "ты" -> "ти"
                "работаешь" -> "працюєш"
                "тебя" -> "тебе"
                "учишься" -> "навчаєшся"
                "завтракаешь" -> "снідаєш"
                "пьёшь" -> "п'єш"
                "ужинаешь" -> "вечеряєш"
                "отдыхаешь" -> "відпочиваєш"
                "идёшь" -> "йдеш"
                "Когда" -> "Коли"
                "Когда?" -> "Коли?"
                "Сколько" -> "Скільки"
                "сколько" -> "скільки"
                else -> token
            }
        }
    }

    private fun cycleStep(size: Int): Int {
        if (size <= 1) return 1

        return (2..size).firstOrNull { candidate ->
            greatestCommonDivisor(candidate, size) == 1
        } ?: 1
    }

    private tailrec fun greatestCommonDivisor(
        left: Int,
        right: Int
    ): Int {
        return if (right == 0) left else greatestCommonDivisor(right, left % right)
    }

    private fun finalizeSourceText(
        text: String,
        template: Lesson10TemplateAsset
    ): String {
        return if (template.isQuestion) "$text?" else text
    }

    private fun capitalizeFirstToken(tokens: List<String>): List<String> {
        if (tokens.isEmpty()) return tokens
        return listOf(tokens.first().replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase() else char.toString()
        }) + tokens.drop(1)
    }

    private fun buildDistractorPool(
        timePhrases: List<Lesson10PhraseAsset>,
        routineActions: List<Lesson10PhraseAsset>,
        intervals: List<Lesson10IntervalAsset>,
        intervalActions: List<Lesson10PhraseAsset>,
        questionActions: List<Lesson10PhraseAsset>,
        questionTemplates: List<Lesson10TemplateAsset>
    ): List<String> {
        return buildList {
            addAll(timePhrases.flatMap { it.bgTokens })
            addAll(routineActions.flatMap { it.bgTokens })
            addAll(intervalActions.flatMap { it.bgTokens })
            addAll(questionActions.flatMap { it.bgTokens })
            addAll(intervals.flatMap { it.bgFromTokens + it.bgToTokens })
            addAll(
                questionTemplates.flatMap { template ->
                    template.bgTokens.filterNot { it.startsWith("{") && it.endsWith("}") }
                }
            )
        }.distinct()
    }
}
