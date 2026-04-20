package ua.tarch64.beejournal.ui.locations.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.tarch64.beejournal.models.HiveModel
import ua.tarch64.beejournal.services.HivesService
import ua.tarch64.beejournal.ui.base.dialogs.ConfirmDialog
import ua.tarch64.beejournal.ui.base.dialogs.rememberConfirmOperation
import ua.tarch64.beejournal.ui.base.menu.ContextMenu
import ua.tarch64.beejournal.ui.locations.details.hives.common.HiveSpotView

@Composable
fun HiveMap(
    hives: List<HiveModel>,
    onEdit: (hive: HiveModel) -> Unit,
) {
    val confirmingDelete = rememberConfirmOperation<HiveModel> {
        HivesService.delete(it)
    }

    LazyVerticalGrid(
        columns = GridCells.FixedSize(60.dp),
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        items(hives, key = { it.id }) { hive ->
            ContextMenu(
                onClick = { onEdit(hive) },

                actions = {
                    action(
                        title = "Видалити",
                        icon = Icons.Default.DeleteOutline,
                        destructive = true,
                        onClick = { confirmingDelete.show(hive) }
                    )
                }
            ) {
                HiveSpotView(
                    position = hive.position,
                    color = hive.color,
                )
            }
        }
    }

    ConfirmDialog(
        title = "Ви впевнені?",
        message = "Ви впевнені що хочете видалити цей вулик?",
        destructive = true,
        operation = confirmingDelete
    )
}
