package com.carbit3333333.oiiglot_bulgary.data.lesson_session.support

import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

object LessonRealSentenceGenerator {

    data class SentenceTemplate(
        val ruPattern: String,
        val bgPattern: List<Token>
    )

    sealed class Token {
        data class Fixed(val value: String) : Token()
        data object SubjectBg : Token()
        data object SubjectRu : Token()
        data object VerbBg : Token()
        data object VerbRu : Token()
        data object PlaceBg : Token()
        data object PlaceRu : Token()
        data object ObjectBg : Token()
        data object ObjectRu : Token()
        data object AdjectiveBg : Token()
        data object AdjectiveRu : Token()
        data object CompareTargetBg : Token()
        data object CompareTargetRu : Token()
        data object PossessiveBg : Token()
        data object PossessiveRu : Token()
        data object NounBg : Token()
        data object NounRu : Token()
    }

    data class SubjectForms(
        val bg: String,
        val ru: String
    )

    data class VerbForms(
        val bg: String,
        val ru: String
    )

    data class Lexicon(
        val subject: SubjectForms,
        val verb: VerbForms? = null,
        val placeBg: String? = null,
        val placeRu: String? = null,
        val objBg: String? = null,
        val objRu: String? = null,
        val adjectiveBg: String? = null,
        val adjectiveRu: String? = null,
        val compareTargetBg: String? = null,
        val compareTargetRu: String? = null,
        val possessiveBg: String? = null,
        val possessiveRu: String? = null,
        val nounBg: String? = null,
        val nounRu: String? = null
    )

    fun buildExercise(
        id: Int,
        template: SentenceTemplate,
        lexicon: Lexicon,
        distractorPool: List<String>,
        totalWords: Int = 8,
        hint: String? = null
    ): LessonExercise {
        val correctWords = template.bgPattern.map { token ->
            resolveBgToken(token, lexicon)
        }

        val availableWords = buildAvailableWords(
            correctWords = correctWords,
            distractorPool = distractorPool,
            totalWords = totalWords
        )

        val sourceText = buildRussianText(template.ruPattern, lexicon)

        return LessonExercise(
            id = id,
            sourceText = sourceText,
            instruction = "Переведите предложение",
            correctAnswerWords = correctWords,
            availableWords = availableWords,
            hint = hint
        )
    }

    private fun resolveBgToken(token: Token, lexicon: Lexicon): String {
        return when (token) {
            is Token.Fixed -> token.value
            Token.SubjectBg -> lexicon.subject.bg
            Token.SubjectRu -> lexicon.subject.ru
            Token.VerbBg -> requireNotNull(lexicon.verb?.bg)
            Token.VerbRu -> requireNotNull(lexicon.verb?.ru)
            Token.PlaceBg -> requireNotNull(lexicon.placeBg)
            Token.PlaceRu -> requireNotNull(lexicon.placeRu)
            Token.ObjectBg -> requireNotNull(lexicon.objBg)
            Token.ObjectRu -> requireNotNull(lexicon.objRu)
            Token.AdjectiveBg -> requireNotNull(lexicon.adjectiveBg)
            Token.AdjectiveRu -> requireNotNull(lexicon.adjectiveRu)
            Token.CompareTargetBg -> requireNotNull(lexicon.compareTargetBg)
            Token.CompareTargetRu -> requireNotNull(lexicon.compareTargetRu)
            Token.PossessiveBg -> requireNotNull(lexicon.possessiveBg)
            Token.PossessiveRu -> requireNotNull(lexicon.possessiveRu)
            Token.NounBg -> requireNotNull(lexicon.nounBg)
            Token.NounRu -> requireNotNull(lexicon.nounRu)
        }
    }

    private fun buildRussianText(
        pattern: String,
        lexicon: Lexicon
    ): String {
        return pattern
            .replace("{subject}", lexicon.subject.ru)
            .replace("{verb}", lexicon.verb?.ru.orEmpty())
            .replace("{place}", lexicon.placeRu.orEmpty())
            .replace("{object}", lexicon.objRu.orEmpty())
            .replace("{adjective}", lexicon.adjectiveRu.orEmpty())
            .replace("{compareTarget}", lexicon.compareTargetRu.orEmpty())
            .replace("{possessive}", lexicon.possessiveRu.orEmpty())
            .replace("{noun}", lexicon.nounRu.orEmpty())
            .replace("  ", " ")
            .trim()
    }

    private fun buildAvailableWords(
        correctWords: List<String>,
        distractorPool: List<String>,
        totalWords: Int
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
            "Available words size must be $totalWords, but was ${result.size}"
        }

        require(uniqueCorrectWords.all { it in result }) {
            "Not all correct words were added"
        }

        return result
    }
}