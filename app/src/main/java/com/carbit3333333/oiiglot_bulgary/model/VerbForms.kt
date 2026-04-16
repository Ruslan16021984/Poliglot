package com.carbit3333333.oiiglot_bulgary.model

data class VerbForms(
    val infinitive: String,
    val present: Map<String, String>,
    val ruPresent: Map<String, String>,
    val past: Map<String, String>,
    val ruPast: Map<String, String>
)
