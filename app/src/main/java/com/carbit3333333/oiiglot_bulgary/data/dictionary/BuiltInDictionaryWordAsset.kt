package com.carbit3333333.oiiglot_bulgary.data.dictionary

import kotlinx.serialization.Serializable

@Serializable
data class BuiltInDictionaryWordAsset(
    val bgWord: String,
    val ruTranslation: String,
    val lessonNumber: Int? = null,
)
