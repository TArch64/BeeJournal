package ua.tarch64.beejournal.ui.locations.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ua.tarch64.beejournal.models.HiveModel
import ua.tarch64.beejournal.services.ActiveLocationService
import ua.tarch64.beejournal.services.HivesService
import ua.tarch64.beejournal.ui.base.dialogs.ErrorReport
import ua.tarch64.beejournal.ui.base.list.ListEmpty
import ua.tarch64.beejournal.ui.hives.add.HiveAddView
import ua.tarch64.beejournal.ui.hives.edit.HiveEditView


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LocationDetails(
    locationId: String,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val location by ActiveLocationService.instance.data.collectAsState()
    val locationLoading by ActiveLocationService.instance.loading.collectAsState()
    val locationError by ActiveLocationService.instance.error.collectAsState()

    val hives by HivesService.instance.list.collectAsState()
    val hivesLoading by HivesService.instance.loading.collectAsState()
    val hivesError by HivesService.instance.error.collectAsState()

    val addSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    fun addSheetShow() = scope.launch { addSheetState.show() }
    fun addSheetHide() = scope.launch { addSheetState.hide() }

    val editSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var editingHive by remember { mutableStateOf<HiveModel?>(null) }
    fun editSheetShow() = scope.launch { editSheetState.show() }
    fun editSheetHide() = scope.launch { editSheetState.hide() }

    val error = locationError?.let { hivesError }

    DisposableEffect(Unit) {
        ActiveLocationService.instance.load(locationId)
        onDispose { ActiveLocationService.instance.unload() }
    }

    ErrorReport(error)

    DetailsLayout(
        loading = locationLoading || hivesLoading,
        location = location,
        onLoad = { ActiveLocationService.instance.load(locationId, force = true) },
        onBack = onBack,
        onAdd = ::addSheetShow,
        contentLoading = { TextView("Завантажується...") },
        contentError = { TextView(error?.message ?: "") }
    ) {
        if (hives.isEmpty()) {
            ListEmpty(
                icon = Icons.Default.Home,
                title = "Пусто",
                description = "Натисніть кнопку внизу щоб додати новий вулик"
            )
        } else {
            HiveMap(
                hives = hives,
                onEdit = { hive ->
                    editingHive = hive
                    editSheetShow()
                }
            )
        }
    }

    BottomSheet(state = addSheetState) {
        HiveAddView(onBack = ::addSheetHide)
    }

    if (editingHive != null) {
        BottomSheet(state = editSheetState) {
            HiveEditView(
                hive = editingHive!!,
                onBack = {
                    editSheetHide()
                    editingHive = null
                }
            )
        }
    }
}

@Composable
private fun TextView(text: String) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BottomSheet(
    state: SheetState,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    fun hide() = scope.launch { state.hide() }

    if (state.isVisible || state.isAnimationRunning) {
        ModalBottomSheet(
            sheetState = state,
            onDismissRequest = ::hide,
            dragHandle = {
                Spacer(
                    modifier = Modifier
                        .height(8.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                )
            },
        ) {
            content()
        }
    }
}
