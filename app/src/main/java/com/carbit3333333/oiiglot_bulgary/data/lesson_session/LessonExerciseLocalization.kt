package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import android.content.Context
import java.util.Locale

internal enum class LessonExerciseLocale {
    Russian,
    Ukrainian,
}

internal fun resolveLessonExerciseLocale(context: Context): LessonExerciseLocale {
    val language = runCatching {
        context.resources.configuration.locales[0]?.language
    }.getOrNull() ?: Locale.getDefault().language

    return if (language == "uk") {
        LessonExerciseLocale.Ukrainian
    } else {
        LessonExerciseLocale.Russian
    }
}

internal object UkrainianLessonStrings {
    const val translationInstruction = "Перекладіть речення"

    fun lessonTitle(lessonId: Int): String {
        return when (lessonId) {
            1 -> "Привітання і знайомство"
            2 -> "Їжа і сніданок"
            3 -> "Ресторан"
            4 -> "Покупки в супермаркеті і на ринку"
            5 -> "Місто, адреса і покупки"
            6 -> "Сім'я"
            7 -> "Погода і час"
            8 -> "Одяг і кольори"
            9 -> "Дім і меблі"
            10 -> "Транспорт"
            11 -> "Розпорядок дня"
            else -> "Урок $lessonId"
        }
    }
}
