package ua.tarch64.beejournal.services

import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
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

    fun load() {
        _loading.value = true

        if (::listener.isInitialized) {
            listener.remove()
        }

        listener = collection
            .whereArrayContains("owners", uid)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, _ ->
                if (snapshots != null) {
                    _locations.value = snapshots.documents.map(LocationModel::fromDocument)
                }
                _loading.value = false
            }
    }

    suspend fun add(name: String): LocationModel {
        val location = LocationModel(
            name = name,
            owners = listOf(uid)
        )

        collection.add(location).await()
        return location
    }

    companion object {
        val instance = LocationsService()
    }
}