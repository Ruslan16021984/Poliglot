package com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.support.LessonRealSentenceGenerator
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

object Lesson8RealGenerator {

    private data class TemplateData(
        val ru: String,
        val bgWords: List<String>,
        val hint: String? = null
    )

    private val templates = listOf(
        TemplateData(
            ru = "Он старше меня",
            bgWords = listOf("Той", "е", "по-стар", "от", "мен"),
            hint = "💡 сравнение: по- + прилагательное + от"
        ),
        TemplateData(
            ru = "Я младше его",
            bgWords = listOf("Аз", "съм", "по-млад", "от", "него"),
            hint = "💡 сравнение: по- + прилагательное + от"
        ),
        TemplateData(
            ru = "Эта книга интереснее той",
            bgWords = listOf("Тази", "книга", "е", "по-интересна", "от", "онази"),
            hint = "💡 \"от\" означает «чем»"
        ),
        TemplateData(
            ru = "Моя машина быстрее твоей",
            bgWords = listOf("Моята", "кола", "е", "по-бърза", "от", "твоята"),
            hint = "💡 полное сравнение в живой фразе"
        ),
        TemplateData(
            ru = "Наш дом больше вашего",
            bgWords = listOf("Нашата", "къща", "е", "по-голяма", "от", "вашата"),
            hint = "💡 полное сравнение в живой фразе"
        ),
        TemplateData(
            ru = "Мой брат выше меня",
            bgWords = listOf("Моят", "брат", "е", "по-висок", "от", "мен"),
            hint = "💡 сравнение: по- + прилагательное + от"
        ),
        TemplateData(
            ru = "Телефон дороже часов",
            bgWords = listOf("Телефонът", "е", "по-скъп", "от", "часовника"),
            hint = "💡 сравнение предметов"
        ),
        TemplateData(
            ru = "Книга лучше фильма",
            bgWords = listOf("Книгата", "е", "по-добра", "от", "филма"),
            hint = "💡 добър → по-добър"
        ),
        TemplateData(
            ru = "Это лучшая книга",
            bgWords = listOf("Това", "е", "най-добрата", "книга"),
            hint = "💡 превосходная степень: най- + прилагательное"
        ),
        TemplateData(
            ru = "Он лучший ученик",
            bgWords = listOf("Той", "е", "най-добрият", "ученик"),
            hint = "💡 превосходная степень: най- + прилагательное"
        ),
        TemplateData(
            ru = "Это самая дорогая машина",
            bgWords = listOf("Това", "е", "най-скъпата", "кола"),
            hint = "💡 с превосходной степенью обычно нужен артикль"
        ),
        TemplateData(
            ru = "Это самый красивый дом",
            bgWords = listOf("Това", "е", "най-красивият", "дом"),
            hint = "💡 с превосходной степенью обычно нужен артикль"
        ),
        TemplateData(
            ru = "Это самый интересный фильм",
            bgWords = listOf("Това", "е", "най-интересният", "филм"),
            hint = "💡 с превосходной степенью обычно нужен артикль"
        ),
        TemplateData(
            ru = "Это лучший день",
            bgWords = listOf("Това", "е", "най-добрият", "ден"),
            hint = "💡 най- = самый"
        ),
        TemplateData(
            ru = "Это лучший день в моей жизни",
            bgWords = listOf("Това", "е", "най-хубавият", "ден", "в", "живота", "ми"),
            hint = "💡 длинное полное предложение"
        )
    )

    private val distractorPool = listOf(
        "Това", "Той", "Аз", "Тази",
        "е", "съм", "от", "в",
        "мен", "него", "ми",
        "по-стар", "по-млад", "по-интересна", "по-бърза", "по-голяма", "по-висок", "по-скъп", "по-добра",
        "най-добрата", "най-добрият", "най-скъпата", "най-красивият", "най-интересният", "най-хубавият",
        "книга", "книгата", "кола", "къща", "дом", "брат", "ученик", "часовника", "филма", "филм", "ден", "живота",
        "Моята", "твоята", "Нашата", "вашата", "Телефонът", "Моят", "онази"
    )

    fun generateExercises(): List<LessonExercise> {
        return (1..60).map { id -> generateExercise(id) }
    }

    private fun generateExercise(id: Int): LessonExercise {
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