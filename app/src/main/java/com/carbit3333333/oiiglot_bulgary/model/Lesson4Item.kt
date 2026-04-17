package com.carbit3333333.oiiglot_bulgary.model

data class Lesson4Item(
    val type: Type,
    val ru: String,
    val correctWords: List<String>
) {
    enum class Type {
        NOUN,
        VERB
    }
}