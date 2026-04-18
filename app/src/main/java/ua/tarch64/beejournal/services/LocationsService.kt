package ua.tarch64.beejournal.services

import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await
import ua.tarch64.beejournal.models.LocationModel
import ua.tarch64.beejournal.models.UserModel

class LocationsService : CollectionService<LocationModel>() {
    private val collection = Firebase.firestore.collection("locations")
    private val uid: String get() = AuthService.instance.user.value!!.uid

    override val query: Query = collection
        .whereArrayContains("owners", uid)
        .whereEqualTo("deleted", false)
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

    fun delete(location: LocationModel) {
        location.deleted = true
        collection.document(location.id).set(location)
    }

    fun share(location: LocationModel, user: UserModel) {
        location.owners = location.owners.plus(user.email)
        collection.document(location.id).set(location)
    }

    companion object {
        val instance = LocationsService()
    }
}