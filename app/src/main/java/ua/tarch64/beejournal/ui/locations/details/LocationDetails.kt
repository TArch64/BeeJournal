package ua.tarch64.beejournal.ui.locations.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.tarch64.beejournal.models.HiveModel
import ua.tarch64.beejournal.services.ActiveLocationService
import ua.tarch64.beejournal.services.HivesService
import ua.tarch64.beejournal.ui.base.dialogs.ErrorReport
import ua.tarch64.beejournal.ui.base.list.ListEmpty
import ua.tarch64.beejournal.ui.base.menu.BottomSheetController
import ua.tarch64.beejournal.ui.base.menu.rememberBottomSheetController
import ua.tarch64.beejournal.ui.locations.details.hives.add.HiveAddView
import ua.tarch64.beejournal.ui.locations.details.hives.edit.HiveEditView


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LocationDetails(
    locationId: String,
    onBack: () -> Unit
) {
    val addSheetController = rememberBottomSheetController(skipPartiallyExpanded = true)

    val editSheetController = rememberBottomSheetController(skipPartiallyExpanded = true)
    var editingHive by remember { mutableStateOf<HiveModel?>(null) }

    val error = ActiveLocationService.error?.let { HivesService.error }

    DisposableEffect(Unit) {
        ActiveLocationService.load(locationId)
        onDispose { ActiveLocationService.unload() }
    }

    ErrorReport(error)

    DetailsLayout(
        loading = ActiveLocationService.loading || HivesService.loading,
        location = ActiveLocationService.data,
        onLoad = { ActiveLocationService.load(locationId, force = true) },
        onBack = onBack,
        onAdd = addSheetController::show,
        contentLoading = { TextView("Завантажується...") },
        contentError = { TextView(error?.message ?: "") }
    ) {
        if (HivesService.list.isEmpty()) {
            ListEmpty(
                icon = Icons.Default.Home,
                title = "Пусто",
                description = "Натисніть кнопку внизу щоб додати новий вулик"
            )
        } else {
            HiveMap(
                hives = HivesService.list,
                onEdit = { hive ->
                    editingHive = hive
                    editSheetController.show()
                }
            )
        }
    }

    BottomSheet(controller = addSheetController) {
        HiveAddView(onBack = addSheetController::hide)
    }

    BottomSheet(controller = editSheetController) {
        HiveEditView(
            hive = editingHive!!,
            onBack = { editSheetController.hide { editingHive = null } }
        )
    }
}

@Composable
private fun TextView(text: String) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BottomSheet(
    controller: BottomSheetController,
    content: @Composable () -> Unit
) {
    if (controller.visible) {
        ModalBottomSheet(
            sheetState = controller.state,
            onDismissRequest = controller::dismiss,
            dragHandle = {
                Spacer(
                    modifier = Modifier
                        .height(8.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                )
            },
        ) { content() }
    }
}
