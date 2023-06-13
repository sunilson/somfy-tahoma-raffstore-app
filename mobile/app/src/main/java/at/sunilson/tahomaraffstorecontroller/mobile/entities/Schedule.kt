package at.sunilson.tahomaraffstorecontroller.mobile.entities

import androidx.compose.runtime.Stable
import androidx.room.Entity
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.entities.EntityWithId
import com.google.errorprone.annotations.Immutable
import com.google.firebase.database.Exclude
import com.google.firebase.encoders.annotations.Encodable.Ignore
import kotlinx.serialization.Serializable
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.UUID

@Serializable
@Immutable
@Stable
data class Schedule(
    override val id: String = UUID.randomUUID().toString(),
    val label: String = "",
    val weekdays: List<DayOfWeek> = emptyList(),
    val hourOfDay: Int = 0,
    val minuteOfHour: Int = 0,
    val onlyWhenSunIsShining: Boolean = false,
    val actionGroupIds: List<String> = emptyList()
) : EntityWithId {

    @get:Exclude
    val time: LocalTime
        get() = LocalTime.of(hourOfDay, minuteOfHour)

}