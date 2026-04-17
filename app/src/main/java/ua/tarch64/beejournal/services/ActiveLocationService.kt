package ua.tarch64.beejournal.services

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ua.tarch64.beejournal.models.LocationModel

class ActiveLocationService {
    private val _loading = MutableStateFlow(true)
    private val _location = MutableStateFlow<LocationModel?>(null)
    private val _error = MutableStateFlow("")

    val loading = _loading.asStateFlow()
    val location = _location.asStateFlow()
    val error = _error.asStateFlow()

    fun load(locationId: String) {
        _loading.value = true

        Firebase.firestore
            .collection("locations")
            .document(locationId)
            .addSnapshotListener { snapshot, exception ->
                _location.value = snapshot?.toObject<LocationModel>()
                _error.value = exception?.message ?: ""
                _loading.value = false
            }
    }

    companion object {
        val instance = ActiveLocationService()
    }
}