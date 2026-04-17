package ua.tarch64.beejournal.services

import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import ua.tarch64.beejournal.models.LocationModel

class LocationsService {
    private val _locations = MutableStateFlow(emptyList<LocationModel>())
    private val _loading = MutableStateFlow(true)

    val locations = _locations.asStateFlow()
    val loading = _loading.asStateFlow()

    private val collection = Firebase.firestore.collection("locations")
    private lateinit var listener: ListenerRegistration

    private val uid: String get() = AuthService.instance.user.value!!.uid

    fun load(force: Boolean = false) {
        if (!::listener.isInitialized) {
            _loading.value = true

            listener = newQuery().addSnapshotListener { snapshots, _ ->
                if (snapshots != null) {
                    _locations.value = snapshots.toObjects()
                }
                _loading.value = false
            }
        } else if (force) {
            _loading.value = true
            newQuery()
                .get(Source.SERVER)
                .addOnSuccessListener { _loading.value = false }
                .addOnFailureListener { _loading.value = false }
        }
    }

    private fun newQuery(): Query {
        return collection
            .whereArrayContains("owners", uid)
            .orderBy("createdAt", Query.Direction.DESCENDING)
    }

    suspend fun add(name: String): LocationModel {
        val location = LocationModel(
            name = name,
            owners = listOf(uid)
        )

        val document = collection.add(location).await()
        return location.copy(id = document.id)
    }

    companion object {
        val instance = LocationsService()
    }
}