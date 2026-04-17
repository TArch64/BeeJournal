package ua.tarch64.beejournal.ui.locations.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ua.tarch64.beejournal.models.LocationModel
import ua.tarch64.beejournal.services.LocationsService
import ua.tarch64.beejournal.ui.base.list.ListEmpty
import ua.tarch64.beejournal.ui.base.list.ListView

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LocationsView(
    onAdd: () -> Unit,
    onOpenLocation: (location: LocationModel) -> Unit
) {
    val locations by LocationsService.instance.locations.collectAsState()
    val loading by LocationsService.instance.loading.collectAsState()

    LaunchedEffect(Unit) {
        LocationsService.instance.load()
    }

    ListView(
        list = locations,
        loading = loading,
        load = LocationsService.instance::load,
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
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            ),
            onClick = { onOpenLocation(location) }
        ) {
            ListItem(
                headlineContent = { Text(location.name) },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
            )
        }
    }
}
