package ua.tarch64.beejournal.services

import androidx.credentials.Credential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class AuthService {
    private val _user = MutableStateFlow(Firebase.auth.currentUser)
    val user = _user.asStateFlow()

    suspend fun handleSignIn(credential: Credential) {
        val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)

        Firebase.auth.signInWithCredential(firebaseCredential).await()
        _user.value = Firebase.auth.currentUser
        if (_user.value != null) UsersService.instance.upsert(_user.value!!)
    }

    companion object {
        val instance = AuthService()
    }
}