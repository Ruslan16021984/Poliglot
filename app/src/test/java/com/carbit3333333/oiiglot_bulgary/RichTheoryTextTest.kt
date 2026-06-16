package com.carbit3333333.oiiglot_bulgary

import com.carbit3333333.oiiglot_bulgary.ui.common.RichTheorySpanKind
import com.carbit3333333.oiiglot_bulgary.ui.common.parseRichTheorySegments
import org.junit.Assert.assertEquals
import org.junit.Test

class RichTheoryTextTest {

    @Test
    fun `parser converts theory markup to typed segments`() {
        val segments = parseRichTheorySegments(
            "Use [[Сутрин се мия]] with {{се}} and <<влизам>>.",
        )

        assertEquals(
            listOf(
                RichTheorySpanKind.Plain,
                RichTheorySpanKind.Phrase,
                RichTheorySpanKind.Plain,
                RichTheorySpanKind.Ending,
                RichTheorySpanKind.Plain,
                RichTheorySpanKind.Keyword,
                RichTheorySpanKind.Plain,
            ),
            segments.map { it.kind },
        )
        assertEquals("Сутрин се мия", segments[1].text)
        assertEquals("се", segments[3].text)
        assertEquals("влизам", segments[5].text)
    }

    @Test
    fun `parser keeps incomplete markup as plain text`() {
        val segments = parseRichTheorySegments("Start [[without end")

        assertEquals(1, segments.size)
        assertEquals(RichTheorySpanKind.Plain, segments.first().kind)
        assertEquals("Start [[without end", segments.first().text)
    }

    @Test
    fun `parser supports nested markers inside highlighted phrase`() {
        val segments = parseRichTheorySegments("[[Сутрин <<се>> мия.]]")

        assertEquals(
            listOf(
                RichTheorySpanKind.Phrase,
                RichTheorySpanKind.Keyword,
                RichTheorySpanKind.Phrase,
            ),
            segments.map { it.kind },
        )
        assertEquals("Сутрин ", segments[0].text)
        assertEquals("се", segments[1].text)
        assertEquals(" мия.", segments[2].text)
    }
}
