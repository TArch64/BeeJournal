package ua.tarch64.beejournal.services

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source
import ua.tarch64.beejournal.models.Identifiable

abstract class CollectionService<V : Identifiable> {
    var list by mutableStateOf(emptyList<V>())
        private set

    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf<Exception?>(null)
        private set

    private var listener: ListenerRegistration? = null
    protected abstract val query: Query


    protected fun loadCollection(force: Boolean = false) {
        if (listener == null) {
            loading = true

            listener = query.addSnapshotListener { snapshots, exception ->
                if (snapshots != null) {
                    list = decode(snapshots)
                }
                error = exception
                loading = false
            }
        } else if (force) {
            loading = true

            query
                .get(Source.SERVER)
                .addOnSuccessListener { loading = false }
                .addOnFailureListener { loading = false }
        }
    }

    protected abstract fun decode(snapshot: QuerySnapshot): List<V>

    open fun unload() {
        listener?.remove()
        listener = null
    }
}