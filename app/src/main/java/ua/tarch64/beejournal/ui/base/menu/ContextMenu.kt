package ua.tarch64.beejournal.ui.base.menu

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

typealias ClickHandler = () -> Unit
typealias ClickDecorator = (action: ClickHandler) -> ClickHandler

@Composable
fun ContextMenu(
    onClick: (() -> Unit)? = null,
    actions: @Composable (onClick: ClickDecorator) -> Unit,
    content: @Composable () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var boxWidth by remember { mutableStateOf(0.dp) }
    var dropdownWidth by remember { mutableStateOf(0.dp) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { boxWidth = it.size.width.dp - 100.dp }
            .combinedClickable(
                onLongClick = { showMenu = true },
                onClick = { if (onClick != null) onClick() }
            )
    ) {
        content()

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
            offset = DpOffset(boxWidth - dropdownWidth - 20.dp, 10.dp),
        ) {
            actions { action ->
                return@actions {
                    showMenu = false
                    action()
                }
            }
        }
    }
}
