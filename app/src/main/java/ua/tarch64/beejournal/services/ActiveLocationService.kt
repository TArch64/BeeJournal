package ua.tarch64.beejournal.services

import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import ua.tarch64.beejournal.models.LocationModel

class ActiveLocationService : DocumentService<LocationModel>() {
    override var document: DocumentReference? = null

    fun load(locationId: String, force: Boolean = false) {
        document = document ?: Firebase.firestore
            .collection("locations")
            .document(locationId)

        loadDocument(force = force)
        HivesService.instance.load(locationId, force = force)
    }

    override fun decode(snapshot: DocumentSnapshot): LocationModel {
        return snapshot.toObject<LocationModel>()!!
    }

    override fun unload() {
        super.unload()
        document = null
        HivesService.instance.unload()
    }

    companion object {
        val instance = ActiveLocationService()
    }
}