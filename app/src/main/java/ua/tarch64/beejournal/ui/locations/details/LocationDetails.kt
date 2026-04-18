package ua.tarch64.beejournal.ui.locations.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import ua.tarch64.beejournal.services.ActiveLocationService
import ua.tarch64.beejournal.services.HivesService
import ua.tarch64.beejournal.ui.base.dialogs.ErrorReport
import ua.tarch64.beejournal.ui.base.list.ListEmpty
import ua.tarch64.beejournal.ui.hives.add.HiveAddView


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

    val addSheetController = rememberModalBottomSheetState()
    fun addSheetShow() = scope.launch { addSheetController.show() }
    fun addSheetHide() = scope.launch { addSheetController.hide() }

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
            HiveMap(hives = hives)
        }
    }

    if (addSheetController.isVisible || addSheetController.isAnimationRunning) {
        ModalBottomSheet(onDismissRequest = ::addSheetHide) {
            HiveAddView(onBack = ::addSheetHide)
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
