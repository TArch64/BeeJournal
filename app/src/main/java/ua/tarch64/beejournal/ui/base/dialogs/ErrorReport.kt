package ua.tarch64.beejournal.ui.base.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics

@Composable
fun ErrorReport(error: Exception?) {
    var displaying by remember { mutableStateOf(false) }

    LaunchedEffect(error) {
        displaying = error != null

        if (displaying) {
            Firebase.crashlytics.recordException(error!!)
        }
    }

    fun onDismiss() {
        displaying = false
    }

    if (displaying) {
        val error = error!!

        AlertDialog(
            title = { Text("Помилка") },
            text = { Text(error.message ?: "") },
            onDismissRequest = ::onDismiss,

            confirmButton = {
                TextButton(onClick = ::onDismiss) {
                    Text("Гаразд")
                }
            }
        )
    }
}
