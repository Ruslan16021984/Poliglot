package com.carbit3333333.oiiglot_bulgary.ui.lessons

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class LessonSessionPalette(
    val pageBackground: Color,
    val topBar: Color,
    val topBarText: Color,
    val counterSurface: Color,
    val counterCorrect: Color,
    val counterWrong: Color,
    val cardSurface: Color,
    val cardBorder: Color,
    val titleText: Color,
    val bodyText: Color,
    val instructionSurface: Color,
    val instructionText: Color,
    val selectedChipSurface: Color,
    val selectedChipText: Color,
    val primaryButton: Color,
    val primaryButtonText: Color,
    val disabledButton: Color,
    val disabledButtonText: Color,
    val progressIdle: Color,
    val progressCorrect: Color,
    val progressWrong: Color,
)

@Composable
fun rememberLessonSessionPalette(): LessonSessionPalette {
    val colorScheme = MaterialTheme.colorScheme
    val isDarkTheme = colorScheme.background.red < 0.5f

    return if (isDarkTheme) {
        LessonSessionPalette(
            pageBackground = Color(0xFF11141C),
            topBar = Color(0xFF1C2333),
            topBarText = Color(0xFFF4F6FF),
            counterSurface = Color(0xFFF4F6FF),
            counterCorrect = Color(0xFF3E5487),
            counterWrong = Color(0xFFB66E72),
            cardSurface = Color(0xFF1A2230),
            cardBorder = Color(0xFF2E3B55),
            titleText = Color(0xFFF2F5FF),
            bodyText = Color(0xFFB9C1D4),
            instructionSurface = Color(0xFF243328),
            instructionText = Color(0xFFD9F2D8),
            selectedChipSurface = Color(0xFF2A3954),
            selectedChipText = Color(0xFFE3ECFF),
            primaryButton = Color(0xFF7089C5),
            primaryButtonText = Color(0xFF11141C),
            disabledButton = Color(0xFF31394A),
            disabledButtonText = Color(0xFF8A92A4),
            progressIdle = Color(0xFF31394A),
            progressCorrect = Color(0xFF5BC46A),
            progressWrong = Color(0xFFE08D8D),
        )
    } else {
        LessonSessionPalette(
            pageBackground = Color(0xFFF8F8FE),
            topBar = Color(0xFF566BA1),
            topBarText = Color(0xFFFDFEFF),
            counterSurface = Color(0xFFFDFEFF),
            counterCorrect = Color(0xFF566BA1),
            counterWrong = Color(0xFFB86C6C),
            cardSurface = Color(0xFFF4F6FF),
            cardBorder = Color(0xFFDCE1F2),
            titleText = Color(0xFF1F2430),
            bodyText = Color(0xFF666E7E),
            instructionSurface = Color(0xFFD9EBD7),
            instructionText = Color(0xFF2E3B2E),
            selectedChipSurface = Color(0xFFDCE7FF),
            selectedChipText = Color(0xFF2B3C63),
            primaryButton = Color(0xFF566BA1),
            primaryButtonText = Color(0xFFFDFEFF),
            disabledButton = Color(0xFFD9DCE4),
            disabledButtonText = Color(0xFF9A9EAA),
            progressIdle = Color(0xFFD7DBE6),
            progressCorrect = Color(0xFF4CAF50),
            progressWrong = Color(0xFFE98B8B),
        )
    }
}
