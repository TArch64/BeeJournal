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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch
import ua.tarch64.beejournal.models.LocationModel
import ua.tarch64.beejournal.ui.base.menu.ContextMenu

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LocationView(
    location: LocationModel,
    onOpen: () -> Unit,
    onDelete: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val shareState = rememberModalBottomSheetState()
    val editState = rememberModalBottomSheetState()

    ContextMenu(
        onClick = onOpen,

        actions = {
            action(title = "Редагувати", icon = Icons.Default.Edit) {
                editState.show()
            }

            action(title = "Поділитися", icon = Icons.Default.Share) {
                shareState.show()
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

    if (shareState.isVisible || shareState.isAnimationRunning) {
        ModalBottomSheet(
            sheetState = shareState,
            onDismissRequest = { scope.launch { shareState.hide() } }
        ) {
            LocationShareView(
                location = location,
                onBack = { scope.launch { shareState.hide() } }
            )
        }
    }

    if (editState.isVisible || editState.isAnimationRunning) {
        ModalBottomSheet(
            sheetState = editState,
            onDismissRequest = { scope.launch { editState.hide() } }
        ) {
            LocationEditView(
                location = location,
                onBack = { scope.launch { editState.hide() } }
            )
        }
    }
}