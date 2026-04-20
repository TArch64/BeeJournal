package ua.tarch64.beejournal.ui.locations.details.hives.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import ua.tarch64.beejournal.models.lighten

@Composable
fun HiveSpotView(position: Int, color: Color) {
    val backgroundColor = remember(color) { color.lighten(0.2f) }

    val backgroundAnimation by animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = tween(200)
    )

    val textColor = if (backgroundAnimation.luminance() > 0.5f) {
        Color.Black
    } else {
        Color.White
    }

    OutlinedCard(
        modifier = Modifier
            .width(60.dp)
            .aspectRatio(1f),
        
        colors = CardDefaults.outlinedCardColors(
            containerColor = backgroundAnimation
        ),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("$position", color = textColor)
        }
    }
}