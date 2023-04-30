package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.data.models

import androidx.room.Entity
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Schedule(
    val id: String,
    val interval: Interval,
    val hourOfDay: Int,
    val minuteOfHour: Int,
    val actionGroupId: String
) {
    enum class Interval {
        DAILY, WEEKLY, MONTHLY
    }
}