package ua.tarch64.beejournal.ui.locations.add

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ua.tarch64.beejournal.models.LocationModel
import ua.tarch64.beejournal.services.LocationsService
import ua.tarch64.beejournal.ui.base.dialogs.ErrorReport
import ua.tarch64.beejournal.ui.base.form.FormView

@Composable
fun LocationAddView(
    onBack: () -> Unit,
    onOpen: (location: LocationModel) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<Exception?>(null) }

    suspend fun addLocation() {
        try {
            val location = LocationsService.add(LocationModel(name = name))
            onOpen(location)
        } catch (e: Exception) {
            error = e
        }
    }

    ErrorReport(error)

    FormView(
        title = "Нове Місце",
        onBack = onBack,
        onSave = ::addLocation,
        saveLabel = { Text("Додати") },
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Назва") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
