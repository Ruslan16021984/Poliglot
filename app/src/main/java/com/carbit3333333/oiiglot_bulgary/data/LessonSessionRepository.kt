package com.carbit3333333.oiiglot_bulgary.data

import android.util.Log
import com.carbit3333333.oiiglot_bulgary.model.Lesson4Item
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise
import com.carbit3333333.oiiglot_bulgary.model.LessonSession
import com.carbit3333333.oiiglot_bulgary.model.VerbForms

private const val mustForm = "трябва"

private enum class Lesson1SentenceType {
    PRESENT,
    PRESENT_QUESTION,
    PRESENT_NEGATIVE,
    FUTURE,
    FUTURE_QUESTION,
    FUTURE_NEGATIVE
}

private enum class Lesson2SentenceType {
    PRESENT,
    QUESTION,
    NEGATIVE,
    THIS_IS,
    THIS_IS_QUESTION,
    THIS_IS_NEGATIVE
}

private enum class Lesson3SentenceType {
    PAST,
    PAST_NEGATIVE
}

private enum class Lesson5SentenceType {
    POSITIVE,
    NEGATIVE,
    QUESTION
}

private enum class Lesson5ModalType {
    CAN,
    WANT,
    MUST
}

private data class Lesson8Template(
    val ru: String,
    val correctWords: List<String>
)

class LessonSessionRepository {

    private val subjects = listOf(
        "Аз", "Ти", "Той", "Ние", "Вие", "Те"
    )

    private val questionSubjects = listOf(
        "Ти", "Той", "Вие", "Те"
    )

    private val subjectRu = mapOf(
        "Аз" to "Я",
        "Ти" to "Ты",
        "Той" to "Он",
        "Ние" to "Мы",
        "Вие" to "Вы",
        "Те" to "Они"
    )

    private val ruFuture = mapOf(
        "Аз" to "буду",
        "Ти" to "будешь",
        "Той" to "будет",
        "Ние" to "будем",
        "Вие" to "будете",
        "Те" to "будут"
    )

    private val sumForms = mapOf(
        "Аз" to "съм",
        "Ти" to "си",
        "Той" to "е",
        "Ние" to "сме",
        "Вие" to "сте",
        "Те" to "са"
    )

    private val complementsBg = listOf(
        "студент",
        "учител",
        "лекар",
        "приятел",
        "дете",
        "вкъщи",
        "тук",
        "на работа",
        "в училище",
        "в града"
    )

    private val complementsRu = mapOf(
        "студент" to "студент",
        "учител" to "учитель",
        "лекар" to "врач",
        "приятел" to "друг",
        "дете" to "ребёнок",
        "вкъщи" to "дома",
        "тук" to "здесь",
        "на работа" to "на работе",
        "в училище" to "в школе",
        "в града" to "в городе"
    )

    private val objectNounsBg = listOf(
        "книга",
        "кафе",
        "вода",
        "хляб",
        "училище",
        "телефон",
        "чай",
        "сок"
    )

    private val objectNounsRu = mapOf(
        "книга" to "книга",
        "кафе" to "кофе",
        "вода" to "вода",
        "хляб" to "хлеб",
        "училище" to "школа",
        "телефон" to "телефон",
        "чай" to "чай",
        "сок" to "сок"
    )

    private val verbs = listOf(
        VerbForms(
            infinitive = "правя",
            present = mapOf(
                "Аз" to "правя",
                "Ти" to "правиш",
                "Той" to "прави",
                "Ние" to "правим",
                "Вие" to "правите",
                "Те" to "правят"
            ),
            ruPresent = mapOf(
                "Аз" to "делаю",
                "Ти" to "делаешь",
                "Той" to "делает",
                "Ние" to "делаем",
                "Вие" to "делаете",
                "Те" to "делают"
            ),
            past = mapOf(
                "Аз" to "правих",
                "Ти" to "прави",
                "Той" to "прави",
                "Ние" to "правихме",
                "Вие" to "правихте",
                "Те" to "правиха"
            ),
            ruPast = mapOf(
                "Аз" to "делал",
                "Ти" to "делал",
                "Той" to "делал",
                "Ние" to "делали",
                "Вие" to "делали",
                "Те" to "делали"
            )
        ),
        VerbForms(
            infinitive = "гледам",
            present = mapOf(
                "Аз" to "гледам",
                "Ти" to "гледаш",
                "Той" to "гледа",
                "Ние" to "гледаме",
                "Вие" to "гледате",
                "Те" to "гледат"
            ),
            ruPresent = mapOf(
                "Аз" to "смотрю",
                "Ти" to "смотришь",
                "Той" to "смотрит",
                "Ние" to "смотрим",
                "Вие" to "смотрите",
                "Те" to "смотрят"
            ),
            past = mapOf(
                "Аз" to "гледах",
                "Ти" to "гледа",
                "Той" to "гледа",
                "Ние" to "гледахме",
                "Вие" to "гледахте",
                "Те" to "гледаха"
            ),
            ruPast = mapOf(
                "Аз" to "смотрел",
                "Ти" to "смотрел",
                "Той" to "смотрел",
                "Ние" to "смотрели",
                "Вие" to "смотрели",
                "Те" to "смотрели"
            )
        ),
        VerbForms(
            infinitive = "отивам",
            present = mapOf(
                "Аз" to "отивам",
                "Ти" to "отиваш",
                "Той" to "отива",
                "Ние" to "отиваме",
                "Вие" to "отивате",
                "Те" to "отиват"
            ),
            ruPresent = mapOf(
                "Аз" to "иду",
                "Ти" to "идёшь",
                "Той" to "идёт",
                "Ние" to "идём",
                "Вие" to "идёте",
                "Те" to "идут"
            ),
            past = mapOf(
                "Аз" to "отивах",
                "Ти" to "отива",
                "Той" to "отива",
                "Ние" to "отивахме",
                "Вие" to "отивахте",
                "Те" to "отиваха"
            ),
            ruPast = mapOf(
                "Аз" to "шёл",
                "Ти" to "шёл",
                "Той" to "шёл",
                "Ние" to "шли",
                "Вие" to "шли",
                "Те" to "шли"
            )
        ),
        VerbForms(
            infinitive = "ям",
            present = mapOf(
                "Аз" to "ям",
                "Ти" to "ядеш",
                "Той" to "яде",
                "Ние" to "ядем",
                "Вие" to "ядете",
                "Те" to "ядат"
            ),
            ruPresent = mapOf(
                "Аз" to "ем",
                "Ти" to "ешь",
                "Той" to "ест",
                "Ние" to "едим",
                "Вие" to "едите",
                "Те" to "едят"
            ),
            past = mapOf(
                "Аз" to "ядох",
                "Ти" to "яде",
                "Той" to "яде",
                "Ние" to "ядохме",
                "Вие" to "ядохте",
                "Те" to "ядоха"
            ),
            ruPast = mapOf(
                "Аз" to "ел",
                "Ти" to "ел",
                "Той" to "ел",
                "Ние" to "ели",
                "Вие" to "ели",
                "Те" to "ели"
            )
        ),
        VerbForms(
            infinitive = "пия",
            present = mapOf(
                "Аз" to "пия",
                "Ти" to "пиеш",
                "Той" to "пие",
                "Ние" to "пием",
                "Вие" to "пиете",
                "Те" to "пият"
            ),
            ruPresent = mapOf(
                "Аз" to "пью",
                "Ти" to "пьёшь",
                "Той" to "пьёт",
                "Ние" to "пьём",
                "Вие" to "пьёте",
                "Те" to "пьют"
            ),
            past = mapOf(
                "Аз" to "пих",
                "Ти" to "пи",
                "Той" to "пи",
                "Ние" to "пихме",
                "Вие" to "пихте",
                "Те" to "пиха"
            ),
            ruPast = mapOf(
                "Аз" to "пил",
                "Ти" to "пил",
                "Той" to "пил",
                "Ние" to "пили",
                "Вие" to "пили",
                "Те" to "пили"
            )
        ),
        VerbForms(
            infinitive = "работя",
            present = mapOf(
                "Аз" to "работя",
                "Ти" to "работиш",
                "Той" to "работи",
                "Ние" to "работим",
                "Вие" to "работите",
                "Те" to "работят"
            ),
            ruPresent = mapOf(
                "Аз" to "работаю",
                "Ти" to "работаешь",
                "Той" to "работает",
                "Ние" to "работаем",
                "Вие" to "работаете",
                "Те" to "работают"
            ),
            past = mapOf(
                "Аз" to "работих",
                "Ти" to "работи",
                "Той" to "работи",
                "Ние" to "работихме",
                "Вие" to "работихте",
                "Те" to "работиха"
            ),
            ruPast = mapOf(
                "Аз" to "работал",
                "Ти" to "работал",
                "Той" to "работал",
                "Ние" to "работали",
                "Вие" to "работали",
                "Те" to "работали"
            )
        ),
        VerbForms(
            infinitive = "уча",
            present = mapOf(
                "Аз" to "уча",
                "Ти" to "учиш",
                "Той" to "учи",
                "Ние" to "учим",
                "Вие" to "учите",
                "Те" to "учат"
            ),
            ruPresent = mapOf(
                "Аз" to "учусь",
                "Ти" to "учишься",
                "Той" to "учится",
                "Ние" to "учимся",
                "Вие" to "учитесь",
                "Те" to "учатся"
            ),
            past = mapOf(
                "Аз" to "учих",
                "Ти" to "учи",
                "Той" to "учи",
                "Ние" to "учихме",
                "Вие" to "учихте",
                "Те" to "учиха"
            ),
            ruPast = mapOf(
                "Аз" to "учился",
                "Ти" to "учился",
                "Той" to "учился",
                "Ние" to "учились",
                "Вие" to "учились",
                "Те" to "учились"
            )
        ),
        VerbForms(
            infinitive = "говоря",
            present = mapOf(
                "Аз" to "говоря",
                "Ти" to "говориш",
                "Той" to "говори",
                "Ние" to "говорим",
                "Вие" to "говорите",
                "Те" to "говорят"
            ),
            ruPresent = mapOf(
                "Аз" to "говорю",
                "Ти" to "говоришь",
                "Той" to "говорит",
                "Ние" to "говорим",
                "Вие" to "говорите",
                "Те" to "говорят"
            ),
            past = mapOf(
                "Аз" to "говорих",
                "Ти" to "говори",
                "Той" to "говори",
                "Ние" to "говорихме",
                "Вие" to "говорихте",
                "Те" to "говориха"
            ),
            ruPast = mapOf(
                "Аз" to "говорил",
                "Ти" to "говорил",
                "Той" to "говорил",
                "Ние" to "говорили",
                "Вие" to "говорили",
                "Те" to "говорили"
            )
        ),
        VerbForms(
            infinitive = "виждам",
            present = mapOf(
                "Аз" to "виждам",
                "Ти" to "виждаш",
                "Той" to "вижда",
                "Ние" to "виждаме",
                "Вие" to "виждате",
                "Те" to "виждат"
            ),
            ruPresent = mapOf(
                "Аз" to "вижу",
                "Ти" to "видишь",
                "Той" to "видит",
                "Ние" to "видим",
                "Вие" to "видите",
                "Те" to "видят"
            ),
            past = mapOf(
                "Аз" to "видях",
                "Ти" to "видя",
                "Той" to "видя",
                "Ние" to "видяхме",
                "Вие" to "видяхте",
                "Те" to "видяха"
            ),
            ruPast = mapOf(
                "Аз" to "видел",
                "Ти" to "видел",
                "Той" to "видел",
                "Ние" to "видели",
                "Вие" to "видели",
                "Те" to "видели"
            )
        ),
        VerbForms(
            infinitive = "искам",
            present = mapOf(
                "Аз" to "искам",
                "Ти" to "искаш",
                "Той" to "иска",
                "Ние" to "искаме",
                "Вие" to "искате",
                "Те" to "искат"
            ),
            ruPresent = mapOf(
                "Аз" to "хочу",
                "Ти" to "хочешь",
                "Той" to "хочет",
                "Ние" to "хотим",
                "Вие" to "хотите",
                "Те" to "хотят"
            ),
            past = mapOf(
                "Аз" to "исках",
                "Ти" to "иска",
                "Той" to "иска",
                "Ние" to "искахме",
                "Вие" to "искахте",
                "Те" to "искаха"
            ),
            ruPast = mapOf(
                "Аз" to "хотел",
                "Ти" to "хотел",
                "Той" to "хотел",
                "Ние" to "хотели",
                "Вие" to "хотели",
                "Те" to "хотели"
            )
        )
    )

    private val lesson4Items = listOf(
        Lesson4Item(Lesson4Item.Type.NOUN, "книга", listOf("книга")),
        Lesson4Item(Lesson4Item.Type.NOUN, "эта книга", listOf("книгата")),
        Lesson4Item(Lesson4Item.Type.NOUN, "женщина", listOf("жена")),
        Lesson4Item(Lesson4Item.Type.NOUN, "эта женщина", listOf("жената")),
        Lesson4Item(Lesson4Item.Type.NOUN, "ребёнок", listOf("дете")),
        Lesson4Item(Lesson4Item.Type.NOUN, "этот ребёнок", listOf("детето")),
        Lesson4Item(Lesson4Item.Type.VERB, "есть", listOf("да", "ям")),
        Lesson4Item(Lesson4Item.Type.VERB, "пить", listOf("да", "пия")),
        Lesson4Item(Lesson4Item.Type.VERB, "я хочу есть", listOf("Аз", "искам", "да", "ям")),
        Lesson4Item(Lesson4Item.Type.VERB, "я хочу пить", listOf("Аз", "искам", "да", "пия")),
        Lesson4Item(Lesson4Item.Type.NOUN, "я хочу книгу", listOf("Аз", "искам", "книга")),
        Lesson4Item(Lesson4Item.Type.NOUN, "я хочу эту книгу", listOf("Аз", "искам", "книгата"))
    )

    private val lesson5Subjects = listOf(
        "Аз", "Ти", "Той", "Ние", "Вие", "Те"
    )

    private val lesson5SubjectRu = mapOf(
        "Аз" to "я",
        "Ти" to "ты",
        "Той" to "он",
        "Ние" to "мы",
        "Вие" to "вы",
        "Те" to "они"
    )

    private val canForms = mapOf(
        "Аз" to "мога",
        "Ти" to "можеш",
        "Той" to "може",
        "Ние" to "можем",
        "Вие" to "можете",
        "Те" to "могат"
    )

    private val canRuForms = mapOf(
        "Аз" to "могу",
        "Ти" to "можешь",
        "Той" to "может",
        "Ние" to "можем",
        "Вие" to "можете",
        "Те" to "могут"
    )

    private val wantForms = mapOf(
        "Аз" to "искам",
        "Ти" to "искаш",
        "Той" to "иска",
        "Ние" to "искаме",
        "Вие" to "искате",
        "Те" to "искат"
    )

    private val wantRuForms = mapOf(
        "Аз" to "хочу",
        "Ти" to "хочешь",
        "Той" to "хочет",
        "Ние" to "хотим",
        "Вие" to "хотите",
        "Те" to "хотят"
    )

    private val mustRuForms = mapOf(
        "Аз" to "мне нужно",
        "Ти" to "тебе нужно",
        "Той" to "ему нужно",
        "Ние" to "нам нужно",
        "Вие" to "вам нужно",
        "Те" to "им нужно"
    )

    private val lesson5ObjectsByInfinitive = mapOf(
        "ям" to listOf("хляб" to "хлеб"),
        "пия" to listOf(
            "вода" to "воду",
            "кафе" to "кофе"
        ),
        "гледам" to listOf("телевизия" to "телевизор"),
        "уча" to listOf("български" to "болгарский")
    )

    private val lesson6Prepositions = listOf(
        "в",
        "на",
        "с",
        "при"
    )

    private val lesson6PlacesByPreposition = mapOf(
        "в" to listOf(
            "града" to "в городе",
            "училище" to "в школе",
            "къщата" to "в доме"
        ),
        "на" to listOf(
            "работа" to "на работе"
        ),
        "с" to listOf(
            "приятеля" to "с другом",
            "учителя" to "с учителем"
        ),
        "при" to listOf(
            "лекаря" to "у врача"
        )
    )

    private val lesson8Templates = listOf(
        Lesson8Template(
            ru = "Он старше меня",
            correctWords = listOf("Той", "е", "по-стар", "от", "мен")
        ),
        Lesson8Template(
            ru = "Я младше его",
            correctWords = listOf("Аз", "съм", "по-млад", "от", "него")
        ),
        Lesson8Template(
            ru = "Эта книга интереснее той",
            correctWords = listOf("Тази", "книга", "е", "по-интересна", "от", "онази")
        ),
        Lesson8Template(
            ru = "Моя машина быстрее твоей",
            correctWords = listOf("Моята", "кола", "е", "по-бърза", "от", "твоята")
        ),
        Lesson8Template(
            ru = "Наш дом больше вашего",
            correctWords = listOf("Нашата", "къща", "е", "по-голяма", "от", "вашата")
        ),
        Lesson8Template(
            ru = "Мой брат выше меня",
            correctWords = listOf("Моят", "брат", "е", "по-висок", "от", "мен")
        ),
        Lesson8Template(
            ru = "Телефон дороже часов",
            correctWords = listOf("Телефонът", "е", "по-скъп", "от", "часовника")
        ),
        Lesson8Template(
            ru = "Книга лучше фильма",
            correctWords = listOf("Книгата", "е", "по-добра", "от", "филма")
        ),
        Lesson8Template(
            ru = "Это лучшая книга",
            correctWords = listOf("Това", "е", "най-добрата", "книга")
        ),
        Lesson8Template(
            ru = "Он лучший ученик",
            correctWords = listOf("Той", "е", "най-добрият", "ученик")
        ),
        Lesson8Template(
            ru = "Это самая дорогая машина",
            correctWords = listOf("Това", "е", "най-скъпата", "кола")
        ),
        Lesson8Template(
            ru = "Это самый красивый дом",
            correctWords = listOf("Това", "е", "най-красивият", "дом")
        ),
        Lesson8Template(
            ru = "Это самый интересный фильм",
            correctWords = listOf("Това", "е", "най-интересният", "филм")
        ),
        Lesson8Template(
            ru = "Это лучший день",
            correctWords = listOf("Това", "е", "най-добрият", "ден")
        ),
        Lesson8Template(
            ru = "Это лучший день в моей жизни",
            correctWords = listOf("Това", "е", "най-хубавият", "ден", "в", "живота", "ми")
        )
    )

    private val lesson8WordPool = listOf(
        "Това", "Той", "Аз", "Тази",
        "е", "съм", "от", "в",
        "мен", "него", "ми",
        "по-стар", "по-млад", "по-интересна", "по-бърза", "по-голяма", "по-висок", "по-скъп", "по-добра",
        "най-добрата", "най-добрият", "най-скъпата", "най-красивият", "най-интересният", "най-хубавият",
        "книга", "книгата", "кола", "къща", "дом", "брат", "ученик", "часовника", "филма", "филм", "ден", "живота",
        "Моята", "твоята", "Нашата", "вашата", "Телефонът", "Моят", "онази"
    )

    fun getLessonSession(lessonId: Int): LessonSession {
        return when (lessonId) {
            1 -> LessonSession(
                lessonId = 1,
                lessonTitle = "Урок 1",
                exercises = generateLesson1Exercises()
            )

            2 -> LessonSession(
                lessonId = 2,
                lessonTitle = "Глагол \"съм\"",
                exercises = generateLesson2Exercises()
            )

            3 -> LessonSession(
                lessonId = 3,
                lessonTitle = "Прошедшее время",
                exercises = generateLesson3Exercises()
            )

            4 -> LessonSession(
                lessonId = 4,
                lessonTitle = "Предмет или действие",
                exercises = generateLesson4Exercises()
            )

            5 -> LessonSession(
                lessonId = 5,
                lessonTitle = "Могу, хочу, должен",
                exercises = generateLesson5Exercises()
            )

            6 -> LessonSession(
                lessonId = 6,
                lessonTitle = "Предлоги и существительные",
                exercises = generateLesson6Exercises()
            )

            8 -> LessonSession(
                lessonId = 8,
                lessonTitle = "Сравнение",
                exercises = generateLesson8Exercises()
            )

            else -> LessonSession(
                lessonId = lessonId,
                lessonTitle = "Урок $lessonId",
                exercises = generateLesson1Exercises()
            )
        }
    }

    private fun generateLesson1Exercises(): List<LessonExercise> {
        return (1..100).map { id ->
            generateLesson1Exercise(id)
        }
    }

    private fun generateLesson1Exercise(id: Int): LessonExercise {
        val sentenceType = randomLesson1SentenceType()
        val verb = verbs.random()

        val subject = when (sentenceType) {
            Lesson1SentenceType.PRESENT,
            Lesson1SentenceType.PRESENT_NEGATIVE,
            Lesson1SentenceType.FUTURE,
            Lesson1SentenceType.FUTURE_NEGATIVE -> subjects.random()

            Lesson1SentenceType.PRESENT_QUESTION,
            Lesson1SentenceType.FUTURE_QUESTION -> questionSubjects.random()
        }

        val bgVerb = verb.present.getValue(subject)
        val ruSubject = subjectRu.getValue(subject)
        val ruVerb = verb.ruPresent.getValue(subject)
        val ruFutureVerb = ruFuture.getValue(subject)

        val correctWords = when (sentenceType) {
            Lesson1SentenceType.PRESENT -> listOf(subject, bgVerb)
            Lesson1SentenceType.PRESENT_QUESTION -> listOf(subject, bgVerb, "ли")
            Lesson1SentenceType.PRESENT_NEGATIVE -> listOf(subject, "не", bgVerb)
            Lesson1SentenceType.FUTURE -> listOf(subject, "ще", bgVerb)
            Lesson1SentenceType.FUTURE_QUESTION -> listOf(subject, "ще", bgVerb, "ли")
            Lesson1SentenceType.FUTURE_NEGATIVE -> listOf(subject, "няма", "да", bgVerb)
        }

        val sourceText = when (sentenceType) {
            Lesson1SentenceType.PRESENT ->
                "$ruSubject $ruVerb"

            Lesson1SentenceType.PRESENT_QUESTION ->
                "$ruSubject $ruVerb?"

            Lesson1SentenceType.PRESENT_NEGATIVE ->
                "$ruSubject не $ruVerb"

            Lesson1SentenceType.FUTURE ->
                "$ruSubject $ruFutureVerb ${toRussianInfinitive(verb.infinitive)}"

            Lesson1SentenceType.FUTURE_QUESTION ->
                "$ruSubject $ruFutureVerb ${toRussianInfinitive(verb.infinitive)}?"

            Lesson1SentenceType.FUTURE_NEGATIVE ->
                "$ruSubject не $ruFutureVerb ${toRussianInfinitive(verb.infinitive)}"
        }

        val distractors = buildLesson1Distractors(correctWords)

        val availableWords = buildAvailableWords(
            correctWords = correctWords,
            distractorPool = distractors,
            totalWords = 8
        )

        Log.d("LessonRepo", "Exercise #$id: correct=$correctWords, available=$availableWords")

        return LessonExercise(
            id = id,
            sourceText = sourceText,
            instruction = "Переведите предложение",
            correctAnswerWords = correctWords,
            availableWords = availableWords,
            hint = buildHint(correctWords)
        )
    }

    private fun randomLesson1SentenceType(): Lesson1SentenceType {
        return when ((1..100).random()) {
            in 1..20 -> Lesson1SentenceType.PRESENT
            in 21..35 -> Lesson1SentenceType.PRESENT_QUESTION
            in 36..50 -> Lesson1SentenceType.PRESENT_NEGATIVE
            in 51..70 -> Lesson1SentenceType.FUTURE
            in 71..85 -> Lesson1SentenceType.FUTURE_QUESTION
            else -> Lesson1SentenceType.FUTURE_NEGATIVE
        }
    }

    private fun buildLesson1Distractors(correctWords: List<String>): List<String> {
        val pool = (
                subjects +
                        listOf("ли", "не", "ще", "няма", "да") +
                        verbs.flatMap { it.present.values }
                ).distinct()

        val distractors = pool
            .filterNot { it in correctWords }
            .distinct()
            .shuffled()

        require(distractors.size >= 6) {
            "Not enough distractors! Got ${distractors.size}, need at least 6"
        }

        return distractors
    }

    private fun generateLesson2Exercises(): List<LessonExercise> {
        return (1..100).map { id ->
            generateLesson2Exercise(id)
        }
    }

    private fun generateLesson2Exercise(id: Int): LessonExercise {
        val type = when ((1..100).random()) {
            in 1..25 -> Lesson2SentenceType.PRESENT
            in 26..45 -> Lesson2SentenceType.QUESTION
            in 46..60 -> Lesson2SentenceType.NEGATIVE
            in 61..78 -> Lesson2SentenceType.THIS_IS
            in 79..90 -> Lesson2SentenceType.THIS_IS_QUESTION
            else -> Lesson2SentenceType.THIS_IS_NEGATIVE
        }

        val subject = subjects.random()
        val verb = sumForms.getValue(subject)
        val ruSubject = subjectRu.getValue(subject)

        val complementBg = complementsBg.random()
        val complementRu = complementsRu.getValue(complementBg)

        val nounBg = objectNounsBg.random()
        val nounRu = objectNounsRu.getValue(nounBg)

        val correctWords = when (type) {
            Lesson2SentenceType.PRESENT ->
                listOf(subject, verb, complementBg)

            Lesson2SentenceType.QUESTION ->
                listOf(subject, complementBg, "ли", verb)

            Lesson2SentenceType.NEGATIVE ->
                listOf(subject, "не", verb, complementBg)

            Lesson2SentenceType.THIS_IS ->
                listOf("Това", "е", nounBg)

            Lesson2SentenceType.THIS_IS_QUESTION ->
                listOf("Това", nounBg, "ли", "е")

            Lesson2SentenceType.THIS_IS_NEGATIVE ->
                listOf("Това", "не", "е", nounBg)
        }

        val sourceText = when (type) {
            Lesson2SentenceType.PRESENT ->
                "$ruSubject $complementRu"

            Lesson2SentenceType.QUESTION ->
                "$ruSubject $complementRu?"

            Lesson2SentenceType.NEGATIVE ->
                "$ruSubject не $complementRu"

            Lesson2SentenceType.THIS_IS ->
                "Это $nounRu"

            Lesson2SentenceType.THIS_IS_QUESTION ->
                "Это $nounRu?"

            Lesson2SentenceType.THIS_IS_NEGATIVE ->
                "Это не $nounRu"
        }

        val distractors = buildLesson2Distractors(
            subject = subject,
            correctVerb = verb,
            correctComplement = complementBg,
            type = type,
            correctNoun = nounBg
        )

        val availableWords = buildAvailableWords(
            correctWords = correctWords,
            distractorPool = distractors,
            totalWords = 8
        )

        Log.d("LessonRepo", "Lesson2 Exercise #$id: correct=$correctWords, available=$availableWords")

        return LessonExercise(
            id = id,
            sourceText = sourceText,
            instruction = "Переведите предложение",
            correctAnswerWords = correctWords,
            availableWords = availableWords,
            hint = when (type) {
                Lesson2SentenceType.QUESTION ->
                    "💡 с \"съм\" вопрос часто строится так: слово + ли + съм"

                Lesson2SentenceType.THIS_IS ->
                    "💡 это → Това е ..."

                Lesson2SentenceType.THIS_IS_QUESTION ->
                    "💡 вопрос: Това + слово + ли + е"

                Lesson2SentenceType.THIS_IS_NEGATIVE ->
                    "💡 это не → Това не е ..."

                else -> buildHint(correctWords)
            }
        )
    }

    private fun buildLesson2Distractors(
        subject: String,
        correctVerb: String,
        correctComplement: String,
        type: Lesson2SentenceType,
        correctNoun: String
    ): List<String> {
        val correctWords = when (type) {
            Lesson2SentenceType.PRESENT ->
                listOf(subject, correctVerb, correctComplement)

            Lesson2SentenceType.QUESTION ->
                listOf(subject, correctComplement, "ли", correctVerb)

            Lesson2SentenceType.NEGATIVE ->
                listOf(subject, "не", correctVerb, correctComplement)

            Lesson2SentenceType.THIS_IS ->
                listOf("Това", "е", correctNoun)

            Lesson2SentenceType.THIS_IS_QUESTION ->
                listOf("Това", correctNoun, "ли", "е")

            Lesson2SentenceType.THIS_IS_NEGATIVE ->
                listOf("Това", "не", "е", correctNoun)
        }

        val subjectDistractors = subjects.filterNot { it == subject }

        val verbDistractors = sumForms.values
            .filterNot { it == correctVerb }
            .distinct()

        val complementDistractors = complementsBg.filterNot { it == correctComplement }
        val nounDistractors = objectNounsBg.filterNot { it == correctNoun }

        val grammarDistractors = listOf("не", "ли", "Това")

        return (
                subjectDistractors +
                        verbDistractors +
                        complementDistractors +
                        nounDistractors +
                        grammarDistractors
                )
            .distinct()
            .filterNot { it in correctWords }
            .shuffled()
    }

    private fun generateLesson3Exercises(): List<LessonExercise> {
        return (1..100).map { id ->
            generateLesson3Exercise(id)
        }
    }

    private fun generateLesson3Exercise(id: Int): LessonExercise {
        val type = if ((1..100).random() <= 70) {
            Lesson3SentenceType.PAST
        } else {
            Lesson3SentenceType.PAST_NEGATIVE
        }

        val verb = verbs.random()
        val subject = subjects.random()

        val bgVerb = verb.past.getValue(subject)
        val ruSubject = subjectRu.getValue(subject)
        val ruVerb = verb.ruPast.getValue(subject)

        val correctWords = when (type) {
            Lesson3SentenceType.PAST ->
                listOf(subject, bgVerb)

            Lesson3SentenceType.PAST_NEGATIVE ->
                listOf(subject, "не", bgVerb)
        }

        val sourceText = when (type) {
            Lesson3SentenceType.PAST ->
                "$ruSubject $ruVerb"

            Lesson3SentenceType.PAST_NEGATIVE ->
                "$ruSubject не $ruVerb"
        }

        val distractorPool = (
                subjects +
                        listOf("не") +
                        verbs.flatMap { it.past.values }
                ).distinct()

        val availableWords = buildAvailableWords(
            correctWords = correctWords,
            distractorPool = distractorPool,
            totalWords = 8
        )

        Log.d("LessonRepo", "Lesson3 Exercise #$id: correct=$correctWords, available=$availableWords")

        return LessonExercise(
            id = id,
            sourceText = sourceText,
            instruction = "Переведите предложение",
            correctAnswerWords = correctWords,
            availableWords = availableWords,
            hint = buildHint(correctWords)
        )
    }

    private fun generateLesson4Exercises(): List<LessonExercise> {
        return (1..40).map { id ->
            generateLesson4Exercise(id)
        }
    }

    private fun generateLesson4Exercise(id: Int): LessonExercise {
        val item = lesson4Items.random()
        val correctWords = item.correctWords

        val distractorPool = listOf(
            "Аз", "Ти", "Той",
            "да", "не",
            "искам", "обичам",
            "книга", "книгата",
            "жена", "жената",
            "дете", "детето",
            "ям", "пия", "работя"
        )

        val availableWords = buildAvailableWords(
            correctWords = correctWords,
            distractorPool = distractorPool,
            totalWords = 8
        )

        val hint = when {
            item.type == Lesson4Item.Type.NOUN && correctWords.any { it.endsWith("та") || it.endsWith("то") } ->
                "💡 это конкретный предмет → добавь окончание"

            "да" in correctWords ->
                "💡 действие → используй \"да\""

            else -> null
        }

        return LessonExercise(
            id = id,
            sourceText = item.ru,
            instruction = "Переведите предложение",
            correctAnswerWords = correctWords,
            availableWords = availableWords,
            hint = hint
        )
    }

    private fun generateLesson5Exercises(): List<LessonExercise> {
        return (1..60).map { id ->
            generateLesson5Exercise(id)
        }
    }

    private fun generateLesson5Exercise(id: Int): LessonExercise {
        val sentenceType = randomLesson5SentenceType()
        val modalType = randomLesson5ModalType()

        val subject = lesson5Subjects.random()
        val subjectRuText = lesson5SubjectRu.getValue(subject)

        val verb = verbs.random()
        val actionBg = verb.present.getValue(subject)
        val actionRu = toRussianInfinitive(verb.infinitive)

        val possibleObjects = lesson5ObjectsByInfinitive[verb.infinitive].orEmpty()
        val objectPair = if (possibleObjects.isNotEmpty() && (1..100).random() <= 55) {
            possibleObjects.random()
        } else {
            null
        }
        val objectBg = objectPair?.first
        val objectRu = objectPair?.second

        val modalBg = when (modalType) {
            Lesson5ModalType.CAN -> canForms.getValue(subject)
            Lesson5ModalType.WANT -> wantForms.getValue(subject)
            Lesson5ModalType.MUST -> mustForm
        }

        val modalRu = when (modalType) {
            Lesson5ModalType.CAN -> canRuForms.getValue(subject)
            Lesson5ModalType.WANT -> wantRuForms.getValue(subject)
            Lesson5ModalType.MUST -> mustRuForms.getValue(subject)
        }

        val correctWords = buildList {
            add(subject)

            if (sentenceType == Lesson5SentenceType.NEGATIVE) {
                add("не")
            }

            add(modalBg)

            if (sentenceType == Lesson5SentenceType.QUESTION) {
                add("ли")
            }

            add("да")
            add(actionBg)

            if (objectBg != null) {
                add(objectBg)
            }
        }

        val sourceText = buildString {
            append(subjectRuText)
            append(" ")

            if (sentenceType == Lesson5SentenceType.NEGATIVE) {
                append("не ")
            }

            append(modalRu)
            append(" ")
            append(actionRu)

            if (objectRu != null) {
                append(" ")
                append(objectRu)
            }

            if (sentenceType == Lesson5SentenceType.QUESTION) {
                append("?")
            }
        }

        val distractorPool = (
                lesson5Subjects +
                        listOf("не", "да", "ли") +
                        canForms.values +
                        wantForms.values +
                        listOf(mustForm) +
                        verbs.flatMap { it.present.values } +
                        lesson5ObjectsByInfinitive.values.flatten().map { it.first }
                ).distinct()

        val availableWords = buildAvailableWords(
            correctWords = correctWords,
            distractorPool = distractorPool,
            totalWords = 8
        )

        val hint = when {
            "ли" in correctWords ->
                "💡 вопрос → \"ли\" ставится после глагола"

            modalType == Lesson5ModalType.CAN ->
                "💡 могу → форма на \"мога\" + да"

            modalType == Lesson5ModalType.WANT ->
                "💡 хочу → форма на \"искам\" + да"

            modalType == Lesson5ModalType.MUST ->
                "💡 нужно → трябва + да"

            "не" in correctWords ->
                "💡 \"не\" ставится перед глаголом"

            else -> null
        }

        return LessonExercise(
            id = id,
            sourceText = sourceText,
            instruction = "Переведите предложение",
            correctAnswerWords = correctWords,
            availableWords = availableWords,
            hint = hint
        )
    }

    private fun generateLesson6Exercises(): List<LessonExercise> {
        return (1..60).map { id ->
            generateLesson6Exercise(id)
        }
    }

    private fun generateLesson6Exercise(id: Int): LessonExercise {
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

        val availableWords = buildAvailableWords(
            correctWords = correctWords,
            distractorPool = distractorPool,
            totalWords = 8
        )

        return LessonExercise(
            id = id,
            sourceText = sourceText,
            instruction = "Переведите предложение",
            correctAnswerWords = correctWords,
            availableWords = availableWords,
            hint = "💡 используй предлог + существительное"
        )
    }

    private fun generateLesson8Exercises(): List<LessonExercise> {
        return (1..60).map { id ->
            generateLesson8Exercise(id)
        }
    }

    private fun generateLesson8Exercise(id: Int): LessonExercise {
        val template = lesson8Templates.random()

        val availableWords = buildAvailableWords(
            correctWords = template.correctWords,
            distractorPool = lesson8WordPool,
            totalWords = 8
        )

        val hint = when {
            template.correctWords.any { it.startsWith("по-") } ->
                "💡 сравнение: по- + прилагательное + от"

            template.correctWords.any { it.startsWith("най-") } ->
                "💡 превосходная степень: най- + прилагательное"

            "от" in template.correctWords ->
                "💡 \"от\" означает «чем»"

            else -> null
        }

        return LessonExercise(
            id = id,
            sourceText = template.ru,
            instruction = "Переведите предложение",
            correctAnswerWords = template.correctWords,
            availableWords = availableWords,
            hint = hint
        )
    }

    private fun randomLesson5SentenceType(): Lesson5SentenceType {
        return when ((1..100).random()) {
            in 1..40 -> Lesson5SentenceType.POSITIVE
            in 41..70 -> Lesson5SentenceType.NEGATIVE
            else -> Lesson5SentenceType.QUESTION
        }
    }

    private fun randomLesson5ModalType(): Lesson5ModalType {
        return when ((1..100).random()) {
            in 1..40 -> Lesson5ModalType.CAN
            in 41..75 -> Lesson5ModalType.WANT
            else -> Lesson5ModalType.MUST
        }
    }

    private fun buildAvailableWords(
        correctWords: List<String>,
        distractorPool: List<String>,
        totalWords: Int = 8
    ): List<String> {
        val uniqueCorrectWords = correctWords.distinct()

        require(uniqueCorrectWords.isNotEmpty()) {
            "Correct words must not be empty"
        }

        require(uniqueCorrectWords.size <= totalWords) {
            "Correct words count (${uniqueCorrectWords.size}) can't be greater than totalWords ($totalWords)"
        }

        val distractors = distractorPool
            .filterNot { it in uniqueCorrectWords }
            .distinct()
            .shuffled()
            .take(totalWords - uniqueCorrectWords.size)

        val result = (uniqueCorrectWords + distractors).shuffled()

        require(result.size == totalWords) {
            "Available words size must be $totalWords, but was ${result.size}. Correct=$uniqueCorrectWords, distractors=$distractors"
        }

        require(uniqueCorrectWords.all { it in result }) {
            "Not all correct words were added. Correct=$uniqueCorrectWords, result=$result"
        }

        return result
    }

    private fun toRussianInfinitive(bgInfinitive: String): String {
        return when (bgInfinitive) {
            "правя" -> "делать"
            "гледам" -> "смотреть"
            "отивам" -> "идти"
            "ям" -> "есть"
            "пия" -> "пить"
            "работя" -> "работать"
            "уча" -> "учиться"
            "говоря" -> "говорить"
            "виждам" -> "видеть"
            "искам" -> "хотеть"
            else -> "делать"
        }
    }

    private fun buildHint(correctWords: List<String>): String? {
        return when {
            "няма" in correctWords && "да" in correctWords ->
                "💡 няма + да + глагол"

            "ще" in correctWords ->
                "💡 ще + глагол"

            "ли" in correctWords ->
                "💡 \"ли\" ставится после глагола"

            "не" in correctWords ->
                "💡 \"не\" ставится перед глаголом"

            else -> null
        }
    }
}