package ua.tarch64.beejournal.services.fireauth

import androidx.credentials.Credential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class Auth {
    private val _user = MutableStateFlow(Firebase.auth.currentUser)
    val user = _user.asStateFlow()

    fun handleSignIn(credential: Credential) {
        val idToken = GoogleIdTokenCredential
            .createFrom(credential.data)
            .idToken

        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)

        Firebase.auth.signInWithCredential(firebaseCredential).addOnSuccessListener {
            _user.value = Firebase.auth.currentUser
        }
    }

    companion object {
        val instance = Auth()
    }
}