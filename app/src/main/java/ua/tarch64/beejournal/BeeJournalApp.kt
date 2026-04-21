package ua.tarch64.beejournal

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.crashlytics.setCustomKeys
import ua.tarch64.beejournal.services.AuthService
import ua.tarch64.beejournal.ui.login.LoginView

const val NAV_DURATION = 350

@PreviewScreenSizes
@Composable
fun BeeJournalApp() {
    val user = AuthService.user
    val navController = rememberNavController()

    LaunchedEffect(user) {
        Firebase.crashlytics.setUserId(user?.uid ?: "")

        Firebase.crashlytics.setCustomKeys {
            key("user_email", user?.email ?: "")
        }
    }

    if (user == null) {
        LoginView()
    } else {
        NavHost(
            navController = navController,
            startDestination = LocationsRoute,
            enterTransition = { slideInHorizontally(tween(NAV_DURATION)) { it } },
            exitTransition = { slideOutHorizontally(tween(NAV_DURATION)) { -it } },
            popEnterTransition = { slideInHorizontally(tween(NAV_DURATION)) { -it } },
            popExitTransition = { slideOutHorizontally(tween(NAV_DURATION)) { it } },
            builder = buildRoutes(navController)
        )
    }
}
