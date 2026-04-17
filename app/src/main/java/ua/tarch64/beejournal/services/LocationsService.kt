package ua.tarch64.beejournal.services

import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ua.tarch64.beejournal.models.LocationModel

class LocationsService {
    private val _locations = MutableStateFlow(emptyList<LocationModel>())
    private val _loading = MutableStateFlow(true)

    val locations = _locations.asStateFlow()
    val loading = _loading.asStateFlow()

    fun load() {
        _loading.value = true

        Firebase.firestore.collection("locations")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                _locations.value = result.documents.map(LocationModel.Companion::fromDocument)
                _loading.value = false
            }
            .addOnFailureListener { _loading.value = false }
    }

    companion object {
        val instance = LocationsService()
    }
}