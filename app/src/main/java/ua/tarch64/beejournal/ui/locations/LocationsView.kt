package ua.tarch64.beejournal.ui.locations

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ua.tarch64.beejournal.services.LocationsService
import ua.tarch64.beejournal.ui.base.list.ListEmpty
import ua.tarch64.beejournal.ui.base.list.ListView

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LocationsView() {
    val locations by LocationsService.instance.locations.collectAsState()
    val loading by LocationsService.instance.loading.collectAsState()

    fun addLocation() {}

    ListView(
        list = locations,
        loading = loading,
        load = LocationsService.instance::load,
        topBar = { TopAppBar(title = { Text("BeeJournal") }) },
        addButton = {
            ExtendedFloatingActionButton(
                onClick = ::addLocation,
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
        ListItem(headlineContent = { Text(location.name) })
    }
}
