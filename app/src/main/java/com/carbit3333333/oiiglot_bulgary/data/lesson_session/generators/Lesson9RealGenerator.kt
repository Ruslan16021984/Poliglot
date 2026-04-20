package com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson9NumberAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson9ObjectAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson9TemplateAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.buildTranslationExercise
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal data class NumberedNounPhrase(
    val numberBg: String,
    val objectBg: String,
    val numberRu: String,
    val objectRu: String
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

    return NumberedNounPhrase(
        numberBg = numberBg,
        objectBg = objectBg,
        numberRu = numberRu,
        objectRu = objectRu
    )
}

internal object Lesson9RealGenerator {

    fun generateExercises(
        numbers: List<Lesson9NumberAsset>,
        objects: List<Lesson9ObjectAsset>,
        templates: List<Lesson9TemplateAsset>
    ): List<LessonExercise> {
        return (1..60).map { id ->
            generateExercise(
                id = id,
                numbers = numbers,
                objects = objects,
                templates = templates
            )
        }
    }

    private fun generateExercise(
        id: Int,
        numbers: List<Lesson9NumberAsset>,
        objects: List<Lesson9ObjectAsset>,
        templates: List<Lesson9TemplateAsset>
    ): LessonExercise {
        val number = numbers.random()
        val noun = objects.random()
        val template = templates.random()
        val phrase = applyNumber(number, noun)

        val sourceText = template.ruTokens
            .map { token ->
                when (token) {
                    "{num}" -> phrase.numberRu
                    "{object}" -> phrase.objectRu
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

        val distractorPool = buildDistractorPool(numbers, objects, templates)

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
