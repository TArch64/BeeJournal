package ua.tarch64.beejournal.services

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.credentials.Credential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

object AuthService {
    var user by mutableStateOf(Firebase.auth.currentUser)
        private set

    suspend fun handleSignIn(credential: Credential) {
        val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)

        Firebase.auth.signInWithCredential(firebaseCredential).await()
        user = Firebase.auth.currentUser
        if (user != null) UsersService.upsert(user!!)
    }
}