package ua.tarch64.beejournal.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.toObject

data class LocationModel(
    @DocumentId
    var id: String = "",

    var name: String = "",
    var owners: List<String> = emptyList(),

    @ServerTimestamp
    var createdAt: Timestamp = Timestamp.now()
) {
    companion object {
        fun fromDocument(snapshot: DocumentSnapshot) = snapshot.toObject<LocationModel>()!!
    }
}
