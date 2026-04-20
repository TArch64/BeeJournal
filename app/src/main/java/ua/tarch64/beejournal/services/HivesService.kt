package ua.tarch64.beejournal.services

import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await
import ua.tarch64.beejournal.models.HiveModel

object HivesService : CollectionService<HiveModel>() {
    private var collection: CollectionReference? = null
    override val query: Query get() = collection!!.orderBy("position")

    val nextPosition: Int get() = list.size.plus(1)

    fun load(locationId: String, force: Boolean = false) {
        collection = collection ?: Firebase.firestore
            .collection("locations")
            .document(locationId)
            .collection("hives")

        loadCollection(force)
    }

    override fun decode(snapshot: QuerySnapshot): List<HiveModel> {
        return snapshot.toObjects<HiveModel>()
    }

    override fun unload() {
        super.unload()
        collection = null
    }

    suspend fun add(hive: HiveModel) {
        collection!!.add(hive).await()
    }

    suspend fun update(hive: HiveModel) {
        document(hive).set(hive).await()
    }

    suspend fun delete(hive: HiveModel) {
        document(hive).delete().await()
    }

    private fun document(hive: HiveModel): DocumentReference {
        return collection!!.document(hive.id)
    }
}