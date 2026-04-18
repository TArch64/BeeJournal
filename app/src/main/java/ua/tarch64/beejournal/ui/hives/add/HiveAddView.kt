package ua.tarch64.beejournal.ui.hives.add


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.tarch64.beejournal.services.HivesService
import ua.tarch64.beejournal.ui.base.form.FormView

@Composable
fun HiveAddView(onBack: () -> Unit) {
    val position = remember { HivesService.instance.nextPosition }
    var name by remember { mutableStateOf("") }

    suspend fun addHive() {
        onBack()
    }

    FormView(
        title = "Новий Вулик",
        onBack = onBack,
        onSave = ::addHive,
        saveLabel = { Text("Додати") },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedCard(
                modifier = Modifier
                    .width(60.dp)
                    .aspectRatio(1f),

                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("$position")
                }
            }

            OutlinedButton(onClick = {}) {
                Text("Обрати Колір")
            }
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
