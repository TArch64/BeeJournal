package ua.tarch64.beejournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import ua.tarch64.beejournal.services.fireauth.Auth
import ua.tarch64.beejournal.ui.theme.BeeJournalTheme
import ua.tarch64.beejournal.views.login.LoginView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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
    val user by Auth.instance.user.collectAsState()

    if (user == null) {
        LoginView()
    } else {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Text(
                text = "Hello World! ${user!!.email ?: "no-email"}",
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}