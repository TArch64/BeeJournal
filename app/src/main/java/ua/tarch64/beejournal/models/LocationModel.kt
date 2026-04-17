package ua.tarch64.beejournal.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.toObject

data class LocationModel(
    @DocumentId
    var id: String = "",
    var name: String = "",
    var owners: List<String> = emptyList()
) {
    companion object {
        fun fromDocument(snapshot: DocumentSnapshot) = snapshot.toObject<LocationModel>()!!
    }
}
