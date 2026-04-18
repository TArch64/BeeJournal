package ua.tarch64.beejournal.ui.base.form

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CounterField(
    modifier: Modifier = Modifier,
    value: Int,
    minValue: Int,
    maxValue: Int,
    onValueChange: (Int) -> Unit
) {
    fun change(delta: Int) {
        val newValue = (value + delta).coerceIn(
            minimumValue = minValue,
            maximumValue = maxValue
        )

        onValueChange(newValue)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier.size(48.dp),
            onClick = { change(-1) }
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Default.Remove,
                contentDescription = "Менше"
            )
        }

        Text(
            text = "$value",
            modifier = Modifier.widthIn(min = 32.dp),
            textAlign = TextAlign.Center
        )

        IconButton(
            modifier = Modifier.size(48.dp),
            onClick = { change(1) }
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Default.Add,
                contentDescription = "Більше"
            )
        }
    }
}