package ua.tarch64.beejournal.ui.hives.add


import androidx.compose.runtime.Composable
import ua.tarch64.beejournal.models.HiveModel
import ua.tarch64.beejournal.services.HivesService
import ua.tarch64.beejournal.ui.hives.common.HiveFormView

@Composable
fun HiveAddView(onBack: () -> Unit) {
    suspend fun addHive(hive: HiveModel) {
        HivesService.instance.add(hive)
        onBack()
    }

    HiveFormView(
        hive = HiveModel(
            position = HivesService.instance.nextPosition,
            frames = listOf(0),
            children = 0,
            honey = 0,
            colorHex = "#ffffff"
        ),

        saveLabel = "Додати",
        onBack = onBack,
        onSave = ::addHive
    )
}
