package ua.tarch64.beejournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.firebase.BuildConfig
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import ua.tarch64.beejournal.services.AuthService
import ua.tarch64.beejournal.ui.login.LoginView
import ua.tarch64.beejournal.ui.theme.BeeJournalTheme

const val NAV_DURATION = 350

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        @Suppress("KotlinConstantConditions")
        Firebase.crashlytics.isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG

        setContent {
            BeeJournalTheme {
                BeeJournalApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun BeeJournalApp() {
    val user = AuthService.user
    val navController = rememberNavController()

    LaunchedEffect(user) {
        Firebase.crashlytics.setUserId(user?.uid ?: "")
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