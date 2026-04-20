package com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.support.LessonRealSentenceGenerator
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

object Lesson7RealGenerator {

    private data class TemplateData(
        val ru: String,
        val bgWords: List<String>,
        val hint: String? = null
    )

    private val templates = listOf(
        TemplateData(
            ru = "Это моя книга",
            bgWords = listOf("Това", "е", "моята", "книга"),
            hint = "💡 притяжательная форма с артиклем"
        ),
        TemplateData(
            ru = "Это мой друг",
            bgWords = listOf("Това", "е", "моят", "приятел"),
            hint = "💡 форма зависит от рода существительного"
        ),
        TemplateData(
            ru = "Это твоя книга",
            bgWords = listOf("Това", "е", "твоята", "книга"),
            hint = "💡 притяжательная форма с артиклем"
        ),
        TemplateData(
            ru = "Это наш ребёнок",
            bgWords = listOf("Това", "е", "нашето", "дете"),
            hint = "💡 форма зависит от рода существительного"
        ),
        TemplateData(
            ru = "Это наши книги",
            bgWords = listOf("Това", "са", "нашите", "книги"),
            hint = "💡 множественное число"
        ),
        TemplateData(
            ru = "У меня есть моя книга",
            bgWords = listOf("Аз", "имам", "моята", "книга"),
            hint = "💡 полная живая фраза"
        ),
        TemplateData(
            ru = "Я вижу свою книгу",
            bgWords = listOf("Аз", "виждам", "моята", "книга"),
            hint = "💡 полная живая фраза"
        ),
        TemplateData(
            ru = "Я беру свою книгу",
            bgWords = listOf("Аз", "взимам", "моята", "книга"),
            hint = "💡 взимам = беру"
        ),
        TemplateData(
            ru = "Мы любим нашего ребёнка",
            bgWords = listOf("Ние", "обичаме", "нашето", "дете"),
            hint = "💡 обичам = люблю"
        ),
        TemplateData(
            ru = "Я даю тебе свою книгу",
            bgWords = listOf("Давам", "ти", "моята", "книга"),
            hint = "💡 в болгарском \"тебе\" часто ставится перед объектом"
        ),
        TemplateData(
            ru = "Я даю тебе книгу",
            bgWords = listOf("Давам", "ти", "книгата"),
            hint = "💡 давать кому-то"
        ),
        TemplateData(
            ru = "Ты видишь своего друга",
            bgWords = listOf("Ти", "виждаш", "твоя", "приятел"),
            hint = "💡 притяжательное согласуется с существительным"
        )
    )

    private val distractorPool = listOf(
        "Това", "е", "са",
        "Аз", "Ти", "Ние",
        "имам", "виждам", "виждаш", "взимам", "обичаме", "Давам",
        "ти",
        "моята", "моят", "твоята", "твоя",
        "нашето", "нашите",
        "книга", "книгата", "приятел", "дете", "книги"
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