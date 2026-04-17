package ua.tarch64.beejournal

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ua.tarch64.beejournal.ui.locations.LocationsView

const val DEFAULT_ROUTE = "locations"

val routes: NavGraphBuilder.() -> Unit = {
    composable(DEFAULT_ROUTE) { LocationsView() }
}
