package ua.tarch64.beejournal.ui.base.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private enum class ListState {
    LIST,
    LOADING,
    EMPTY
}

@Composable
fun <T> ListView(
    list: List<T>,
    loading: Boolean,
    load: () -> Unit,
    topBar: @Composable () -> Unit,
    addButton: @Composable () -> Unit,
    empty: @Composable () -> Unit,
    item: @Composable (T) -> Unit
) {


    Scaffold(
        topBar = topBar,
        floatingActionButton = addButton,
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = loading,
            onRefresh = load
        ) {
            val state = if (list.isNotEmpty()) {
                ListState.LIST
            } else if (loading) {
                ListState.LOADING
            } else {
                ListState.EMPTY
            }

            AnimatedContent(targetState = state) { state ->
                when (state) {
                    ListState.LIST -> LazyColumn(
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        items(list) { item(it) }
                    }

                    ListState.LOADING -> Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        empty()
                    }

                    else -> Box { }
                }
            }
        }
    }
}
