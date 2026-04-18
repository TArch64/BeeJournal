package ua.tarch64.beejournal.ui.locations.details.hives.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.tarch64.beejournal.ui.base.form.CounterField
import ua.tarch64.beejournal.ui.base.form.LabelRow

@Composable
fun HiveFramesField(
    modifier: Modifier = Modifier,
    value: List<Int>,
    onValueChange: (value: List<Int>) -> Unit
) {
    fun change(index: Int, frames: Int) {
        val updated = value.toMutableList().apply { set(index, frames) }
        onValueChange(updated)
    }

    fun add() {
        onValueChange(value.plus(0))
    }

    fun remove(index: Int) {
        val updated = value.toMutableList().apply { removeAt(index = index) }
        onValueChange(updated)
    }

    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )
    ) {
        Column {
            value.forEachIndexed { index, frames ->
                LabelRow(
                    label = {
                        if (index == 0) {
                            Text("Рамки")
                        } else {
                            IconButton(
                                modifier = Modifier
                                    .size(48.dp)
                                    .offset(x = (-12).dp),

                                onClick = { remove(index) }
                            ) {
                                Icon(
                                    modifier = Modifier.size(32.dp),
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Видалити"
                                )
                            }
                        }
                    }
                ) {
                    CounterField(
                        value = frames,
                        minValue = 0,
                        maxValue = 99,
                        onValueChange = { change(index, it) }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = ::add) {
                    Text("Додати")
                }
            }
        }
    }
}
