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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.tarch64.beejournal.models.LocationModel

private enum class ScreenState {
    DETAILS,
    LOADING,
    ERROR
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DetailsLayout(
    loading: Boolean,
    location: LocationModel?,
    onLoad: () -> Unit,
    onBack: () -> Unit,
    contentLoading: @Composable () -> Unit,
    contentError: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val state = if (location != null) {
        ScreenState.DETAILS
    } else if (loading) {
        ScreenState.LOADING
    } else {
        ScreenState.ERROR
    }

    PullToRefreshBox(
        isRefreshing = loading,
        onRefresh = onLoad,
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(location?.name ?: "") },
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
                AnimatedContent(targetState = state) { state ->
                    when (state) {
                        ScreenState.DETAILS -> content()
                        ScreenState.ERROR -> contentError()
                        ScreenState.LOADING -> contentLoading()
                    }
                }
            }
        }
    }
}
