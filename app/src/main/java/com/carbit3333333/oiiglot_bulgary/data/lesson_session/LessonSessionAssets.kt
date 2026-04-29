package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import kotlinx.serialization.Serializable

@Serializable
internal data class Lesson9NumberAsset(
    val value: Int,
    val bgMasculine: String,
    val bgFeminine: String,
    val bgNeuter: String,
    val ruMasculine: String,
    val ruFeminine: String,
    val ruNeuter: String,
    val ukMasculine: String = ruMasculine,
    val ukFeminine: String = ruFeminine,
    val ukNeuter: String = ruNeuter,
)

@Serializable
internal data class Lesson9ObjectAsset(
    val gender: String,
    val singular: String,
    val plural: String,
    val countForm: String,
    val ruSingular: String,
    val ruPlural: String,
    val ruMany: String = ruPlural,
    val ukSingular: String = ruSingular,
    val ukPlural: String = ruPlural,
    val ukMany: String = ruMany,
)
