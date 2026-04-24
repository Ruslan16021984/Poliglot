package com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonExerciseLocale
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.support.LessonRealSentenceGenerator
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal object Lesson2RealGenerator {

    private enum class Lesson2TemplateCategory {
        PLACE,
        ROLE,
        DEMO,
    }

    private data class Lesson2TemplateEntry(
        val category: Lesson2TemplateCategory,
        val hint: String,
        val sentenceTemplate: LessonRealSentenceGenerator.SentenceTemplate,
    )

    private val templates = listOf(
        Lesson2TemplateEntry(
            category = Lesson2TemplateCategory.PLACE,
            hint = "💡 место: подлежащее + форма на \"съм\" + место",
            sentenceTemplate = LessonRealSentenceGenerator.SentenceTemplate(
                ruPattern = "{subject} {place}",
                bgPattern = listOf(
                    LessonRealSentenceGenerator.Token.SubjectBg,
                    LessonRealSentenceGenerator.Token.VerbBg,
                    LessonRealSentenceGenerator.Token.PlaceBg,
                ),
            ),
        ),
        Lesson2TemplateEntry(
            category = Lesson2TemplateCategory.PLACE,
            hint = "💡 отрицание: не + форма на \"съм\"",
            sentenceTemplate = LessonRealSentenceGenerator.SentenceTemplate(
                ruPattern = "{subject} не {place}",
                bgPattern = listOf(
                    LessonRealSentenceGenerator.Token.SubjectBg,
                    LessonRealSentenceGenerator.Token.Fixed("не"),
                    LessonRealSentenceGenerator.Token.VerbBg,
                    LessonRealSentenceGenerator.Token.PlaceBg,
                ),
            ),
        ),
        Lesson2TemplateEntry(
            category = Lesson2TemplateCategory.PLACE,
            hint = "💡 вопрос: место + ли + форма на \"съм\"",
            sentenceTemplate = LessonRealSentenceGenerator.SentenceTemplate(
                ruPattern = "{subject} {place}?",
                bgPattern = listOf(
                    LessonRealSentenceGenerator.Token.SubjectBg,
                    LessonRealSentenceGenerator.Token.PlaceBg,
                    LessonRealSentenceGenerator.Token.Fixed("ли"),
                    LessonRealSentenceGenerator.Token.VerbBg,
                ),
            ),
        ),
        Lesson2TemplateEntry(
            category = Lesson2TemplateCategory.ROLE,
            hint = "💡 кто это: подлежащее + форма на \"съм\" + слово",
            sentenceTemplate = LessonRealSentenceGenerator.SentenceTemplate(
                ruPattern = "{subject} {noun}",
                bgPattern = listOf(
                    LessonRealSentenceGenerator.Token.SubjectBg,
                    LessonRealSentenceGenerator.Token.VerbBg,
                    LessonRealSentenceGenerator.Token.NounBg,
                ),
            ),
        ),
        Lesson2TemplateEntry(
            category = Lesson2TemplateCategory.ROLE,
            hint = "💡 отрицание: не + форма на \"съм\"",
            sentenceTemplate = LessonRealSentenceGenerator.SentenceTemplate(
                ruPattern = "{subject} не {noun}",
                bgPattern = listOf(
                    LessonRealSentenceGenerator.Token.SubjectBg,
                    LessonRealSentenceGenerator.Token.Fixed("не"),
                    LessonRealSentenceGenerator.Token.VerbBg,
                    LessonRealSentenceGenerator.Token.NounBg,
                ),
            ),
        ),
        Lesson2TemplateEntry(
            category = Lesson2TemplateCategory.ROLE,
            hint = "💡 вопрос: слово + ли + форма на \"съм\"",
            sentenceTemplate = LessonRealSentenceGenerator.SentenceTemplate(
                ruPattern = "{subject} {noun}?",
                bgPattern = listOf(
                    LessonRealSentenceGenerator.Token.SubjectBg,
                    LessonRealSentenceGenerator.Token.NounBg,
                    LessonRealSentenceGenerator.Token.Fixed("ли"),
                    LessonRealSentenceGenerator.Token.VerbBg,
                ),
            ),
        ),
        Lesson2TemplateEntry(
            category = Lesson2TemplateCategory.DEMO,
            hint = "💡 это → Това е ...",
            sentenceTemplate = LessonRealSentenceGenerator.SentenceTemplate(
                ruPattern = "Это {noun}",
                bgPattern = listOf(
                    LessonRealSentenceGenerator.Token.Fixed("Това"),
                    LessonRealSentenceGenerator.Token.Fixed("е"),
                    LessonRealSentenceGenerator.Token.NounBg,
                ),
            ),
        ),
        Lesson2TemplateEntry(
            category = Lesson2TemplateCategory.DEMO,
            hint = "💡 это не ... → Това не е ...",
            sentenceTemplate = LessonRealSentenceGenerator.SentenceTemplate(
                ruPattern = "Это не {noun}",
                bgPattern = listOf(
                    LessonRealSentenceGenerator.Token.Fixed("Това"),
                    LessonRealSentenceGenerator.Token.Fixed("не"),
                    LessonRealSentenceGenerator.Token.Fixed("е"),
                    LessonRealSentenceGenerator.Token.NounBg,
                ),
            ),
        ),
        Lesson2TemplateEntry(
            category = Lesson2TemplateCategory.DEMO,
            hint = "💡 вопрос: Това + слово + ли + е",
            sentenceTemplate = LessonRealSentenceGenerator.SentenceTemplate(
                ruPattern = "Это {noun}?",
                bgPattern = listOf(
                    LessonRealSentenceGenerator.Token.Fixed("Това"),
                    LessonRealSentenceGenerator.Token.NounBg,
                    LessonRealSentenceGenerator.Token.Fixed("ли"),
                    LessonRealSentenceGenerator.Token.Fixed("е"),
                ),
            ),
        ),
    )

    private val subjectForms = listOf(
        Triple("Аз", "Я", "съм"),
        Triple("Ти", "Ты", "си"),
        Triple("Той", "Он", "е"),
        Triple("Тя", "Она", "е"),
        Triple("То", "Оно", "е"),
        Triple("Ние", "Мы", "сме"),
        Triple("Вие", "Вы", "сте"),
        Triple("Те", "Они", "са"),
    )

    private val places = listOf(
        "вкъщи" to "дома",
        "тук" to "здесь",
        "в училище" to "в школе",
        "в града" to "в городе",
        "на работа" to "на работе",
        "в офиса" to "в офисе",
        "в университета" to "в университете",
        "в магазина" to "в магазине",
        "в болницата" to "в больнице",
        "в къщата" to "в доме",
        "с приятел" to "с другом",
    )

    private val roles = listOf(
        "лекар" to "врач",
        "учител" to "учитель",
        "студент" to "студент",
        "приятел" to "друг",
        "ученик" to "ученик",
        "колега" to "коллега",
        "шофьор" to "водитель",
        "продавач" to "продавец",
    )

    private val nouns = listOf(
        "книга" to "книга",
        "кафе" to "кофе",
        "вода" to "вода",
        "хляб" to "хлеб",
        "телефон" to "телефон",
        "чай" to "чай",
        "сок" to "сок",
        "филм" to "фильм",
        "училище" to "школа",
        "магазин" to "магазин",
        "кола" to "машина",
        "болница" to "больница",
    )

    fun generateExercises(
        exerciseLocale: LessonExerciseLocale = LessonExerciseLocale.Russian,
    ): List<LessonExercise> {
        val distractorPool = buildDistractorPool()
        return (1..100).map { id ->
            generateExercise(id, distractorPool, exerciseLocale)
        }
    }

    private fun generateExercise(
        id: Int,
        distractorPool: List<String>,
        exerciseLocale: LessonExerciseLocale,
    ): LessonExercise {
        val template = templates[(id - 1) % templates.size]
        val subjectTriple = subjectForms[((id - 1) / templates.size) % subjectForms.size]
        val subjectBg = subjectTriple.first
        val subjectRu = subjectTriple.second
        val verbBg = subjectTriple.third

        val cycleIndex = ((id - 1) / (templates.size * subjectForms.size))
        val placePair = places[cycleIndex % places.size]
        val rolePair = roles[cycleIndex % roles.size]
        val nounPair = nouns[cycleIndex % nouns.size]

        val lexicon = LessonRealSentenceGenerator.Lexicon(
            subject = LessonRealSentenceGenerator.SubjectForms(
                bg = subjectBg,
                ru = subjectRu,
            ),
            verb = LessonRealSentenceGenerator.VerbForms(
                bg = verbBg,
                ru = "",
            ),
            placeBg = if (template.category == Lesson2TemplateCategory.PLACE) placePair.first else null,
            placeRu = if (template.category == Lesson2TemplateCategory.PLACE) placePair.second else null,
            nounBg = when (template.category) {
                Lesson2TemplateCategory.PLACE -> null
                Lesson2TemplateCategory.ROLE -> rolePair.first
                Lesson2TemplateCategory.DEMO -> nounPair.first
            },
            nounRu = when (template.category) {
                Lesson2TemplateCategory.PLACE -> null
                Lesson2TemplateCategory.ROLE -> rolePair.second
                Lesson2TemplateCategory.DEMO -> nounPair.second
            },
        )

        return LessonRealSentenceGenerator.buildExercise(
            id = id,
            template = template.sentenceTemplate,
            lexicon = lexicon,
            sourceTextOverride = buildSourceText(
                template = template,
                subjectRu = subjectRu,
                placeRu = placePair.second,
                roleRu = rolePair.second,
                nounRu = nounPair.second,
                exerciseLocale = exerciseLocale,
            ),
            distractorPool = distractorPool,
            totalWords = 8,
            hint = template.hint,
        )
    }

    private fun buildSourceText(
        template: Lesson2TemplateEntry,
        subjectRu: String,
        placeRu: String,
        roleRu: String,
        nounRu: String,
        exerciseLocale: LessonExerciseLocale,
    ): String {
        val subject = if (exerciseLocale == LessonExerciseLocale.Ukrainian) {
            when (subjectRu) {
                "Я" -> "Я"
                "Ты" -> "Ти"
                "Он" -> "Він"
                "Она" -> "Вона"
                "Оно" -> "Воно"
                "Мы" -> "Ми"
                "Вы" -> "Ви"
                "Они" -> "Вони"
                else -> subjectRu
            }
        } else {
            subjectRu
        }

        val place = if (exerciseLocale == LessonExerciseLocale.Ukrainian) {
            when (placeRu) {
                "дома" -> "вдома"
                "здесь" -> "тут"
                "в школе" -> "в школі"
                "в городе" -> "в місті"
                "на работе" -> "на роботі"
                "в офисе" -> "в офісі"
                "в университете" -> "в університеті"
                "в магазине" -> "в магазині"
                "в больнице" -> "в лікарні"
                "в доме" -> "в будинку"
                "с другом" -> "з другом"
                else -> placeRu
            }
        } else {
            placeRu
        }

        val role = if (exerciseLocale == LessonExerciseLocale.Ukrainian) {
            when (roleRu) {
                "врач" -> "лікар"
                "учитель" -> "учитель"
                "студент" -> "студент"
                "друг" -> "друг"
                "ученик" -> "учень"
                "коллега" -> "колега"
                "водитель" -> "водій"
                "продавец" -> "продавець"
                else -> roleRu
            }
        } else {
            roleRu
        }

        val noun = if (exerciseLocale == LessonExerciseLocale.Ukrainian) {
            when (nounRu) {
                "книга" -> "книга"
                "кофе" -> "кава"
                "вода" -> "вода"
                "хлеб" -> "хліб"
                "телефон" -> "телефон"
                "чай" -> "чай"
                "сок" -> "сік"
                "фильм" -> "фільм"
                "школа" -> "школа"
                "магазин" -> "магазин"
                "машина" -> "машина"
                "больница" -> "лікарня"
                else -> nounRu
            }
        } else {
            nounRu
        }

        return when (template.category) {
            Lesson2TemplateCategory.PLACE -> when {
                template.sentenceTemplate.ruPattern.contains(" не ") -> "$subject не $place"
                template.sentenceTemplate.ruPattern.endsWith("?") -> "$subject $place?"
                else -> "$subject $place"
            }

            Lesson2TemplateCategory.ROLE -> when {
                template.sentenceTemplate.ruPattern.contains(" не ") -> "$subject не $role"
                template.sentenceTemplate.ruPattern.endsWith("?") -> "$subject $role?"
                else -> "$subject $role"
            }

            Lesson2TemplateCategory.DEMO -> when {
                template.sentenceTemplate.ruPattern.contains(" не ") -> if (exerciseLocale == LessonExerciseLocale.Ukrainian) {
                    "Це не $noun"
                } else {
                    "Это не $noun"
                }

                template.sentenceTemplate.ruPattern.endsWith("?") -> if (exerciseLocale == LessonExerciseLocale.Ukrainian) {
                    "Це $noun?"
                } else {
                    "Это $noun?"
                }

                else -> if (exerciseLocale == LessonExerciseLocale.Ukrainian) {
                    "Це $noun"
                } else {
                    "Это $noun"
                }
            }
        }
    }

    private fun buildDistractorPool(): List<String> {
        return buildList {
            addAll(subjectForms.map { it.first })
            addAll(subjectForms.map { it.third })
            addAll(listOf("не", "ли", "Това", "е"))
            addAll(places.map { it.first })
            addAll(roles.map { it.first })
            addAll(nouns.map { it.first })
        }.distinct()
    }
}
