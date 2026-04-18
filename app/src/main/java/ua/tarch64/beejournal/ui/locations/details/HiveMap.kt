package ua.tarch64.beejournal.ui.locations.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.tarch64.beejournal.models.HiveModel
import ua.tarch64.beejournal.ui.hives.common.HiveSpotView

@Composable
fun HiveMap(
    hives: List<HiveModel>,
    onEdit: (hive: HiveModel) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.FixedSize(60.dp),
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        items(hives, key = { it.id }) { hive ->
            HiveSpotView(
                position = hive.position,
                color = hive.color,
                onClick = { onEdit(hive) }
            )
        }
    }
}
