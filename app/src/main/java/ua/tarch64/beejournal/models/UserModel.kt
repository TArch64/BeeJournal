package ua.tarch64.beejournal.models

import com.google.firebase.firestore.DocumentId

data class UserModel(
    @DocumentId
    override var id: String = "",

    var email: String = ""
) : Identifiable
