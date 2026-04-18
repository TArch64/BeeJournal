package ua.tarch64.beejournal.ui.locations.details.hives.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ua.tarch64.beejournal.models.HiveModel
import ua.tarch64.beejournal.models.toHex
import ua.tarch64.beejournal.ui.base.form.ColorSwatches
import ua.tarch64.beejournal.ui.base.form.CounterField
import ua.tarch64.beejournal.ui.base.form.FormView
import ua.tarch64.beejournal.ui.base.form.OutlinedLabelRow

@Composable
fun HiveFormView(
    hive: HiveModel,
    saveLabel: String,
    onBack: () -> Unit,
    onSave: suspend (hive: HiveModel) -> Unit,
) {
    val position = remember { hive.position }
    var frames by remember { mutableStateOf(hive.frames) }
    var children by remember { mutableIntStateOf(hive.children) }
    var honey by remember { mutableIntStateOf(hive.honey) }
    var color by remember { mutableStateOf(hive.color) }

    suspend fun save() = onSave(
        hive.copy(
            frames = frames,
            children = children,
            honey = honey,
            colorHex = color.toHex(),
        )
    )

    FormView(
        title = "Новий Вулик",
        onBack = onBack,
        onSave = ::save,
        saveLabel = { Text(saveLabel) },
    ) {
        HiveSpotView(
            position = position,
            color = color,
        )

        ColorSwatches(
            modifier = Modifier.fillMaxWidth(),
            value = color,
            variants = HiveModel.COLORS,
            onValueChange = { color = it }
        )

        HiveFramesField(
            value = frames,
            onValueChange = { frames = it }
        )

        OutlinedLabelRow(label = { Text("Розплід") }) {
            CounterField(
                value = children,
                minValue = 0,
                maxValue = 99,
                onValueChange = { children = it }
            )
        }

        OutlinedLabelRow(label = { Text("Мед") }) {
            CounterField(
                value = honey,
                minValue = 0,
                maxValue = 99,
                onValueChange = { honey = it }
            )
        }
    }
}