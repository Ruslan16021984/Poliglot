package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import com.carbit3333333.oiiglot_bulgary.model.Lesson4Item
import com.carbit3333333.oiiglot_bulgary.model.VerbForms

internal const val mustForm = "трябва"

internal enum class Lesson1SentenceType {
PRESENT,
PRESENT_QUESTION,
PRESENT_NEGATIVE,
FUTURE,
FUTURE_QUESTION,
FUTURE_NEGATIVE
}

internal enum class Lesson2SentenceType {
PRESENT,
QUESTION,
NEGATIVE,
THIS_IS,
THIS_IS_QUESTION,
THIS_IS_NEGATIVE
}

internal enum class Lesson3SentenceType {
PAST,
PAST_NEGATIVE
}

internal enum class Lesson5SentenceType {
POSITIVE,
NEGATIVE,
QUESTION
}

internal enum class Lesson5ModalType {
CAN,
WANT,
MUST
}

internal data class Lesson8Template(
val ru: String,
val correctWords: List<String>
)

internal data class Lesson7Template(
val ru: String,
val correctWords: List<String>
)


internal val subjects = listOf(
    "Аз", "Ти", "Той", "Ние", "Вие", "Те"
)

internal val questionSubjects = listOf(
    "Ти", "Той", "Вие", "Те"
)

internal val subjectRu = mapOf(
    "Аз" to "Я",
    "Ти" to "Ты",
    "Той" to "Он",
    "Ние" to "Мы",
    "Вие" to "Вы",
    "Те" to "Они"
)

internal val ruFuture = mapOf(
    "Аз" to "буду",
    "Ти" to "будешь",
    "Той" to "будет",
    "Ние" to "будем",
    "Вие" to "будете",
    "Те" to "будут"
)

internal val sumForms = mapOf(
    "Аз" to "съм",
    "Ти" to "си",
    "Той" to "е",
    "Ние" to "сме",
    "Вие" to "сте",
    "Те" to "са"
)

internal val complementsBg = listOf(
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

internal val complementsRu = mapOf(
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

internal val objectNounsBg = listOf(
    "книга",
    "кафе",
    "вода",
    "хляб",
    "училище",
    "телефон",
    "чай",
    "сок"
)

internal val objectNounsRu = mapOf(
    "книга" to "книга",
    "кафе" to "кофе",
    "вода" to "вода",
    "хляб" to "хлеб",
    "училище" to "школа",
    "телефон" to "телефон",
    "чай" to "чай",
    "сок" to "сок"
)

internal val verbs = listOf(
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

internal val lesson4Items = listOf(
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

internal val lesson5Subjects = listOf(
    "Аз", "Ти", "Той", "Ние", "Вие", "Те"
)

internal val lesson5SubjectRu = mapOf(
    "Аз" to "я",
    "Ти" to "ты",
    "Той" to "он",
    "Ние" to "мы",
    "Вие" to "вы",
    "Те" to "они"
)

internal val canForms = mapOf(
    "Аз" to "мога",
    "Ти" to "можеш",
    "Той" to "може",
    "Ние" to "можем",
    "Вие" to "можете",
    "Те" to "могат"
)

internal val canRuForms = mapOf(
    "Аз" to "могу",
    "Ти" to "можешь",
    "Той" to "может",
    "Ние" to "можем",
    "Вие" to "можете",
    "Те" to "могут"
)

internal val wantForms = mapOf(
    "Аз" to "искам",
    "Ти" to "искаш",
    "Той" to "иска",
    "Ние" to "искаме",
    "Вие" to "искате",
    "Те" to "искат"
)

internal val wantRuForms = mapOf(
    "Аз" to "хочу",
    "Ти" to "хочешь",
    "Той" to "хочет",
    "Ние" to "хотим",
    "Вие" to "хотите",
    "Те" to "хотят"
)

internal val mustRuForms = mapOf(
    "Аз" to "мне нужно",
    "Ти" to "тебе нужно",
    "Той" to "ему нужно",
    "Ние" to "нам нужно",
    "Вие" to "вам нужно",
    "Те" to "им нужно"
)

internal val lesson5ObjectsByInfinitive = mapOf(
    "ям" to listOf("хляб" to "хлеб"),
    "пия" to listOf(
        "вода" to "воду",
        "кафе" to "кофе"
    ),
    "гледам" to listOf("телевизия" to "телевизор"),
    "уча" to listOf("български" to "болгарский")
)

internal val lesson6Prepositions = listOf(
    "в",
    "на",
    "с",
    "при"
)

internal val lesson6PlacesByPreposition = mapOf(
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

internal val lesson7Templates = listOf(
    Lesson7Template(
        ru = "Это моя книга",
        correctWords = listOf("Това", "е", "моята", "книга")
    ),
    Lesson7Template(
        ru = "Это мой друг",
        correctWords = listOf("Това", "е", "моят", "приятел")
    ),
    Lesson7Template(
        ru = "Это наш ребёнок",
        correctWords = listOf("Това", "е", "нашето", "дете")
    ),
    Lesson7Template(
        ru = "Это наши книги",
        correctWords = listOf("Това", "са", "нашите", "книги")
    ),
    Lesson7Template(
        ru = "У меня есть моя книга",
        correctWords = listOf("Аз", "имам", "моята", "книга")
    ),
    Lesson7Template(
        ru = "У меня есть эта книга",
        correctWords = listOf("Аз", "имам", "книгата")
    ),
    Lesson7Template(
        ru = "Я беру свою книгу",
        correctWords = listOf("Аз", "взимам", "моята", "книга")
    ),
    Lesson7Template(
        ru = "Я вижу свою книгу",
        correctWords = listOf("Аз", "виждам", "моята", "книга")
    ),
    Lesson7Template(
        ru = "Мы любим нашего ребёнка",
        correctWords = listOf("Ние", "обичаме", "нашето", "дете")
    ),
    Lesson7Template(
        ru = "Я даю тебе свою книгу",
        correctWords = listOf("Давам", "ти", "моята", "книга")
    ),
    Lesson7Template(
        ru = "Я даю тебе книгу",
        correctWords = listOf("Давам", "ти", "книгата")
    ),
    Lesson7Template(
        ru = "Это эта книга",
        correctWords = listOf("Това", "е", "книгата")
    )
)

internal val lesson7WordPool = listOf(
    "Това", "е", "са",
    "Аз", "Ние", "ти",
    "имам", "взимам", "виждам", "обичаме", "Давам",
    "моя", "моята", "моят", "наше", "нашето", "нашите",
    "книга", "книгата", "приятел", "дете", "книги"
)

internal val lesson8Templates = listOf(
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

internal val lesson8WordPool = listOf(
    "Това", "Той", "Аз", "Тази",
    "е", "съм", "от", "в",
    "мен", "него", "ми",
    "по-стар", "по-млад", "по-интересна", "по-бърза", "по-голяма", "по-висок", "по-скъп", "по-добра",
    "най-добрата", "най-добрият", "най-скъпата", "най-красивият", "най-интересният", "най-хубавият",
    "книга", "книгата", "кола", "къща", "дом", "брат", "ученик", "часовника", "филма", "филм", "ден", "живота",
    "Моята", "твоята", "Нашата", "вашата", "Телефонът", "Моят", "онази"
)
