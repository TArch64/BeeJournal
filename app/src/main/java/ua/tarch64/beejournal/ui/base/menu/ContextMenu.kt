package ua.tarch64.beejournal.ui.base.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class ContextMenuScope(
    private val scope: CoroutineScope,
    private val sheetController: BottomSheetController
) {
    val items = mutableListOf<@Composable () -> Unit>()

    fun action(
        title: String,
        icon: ImageVector? = null,
        destructive: Boolean = false,
        onClick: suspend () -> Unit
    ) {
        items.add {
            ListItem(
                headlineContent = { Text(title) },
                leadingContent = icon?.let { { Icon(it, title) } },
                colors = if (destructive) {
                    ListItemDefaults.colors(
                        headlineColor = MaterialTheme.colorScheme.error,
                        leadingIconColor = MaterialTheme.colorScheme.error
                    )
                } else {
                    ListItemDefaults.colors()
                },
                modifier = Modifier.clickable {
                    sheetController.hide()
                    scope.launch { onClick() }
                }
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ContextMenu(
    onClick: (() -> Unit)? = null,
    actions: ContextMenuScope.() -> Unit,
    content: @Composable () -> Unit
) {
    val sheetController = rememberBottomSheetController()
    val scope = rememberCoroutineScope()

    val actionsScope = remember(actions) {
        ContextMenuScope(scope, sheetController).apply(actions)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onLongClick = sheetController::show,
                onClick = { if (onClick != null) onClick() }
            ),

        content = { content() }
    )

    if (sheetController.visible) {
        ModalBottomSheet(
            sheetState = sheetController.state,
            onDismissRequest = sheetController::dismiss,

            content = {
                actionsScope.items.forEach { it() }
                Spacer(Modifier.height(40.dp))
            }
        )
    }
}