package ua.tarch64.beejournal.ui.hives.add


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ua.tarch64.beejournal.models.HiveModel
import ua.tarch64.beejournal.services.HivesService
import ua.tarch64.beejournal.ui.base.form.ColorSwatches
import ua.tarch64.beejournal.ui.base.form.CounterField
import ua.tarch64.beejournal.ui.base.form.FormView
import ua.tarch64.beejournal.ui.base.form.OutlinedLabelRow
import ua.tarch64.beejournal.ui.hives.common.HiveFramesField
import ua.tarch64.beejournal.ui.hives.common.HiveSpotView

@Composable
fun HiveAddView(onBack: () -> Unit) {
    val position = remember { HivesService.instance.nextPosition }
    var frames by remember { mutableStateOf(listOf(0.toUInt())) }
    var children by remember { mutableStateOf(0.toUInt()) }
    var honey by remember { mutableStateOf(0.toUInt()) }
    var color by remember { mutableStateOf(Color.White) }

    suspend fun addHive() {
        onBack()
    }

    FormView(
        title = "Новий Вулик",
        onBack = onBack,
        onSave = ::addHive,
        saveLabel = { Text("Додати") },
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
                value = children.toInt(),
                minValue = 0,
                maxValue = 99,
                onValueChange = { children = it.toUInt() }
            )
        }

        OutlinedLabelRow(label = { Text("Мед") }) {
            CounterField(
                value = honey.toInt(),
                minValue = 0,
                maxValue = 99,
                onValueChange = { honey = it.toUInt() }
            )
        }
    }
}
