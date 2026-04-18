package ua.tarch64.beejournal.ui.base.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.tarch64.beejournal.models.Identifiable

private enum class ListState {
    LIST,
    LOADING,
    EMPTY
}

@Composable
fun <T : Identifiable> ListView(
    list: List<T>,
    loading: Boolean,
    load: () -> Unit,
    topBar: @Composable () -> Unit,
    addButton: @Composable () -> Unit,
    empty: @Composable () -> Unit,
    item: @Composable (T) -> Unit
) {
    PullToRefreshBox(
        isRefreshing = loading,
        onRefresh = load
    ) {
        Scaffold(
            topBar = topBar,
            floatingActionButton = addButton,
        ) { innerPadding ->
            val state = if (list.isNotEmpty()) {
                ListState.LIST
            } else if (loading) {
                ListState.LOADING
            } else {
                ListState.EMPTY
            }

            val containerModifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)

            AnimatedContent(targetState = state) { state ->
                when (state) {
                    ListState.LIST -> LazyColumn(
                        containerModifier,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        items(list, key = { it.id }) {
                            Box(modifier = Modifier.animateItem()) {
                                item(it)
                            }
                        }
                    }

                    ListState.EMPTY -> Box(modifier = containerModifier) {
                        empty()
                    }

                    ListState.LOADING -> Box { }
                }
            }
        }
    }
}
