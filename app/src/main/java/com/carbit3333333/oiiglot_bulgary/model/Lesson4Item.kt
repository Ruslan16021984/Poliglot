package com.carbit3333333.oiiglot_bulgary.model

import kotlinx.serialization.Serializable

@Serializable
data class Lesson4Item(
    val type: Type,
    val ru: String,
    val correctWords: List<String>,
    val uk: String? = null,
) {
    @Serializable
    enum class Type {
        NOUN,
        VERB
    }
}
