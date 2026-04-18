package ua.tarch64.beejournal.services

import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await
import ua.tarch64.beejournal.models.HiveModel

class HivesService : CollectionService<HiveModel>() {
    private var collection: CollectionReference? = null
    override val query: Query get() = collection!!.orderBy("position")

    val nextPosition: Int get() = list.value.size.plus(1)

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

    companion object {
        val instance = HivesService()
    }
}