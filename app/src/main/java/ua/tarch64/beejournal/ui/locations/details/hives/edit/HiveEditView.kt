package ua.tarch64.beejournal.ui.locations.details.hives.edit

import androidx.compose.runtime.Composable
import ua.tarch64.beejournal.models.HiveModel
import ua.tarch64.beejournal.services.HivesService
import ua.tarch64.beejournal.ui.locations.details.hives.common.HiveFormView

@Composable
fun HiveEditView(
    onBack: () -> Unit,
    hive: HiveModel
) {
    suspend fun saveHive(hive: HiveModel) {
        HivesService.update(hive)
        onBack()
    }

    HiveFormView(
        hive = hive,
        saveLabel = "Зберегти",
        onBack = onBack,
        onSave = ::saveHive
    )
}
