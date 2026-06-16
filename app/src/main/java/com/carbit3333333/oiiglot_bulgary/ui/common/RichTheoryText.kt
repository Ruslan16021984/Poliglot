package com.carbit3333333.oiiglot_bulgary.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration.Companion.None
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

enum class RichTheorySpanKind {
    Plain,
    Phrase,
    Ending,
    Keyword,
}

data class RichTheorySegment(
    val text: String,
    val kind: RichTheorySpanKind,
)

fun parseRichTheorySegments(text: String): List<RichTheorySegment> {
    return parseRichTheorySegments(
        text = text,
        defaultKind = RichTheorySpanKind.Plain,
    ).mergeAdjacentPlainSegments()
}

private fun parseRichTheorySegments(
    text: String,
    defaultKind: RichTheorySpanKind,
): List<RichTheorySegment> {
    val result = mutableListOf<RichTheorySegment>()
    var index = 0

    fun appendDefault(value: String) {
        if (value.isNotEmpty()) {
            result += RichTheorySegment(value, defaultKind)
        }
    }

    while (index < text.length) {
        val marker = richTheoryMarkers
            .filter { text.startsWith(it.open, index) }
            .firstOrNull()

        if (marker == null) {
            val nextMarkerIndex = richTheoryMarkers
                .map { text.indexOf(it.open, startIndex = index).takeIf { found -> found >= 0 } ?: text.length }
                .min()
            appendDefault(text.substring(index, nextMarkerIndex))
            index = nextMarkerIndex
            continue
        }

        val contentStart = index + marker.open.length
        val closeIndex = text.indexOf(marker.close, startIndex = contentStart)
        if (closeIndex < 0) {
            appendDefault(text.substring(index))
            break
        }

        val content = text.substring(contentStart, closeIndex)
        if (content.isBlank()) {
            appendDefault(text.substring(index, closeIndex + marker.close.length))
        } else {
            result += parseRichTheorySegments(
                text = content,
                defaultKind = marker.kind,
            )
        }
        index = closeIndex + marker.close.length
    }

    return result.mergeAdjacentPlainSegments()
}

@Composable
fun RichTheoryText(
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    val colorScheme = MaterialTheme.colorScheme
    val phraseColor = Color(0xFF1F8A54)
    val endingColor = Color(0xFFE47A1F)
    val keywordColor = colorScheme.primary

    val annotated = buildAnnotatedString {
        parseRichTheorySegments(text).forEach { segment ->
            when (segment.kind) {
                RichTheorySpanKind.Plain -> append(segment.text)
                RichTheorySpanKind.Phrase -> withStyle(
                    SpanStyle(
                        color = phraseColor,
                        background = phraseColor.copy(alpha = 0.12f),
                        fontWeight = FontWeight.SemiBold,
                    )
                ) {
                    append(segment.text)
                }
                RichTheorySpanKind.Ending -> withStyle(
                    SpanStyle(
                        color = endingColor,
                        background = endingColor.copy(alpha = 0.14f),
                        fontWeight = FontWeight.Bold,
                    )
                ) {
                    append(segment.text)
                }
                RichTheorySpanKind.Keyword -> withStyle(
                    SpanStyle(
                        color = keywordColor,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = None,
                    )
                ) {
                    append(segment.text)
                }
            }
        }
    }

    Text(
        text = annotated,
        style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 26.sp),
        color = textColor,
    )
}

private data class RichTheoryMarker(
    val open: String,
    val close: String,
    val kind: RichTheorySpanKind,
)

private val richTheoryMarkers = listOf(
    RichTheoryMarker("[[", "]]", RichTheorySpanKind.Phrase),
    RichTheoryMarker("{{", "}}", RichTheorySpanKind.Ending),
    RichTheoryMarker("<<", ">>", RichTheorySpanKind.Keyword),
)

private fun List<RichTheorySegment>.mergeAdjacentPlainSegments(): List<RichTheorySegment> {
    if (isEmpty()) return this

    val merged = mutableListOf<RichTheorySegment>()
    forEach { segment ->
        val previous = merged.lastOrNull()
        if (previous?.kind == RichTheorySpanKind.Plain && segment.kind == RichTheorySpanKind.Plain) {
            merged[merged.lastIndex] = previous.copy(text = previous.text + segment.text)
        } else {
            merged += segment
        }
    }
    return merged
}
