package ua.tarch64.beejournal.services

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Source
import ua.tarch64.beejournal.models.Identifiable

abstract class DocumentService<V : Identifiable> {

    var data by mutableStateOf<V?>(null)
        private set

    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf<Exception?>(null)
        private set

    private var listener: ListenerRegistration? = null
    protected abstract val document: DocumentReference?

    protected fun loadDocument(force: Boolean = false) {
        val document = document!!

        if (listener == null) {
            loading = true

            listener = document.addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    data = decode(snapshot)
                }
                error = exception
                loading = false
            }
        } else if (force) {
            document
                .get(Source.SERVER)
                .addOnSuccessListener { loading = false }
                .addOnFailureListener { loading = false }

        }
    }

    protected abstract fun decode(snapshot: DocumentSnapshot): V

    open fun unload() {
        listener?.remove()
        listener = null
    }
}