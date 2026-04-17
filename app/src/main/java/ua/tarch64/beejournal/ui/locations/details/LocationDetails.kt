package ua.tarch64.beejournal.ui.locations.details

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.tarch64.beejournal.services.ActiveLocationService

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
    val loading by ActiveLocationService.instance.loading.collectAsState()
    val location by ActiveLocationService.instance.location.collectAsState()
    val error by ActiveLocationService.instance.error.collectAsState()
    fun load() = ActiveLocationService.instance.load(locationId)

    LaunchedEffect(Unit) { load() }

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
                    PullToRefreshBox(
                        isRefreshing = loading,
                        onRefresh = ::load
                    ) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .padding(20.dp)
                        ) {

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
