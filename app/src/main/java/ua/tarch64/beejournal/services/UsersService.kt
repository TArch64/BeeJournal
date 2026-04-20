package ua.tarch64.beejournal.services

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import ua.tarch64.beejournal.models.UserModel

object UsersService {
    val collection = Firebase.firestore.collection("users")

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

    suspend fun getUser(email: String): UserModel? {
        val user = collection
            .whereEqualTo("email", email)
            .limit(1)
            .get()
            .await()

        return if (user.isEmpty) null else user.documents.first().toObject<UserModel>()
    }

}
