package ua.tarch64.beejournal

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ua.tarch64.beejournal.ui.locations.add.LocationAddView
import ua.tarch64.beejournal.ui.locations.list.LocationsView

enum class Route(val path: String) {
    LOCATIONS("locations"),
    LOCATION_ADD("locations/add")
}

val DEFAULT_ROUTE = Route.LOCATIONS

fun buildRoutes(nav: NavController): NavGraphBuilder.() -> Unit = {
    composable(Route.LOCATIONS.path) {
        LocationsView(onAdd = { nav.navigate(Route.LOCATION_ADD.path) })
    }

    composable(Route.LOCATION_ADD.path) {
        LocationAddView(onBack = { nav.popBackStack() })
    }
}
