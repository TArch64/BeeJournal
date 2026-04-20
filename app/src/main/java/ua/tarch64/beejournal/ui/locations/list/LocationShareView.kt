package ua.tarch64.beejournal.ui.locations.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import ua.tarch64.beejournal.models.LocationModel
import ua.tarch64.beejournal.services.LocationsService
import ua.tarch64.beejournal.services.UsersService
import ua.tarch64.beejournal.ui.base.dialogs.ErrorReport
import ua.tarch64.beejournal.ui.base.form.FormView

@Composable
fun LocationShareView(
    location: LocationModel,
    onBack: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<Exception?>(null) }

    suspend fun share() {
        val user = UsersService.getUser(email)
        if (user == null) {
            error = Exception("Немає користувача з такою поштою")
            return
        }

        LocationsService.share(location, user)
        onBack()
    }

    ErrorReport(error)

    FormView(
        title = "Поділится Місцем",
        onBack = onBack,
        onSave = ::share,
        saveLabel = { Text("Поділитися") }
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Пошта") },
            value = email,
            onValueChange = { email = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                autoCorrectEnabled = false,
                capitalization = KeyboardCapitalization.None,
                keyboardType = KeyboardType.Email,
            )
        )
    }
}
