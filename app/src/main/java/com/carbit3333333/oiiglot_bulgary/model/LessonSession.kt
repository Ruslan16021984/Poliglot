package com.carbit3333333.oiiglot_bulgary.model

data class LessonSession(
    val lessonId: Int,
    val lessonTitle: String,
    val exercises: List<LessonExercise>
)
