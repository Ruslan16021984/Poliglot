package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import android.content.Context
import com.carbit3333333.oiiglot_bulgary.data.localization.DEFAULT_CONTENT_LANGUAGE_CODE
import com.carbit3333333.oiiglot_bulgary.data.localization.resolveCurrentLanguageCode

internal data class LessonExerciseLocale(
    val languageCode: String,
    val fallbackLanguageCode: String = DEFAULT_CONTENT_LANGUAGE_CODE,
)

internal fun resolveLessonExerciseLocale(context: Context): LessonExerciseLocale {
    return LessonExerciseLocale(
        languageCode = resolveCurrentLanguageCode(context.resources),
    )
}

internal object LessonExerciseStrings {
    private val translationInstructions = mapOf(
        "ru" to "Переведите предложение",
        "uk" to "Перекладіть речення",
        "en" to "Translate the sentence",
    )

    private val localizedLessonTitles = mapOf(
        "en" to mapOf(
            1 to "Greetings and Introductions",
            2 to "Food and Breakfast",
            3 to "Restaurant",
            4 to "Shopping at the Supermarket and Market",
            5 to "City, Address and Directions",
            6 to "Family",
            7 to "Weather and Time",
            8 to "Clothes and Colors",
            9 to "Home and Furniture",
            10 to "Transport",
            11 to "Daily Routine",
        ),
        "uk" to mapOf(
            1 to "Привітання і знайомство",
            2 to "Їжа і сніданок",
            3 to "Ресторан",
            4 to "Покупки в супермаркеті і на ринку",
            5 to "Місто, адреса і покупки",
            6 to "Сім'я",
            7 to "Погода і час",
            8 to "Одяг і кольори",
            9 to "Дім і меблі",
            10 to "Транспорт",
            11 to "Розпорядок дня",
        )
    )

    fun translationInstruction(
        languageCode: String,
        fallbackLanguageCode: String = DEFAULT_CONTENT_LANGUAGE_CODE,
    ): String {
        return translationInstructions[languageCode]
            ?: translationInstructions[languageCode.substringBefore('-')]
            ?: translationInstructions[fallbackLanguageCode]
            ?: translationInstructions.getValue(DEFAULT_CONTENT_LANGUAGE_CODE)
    }

    fun lessonTitle(
        languageCode: String,
        lessonId: Int,
        defaultTitle: String,
    ): String {
        return localizedLessonTitles[languageCode]?.get(lessonId)
            ?: localizedLessonTitles[languageCode.substringBefore('-')]?.get(lessonId)
            ?: defaultTitle
    }
}
