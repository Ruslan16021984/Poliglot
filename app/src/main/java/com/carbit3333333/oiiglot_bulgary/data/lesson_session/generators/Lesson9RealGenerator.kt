package com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson9NumberAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson9ObjectAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson9TemplateAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonExerciseLocale
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.buildTranslationExercise
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal data class NumberedNounPhrase(
    val numberBg: String,
    val objectBg: String,
    val numberRu: String,
    val objectRu: String,
    val numberUk: String,
    val objectUk: String,
)

internal fun applyNumber(
    number: Lesson9NumberAsset,
    noun: Lesson9ObjectAsset
): NumberedNounPhrase {
    val numberBg = when (noun.gender) {
        "masculine" -> number.bgMasculine
        "neuter" -> number.bgNeuter
        else -> number.bgFeminine
    }

    val numberRu = when (noun.gender) {
        "masculine" -> number.ruMasculine
        "neuter" -> number.ruNeuter
        else -> number.ruFeminine
    }

    val numberUk = when (noun.gender) {
        "masculine" -> fallbackUkrainianNumber(number.ukMasculine, number.ruMasculine)
        "neuter" -> fallbackUkrainianNumber(number.ukNeuter, number.ruNeuter)
        else -> fallbackUkrainianNumber(number.ukFeminine, number.ruFeminine)
    }

    val objectBg = if (number.value == 1) {
        noun.singular
    } else {
        noun.countForm
    }

    val objectRu = when {
        number.value == 1 -> noun.ruSingular
        number.value in 2..4 -> noun.ruPlural
        else -> noun.ruMany
    }

    val objectUk = when {
        number.value == 1 -> fallbackUkrainianObject(noun.ukSingular, noun.ruSingular)
        number.value in 2..4 -> fallbackUkrainianObject(noun.ukPlural, noun.ruPlural)
        else -> fallbackUkrainianObject(noun.ukMany, noun.ruMany)
    }

    return NumberedNounPhrase(
        numberBg = numberBg,
        objectBg = objectBg,
        numberRu = numberRu,
        objectRu = objectRu,
        numberUk = numberUk,
        objectUk = objectUk,
    )
}

private fun fallbackUkrainianNumber(uk: String, ru: String): String {
    if (uk != ru) return uk
    return when (ru) {
        "один" -> "один"
        "одну" -> "одну"
        "одно" -> "одне"
        "два" -> "два"
        "две" -> "дві"
        "три" -> "три"
        "четыре" -> "чотири"
        "пять" -> "п'ять"
        "шесть" -> "шість"
        "семь" -> "сім"
        "восемь" -> "вісім"
        "девять" -> "дев'ять"
        "десять" -> "десять"
        "одиннадцать" -> "одинадцять"
        "двенадцать" -> "дванадцять"
        "тринадцать" -> "тринадцять"
        "четырнадцать" -> "чотирнадцять"
        "пятнадцать" -> "п'ятнадцять"
        "шестнадцать" -> "шістнадцять"
        "семнадцать" -> "сімнадцять"
        "восемнадцать" -> "вісімнадцять"
        "девятнадцать" -> "дев'ятнадцять"
        "двадцать" -> "двадцять"
        else -> ru
    }
}

private fun fallbackUkrainianObject(uk: String, ru: String): String {
    if (uk != ru) return uk
    return when (ru) {
        "книгу" -> "книгу"
        "книги" -> "книги"
        "книг" -> "книг"
        "телефон" -> "телефон"
        "телефона" -> "телефони"
        "телефонов" -> "телефонів"
        "письмо" -> "лист"
        "письма" -> "листи"
        "писем" -> "листів"
        "билет" -> "квиток"
        "билета" -> "квитки"
        "билетов" -> "квитків"
        "чашку" -> "чашку"
        "чашки" -> "чашки"
        "чашек" -> "чашок"
        else -> ru
    }
}

private fun fallbackUkrainianTokens(tokens: List<String>, locale: LessonExerciseLocale): List<String> {
    if (locale != LessonExerciseLocale.Ukrainian) return tokens
    return tokens.map { token ->
        when (token) {
            "Я" -> "Я"
            "Ты" -> "Ти"
            "Мы" -> "Ми"
            "меня" -> "мене"
            "есть" -> "є"
            "беру" -> "беру"
            "вижу" -> "бачу"
            "даю" -> "даю"
            "тебе" -> "тобі"
            "покупаю" -> "купую"
            else -> token
        }
    }
}

internal object Lesson9RealGenerator {

    fun generateExercises(
        numbers: List<Lesson9NumberAsset>,
        objects: List<Lesson9ObjectAsset>,
        templates: List<Lesson9TemplateAsset>,
        exerciseLocale: LessonExerciseLocale = LessonExerciseLocale.Russian,
    ): List<LessonExercise> {
        val distractorPool = buildDistractorPool(numbers, objects, templates)
        return (1..100).map { id ->
            generateExercise(
                id = id,
                numbers = numbers,
                objects = objects,
                templates = templates,
                distractorPool = distractorPool,
                exerciseLocale = exerciseLocale,
            )
        }
    }

    private fun generateExercise(
        id: Int,
        numbers: List<Lesson9NumberAsset>,
        objects: List<Lesson9ObjectAsset>,
        templates: List<Lesson9TemplateAsset>,
        distractorPool: List<String>,
        exerciseLocale: LessonExerciseLocale,
    ): LessonExercise {
        val template = templates[(id - 1) % templates.size]
        val noun = objects[((id - 1) * 2) % objects.size]
        val number = numbers[(id - 1) % numbers.size]
        val phrase = applyNumber(number, noun)

        val sourceTokens = when (exerciseLocale) {
            LessonExerciseLocale.Ukrainian -> fallbackUkrainianTokens(template.ukTokens, exerciseLocale)
            LessonExerciseLocale.Russian -> template.ruTokens
        }

        val sourceText = sourceTokens
            .map { token ->
                when (token) {
                    "{num}" -> when (exerciseLocale) {
                        LessonExerciseLocale.Ukrainian -> phrase.numberUk
                        LessonExerciseLocale.Russian -> phrase.numberRu
                    }
                    "{object}" -> when (exerciseLocale) {
                        LessonExerciseLocale.Ukrainian -> phrase.objectUk
                        LessonExerciseLocale.Russian -> phrase.objectRu
                    }
                    else -> token
                }
            }
            .joinToString(" ")
            .replace(" ?", "?")

        val correctWords = template.bgTokens.map { token ->
            when (token) {
                "{num}" -> phrase.numberBg
                "{object}" -> phrase.objectBg
                else -> token
            }
        }

        return buildTranslationExercise(
            id = id,
            sourceText = sourceText,
            correctWords = correctWords,
            distractorPool = distractorPool,
            hint = template.hint
        )
    }

    private fun buildDistractorPool(
        numbers: List<Lesson9NumberAsset>,
        objects: List<Lesson9ObjectAsset>,
        templates: List<Lesson9TemplateAsset>
    ): List<String> {
        return buildList {
            addAll(numbers.flatMap { listOf(it.bgMasculine, it.bgFeminine, it.bgNeuter) })
            addAll(objects.flatMap { listOf(it.singular, it.plural, it.countForm) })
            addAll(
                templates.flatMap { template ->
                    template.bgTokens.filterNot { it.startsWith("{") && it.endsWith("}") }
                }
            )
        }.distinct()
    }
}
