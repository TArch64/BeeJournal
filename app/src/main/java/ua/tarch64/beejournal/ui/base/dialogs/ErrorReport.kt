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

@Composable
fun ErrorReport(error: String) {
    var displaying by remember { mutableStateOf(false) }

    LaunchedEffect(error) {
        displaying = error.isNotEmpty()
    }

    fun onDismiss() {
        displaying = false
    }

    if (displaying) {
        AlertDialog(
            title = { Text("Помилка") },
            text = { Text(error) },
            onDismissRequest = ::onDismiss,

            confirmButton = {
                TextButton(onClick = ::onDismiss) {
                    Text("Гаразд")
                }
            }
        )
    }
}
