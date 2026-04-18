package ua.tarch64.beejournal.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.toArgb

fun String.toColor(): Color {
    return Color(removePrefix("#").toLong(16) or 0xFF000000)
}

fun Color.toHex(): String {
    return String.format("#%06X", toArgb() and 0xFFFFFF)
}

fun Color.lighten(fraction: Float): Color {
    return lerp(this, Color.White, fraction)
}
