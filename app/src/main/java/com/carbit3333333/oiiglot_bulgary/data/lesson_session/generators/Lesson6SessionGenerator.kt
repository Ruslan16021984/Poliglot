package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal fun generateLesson6Exercises(): List<LessonExercise> {
    return (1..60).map { id ->
        generateLesson6Exercise(id)
    }
}

internal fun generateLesson6Exercise(id: Int): LessonExercise {
    val subject = subjects.random()
    val subjectRuText = subjectRu.getValue(subject)
    val verb = sumForms.getValue(subject)

    val preposition = lesson6Prepositions.random()
    val placePair = lesson6PlacesByPreposition.getValue(preposition).random()
    val placeBg = placePair.first
    val placeRu = placePair.second

    val correctWords = listOf(subject, verb, preposition, placeBg)
    val sourceText = "$subjectRuText $placeRu"

    val distractorPool = (
            subjects +
                    sumForms.values +
                    lesson6Prepositions +
                    lesson6PlacesByPreposition.values.flatten().map { it.first } +
                    listOf("не", "ли")
            ).distinct()

    return buildTranslationExercise(
        id = id,
        sourceText = sourceText,
        correctWords = correctWords,
        distractorPool = distractorPool,
        hint = "💡 используй предлог + существительное"
    )
}
