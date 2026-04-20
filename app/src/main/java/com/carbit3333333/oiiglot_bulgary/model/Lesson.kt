package com.carbit3333333.oiiglot_bulgary.model

data class Lesson(
    val id: Int,
    val title: String,
    val subtitle: String,
    val theory: List<TheoryBlock> = emptyList(),
    val isCompleted: Boolean = false,
    val isLocked: Boolean = false,
    val bestScore: Float? = null,
    val currentProgress: Int = 0,
    val totalProgress: Int = 0
) {
    val progressPercent: Int
        get() = if (totalProgress > 0) {
            (currentProgress * 100) / totalProgress
        } else {
            0
        }
}
