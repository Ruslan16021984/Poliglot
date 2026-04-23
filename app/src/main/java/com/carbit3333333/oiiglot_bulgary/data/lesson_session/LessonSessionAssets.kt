package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import android.content.Context
import com.carbit3333333.oiiglot_bulgary.model.Lesson4Item
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
internal data class LessonTemplateAsset(
    val ru: String,
    val bgWords: List<String>,
    val hint: String? = null
)

@Serializable
internal data class Lesson1SubjectAsset(
    val bg: String,
    val ru: String,
    val futureAuxRu: String,
    val haveRuPresent: String = "",
    val haveRuFuture: String = ""
)

@Serializable
internal data class Lesson1ObjectAsset(
    val bg: String,
    val ru: String
)

@Serializable
internal data class Lesson1VerbAsset(
    val kind: String = "default",
    val ruInfinitive: String,
    val formsBg: Map<String, String>,
    val formsRu: Map<String, String>,
    val objects: List<Lesson1ObjectAsset>
)

@Serializable
internal data class Lesson1TemplateAsset(
    val key: String,
    val ruPattern: String,
    val bgTokens: List<String>,
    val hint: String? = null
)

@Serializable
internal data class Lesson3VerbAsset(
    val past: Map<String, String>,
    val ruPast: Map<String, String>
)

@Serializable
internal data class Lesson9NumberAsset(
    val value: Int,
    val bgMasculine: String,
    val bgFeminine: String,
    val bgNeuter: String,
    val ruMasculine: String,
    val ruFeminine: String,
    val ruNeuter: String
)

@Serializable
internal data class Lesson9ObjectAsset(
    val gender: String,
    val singular: String,
    val plural: String,
    val countForm: String,
    val ruSingular: String,
    val ruPlural: String,
    val ruMany: String = ruPlural
)

@Serializable
internal data class Lesson9TemplateAsset(
    val ruTokens: List<String>,
    val bgTokens: List<String>,
    val hint: String? = null
)

@Serializable
internal data class Lesson10PhraseAsset(
    val ruTokens: List<String>,
    val bgTokens: List<String>
)

@Serializable
internal data class Lesson10IntervalAsset(
    val ruFromTokens: List<String>,
    val ruToTokens: List<String>,
    val bgFromTokens: List<String>,
    val bgToTokens: List<String>
)

@Serializable
internal data class Lesson10TemplateAsset(
    val ruTokens: List<String>,
    val bgTokens: List<String>,
    val hint: String? = null,
    val isQuestion: Boolean = false
)

@Serializable
internal data class LessonSessionAssets(
    val lesson1Subjects: List<Lesson1SubjectAsset> = emptyList(),
    val lesson1Templates: List<Lesson1TemplateAsset> = emptyList(),
    val lesson1Verbs: List<Lesson1VerbAsset> = emptyList(),
    val lesson4Items: List<Lesson4Item> = emptyList(),
    val lesson3SubjectRu: Map<String, String> = emptyMap(),
    val lesson3Verbs: List<Lesson3VerbAsset> = emptyList(),
    val lesson7Templates: List<LessonTemplateAsset> = emptyList(),
    val lesson8Templates: List<LessonTemplateAsset> = emptyList(),
    val lesson9Numbers: List<Lesson9NumberAsset> = emptyList(),
    val lesson9Objects: List<Lesson9ObjectAsset> = emptyList(),
    val lesson9Templates: List<Lesson9TemplateAsset> = emptyList(),
    val lesson10TimePhrases: List<Lesson10PhraseAsset> = emptyList(),
    val lesson10RoutineActions: List<Lesson10PhraseAsset> = emptyList(),
    val lesson10Intervals: List<Lesson10IntervalAsset> = emptyList(),
    val lesson10IntervalActions: List<Lesson10PhraseAsset> = emptyList(),
    val lesson10QuestionActions: List<Lesson10PhraseAsset> = emptyList(),
    val lesson10Templates: List<Lesson10TemplateAsset> = emptyList(),
    val lesson10IntervalTemplates: List<Lesson10TemplateAsset> = emptyList(),
    val lesson10QuestionTemplates: List<Lesson10TemplateAsset> = emptyList()
)

internal class LessonSessionAssetsRepository(
    context: Context
) {

    private val appContext = context.applicationContext

    private val json = Json {
        ignoreUnknownKeys = true
    }

    fun load(): LessonSessionAssets {
        return runCatching {
            appContext.assets
                .open(LESSON_SESSION_ASSET_FILE)
                .bufferedReader(Charsets.UTF_8)
                .use { reader ->
                    json.decodeFromString<LessonSessionAssets>(reader.readText())
                }
        }.getOrElse {
            defaultLessonSessionAssets()
        }
    }

    private companion object {
        const val LESSON_SESSION_ASSET_FILE = "lesson_session_content.json"
    }
}

internal fun defaultLessonSessionAssets(): LessonSessionAssets {
    return LessonSessionAssets(
        lesson4Items = listOf(
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
            Lesson4Item(Lesson4Item.Type.NOUN, "я хочу эту книгу", listOf("Аз", "искам", "книгата")),
            Lesson4Item(Lesson4Item.Type.NOUN, "вода", listOf("вода")),
            Lesson4Item(Lesson4Item.Type.NOUN, "эта вода", listOf("водата")),
            Lesson4Item(Lesson4Item.Type.NOUN, "работа", listOf("работа")),
            Lesson4Item(Lesson4Item.Type.NOUN, "эта работа", listOf("работата")),
            Lesson4Item(Lesson4Item.Type.NOUN, "кофе", listOf("кафе")),
            Lesson4Item(Lesson4Item.Type.NOUN, "это кофе", listOf("кафето")),
            Lesson4Item(Lesson4Item.Type.VERB, "работать", listOf("да", "работя")),
            Lesson4Item(Lesson4Item.Type.VERB, "читать", listOf("да", "чета")),
            Lesson4Item(Lesson4Item.Type.VERB, "учиться", listOf("да", "уча")),
            Lesson4Item(Lesson4Item.Type.VERB, "говорить", listOf("да", "говоря")),
            Lesson4Item(Lesson4Item.Type.VERB, "я хочу работать", listOf("Аз", "искам", "да", "работя")),
            Lesson4Item(Lesson4Item.Type.VERB, "я хочу читать", listOf("Аз", "искам", "да", "чета")),
            Lesson4Item(Lesson4Item.Type.NOUN, "я люблю кофе", listOf("Аз", "обичам", "кафе")),
            Lesson4Item(Lesson4Item.Type.NOUN, "я люблю эту работу", listOf("Аз", "обичам", "работата")),
            Lesson4Item(Lesson4Item.Type.VERB, "я люблю читать", listOf("Аз", "обичам", "да", "чета")),
            Lesson4Item(Lesson4Item.Type.VERB, "мы хотим работать", listOf("Ние", "искаме", "да", "работим")),
            Lesson4Item(Lesson4Item.Type.VERB, "мы любим читать", listOf("Ние", "обичаме", "да", "четем")),
            Lesson4Item(Lesson4Item.Type.NOUN, "мы хотим эту воду", listOf("Ние", "искаме", "водата")),
            Lesson4Item(Lesson4Item.Type.VERB, "я люблю пить кофе", listOf("Аз", "обичам", "да", "пия", "кафе")),
            Lesson4Item(Lesson4Item.Type.VERB, "он хочет работать", listOf("Той", "иска", "да", "работи")),
            Lesson4Item(Lesson4Item.Type.NOUN, "он хочет эту работу", listOf("Той", "иска", "работата")),
            Lesson4Item(Lesson4Item.Type.VERB, "вы хотите читать", listOf("Вие", "искате", "да", "четете")),
            Lesson4Item(Lesson4Item.Type.NOUN, "мы любим кофе", listOf("Ние", "обичаме", "кафе")),
            Lesson4Item(Lesson4Item.Type.VERB, "мы любим пить кофе", listOf("Ние", "обичаме", "да", "пием", "кафе"))
        ),
        lesson3SubjectRu = mapOf(
            "Аз" to "Я",
            "Ти" to "Ты",
            "Той" to "Он",
            "Ние" to "Мы",
            "Вие" to "Вы",
            "Те" to "Они"
        ),
        lesson3Verbs = listOf(
            Lesson3VerbAsset(
                past = mapOf(
                    "Аз" to "правих",
                    "Ти" to "прави",
                    "Той" to "прави",
                    "Ние" to "правихме",
                    "Вие" to "правихте",
                    "Те" to "правиха"
                ),
                ruPast = mapOf(
                    "Аз" to "делал(а)",
                    "Ти" to "делал(а)",
                    "Той" to "делал",
                    "Ние" to "делали",
                    "Вие" to "делали",
                    "Те" to "делали"
                )
            ),
            Lesson3VerbAsset(
                past = mapOf(
                    "Аз" to "гледах",
                    "Ти" to "гледа",
                    "Той" to "гледа",
                    "Ние" to "гледахме",
                    "Вие" to "гледахте",
                    "Те" to "гледаха"
                ),
                ruPast = mapOf(
                    "Аз" to "смотрел(а)",
                    "Ти" to "смотрел(а)",
                    "Той" to "смотрел",
                    "Ние" to "смотрели",
                    "Вие" to "смотрели",
                    "Те" to "смотрели"
                )
            ),
            Lesson3VerbAsset(
                past = mapOf(
                    "Аз" to "отивах",
                    "Ти" to "отива",
                    "Той" to "отива",
                    "Ние" to "отивахме",
                    "Вие" to "отивахте",
                    "Те" to "отиваха"
                ),
                ruPast = mapOf(
                    "Аз" to "ходил(а)",
                    "Ти" to "ходил(а)",
                    "Той" to "ходил",
                    "Ние" to "ходили",
                    "Вие" to "ходили",
                    "Те" to "ходили"
                )
            ),
            Lesson3VerbAsset(
                past = mapOf(
                    "Аз" to "ядох",
                    "Ти" to "яде",
                    "Той" to "яде",
                    "Ние" to "ядохме",
                    "Вие" to "ядохте",
                    "Те" to "ядоха"
                ),
                ruPast = mapOf(
                    "Аз" to "ел(а)",
                    "Ти" to "ел(а)",
                    "Той" to "ел",
                    "Ние" to "ели",
                    "Вие" to "ели",
                    "Те" to "ели"
                )
            ),
            Lesson3VerbAsset(
                past = mapOf(
                    "Аз" to "пих",
                    "Ти" to "пи",
                    "Той" to "пи",
                    "Ние" to "пихме",
                    "Вие" to "пихте",
                    "Те" to "пиха"
                ),
                ruPast = mapOf(
                    "Аз" to "пил(а)",
                    "Ти" to "пил(а)",
                    "Той" to "пил",
                    "Ние" to "пили",
                    "Вие" to "пили",
                    "Те" to "пили"
                )
            ),
            Lesson3VerbAsset(
                past = mapOf(
                    "Аз" to "работих",
                    "Ти" to "работи",
                    "Той" to "работи",
                    "Ние" to "работихме",
                    "Вие" to "работихте",
                    "Те" to "работиха"
                ),
                ruPast = mapOf(
                    "Аз" to "работал(а)",
                    "Ти" to "работал(а)",
                    "Той" to "работал",
                    "Ние" to "работали",
                    "Вие" to "работали",
                    "Те" to "работали"
                )
            ),
            Lesson3VerbAsset(
                past = mapOf(
                    "Аз" to "учих",
                    "Ти" to "учи",
                    "Той" to "учи",
                    "Ние" to "учихме",
                    "Вие" to "учихте",
                    "Те" to "учиха"
                ),
                ruPast = mapOf(
                    "Аз" to "учился(ась)",
                    "Ти" to "учился(ась)",
                    "Той" to "учился",
                    "Ние" to "учились",
                    "Вие" to "учились",
                    "Те" to "учились"
                )
            ),
            Lesson3VerbAsset(
                past = mapOf(
                    "Аз" to "говорих",
                    "Ти" to "говори",
                    "Той" to "говори",
                    "Ние" to "говорихме",
                    "Вие" to "говорихте",
                    "Те" to "говориха"
                ),
                ruPast = mapOf(
                    "Аз" to "говорил(а)",
                    "Ти" to "говорил(а)",
                    "Той" to "говорил",
                    "Ние" to "говорили",
                    "Вие" to "говорили",
                    "Те" to "говорили"
                )
            ),
            Lesson3VerbAsset(
                past = mapOf(
                    "Аз" to "видях",
                    "Ти" to "видя",
                    "Той" to "видя",
                    "Ние" to "видяхме",
                    "Вие" to "видяхте",
                    "Те" to "видяха"
                ),
                ruPast = mapOf(
                    "Аз" to "видел(а)",
                    "Ти" to "видел(а)",
                    "Той" to "видел",
                    "Ние" to "видели",
                    "Вие" to "видели",
                    "Те" to "видели"
                )
            ),
            Lesson3VerbAsset(
                past = mapOf(
                    "Аз" to "исках",
                    "Ти" to "иска",
                    "Той" to "иска",
                    "Ние" to "искахме",
                    "Вие" to "искахте",
                    "Те" to "искаха"
                ),
                ruPast = mapOf(
                    "Аз" to "хотел(а)",
                    "Ти" to "хотел(а)",
                    "Той" to "хотел",
                    "Ние" to "хотели",
                    "Вие" to "хотели",
                    "Те" to "хотели"
                )
            )
        ),
        lesson7Templates = listOf(
            LessonTemplateAsset("Это моя книга", listOf("Това", "е", "моята", "книга"), "💡 притяжательная форма с артиклем"),
            LessonTemplateAsset("Это мой друг", listOf("Това", "е", "моят", "приятел"), "💡 форма зависит от рода существительного"),
            LessonTemplateAsset("Это твоя книга", listOf("Това", "е", "твоята", "книга"), "💡 притяжательная форма с артиклем"),
            LessonTemplateAsset("Это наш ребёнок", listOf("Това", "е", "нашето", "дете"), "💡 форма зависит от рода существительного"),
            LessonTemplateAsset("Это наши книги", listOf("Това", "са", "нашите", "книги"), "💡 множественное число"),
            LessonTemplateAsset("У меня есть своя книга", listOf("Аз", "имам", "моята", "книга"), "💡 полная живая фраза"),
            LessonTemplateAsset("Я вижу свою книгу", listOf("Аз", "виждам", "моята", "книга"), "💡 полная живая фраза"),
            LessonTemplateAsset("Я беру свою книгу", listOf("Аз", "взимам", "моята", "книга"), "💡 взимам = беру"),
            LessonTemplateAsset("Мы любим нашего ребёнка", listOf("Ние", "обичаме", "нашето", "дете"), "💡 обичам = люблю"),
            LessonTemplateAsset("Я даю тебе свою книгу", listOf("Давам", "ти", "моята", "книга"), "💡 в болгарском \"тебе\" часто ставится перед объектом"),
            LessonTemplateAsset("Я даю тебе эту книгу", listOf("Давам", "ти", "книгата"), "💡 давать кому-то"),
            LessonTemplateAsset("Ты видишь своего друга", listOf("Ти", "виждаш", "своя", "приятел"), "💡 для собственного объекта естественнее своя / свой")
        ),
        lesson8Templates = listOf(
            LessonTemplateAsset("Он старше меня", listOf("Той", "е", "по-стар", "от", "мен"), "💡 сравнение: по- + прилагательное + от"),
            LessonTemplateAsset("Я младше его", listOf("Аз", "съм", "по-млад", "от", "него"), "💡 сравнение: по- + прилагательное + от"),
            LessonTemplateAsset("Эта книга интереснее той книги", listOf("Тази", "книга", "е", "по-интересна", "от", "онази"), "💡 \"от\" означает «чем»"),
            LessonTemplateAsset("Моя машина быстрее твоей", listOf("Моята", "кола", "е", "по-бърза", "от", "твоята"), "💡 полное сравнение в живой фразе"),
            LessonTemplateAsset("Наш дом больше вашего дома", listOf("Нашият", "дом", "е", "по-голям", "от", "вашия"), "💡 полное сравнение в живой фразе"),
            LessonTemplateAsset("Мой брат выше меня", listOf("Моят", "брат", "е", "по-висок", "от", "мен"), "💡 сравнение: по- + прилагательное + от"),
            LessonTemplateAsset("Телефон дороже, чем часы", listOf("Телефонът", "е", "по-скъп", "от", "часовника"), "💡 сравнение предметов"),
            LessonTemplateAsset("Книга лучше фильма", listOf("Книгата", "е", "по-добра", "от", "филма"), "💡 добър → по-добър"),
            LessonTemplateAsset("Это лучшая книга", listOf("Това", "е", "най-добрата", "книга"), "💡 превосходная степень: най- + прилагательное"),
            LessonTemplateAsset("Он лучший ученик", listOf("Той", "е", "най-добрият", "ученик"), "💡 превосходная степень: най- + прилагательное"),
            LessonTemplateAsset("Это самая дорогая машина", listOf("Това", "е", "най-скъпата", "кола"), "💡 с превосходной степенью обычно нужен артикль"),
            LessonTemplateAsset("Это самый красивый дом", listOf("Това", "е", "най-красивият", "дом"), "💡 с превосходной степенью обычно нужен артикль"),
            LessonTemplateAsset("Это самый интересный фильм", listOf("Това", "е", "най-интересният", "филм"), "💡 с превосходной степенью обычно нужен артикль"),
            LessonTemplateAsset("Это лучший день", listOf("Това", "е", "най-добрият", "ден"), "💡 най- = самый"),
            LessonTemplateAsset("Это самый прекрасный день в моей жизни", listOf("Това", "е", "най-хубавият", "ден", "в", "живота", "ми"), "💡 длинное полное предложение")
        ),
        lesson9Numbers = listOf(
            Lesson9NumberAsset(1, "един", "една", "едно", "один", "одну", "одно"),
            Lesson9NumberAsset(2, "два", "две", "две", "два", "две", "два"),
            Lesson9NumberAsset(3, "три", "три", "три", "три", "три", "три"),
            Lesson9NumberAsset(4, "четири", "четири", "четири", "четыре", "четыре", "четыре"),
            Lesson9NumberAsset(5, "пет", "пет", "пет", "пять", "пять", "пять"),
            Lesson9NumberAsset(6, "шест", "шест", "шест", "шесть", "шесть", "шесть"),
            Lesson9NumberAsset(7, "седем", "седем", "седем", "семь", "семь", "семь"),
            Lesson9NumberAsset(8, "осем", "осем", "осем", "восемь", "восемь", "восемь"),
            Lesson9NumberAsset(9, "девет", "девет", "девет", "девять", "девять", "девять"),
            Lesson9NumberAsset(10, "десет", "десет", "десет", "десять", "десять", "десять"),
            Lesson9NumberAsset(11, "единадесет", "единадесет", "единадесет", "одиннадцать", "одиннадцать", "одиннадцать"),
            Lesson9NumberAsset(12, "дванадесет", "дванадесет", "дванадесет", "двенадцать", "двенадцать", "двенадцать"),
            Lesson9NumberAsset(13, "тринадесет", "тринадесет", "тринадесет", "тринадцать", "тринадцать", "тринадцать"),
            Lesson9NumberAsset(14, "четиринадесет", "четиринадесет", "четиринадесет", "четырнадцать", "четырнадцать", "четырнадцать"),
            Lesson9NumberAsset(15, "петнадесет", "петнадесет", "петнадесет", "пятнадцать", "пятнадцать", "пятнадцать"),
            Lesson9NumberAsset(16, "шестнадесет", "шестнадесет", "шестнадесет", "шестнадцать", "шестнадцать", "шестнадцать"),
            Lesson9NumberAsset(17, "седемнадесет", "седемнадесет", "седемнадесет", "семнадцать", "семнадцать", "семнадцать"),
            Lesson9NumberAsset(18, "осемнадесет", "осемнадесет", "осемнадесет", "восемнадцать", "восемнадцать", "восемнадцать"),
            Lesson9NumberAsset(19, "деветнадесет", "деветнадесет", "деветнадесет", "девятнадцать", "девятнадцать", "девятнадцать"),
            Lesson9NumberAsset(20, "двадесет", "двадесет", "двадесет", "двадцать", "двадцать", "двадцать")
        ),
        lesson9Objects = listOf(
            Lesson9ObjectAsset("feminine", "книга", "книги", "книги", "книгу", "книги", "книг"),
            Lesson9ObjectAsset("masculine", "телефон", "телефони", "телефона", "телефон", "телефона", "телефонов"),
            Lesson9ObjectAsset("neuter", "писмо", "писма", "писма", "письмо", "письма", "писем")
        ),
        lesson9Templates = listOf(
            Lesson9TemplateAsset(
                ruTokens = listOf("Я", "беру", "{num}", "{object}"),
                bgTokens = listOf("Аз", "взимам", "{num}", "{object}"),
                hint = "💡 после числа форма существительного может меняться"
            ),
            Lesson9TemplateAsset(
                ruTokens = listOf("Я", "вижу", "{num}", "{object}"),
                bgTokens = listOf("Аз", "виждам", "{num}", "{object}"),
                hint = "💡 число + существительное"
            ),
            Lesson9TemplateAsset(
                ruTokens = listOf("Я", "даю", "тебе", "{num}", "{object}"),
                bgTokens = listOf("Аз", "ти", "давам", "{num}", "{object}"),
                hint = "💡 в болгарском \"ти\" обычно стоит перед глаголом"
            ),
            Lesson9TemplateAsset(
                ruTokens = listOf("Ты", "видишь", "{num}", "{object}", "?"),
                bgTokens = listOf("Ти", "виждаш", "ли", "{num}", "{object}"),
                hint = "💡 в вопросе \"ли\" ставится после глагола"
            ),
            Lesson9TemplateAsset(
                ruTokens = listOf("Ты", "берёшь", "{num}", "{object}", "?"),
                bgTokens = listOf("Ти", "взимаш", "ли", "{num}", "{object}"),
                hint = "💡 вопрос: глагол + ли + число + предмет"
            )
        ),
        lesson10TimePhrases = listOf(
            Lesson10PhraseAsset(listOf("В", "понедельник"), listOf("в", "понеделник")),
            Lesson10PhraseAsset(listOf("Во", "вторник"), listOf("във", "вторник")),
            Lesson10PhraseAsset(listOf("В", "среду"), listOf("в", "сряда")),
            Lesson10PhraseAsset(listOf("В", "четверг"), listOf("в", "четвъртък")),
            Lesson10PhraseAsset(listOf("В", "пятницу"), listOf("в", "петък")),
            Lesson10PhraseAsset(listOf("Утром"), listOf("сутрин")),
            Lesson10PhraseAsset(listOf("Днём"), listOf("следобед")),
            Lesson10PhraseAsset(listOf("Вечером"), listOf("вечер")),
            Lesson10PhraseAsset(listOf("Ночью"), listOf("нощем")),
            Lesson10PhraseAsset(listOf("Сегодня"), listOf("днес")),
            Lesson10PhraseAsset(listOf("Завтра"), listOf("утре")),
            Lesson10PhraseAsset(listOf("После", "работы"), listOf("след", "работа")),
            Lesson10PhraseAsset(listOf("Перед", "обедом"), listOf("преди", "обяд")),
            Lesson10PhraseAsset(listOf("В", "час", "дня"), listOf("в", "един", "час")),
            Lesson10PhraseAsset(listOf("В", "два", "часа"), listOf("в", "два", "часа")),
            Lesson10PhraseAsset(listOf("В", "три", "часа"), listOf("в", "три", "часа")),
            Lesson10PhraseAsset(listOf("В", "пять", "часов"), listOf("в", "пет", "часа"))
        ),
        lesson10RoutineActions = listOf(
            Lesson10PhraseAsset(listOf("работаю"), listOf("работя")),
            Lesson10PhraseAsset(listOf("у", "меня", "урок"), listOf("имам", "урок")),
            Lesson10PhraseAsset(listOf("учусь"), listOf("уча")),
            Lesson10PhraseAsset(listOf("завтракаю"), listOf("закусвам")),
            Lesson10PhraseAsset(listOf("пью", "кофе"), listOf("пия", "кафе")),
            Lesson10PhraseAsset(listOf("ужинаю"), listOf("вечерям")),
            Lesson10PhraseAsset(listOf("отдыхаю"), listOf("почивам")),
            Lesson10PhraseAsset(listOf("иду", "в", "магазин"), listOf("отивам", "в", "магазина")),
            Lesson10PhraseAsset(listOf("возвращаюсь", "домой"), listOf("се", "прибирам", "вкъщи"))
        ),
        lesson10Intervals = listOf(
            Lesson10IntervalAsset(
                ruFromTokens = listOf("с", "девяти"),
                ruToTokens = listOf("до", "пяти"),
                bgFromTokens = listOf("от", "девет"),
                bgToTokens = listOf("до", "пет")
            ),
            Lesson10IntervalAsset(
                ruFromTokens = listOf("с", "двух"),
                ruToTokens = listOf("до", "трёх"),
                bgFromTokens = listOf("от", "два"),
                bgToTokens = listOf("до", "три")
            ),
            Lesson10IntervalAsset(
                ruFromTokens = listOf("с", "часа"),
                ruToTokens = listOf("до", "двух"),
                bgFromTokens = listOf("от", "един", "час"),
                bgToTokens = listOf("до", "два")
            )
        ),
        lesson10IntervalActions = listOf(
            Lesson10PhraseAsset(listOf("работаю"), listOf("работя")),
            Lesson10PhraseAsset(listOf("учусь"), listOf("уча")),
            Lesson10PhraseAsset(listOf("отдыхаю"), listOf("почивам")),
            Lesson10PhraseAsset(listOf("читаю"), listOf("чета")),
            Lesson10PhraseAsset(listOf("сплю"), listOf("спя"))
        ),
        lesson10QuestionActions = listOf(
            Lesson10PhraseAsset(listOf("ты", "работаешь"), listOf("работиш")),
            Lesson10PhraseAsset(listOf("у", "тебя", "урок"), listOf("имаш", "урок")),
            Lesson10PhraseAsset(listOf("ты", "учишься"), listOf("учиш")),
            Lesson10PhraseAsset(listOf("ты", "завтракаешь"), listOf("закусваш")),
            Lesson10PhraseAsset(listOf("ты", "пьёшь", "кофе"), listOf("пиеш", "кафе")),
            Lesson10PhraseAsset(listOf("ты", "ужинаешь"), listOf("вечеряш")),
            Lesson10PhraseAsset(listOf("ты", "отдыхаешь"), listOf("почиваш")),
            Lesson10PhraseAsset(listOf("ты", "идёшь", "в", "магазин"), listOf("отиваш", "в", "магазина")),
            Lesson10PhraseAsset(listOf("ты", "возвращаешься", "домой"), listOf("се", "прибираш", "вкъщи"))
        ),
        lesson10Templates = listOf(
            Lesson10TemplateAsset(
                ruTokens = listOf("{time}", "{action}"),
                bgTokens = listOf("{time}", "{action}"),
                hint = "💡 время часто ставится в начале фразы"
            ),
            Lesson10TemplateAsset(
                ruTokens = listOf("{action}", "{time}"),
                bgTokens = listOf("{action}", "{time}"),
                hint = "💡 порядок слов можно менять, если фраза звучит естественно"
            )
        ),
        lesson10IntervalTemplates = listOf(
            Lesson10TemplateAsset(
                ruTokens = listOf("{action}", "{from}", "{to}"),
                bgTokens = listOf("{action}", "{from}", "{to}"),
                hint = "💡 промежуток времени: от ... до ..."
            ),
            Lesson10TemplateAsset(
                ruTokens = listOf("{from}", "{to}", "{action}"),
                bgTokens = listOf("{from}", "{to}", "{action}"),
                hint = "💡 фразу можно начать с промежутка времени"
            )
        ),
        lesson10QuestionTemplates = listOf(
            Lesson10TemplateAsset(
                ruTokens = listOf("Когда", "{action}"),
                bgTokens = listOf("Кога", "{action}"),
                hint = "💡 вопросительное слово обычно стоит в начале",
                isQuestion = true
            ),
            Lesson10TemplateAsset(
                ruTokens = listOf("Во", "сколько", "часов", "{action}"),
                bgTokens = listOf("В", "колко", "часа", "{action}"),
                hint = "💡 В колко часа = во сколько",
                isQuestion = true
            )
        )
    )
}
