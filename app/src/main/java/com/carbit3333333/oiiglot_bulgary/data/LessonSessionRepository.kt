package com.carbit3333333.oiiglot_bulgary.data

import android.content.Context
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonSessionAssets
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonSessionAssetsRepository
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonSessionFactory
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.defaultLessonSessionAssets
import com.carbit3333333.oiiglot_bulgary.model.LessonSession

class LessonSessionRepository private constructor(
    private val sessionAssets: LessonSessionAssets
) {

    constructor(context: Context) : this(
        sessionAssets = LessonSessionAssetsRepository(context).load()
    )

    constructor() : this(
        sessionAssets = defaultLessonSessionAssets()
    )

    fun getLessonSession(lessonId: Int): LessonSession {
        return LessonSessionFactory.create(
            lessonId = lessonId,
            assets = sessionAssets
        )
    }
}
