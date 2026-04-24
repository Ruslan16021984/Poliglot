package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import android.content.Context
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal enum class LessonExerciseLocale {
    Russian,
    Ukrainian,
}

internal fun resolveLessonExerciseLocale(context: Context): LessonExerciseLocale {
    val language = runCatching {
        context.resources.configuration.locales[0]?.language
    }.getOrNull() ?: java.util.Locale.getDefault().language

    return if (language == "uk") {
        LessonExerciseLocale.Ukrainian
    } else {
        LessonExerciseLocale.Russian
    }
}

internal fun localizeLessonExercises(
    exercises: List<LessonExercise>,
    locale: LessonExerciseLocale,
): List<LessonExercise> {
    if (locale == LessonExerciseLocale.Russian) return exercises

    return localizeLessonExercisesInternal(
        exercises = exercises,
        translateSourceText = true,
    )
}

internal fun localizeLessonExercisesUsingExistingSource(
    exercises: List<LessonExercise>,
    locale: LessonExerciseLocale,
): List<LessonExercise> {
    if (locale == LessonExerciseLocale.Russian) return exercises

    return localizeLessonExercisesInternal(
        exercises = exercises,
        translateSourceText = false,
    )
}

private fun localizeLessonExercisesInternal(
    exercises: List<LessonExercise>,
    translateSourceText: Boolean,
): List<LessonExercise> {

    return exercises.map { exercise ->
        exercise.copy(
            sourceText = if (translateSourceText) {
                RussianToUkrainianLessonTranslator.translate(exercise.sourceText)
            } else {
                exercise.sourceText
            },
            instruction = UkrainianLessonStrings.translationInstruction,
            hint = exercise.hint?.let(RussianToUkrainianLessonTranslator::translateHint),
        )
    }
}

internal object UkrainianLessonStrings {
    const val translationInstruction = "Перекладіть речення"

    fun lessonTitle(lessonId: Int): String {
        return when (lessonId) {
            1 -> "Основні форми дієслова"
            2 -> "Дієслово \"съм\""
            3 -> "Минулий час"
            4 -> "Предмет чи дія"
            5 -> "Можу, хочу, повинен"
            6 -> "Прийменники й іменники"
            7 -> "Моя книга: займенники й артикль"
            8 -> "Порівняння"
            9 -> "Числа"
            10 -> "Час і розпорядок дня"
            else -> "Урок $lessonId"
        }
    }
}

private object RussianToUkrainianLessonTranslator {

    private val phraseReplacements = listOf(
        "Переведите предложение" to "Перекладіть речення",
        "Порядок: подлежащее + глагол + дополнение" to "Порядок: підмет + дієслово + додаток",
        "Отрицание: не стоит перед глаголом" to "Заперечення: не стоїть перед дієсловом",
        "Будущее время: ще + глагол" to "Майбутній час: ще + дієслово",
        "Отрицание в будущем: няма да + глагол" to "Заперечення в майбутньому: няма да + дієслово",
        "Вопрос в будущем: ще + глагол + ли" to "Питання в майбутньому: ще + дієслово + ли",
        "В болгарском вопросе «ли» стоит после глагола" to "У болгарському питанні «ли» стоїть після дієслова",
        "После искам / обичам действие идёт с \"да\"." to "Після искам / обичам дія йде з \"да\".",
        "Если речь о конкретном предмете, используй форму с артиклем." to "Якщо йдеться про конкретний предмет, використай форму з артиклем.",
        "Это не " to "Це не ",
        "Это " to "Це ",
        "У меня есть" to "У мене є",
        "У тебя есть" to "У тебе є",
        "У него есть" to "У нього є",
        "У неё есть" to "У неї є",
        "У нас есть" to "У нас є",
        "У вас есть" to "У вас є",
        "У них есть" to "У них є",
        "У меня нет" to "У мене немає",
        "У тебя нет" to "У тебе немає",
        "У него нет" to "У нього немає",
        "У неё нет" to "У неї немає",
        "У нас нет" to "У нас немає",
        "У вас нет" to "У вас немає",
        "У них нет" to "У них немає",
        "Мне нужно" to "Мені потрібно",
        "Тебе нужно" to "Тобі потрібно",
        "Ему нужно" to "Йому потрібно",
        "Ей нужно" to "Їй потрібно",
        "Нам нужно" to "Нам потрібно",
        "Вам нужно" to "Вам потрібно",
        "Им нужно" to "Їм потрібно",
        "не нужно" to "не потрібно",
        "Во сколько часов" to "О котрій годині",
        "Когда" to "Коли",
        "После работы" to "Після роботи",
        "Перед обедом" to "Перед обідом",
        "В час дня" to "О першій годині дня",
        "В два часа" to "О другій годині",
        "В три часа" to "О третій годині",
        "В пять часов" to "О п'ятій годині",
        "В понедельник" to "У понеділок",
        "Во вторник" to "У вівторок",
        "В среду" to "У середу",
        "В четверг" to "У четвер",
        "В пятницу" to "У п'ятницю",
        "Утром" to "Вранці",
        "Днём" to "Вдень",
        "Вечером" to "Увечері",
        "Ночью" to "Вночі",
        "Сегодня" to "Сьогодні",
        "Я самый высокий в классе" to "Я найвищий у класі",
        "Он самый высокий в классе" to "Він найвищий у класі",
    )

    private val wordReplacements = listOf(
        "Я" to "Я",
        "Ты" to "Ти",
        "Он" to "Він",
        "Она" to "Вона",
        "Оно" to "Воно",
        "Мы" to "Ми",
        "Вы" to "Ви",
        "Они" to "Вони",
        "этот" to "цей",
        "эта" to "ця",
        "это" to "це",
        "эти" to "ці",
        "тот" to "той",
        "того" to "того",
        "твоей" to "твоєї",
        "твою" to "твою",
        "вашего" to "вашого",
        "ваша" to "ваша",
        "ваш" to "ваш",
        "наш" to "наш",
        "наша" to "наша",
        "свой" to "свій",
        "свою" to "свою",
        "моей" to "моєї",
        "мой" to "мій",
        "моя" to "моя",
        "я" to "я",
        "ты" to "ти",
        "он" to "він",
        "она" to "вона",
        "оно" to "воно",
        "мы" to "ми",
        "вы" to "ви",
        "они" to "вони",
        "смотрю" to "дивлюся",
        "смотришь" to "дивишся",
        "смотрит" to "дивиться",
        "смотрим" to "дивимося",
        "смотрите" to "дивитеся",
        "смотрят" to "дивляться",
        "смотреть" to "дивитися",
        "работаю" to "працюю",
        "работаешь" to "працюєш",
        "работает" to "працює",
        "работаем" to "працюємо",
        "работаете" to "працюєте",
        "работают" to "працюють",
        "работать" to "працювати",
        "учусь" to "вчуся",
        "учишься" to "вчишся",
        "учится" to "вчиться",
        "учимся" to "вчимося",
        "учитесь" to "вчитеся",
        "учатся" to "вчаться",
        "учиться" to "вчитися",
        "говорю" to "говорю",
        "говоришь" to "говориш",
        "говорит" to "говорить",
        "говорим" to "говоримо",
        "говорите" to "говорите",
        "говорят" to "говорять",
        "говорить" to "говорити",
        "пью" to "п'ю",
        "пьёшь" to "п'єш",
        "пьёт" to "п'є",
        "пьём" to "п'ємо",
        "пьёте" to "п'єте",
        "пьют" to "п'ють",
        "пить" to "пити",
        "люблю" to "люблю",
        "любишь" to "любиш",
        "любит" to "любить",
        "любим" to "любимо",
        "любите" to "любите",
        "любят" to "люблять",
        "любить" to "любити",
        "хочу" to "хочу",
        "хочешь" to "хочеш",
        "хочет" to "хоче",
        "хотим" to "хочемо",
        "хотите" to "хочете",
        "хотят" to "хочуть",
        "хотеть" to "хотіти",
        "могу" to "можу",
        "можешь" to "можеш",
        "может" to "може",
        "можем" to "можемо",
        "можете" to "можете",
        "могут" to "можуть",
        "иду" to "йду",
        "идёшь" to "йдеш",
        "идёт" to "йде",
        "идём" to "йдемо",
        "идёте" to "йдете",
        "идут" to "йдуть",
        "идти" to "йти",
        "беру" to "беру",
        "берёшь" to "береш",
        "берёт" to "бере",
        "берём" to "беремо",
        "берёте" to "берете",
        "берут" to "беруть",
        "вижу" to "бачу",
        "видишь" to "бачиш",
        "видит" to "бачить",
        "видим" to "бачимо",
        "видите" to "бачите",
        "видят" to "бачать",
        "даю" to "даю",
        "даёшь" to "даєш",
        "даёт" to "дає",
        "даём" to "даємо",
        "даёте" to "даєте",
        "дают" to "дають",
        "покупаю" to "купую",
        "покупаешь" to "купуєш",
        "покупает" to "купує",
        "покупаем" to "купуємо",
        "покупаете" to "купуєте",
        "покупают" to "купують",
        "возвращаюсь" to "повертаюся",
        "возвращаешься" to "повертаєшся",
        "завтракаю" to "снідаю",
        "завтракаешь" to "снідаєш",
        "ужинаю" to "вечеряю",
        "ужинаешь" to "вечеряєш",
        "обедаю" to "обідаю",
        "обедаешь" to "обідаєш",
        "отдыхаю" to "відпочиваю",
        "отдыхаешь" to "відпочиваєш",
        "сплю" to "сплю",
        "читаю" to "читаю",
        "читаешь" to "читаєш",
        "книга" to "книга",
        "книгу" to "книгу",
        "книги" to "книги",
        "вода" to "вода",
        "воде" to "воді",
        "воду" to "воду",
        "работа" to "робота",
        "работу" to "роботу",
        "работы" to "роботи",
        "работе" to "роботі",
        "кофе" to "каву",
        "чай" to "чай",
        "фильм" to "фільм",
        "фильма" to "фільму",
        "сериал" to "серіал",
        "телевизор" to "телевізор",
        "время" to "час",
        "времени" to "часу",
        "телефон" to "телефон",
        "письмо" to "лист",
        "письма" to "листи",
        "писем" to "листів",
        "билет" to "квиток",
        "билета" to "квитки",
        "билетов" to "квитків",
        "чашку" to "чашку",
        "чашки" to "чашки",
        "чашек" to "чашок",
        "магазин" to "магазин",
        "магазина" to "магазину",
        "магазине" to "магазині",
        "машина" to "машина",
        "машину" to "машину",
        "машины" to "машини",
        "больница" to "лікарня",
        "больнице" to "лікарні",
        "дом" to "будинок",
        "дома" to "вдома",
        "домой" to "додому",
        "город" to "місто",
        "городе" to "місті",
        "офисе" to "офісі",
        "университете" to "університеті",
        "школе" to "школі",
        "классе" to "класі",
        "другом" to "другом",
        "коллегой" to "колегою",
        "врач" to "лікар",
        "учителя" to "вчителя",
        "учитель" to "вчитель",
        "ученик" to "учень",
        "студент" to "студент",
        "продавец" to "продавець",
        "водитель" to "водій",
        "день" to "день",
        "лучше" to "краще",
        "лучший" to "кращий",
        "лучшая" to "краща",
        "лучшее" to "краще",
        "больше" to "більший",
        "меньше" to "менший",
        "красивее" to "гарніший",
        "легче" to "легша",
        "труднее" to "важчий",
        "самый" to "най",
        "самая" to "най",
        "самое" to "най",
        "самые" to "най",
        "красивый" to "гарний",
        "тёплый" to "теплий",
        "быстрая" to "швидка",
        "высокий" to "високий",
        "высший" to "вищий",
        "не" to "не",
        "есть" to "їсти",
        "здесь" to "тут",
        "медленно" to "повільно",
        "по-болгарски" to "болгарською",
        "сегодня" to "сьогодні",
        "завтра" to "завтра",
        "утром" to "вранці",
        "днём" to "вдень",
        "вечером" to "увечері",
        "ночью" to "вночі",
        "после" to "після",
        "перед" to "перед",
        "часа" to "години",
        "часов" to "годин",
        "час" to "година",
        "один" to "один",
        "одну" to "одну",
        "одно" to "одне",
        "два" to "два",
        "две" to "дві",
        "три" to "три",
        "четыре" to "чотири",
        "пять" to "п'ять",
        "шесть" to "шість",
        "семь" to "сім",
        "восемь" to "вісім",
        "девять" to "дев'ять",
        "десять" to "десять",
        "одиннадцать" to "одинадцять",
        "двенадцать" to "дванадцять",
        "тринадцать" to "тринадцять",
        "четырнадцать" to "чотирнадцять",
        "пятнадцать" to "п'ятнадцять",
        "шестнадцать" to "шістнадцять",
        "семнадцать" to "сімнадцять",
        "восемнадцать" to "вісімнадцять",
        "девятнадцать" to "дев'ятнадцять",
        "двадцать" to "двадцять",
        "порядок" to "порядок",
        "подлежащее" to "підмет",
        "глагол" to "дієслово",
        "дополнение" to "додаток",
        "отрицание" to "заперечення",
        "вопрос" to "питання",
        "будущее" to "майбутній",
        "время" to "час",
        "форма" to "форма",
        "предмет" to "предмет",
        "предмете" to "предмет",
        "конкретном" to "конкретний",
        "используй" to "використай",
        "использовать" to "використовувати",
        "после" to "після",
        "действие" to "дія",
        "идёт" to "йде",
        "собери" to "збери",
        "полное" to "повне",
        "предложение" to "речення",
        "прошедшее" to "минулий",
        "формы" to "форми",
        "часто" to "часто",
        "заканчивается" to "закінчується",
        "лучше" to "краще",
        "запоминать" to "запам'ятовувати",
        "целиком" to "цілком",
        "вопросительное" to "питальне",
        "слово" to "слово",
        "обычно" to "зазвичай",
        "стоит" to "стоїть",
        "в начале" to "на початку",
        "промежуток" to "проміжок",
        "между" to "між",
        "можно" to "можна",
        "менять" to "змінювати",
        "если" to "якщо",
        "фраза" to "фраза",
        "звучит" to "звучить",
        "естественно" to "природно",
    )

    fun translate(text: String): String {
        var result = text
        phraseReplacements.forEach { (source, target) ->
            result = replacePreservingCase(result, source, target, phrase = true)
        }
        wordReplacements.forEach { (source, target) ->
            result = replacePreservingCase(result, source, target, phrase = false)
        }
        return result
            .replace("  ", " ")
            .replace(" ?", "?")
            .replace(" ,", ",")
            .replace(" .", ".")
            .trim()
    }

    fun translateHint(text: String): String = translate(text)

    private fun replacePreservingCase(
        input: String,
        source: String,
        target: String,
        phrase: Boolean,
    ): String {
        val regex = if (phrase) {
            Regex(Regex.escape(source), RegexOption.IGNORE_CASE)
        } else {
            Regex(
                pattern = "(?<!\\p{L})${Regex.escape(source)}(?!\\p{L})",
                options = setOf(RegexOption.IGNORE_CASE),
            )
        }

        return regex.replace(input) { match ->
            applyMatchCase(target, match.value)
        }
    }

    private fun applyMatchCase(
        replacement: String,
        original: String,
    ): String {
        return when {
            original.all { !it.isLetter() || it.isUpperCase() } -> replacement.uppercase()
            original.firstOrNull()?.isUpperCase() == true -> replacement.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase() else it.toString()
            }
            else -> replacement
        }
    }
}
