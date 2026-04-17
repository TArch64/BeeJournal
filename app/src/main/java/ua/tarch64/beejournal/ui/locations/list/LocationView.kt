package ua.tarch64.beejournal.ui.locations.list

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ua.tarch64.beejournal.models.LocationModel
import ua.tarch64.beejournal.ui.base.menu.ContextMenu

@Composable
fun LocationView(
    location: LocationModel,
    onOpen: () -> Unit,
    onDelete: () -> Unit
) {
    ContextMenu(
        onClick = onOpen,
        actions = { onClick ->
            DropdownMenuItem(
                text = { Text("Видалити") },
                onClick = onClick { onDelete() }
            )
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
}