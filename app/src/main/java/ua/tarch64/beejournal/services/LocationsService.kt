package ua.tarch64.beejournal.services

import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await
import ua.tarch64.beejournal.models.LocationModel
import ua.tarch64.beejournal.models.UserModel

object LocationsService : CollectionService<LocationModel>() {
    private val collection = Firebase.firestore.collection("locations")
    private val uid: String get() = AuthService.user!!.uid

    override val query: Query = collection
        .whereArrayContains("owners", uid)
        .orderBy("createdAt", Query.Direction.DESCENDING)

    fun load(force: Boolean = false) = loadCollection(force)

    override fun decode(snapshot: QuerySnapshot): List<LocationModel> {
        return snapshot.toObjects<LocationModel>()
    }

    suspend fun add(location: LocationModel): LocationModel {
        location.owners = listOf(uid)
        val document = collection.add(location).await()
        return location.apply { id = document.id }
    }

    suspend fun update(location: LocationModel) {
        document(location).set(location).await()
    }

    suspend fun delete(location: LocationModel) {
        val document = document(location)
        val hivesQuery = document.collection("hives").get().await()

        Firebase.firestore
            .runBatch { batch ->
                for (hiveDoc in hivesQuery) {
                    batch.delete(hiveDoc.reference)
                }

                batch.delete(document)
            }
            .await()
    }

    suspend fun share(location: LocationModel, user: UserModel) {
        location.owners = location.owners.plus(user.id)
        document(location).set(location).await()
    }

    private fun document(location: LocationModel): DocumentReference {
        return collection.document(location.id)
    }
}