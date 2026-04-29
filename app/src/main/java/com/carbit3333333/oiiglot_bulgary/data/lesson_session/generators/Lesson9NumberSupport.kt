package com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson9NumberAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson9ObjectAsset

internal data class NumberedNounPhrase(
    val numberBg: String,
    val objectBg: String,
    val numberRu: String,
    val objectRu: String,
)

internal fun applyNumber(
    number: Lesson9NumberAsset,
    noun: Lesson9ObjectAsset
): NumberedNounPhrase {
    val useSingular = number.value == 1
    val useFewFormInRussian = number.value in 2..4

    val numberBg = when (noun.gender) {
        "masculine" -> number.bgMasculine
        "feminine" -> number.bgFeminine
        "neuter" -> number.bgNeuter
        else -> number.bgMasculine
    }

    val numberRu = when (noun.gender) {
        "masculine" -> number.ruMasculine
        "feminine" -> number.ruFeminine
        "neuter" -> number.ruNeuter
        else -> number.ruMasculine
    }

    val objectBg = when {
        useSingular -> noun.singular
        noun.gender == "masculine" -> noun.countForm
        else -> noun.plural
    }

    val objectRu = when {
        useSingular -> noun.ruSingular
        useFewFormInRussian -> noun.ruPlural
        else -> noun.ruMany
    }

    return NumberedNounPhrase(
        numberBg = numberBg,
        objectBg = objectBg,
        numberRu = numberRu,
        objectRu = objectRu,
    )
}
