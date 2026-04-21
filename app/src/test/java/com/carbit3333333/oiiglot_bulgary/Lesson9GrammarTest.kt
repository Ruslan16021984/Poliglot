package com.carbit3333333.oiiglot_bulgary

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson9NumberAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson9ObjectAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators.applyNumber
import org.junit.Assert.assertEquals
import org.junit.Test

class Lesson9GrammarTest {

    private val masculineNoun = Lesson9ObjectAsset(
        gender = "masculine",
        singular = "хляб",
        plural = "хлябове",
        countForm = "хляба",
        ruSingular = "хлеб",
        ruPlural = "хлеба",
        ruMany = "хлебов"
    )

    private val feminineNoun = Lesson9ObjectAsset(
        gender = "feminine",
        singular = "книга",
        plural = "книги",
        countForm = "книги",
        ruSingular = "книгу",
        ruPlural = "книги",
        ruMany = "книг"
    )

    private val neuterNoun = Lesson9ObjectAsset(
        gender = "neuter",
        singular = "писмо",
        plural = "писма",
        countForm = "писма",
        ruSingular = "письмо",
        ruPlural = "письма",
        ruMany = "писем"
    )

    @Test
    fun `applyNumber uses masculine form for one`() {
        val phrase = applyNumber(
            number = Lesson9NumberAsset(
                value = 1,
                bgMasculine = "един",
                bgFeminine = "една",
                bgNeuter = "едно",
                ruMasculine = "один",
                ruFeminine = "одну",
                ruNeuter = "одно"
            ),
            noun = masculineNoun
        )

        assertEquals("един", phrase.numberBg)
        assertEquals("хляб", phrase.objectBg)
        assertEquals("один", phrase.numberRu)
        assertEquals("хлеб", phrase.objectRu)
    }

    @Test
    fun `applyNumber uses feminine form for one`() {
        val phrase = applyNumber(
            number = Lesson9NumberAsset(
                value = 1,
                bgMasculine = "един",
                bgFeminine = "една",
                bgNeuter = "едно",
                ruMasculine = "один",
                ruFeminine = "одну",
                ruNeuter = "одно"
            ),
            noun = feminineNoun
        )

        assertEquals("една", phrase.numberBg)
        assertEquals("книга", phrase.objectBg)
        assertEquals("одну", phrase.numberRu)
        assertEquals("книгу", phrase.objectRu)
    }

    @Test
    fun `applyNumber uses neuter form for one`() {
        val phrase = applyNumber(
            number = Lesson9NumberAsset(
                value = 1,
                bgMasculine = "един",
                bgFeminine = "една",
                bgNeuter = "едно",
                ruMasculine = "один",
                ruFeminine = "одну",
                ruNeuter = "одно"
            ),
            noun = neuterNoun
        )

        assertEquals("едно", phrase.numberBg)
        assertEquals("писмо", phrase.objectBg)
        assertEquals("одно", phrase.numberRu)
        assertEquals("письмо", phrase.objectRu)
    }

    @Test
    fun `applyNumber uses count form and gendered two`() {
        val phrase = applyNumber(
            number = Lesson9NumberAsset(
                value = 2,
                bgMasculine = "два",
                bgFeminine = "две",
                bgNeuter = "две",
                ruMasculine = "два",
                ruFeminine = "две",
                ruNeuter = "два"
            ),
            noun = feminineNoun
        )

        assertEquals("две", phrase.numberBg)
        assertEquals("книги", phrase.objectBg)
        assertEquals("две", phrase.numberRu)
        assertEquals("книги", phrase.objectRu)
    }

    @Test
    fun `applyNumber uses few form for values from two to four in russian`() {
        val phrase = applyNumber(
            number = Lesson9NumberAsset(
                value = 4,
                bgMasculine = "четири",
                bgFeminine = "четири",
                bgNeuter = "четири",
                ruMasculine = "четыре",
                ruFeminine = "четыре",
                ruNeuter = "четыре"
            ),
            noun = masculineNoun
        )

        assertEquals("четири", phrase.numberBg)
        assertEquals("хляба", phrase.objectBg)
        assertEquals("четыре", phrase.numberRu)
        assertEquals("хлеба", phrase.objectRu)
    }

    @Test
    fun `applyNumber uses many form for values above four in russian`() {
        val phrase = applyNumber(
            number = Lesson9NumberAsset(
                value = 5,
                bgMasculine = "пет",
                bgFeminine = "пет",
                bgNeuter = "пет",
                ruMasculine = "пять",
                ruFeminine = "пять",
                ruNeuter = "пять"
            ),
            noun = masculineNoun
        )

        assertEquals("пет", phrase.numberBg)
        assertEquals("хляба", phrase.objectBg)
        assertEquals("пять", phrase.numberRu)
        assertEquals("хлебов", phrase.objectRu)
    }

    @Test
    fun `applyNumber uses many form for feminine nouns in russian`() {
        val phrase = applyNumber(
            number = Lesson9NumberAsset(
                value = 8,
                bgMasculine = "осем",
                bgFeminine = "осем",
                bgNeuter = "осем",
                ruMasculine = "восемь",
                ruFeminine = "восемь",
                ruNeuter = "восемь"
            ),
            noun = feminineNoun
        )

        assertEquals("восемь", phrase.numberRu)
        assertEquals("книг", phrase.objectRu)
    }

    @Test
    fun `applyNumber uses many form for neuter nouns in russian`() {
        val phrase = applyNumber(
            number = Lesson9NumberAsset(
                value = 8,
                bgMasculine = "осем",
                bgFeminine = "осем",
                bgNeuter = "осем",
                ruMasculine = "восемь",
                ruFeminine = "восемь",
                ruNeuter = "восемь"
            ),
            noun = neuterNoun
        )

        assertEquals("восемь", phrase.numberRu)
        assertEquals("писем", phrase.objectRu)
    }

    @Test
    fun `applyNumber keeps masculine count form in bulgarian for values above four`() {
        val phrase = applyNumber(
            number = Lesson9NumberAsset(
                value = 8,
                bgMasculine = "осем",
                bgFeminine = "осем",
                bgNeuter = "осем",
                ruMasculine = "восемь",
                ruFeminine = "восемь",
                ruNeuter = "восемь"
            ),
            noun = masculineNoun
        )

        assertEquals("осем", phrase.numberBg)
        assertEquals("хляба", phrase.objectBg)
    }

    @Test
    fun `applyNumber keeps feminine plural form in bulgarian for values above four`() {
        val phrase = applyNumber(
            number = Lesson9NumberAsset(
                value = 8,
                bgMasculine = "осем",
                bgFeminine = "осем",
                bgNeuter = "осем",
                ruMasculine = "восемь",
                ruFeminine = "восемь",
                ruNeuter = "восемь"
            ),
            noun = feminineNoun
        )

        assertEquals("осем", phrase.numberBg)
        assertEquals("книги", phrase.objectBg)
    }

    @Test
    fun `applyNumber keeps neuter plural form in bulgarian for values above four`() {
        val phrase = applyNumber(
            number = Lesson9NumberAsset(
                value = 8,
                bgMasculine = "осем",
                bgFeminine = "осем",
                bgNeuter = "осем",
                ruMasculine = "восемь",
                ruFeminine = "восемь",
                ruNeuter = "восемь"
            ),
            noun = neuterNoun
        )

        assertEquals("осем", phrase.numberBg)
        assertEquals("писма", phrase.objectBg)
    }
}
