package ua.tarch64.beejournal.views.login

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import kotlinx.coroutines.launch
import ua.tarch64.beejournal.R
import ua.tarch64.beejournal.services.fireauth.Auth

@Composable
@PreviewScreenSizes
fun LoginView() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }
    val serverClientId = stringResource(R.string.default_web_client_id)

    suspend fun login() {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(serverClientId)
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val response = credentialManager.getCredential(context as Activity, request)
            Auth.instance.handleSignIn(response.credential)
        } catch (e: GetCredentialException) {
            Toast
                .makeText(context, e.errorMessage ?: "Помилка", Toast.LENGTH_SHORT)
                .show()
        } catch (e: Exception) {
            Toast
                .makeText(context, e.message ?: "Помилка", Toast.LENGTH_SHORT)
                .show()
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = {
                scope.launch { login() }
            }) {
                Text("Увійти з Google")
            }
        }
    }
}