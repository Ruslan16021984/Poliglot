package com.carbit3333333.oiiglot_bulgary.data

import com.carbit3333333.oiiglot_bulgary.model.Lesson
import com.carbit3333333.oiiglot_bulgary.model.Phrase
import com.carbit3333333.oiiglot_bulgary.model.Question
import com.carbit3333333.oiiglot_bulgary.model.TheoryBlock

class LessonRepository {

    private val lesson1Theory = listOf(
        TheoryBlock(
            title = "Настоящее время",
            text =
                "В болгарском языке глагол меняется в зависимости от лица.\n\n" +
                        "Аз правя\n" +
                        "Ти правиш\n" +
                        "Той прави\n" +
                        "Ние правим\n" +
                        "Вие правите\n" +
                        "Те правят"
        ),
        TheoryBlock(
            title = "Вопрос",
            text =
                "Вопрос образуется с помощью частицы \"ли\".\n\n" +
                        "Ти правиш ли?\n" +
                        "Той гледа ли?\n" +
                        "Вие работите ли?"
        ),
        TheoryBlock(
            title = "Отрицание",
            text =
                "Отрицание образуется с помощью частицы \"не\".\n\n" +
                        "Аз не правя\n" +
                        "Ти не гледаш\n" +
                        "Те не работят"
        ),
        TheoryBlock(
            title = "Частица \"да\" и будущее время",
            text =
                "В болгарском языке нет инфинитива (как \"делать\" в русском).\n\n" +
                        "Вместо этого используется конструкция:\n" +
                        "да + глагол\n\n" +

                        "Например:\n" +
                        "да правя — делать\n" +
                        "да пия — пить\n\n" +

                        "Будущее время:\n" +
                        "ще + глагол\n\n" +
                        "Аз ще правя — Я буду делать\n" +
                        "Ние ще пием — Мы будем пить\n\n" +

                        "Отрицание в будущем:\n" +
                        "няма + да + глагол\n\n" +
                        "Аз няма да правя — Я не буду делать\n" +
                        "Той няма да работи — Он не будет работать\n" +
                        "Ние няма да пием — Мы не будем пить\n\n" +

                        "Важно:\n" +
                        "частица \"да\" обязательна в этой конструкции.\n\n" +
                        "Неправильно:\n" +
                        "Той няма прави\n\n" +
                        "Правильно:\n" +
                        "Той няма да прави"
        ),
        TheoryBlock(
            title = "Будущее время",
            text =
                "Будущее время образуется с помощью частицы \"ще\".\n\n" +
                        "Аз ще правя\n" +
                        "Ти ще гледаш\n" +
                        "Те ще работят"
        ),
        TheoryBlock(
            title = "Отрицание в будущем",
            text =
                "Отрицание в будущем времени образуется с помощью конструкции \"няма да\".\n\n" +
                        "Аз няма да правя\n" +
                        "Ти няма да гледаш\n" +
                        "Те няма да работят"
        ),
        TheoryBlock(
            title = "Окончания",
            text =
                "Попробуй замечать окончания:\n\n" +
                        "Ти часто заканчивается на -ш\n" +
                        "Те часто заканчивается на -т\n\n" +
                        "Пример:\n" +
                        "правя → правиш → прави → правим → правите → правят"
        )
    )

    private val lesson2Theory = listOf(
        TheoryBlock(
            title = "Глагол \"съм\"",
            text =
                "В болгарском языке глагол \"съм\" используется там, где в русском часто нет слова \"есть\".\n\n" +
                        "Аз съм вкъщи\n" +
                        "Ти си лекар\n" +
                        "Той е тук"
        ),
        TheoryBlock(
            title = "Формы глагола",
            text =
                "Аз съм\n" +
                        "Ти си\n" +
                        "Той е\n" +
                        "Ние сме\n" +
                        "Вие сте\n" +
                        "Те са"
        ),
        TheoryBlock(
            title = "Утверждение",
            text =
                "Схема простая:\n" +
                        "подлежащее + форма \"съм\" + слово или место.\n\n" +
                        "Аз съм студент\n" +
                        "Ти си вкъщи\n" +
                        "Ние сме в училище"
        ),
        TheoryBlock(
            title = "Отрицание",
            text =
                "Отрицание образуется с помощью \"не\".\n\n" +
                        "Аз не съм вкъщи\n" +
                        "Ти не си лекар\n" +
                        "Те не са в града"
        ),
        TheoryBlock(
            title = "Вопрос",
            text =
                "Вопрос образуется с помощью частицы \"ли\".\n\n" +
                        "Ти си ли тук?\n" +
                        "Той е ли лекар?\n" +
                        "Вие сте ли вкъщи?"
        ),
        TheoryBlock(
            title = "Важно",
            text =
                "В русском языке слово \"есть\" часто опускается.\n" +
                        "В болгарском языке форма \"съм\" обязательна.\n\n" +
                        "Я дома → Аз съм вкъщи\n" +
                        "Мы в школе → Ние сме в училище"
        )
    )

    private val lesson3Theory = listOf(
        TheoryBlock(
            title = "Прошедшее время",
            text =
                "Прошедшее время показывает действие в прошлом.\n\n" +
                        "Аз правих\n" +
                        "Ти прави\n" +
                        "Те правиха"
        ),
        TheoryBlock(
            title = "Формы",
            text =
                "Аз → -х\n" +
                        "Ти → без -х\n" +
                        "Той → без -х\n" +
                        "Ние → -хме\n" +
                        "Вие → -хте\n" +
                        "Те → -ха"
        ),
        TheoryBlock(
            title = "Отрицание",
            text =
                "Отрицание образуется с помощью \"не\".\n\n" +
                        "Аз не правих\n" +
                        "Ти не гледа\n" +
                        "Те не работиха"
        ),
        TheoryBlock(
            title = "Важно",
            text =
                "Формы прошедшего времени лучше запоминать целиком.\n" +
                        "Они не всегда похожи на настоящее время."
        )
    )

    private val lessons = listOf(
        Lesson(
            id = 1,
            title = "Урок 1",
            subtitle = "Основные формы глагола",
            theory = lesson1Theory,
            phrases = listOf(
                Phrase("Аз правя", "Я делаю"),
                Phrase("Ти гледаш ли?", "Ты смотришь?"),
                Phrase("Аз не работя", "Я не работаю"),
                Phrase("Той ще учи", "Он будет учиться"),
                Phrase("Ние няма да говорим", "Мы не будем говорить")
            ),
            questions = listOf(
                Question(
                    id = 1,
                    text = "Как переводится «Аз правя»?",
                    options = listOf("Я делаю", "Я сделал", "Я буду делать"),
                    correctAnswer = "Я делаю"
                ),
                Question(
                    id = 2,
                    text = "Как переводится «Ти гледаш ли?»?",
                    options = listOf("Ты смотришь?", "Ты не смотришь", "Ты будешь смотреть"),
                    correctAnswer = "Ты смотришь?"
                )
            )
        ),
        Lesson(
            id = 2,
            title = "Урок 2",
            subtitle = "Глагол „съм“",
            theory = lesson2Theory,
            phrases = listOf(
                Phrase("Аз съм тук", "Я здесь"),
                Phrase("Ти си лекар", "Ты врач"),
                Phrase("Ние сме вкъщи", "Мы дома"),
                Phrase("Те не са в града", "Они не в городе"),
                Phrase("Вие сте ли в училище?", "Вы в школе?")
            ),
            questions = listOf(
                Question(
                    id = 1,
                    text = "Как переводится «Аз съм тук»?",
                    options = listOf("Я здесь", "Я там", "Я дома"),
                    correctAnswer = "Я здесь"
                ),
                Question(
                    id = 2,
                    text = "Как переводится «Те не са в града»?",
                    options = listOf("Они не в городе", "Они в городе", "Они не дома"),
                    correctAnswer = "Они не в городе"
                )
            )
        ),
        Lesson(
            id = 3,
            title = "Урок 3",
            subtitle = "Прошедшее время",
            theory = lesson3Theory,
            phrases = listOf(
                Phrase("Аз правих", "Я делал"),
                Phrase("Ти гледа", "Ты смотрел"),
                Phrase("Ние работихме", "Мы работали"),
                Phrase("Те не учиха", "Они не учились"),
                Phrase("Аз видях", "Я видел")
            ),
            questions = listOf(
                Question(
                    id = 1,
                    text = "Как переводится «Аз правих»?",
                    options = listOf("Я делал", "Я делаю", "Я буду делать"),
                    correctAnswer = "Я делал"
                ),
                Question(
                    id = 2,
                    text = "Как переводится «Те не учиха»?",
                    options = listOf("Они не учились", "Они учатся", "Они не будут учиться"),
                    correctAnswer = "Они не учились"
                )
            )
        )
    )

    fun getLessons(): List<Lesson> = lessons

    fun getLessonById(lessonId: Int): Lesson? {
        return lessons.find { it.id == lessonId }
    }

    fun hasNextLesson(currentLessonId: Int): Boolean {
        return lessons.any { it.id == currentLessonId + 1 }
    }

    fun getNextLessonId(currentLessonId: Int): Int? {
        return lessons.find { it.id == currentLessonId + 1 }?.id
    }
}