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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ConfirmOperation<D>(
    private val action: suspend (D) -> Unit,
    private val scope: CoroutineScope
) {
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

    fun confirm() {
        scope.launch {
            confirming = false
            action(data!!)
            data = null
        }
    }
}

@Composable
fun <D> rememberConfirmOperation(action: suspend (D) -> Unit): ConfirmOperation<D> {
    val scope = rememberCoroutineScope()
    return remember { ConfirmOperation(action, scope) }
}

@Composable
fun <D> ConfirmDialog(
    title: String,
    message: String,
    confirmText: String = "Так",
    dismissText: String = "Ні",
    destructive: Boolean = false,
    operation: ConfirmOperation<D>,
) {
    if (operation.confirming) {
        AlertDialog(
            onDismissRequest = operation::dismiss,
            title = { Text(title) },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = operation::confirm) {
                    Text(
                        confirmText,
                        color = if (destructive) MaterialTheme.colorScheme.error else Color.Unspecified
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = operation::dismiss) {
                    Text(dismissText)
                }
            }
        )
    }
}