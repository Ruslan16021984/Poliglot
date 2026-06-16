package com.carbit3333333.oiiglot_bulgary.model.theory

import com.carbit3333333.oiiglot_bulgary.model.TheoryBlock
import kotlinx.serialization.Serializable

@Serializable
data class StructuredTheoryLessonAsset(
    val lessonId: Int,
    val blocks: List<StructuredTheoryBlock>,
)

@Serializable
data class StructuredTheoryBlock(
    val kind: StructuredTheoryBlockKind = StructuredTheoryBlockKind.Paragraph,
    val title: String? = null,
    val segments: List<StructuredTheorySegment> = emptyList(),
)

@Serializable
data class StructuredTheorySegment(
    val text: String,
    val style: StructuredTheorySegmentStyle = StructuredTheorySegmentStyle.Plain,
)

@Serializable
enum class StructuredTheoryBlockKind {
    Paragraph,
    Rule,
    Example,
    Note,
}

@Serializable
enum class StructuredTheorySegmentStyle {
    Plain,
    Keyword,
    Ending,
    Phrase,
}

fun TheoryBlock.toStructuredTheoryBlock(): StructuredTheoryBlock {
    return StructuredTheoryBlock(
        kind = StructuredTheoryBlockKind.Paragraph,
        title = title,
        segments = listOf(
            StructuredTheorySegment(
                text = text.orEmpty(),
                style = StructuredTheorySegmentStyle.Plain,
            )
        ),
    )
}

fun List<TheoryBlock>.toStructuredTheoryBlocks(): List<StructuredTheoryBlock> {
    return map { it.toStructuredTheoryBlock() }
}
