package ua.tarch64.beejournal.ui.base.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch

class ConfirmOperation<D> {
    var data by mutableStateOf<D?>(null)
        private set

    var confirming by mutableStateOf(false)
        private set

    fun show(data: D) {
        this.data = data
        this.confirming = true
    }

    fun dismiss() {
        confirming = false
        data = null
    }

    suspend fun execute(action: suspend (D) -> Unit) {
        confirming = false
        action(data!!)
        data = null
    }
}

@Composable
fun <D> rememberConfirmOperation(): ConfirmOperation<D> {
    return remember { ConfirmOperation() }
}

@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmText: String = "Так",
    dismissText: String = "Ні",
    destructive: Boolean = false,
    onConfirm: suspend () -> Unit,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = { scope.launch { onConfirm() } }) {
                Text(
                    confirmText,
                    color = if (destructive) MaterialTheme.colorScheme.error else Color.Unspecified
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText)
            }
        }
    )
}