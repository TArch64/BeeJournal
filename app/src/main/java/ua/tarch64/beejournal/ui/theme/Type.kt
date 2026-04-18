package ua.tarch64.beejournal.ui.theme

import androidx.compose.material3.Typography

val Typography = Typography().run {
    val scale = 1.5f

    copy(
        bodyLarge = bodyLarge.copy(fontSize = bodyLarge.fontSize * scale),
        bodyMedium = bodyMedium.copy(fontSize = bodyMedium.fontSize * scale),
        bodySmall = bodySmall.copy(fontSize = bodySmall.fontSize * scale),
        titleLarge = titleLarge.copy(fontSize = titleLarge.fontSize * 1.3f),
        titleMedium = titleMedium.copy(fontSize = titleMedium.fontSize * scale),
        titleSmall = titleSmall.copy(fontSize = titleSmall.fontSize * scale),
        labelLarge = labelLarge.copy(fontSize = labelLarge.fontSize * scale),
        labelMedium = labelMedium.copy(fontSize = labelMedium.fontSize * scale),
        labelSmall = labelSmall.copy(fontSize = labelSmall.fontSize * scale),
    )
}