package ua.tarch64.beejournal.ui.locations.details

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ua.tarch64.beejournal.services.ActiveLocationService
import ua.tarch64.beejournal.services.HivesService

private enum class ScreenState {
    DETAILS,
    LOADING,
    ERROR
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LocationDetails(
    locationId: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val location by ActiveLocationService.instance.location.collectAsState()
    val locationLoading by ActiveLocationService.instance.loading.collectAsState()
    val locationError by ActiveLocationService.instance.error.collectAsState()

    val hives by HivesService.instance.list.collectAsState()
    val hivesLoading by HivesService.instance.loading.collectAsState()
    val hivesError by HivesService.instance.error.collectAsState()

    val loading = locationLoading || hivesLoading
    val error = locationError.ifEmpty { hivesError }

    DisposableEffect(Unit) {
        ActiveLocationService.instance.load(locationId)
        onDispose { ActiveLocationService.instance.unload() }
    }

    LaunchedEffect(error) {
        if (error.isNotEmpty()) {
            Toast
                .makeText(context, error, Toast.LENGTH_SHORT)
                .show()
        }
    }

    val state = if (location != null) {
        ScreenState.DETAILS
    } else if (loading) {
        ScreenState.LOADING
    } else {
        ScreenState.ERROR
    }

    AnimatedContent(targetState = state) { state ->
        when (state) {
            ScreenState.DETAILS -> {
                val location = location!!

                PullToRefreshBox(
                    isRefreshing = loading,
                    onRefresh = { ActiveLocationService.instance.load(locationId, force = true) }
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text(location.name) },
                                navigationIcon = {
                                    IconButton(onClick = onBack) {
                                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                                    }
                                },
                            )
                        },
                    ) { innerPadding ->
                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .padding(20.dp)
                        ) {
                            HiveMap(hives = hives)
                        }
                    }
                }
            }

            ScreenState.LOADING -> TextView("Завантажується...")
            ScreenState.ERROR -> TextView(error)
        }
    }
}

@Composable
private fun TextView(text: String) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text)
    }
}
