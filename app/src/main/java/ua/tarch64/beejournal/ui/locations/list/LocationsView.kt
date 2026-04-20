package ua.tarch64.beejournal.ui.locations.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import ua.tarch64.beejournal.models.LocationModel
import ua.tarch64.beejournal.services.LocationsService
import ua.tarch64.beejournal.ui.base.dialogs.ConfirmDialog
import ua.tarch64.beejournal.ui.base.dialogs.ErrorReport
import ua.tarch64.beejournal.ui.base.dialogs.rememberConfirmOperation
import ua.tarch64.beejournal.ui.base.list.ListEmpty
import ua.tarch64.beejournal.ui.base.list.ListView

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LocationsView(
    onAdd: () -> Unit,
    onOpen: (location: LocationModel) -> Unit
) {
    val confirmingDelete = rememberConfirmOperation<LocationModel>()

    DisposableEffect(Unit) {
        LocationsService.load()
        onDispose { LocationsService.unload() }
    }

    ErrorReport(LocationsService.error)

    ListView(
        list = LocationsService.list,
        loading = LocationsService.loading,
        load = { LocationsService.load(true) },
        topBar = { TopAppBar(title = { Text("BeeJournal") }) },
        addButton = {
            ExtendedFloatingActionButton(
                onClick = onAdd,
                icon = { Icon(Icons.Default.Add, contentDescription = "Додати Місце") },
                text = { Text("Додати") }
            )
        },
        empty = {
            ListEmpty(
                icon = Icons.Default.Place,
                title = "Пусто",
                description = "Натисніть кнопку внизу щоб додати нове місце"
            )
        }
    ) { location ->
        LocationView(
            location = location,
            onOpen = { onOpen(location) },
            onDelete = { confirmingDelete.show(location) }
        )
    }

    if (confirmingDelete.confirming) {
        ConfirmDialog(
            title = "Ви впевнені?",
            message = "Ви впевнені що хочете видалити це місце?",
            destructive = true,
            onConfirm = { confirmingDelete.execute(LocationsService::delete) },
            onDismiss = confirmingDelete::dismiss
        )
    }
}
