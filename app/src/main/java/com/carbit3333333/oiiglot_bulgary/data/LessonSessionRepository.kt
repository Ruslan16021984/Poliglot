package com.carbit3333333.oiiglot_bulgary.data

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonSessionFactory
import com.carbit3333333.oiiglot_bulgary.model.LessonSession

class LessonSessionRepository {
    fun getLessonSession(lessonId: Int): LessonSession {
        return LessonSessionFactory.create(lessonId)
    }
}
