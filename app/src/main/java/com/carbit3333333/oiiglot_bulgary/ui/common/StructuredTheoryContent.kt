package com.carbit3333333.oiiglot_bulgary.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carbit3333333.oiiglot_bulgary.model.theory.StructuredTheoryBlock
import com.carbit3333333.oiiglot_bulgary.model.theory.StructuredTheorySegment
import com.carbit3333333.oiiglot_bulgary.model.theory.StructuredTheorySegmentStyle

@Composable
fun StructuredTheorySectionCard(
    theoryBlocks: List<StructuredTheoryBlock>,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    val sectionTitle = theoryBlocks.firstNotNullOfOrNull { it.title?.takeIf(String::isNotBlank) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            sectionTitle?.let { title ->
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            theoryBlocks.forEachIndexed { index, theoryBlock ->
                StructuredTheoryText(
                    segments = theoryBlock.segments,
                    textColor = colorScheme.onSurface,
                )

                if (index != theoryBlocks.lastIndex) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

fun groupStructuredTheorySections(
    theoryBlocks: List<StructuredTheoryBlock>,
): List<List<StructuredTheoryBlock>> {
    if (theoryBlocks.isEmpty()) return emptyList()

    val grouped = mutableListOf<MutableList<StructuredTheoryBlock>>()

    theoryBlocks.forEach { block ->
        val shouldStartNewSection = block.title?.isNotBlank() == true || grouped.isEmpty()
        if (shouldStartNewSection) {
            grouped += mutableListOf(block)
        } else {
            grouped.last() += block
        }
    }

    return grouped.map { it.toList() }
}

@Composable
fun StructuredTheoryText(
    segments: List<StructuredTheorySegment>,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    val colorScheme = MaterialTheme.colorScheme
    val phraseColor = Color(0xFF1F8A54)
    val endingColor = Color(0xFFE47A1F)
    val keywordColor = colorScheme.primary

    val annotated = buildAnnotatedString {
        segments.forEach { segment ->
            val style = when (segment.style) {
                StructuredTheorySegmentStyle.Plain -> null
                StructuredTheorySegmentStyle.Keyword -> SpanStyle(
                    color = keywordColor,
                    fontWeight = FontWeight.SemiBold,
                )
                StructuredTheorySegmentStyle.Ending -> SpanStyle(
                    color = endingColor,
                    fontWeight = FontWeight.Bold,
                )
                StructuredTheorySegmentStyle.Phrase -> SpanStyle(
                    color = phraseColor,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            if (style == null) {
                append(segment.text)
            } else {
                withStyle(style) {
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
