package ua.tarch64.beejournal.ui.locations.list

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ua.tarch64.beejournal.models.LocationModel
import ua.tarch64.beejournal.ui.base.menu.ContextMenu
import ua.tarch64.beejournal.ui.base.menu.rememberBottomSheetController

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LocationView(
    location: LocationModel,
    onOpen: () -> Unit,
    onDelete: () -> Unit
) {
    val shareController = rememberBottomSheetController()
    val editController = rememberBottomSheetController()

    ContextMenu(
        onClick = onOpen,

        actions = {
            action(title = "Редагувати", icon = Icons.Default.Edit) {
                editController.show()
            }

            action(title = "Поділитися", icon = Icons.Default.Share) {
                shareController.show()
            }

            action(title = "Видалити", icon = Icons.Default.DeleteOutline, destructive = true) {
                onDelete()
            }
        }
    ) {
        Box {
            OutlinedCard(
                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                ),
            ) {
                ListItem(
                    headlineContent = { Text(location.name) },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
            }
        }
    }

    if (shareController.visible) {
        ModalBottomSheet(
            sheetState = shareController.state,
            onDismissRequest = shareController::dismiss,
        ) {
            LocationShareView(
                location = location,
                onBack = shareController::hide
            )
        }
    }

    if (editController.visible) {
        ModalBottomSheet(
            sheetState = editController.state,
            onDismissRequest = editController::dismiss,
        ) {
            LocationEditView(
                location = location,
                onBack = editController::hide
            )
        }
    }
}