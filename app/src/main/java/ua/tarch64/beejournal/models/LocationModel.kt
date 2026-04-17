package ua.tarch64.beejournal.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class LocationModel(
    @DocumentId
    override var id: String = "",

    var name: String = "",
    var deleted: Boolean = false,
    var owners: List<String> = emptyList(),

    @ServerTimestamp
    var createdAt: Timestamp = Timestamp.now()
) : Identifiable
