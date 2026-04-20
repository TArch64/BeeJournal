package ua.tarch64.beejournal.services

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await
import ua.tarch64.beejournal.models.UserModel

object UsersService {
    val collection = Firebase.firestore.collection("users")
    private var searchCache: List<UserModel> = emptyList()

    suspend fun upsert(user: FirebaseUser) {
        collection
            .document(user.uid)
            .set(
                UserModel(
                    id = user.uid,
                    email = user.email ?: ""
                )
            )
            .await()
    }

    suspend fun search(email: String): List<UserModel> {
        if (searchCache.isEmpty()) {
            searchCache = collection.get().await().toObjects()
        }
        return searchCache.filter { it.email.contains(email, ignoreCase = true) }
    }

    fun clearSearchCache() {
        searchCache = emptyList()
    }
}
