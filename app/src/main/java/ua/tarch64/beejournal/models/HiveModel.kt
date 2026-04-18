package ua.tarch64.beejournal.models

import androidx.compose.ui.graphics.Color
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp

data class HiveModel(
    @DocumentId
    override var id: String = "",

    var position: UInt = 0.toUInt(),
    var frames: List<UInt> = listOf(0.toUInt()),
    var children: UInt = 0.toUInt(),
    var honey: UInt = 0.toUInt(),

    @get:PropertyName("color")
    @set:PropertyName("color")
    var colorHex: String = "",

    @ServerTimestamp
    var createdAt: Timestamp = Timestamp.now()
) : Identifiable {
    var color: Color
        get() = colorHex.toColor()
        set(value) {
            colorHex = value.toHex()
        }

    companion object {
        val COLORS = listOf(
            Color.White,
            Color.Yellow,
            Color.Red,
            Color.Green,
            Color.Blue.lighten(0.2f)
        )
    }
}
