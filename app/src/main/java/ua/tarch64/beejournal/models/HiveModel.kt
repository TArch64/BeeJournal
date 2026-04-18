package ua.tarch64.beejournal.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class HiveModel(
    @DocumentId
    override val id: String = "",

    val position: UInt = 0.toUInt(),

    @ServerTimestamp
    var createdAt: Timestamp = Timestamp.now()
) : Identifiable
