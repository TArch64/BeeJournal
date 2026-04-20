package ua.tarch64.beejournal.ui.base.menu

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class BottomSheetController(
    val state: SheetState,
    private val scope: CoroutineScope
) {
    var visible by mutableStateOf(false)
        private set

    fun show() {
        visible = true

        scope.launch {
            delay(50)
            state.show()
        }
    }

    fun hide(onComplete: (() -> Unit) = {}) {
        scope
            .launch { state.hide() }
            .invokeOnCompletion {
                visible = false
                onComplete()
            }
    }

    fun dismiss() {
        visible = false
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun rememberBottomSheetController(skipPartiallyExpanded: Boolean = false): BottomSheetController {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    val scope = rememberCoroutineScope()
    return remember { BottomSheetController(sheetState, scope) }
}