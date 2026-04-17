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
import ua.tarch64.beejournal.ui.base.form.FormView

@Composable
fun LocationAddView(onBack: () -> Unit) {
    var name by remember { mutableStateOf("") }

    suspend fun addLocation() {}

    FormView(
        title = "Нове Місце",
        onBack = onBack,
        onSave = ::addLocation,
        saveLabel = { Text("Додати") },
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
