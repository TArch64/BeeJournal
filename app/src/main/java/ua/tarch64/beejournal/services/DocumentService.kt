package ua.tarch64.beejournal.services

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Source
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ua.tarch64.beejournal.models.Identifiable

abstract class DocumentService<V : Identifiable> {
    private val _loading = MutableStateFlow(true)
    private val _location = MutableStateFlow<V?>(null)
    private val _error = MutableStateFlow("")
    private var listener: ListenerRegistration? = null

    val loading = _loading.asStateFlow()
    val location = _location.asStateFlow()
    val error = _error.asStateFlow()

    protected abstract val document: DocumentReference?

    protected fun loadDocument(force: Boolean = false) {
        val document = document!!

        if (listener == null) {
            _loading.value = true

            listener = document.addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    _location.value = decode(snapshot)
                }
                _error.value = exception?.message ?: ""
                _loading.value = false
            }
        } else if (force) {
            document
                .get(Source.SERVER)
                .addOnSuccessListener { _loading.value = false }
                .addOnFailureListener { _loading.value = false }

        }
    }

    protected abstract fun decode(snapshot: DocumentSnapshot): V

    open fun unload() {
        listener?.remove()
        listener = null
    }
}