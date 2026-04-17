package ua.tarch64.beejournal

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import ua.tarch64.beejournal.ui.locations.add.LocationAddView
import ua.tarch64.beejournal.ui.locations.details.LocationDetails
import ua.tarch64.beejournal.ui.locations.list.LocationsView

@Serializable
object LocationsRoute {}

@Serializable
object LocationAddRoute {}

@Serializable
data class LocationDetailsRoute(val locationId: String)

fun buildRoutes(nav: NavController): NavGraphBuilder.() -> Unit = {
    composable<LocationsRoute> {
        LocationsView(
            onAdd = { nav.navigate(LocationAddRoute) },
            onOpenLocation = { nav.navigate(LocationDetailsRoute(it.id)) }
        )
    }

    composable<LocationAddRoute> {
        LocationAddView(
            onBack = { nav.popBackStack() },
            onOpenLocation = { nav.navigate(LocationDetailsRoute(it.id)) }
        )
    }

    composable<LocationDetailsRoute> { entry ->
        val route = entry.toRoute<LocationDetailsRoute>()

        LocationDetails(
            locationId = route.locationId,
            onBack = { nav.popBackStack() }
        )
    }
}
