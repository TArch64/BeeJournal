package ua.tarch64.beejournal.services

import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ua.tarch64.beejournal.models.Identifiable

abstract class CollectionService<V : Identifiable> {
    private val _list = MutableStateFlow(emptyList<V>())
    private val _loading = MutableStateFlow(true)
    private val _error = MutableStateFlow<Exception?>(null)
    private var listener: ListenerRegistration? = null

    protected abstract val query: Query

    val list = _list.asStateFlow()
    val loading = _loading.asStateFlow()
    val error = _error.asStateFlow()

    protected fun loadCollection(force: Boolean = false) {
        if (listener == null) {
            _loading.value = true

            listener = query.addSnapshotListener { snapshots, exception ->
                if (snapshots != null) {
                    _list.value = decode(snapshots)
                }
                _error.value = exception
                _loading.value = false
            }
        } else if (force) {
            _loading.value = true

            query
                .get(Source.SERVER)
                .addOnSuccessListener { _loading.value = false }
                .addOnFailureListener { _loading.value = false }
        }
    }

    protected abstract fun decode(snapshot: QuerySnapshot): List<V>

    open fun unload() {
        listener?.remove()
        listener = null
    }
}