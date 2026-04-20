package com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonTemplateAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.support.LessonRealSentenceGenerator
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal object Lesson8RealGenerator {

    private val distractorPool = listOf(
        "Това", "Той", "Аз", "Тази",
        "е", "съм", "от", "в",
        "мен", "него", "ми",
        "по-стар", "по-млад", "по-интересна", "по-бърза", "по-голяма", "по-висок", "по-скъп", "по-добра",
        "най-добрата", "най-добрият", "най-скъпата", "най-красивият", "най-интересният", "най-хубавият",
        "книга", "книгата", "кола", "къща", "дом", "брат", "ученик", "часовника", "филма", "филм", "ден", "живота",
        "Моята", "твоята", "Нашата", "вашата", "Телефонът", "Моят", "онази"
    )

    internal fun generateExercises(templates: List<LessonTemplateAsset>): List<LessonExercise> {
        return (1..60).map { id -> generateExercise(id, templates) }
    }

    private fun generateExercise(
        id: Int,
        templates: List<LessonTemplateAsset>
    ): LessonExercise {
        val template = templates.random()

        return LessonRealSentenceGenerator.buildExercise(
            id = id,
            template = LessonRealSentenceGenerator.SentenceTemplate(
                ruPattern = template.ru,
                bgPattern = template.bgWords.map { LessonRealSentenceGenerator.Token.Fixed(it) }
            ),
            lexicon = LessonRealSentenceGenerator.Lexicon(
                subject = LessonRealSentenceGenerator.SubjectForms(
                    bg = "Аз",
                    ru = "Я"
                )
            ),
            distractorPool = distractorPool,
            totalWords = 8,
            hint = template.hint
        )
    }
}
