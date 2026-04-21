package ua.tarch64.beejournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.BuildConfig
import com.google.firebase.Firebase
import com.google.firebase.appdistribution.appDistribution
import com.google.firebase.crashlytics.crashlytics
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

    override fun onResume() {
        super.onResume()
        Firebase.appDistribution.updateIfNewReleaseAvailable()
    }
}
