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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.firebase.BuildConfig
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import ua.tarch64.beejournal.services.AuthService
import ua.tarch64.beejournal.ui.login.LoginView
import ua.tarch64.beejournal.ui.theme.BeeJournalTheme

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
    val user by AuthService.instance.user.collectAsState()
    val navController = rememberNavController()

    LaunchedEffect(user) {
        Firebase.crashlytics.setUserId(user?.uid ?: "unkown")
    }

    if (user == null) {
        LoginView()
    } else {
        NavHost(
            navController = navController,
            startDestination = LocationsRoute,
            enterTransition = { slideInHorizontally(tween(500)) { it } },
            exitTransition = { slideOutHorizontally(tween(500)) { -it } },
            popEnterTransition = { slideInHorizontally(tween(500)) { -it } },
            popExitTransition = { slideOutHorizontally(tween(500)) { it } },
            builder = buildRoutes(navController)
        )
    }
}