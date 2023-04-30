package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.domain

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.SchedulesBroadcastReceiver
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.data.models.Schedule
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.TimeUtils
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import java.util.concurrent.TimeUnit

class UpdateSchedulesUseCase(
    private val context: Context,
    private val tahomaLocalDatabase: TahomaLocalDatabase,
    private val alarmManager: AlarmManager
) : UseCase<Unit, Unit>() {
    override suspend fun doWork(params: Unit) {
        // TODO Load schedules
        val schedules = listOf<Schedule>()

        schedules.forEach { schedule ->
            val intent = Intent(context, SchedulesBroadcastReceiver::class.java).apply {
                putExtra(KEY_EXTRA_SCHEDULE_ID, schedule.id)
            }
            val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                TimeUtils.getCurrentZoneDateTime().withHour(schedule.hourOfDay).withMinute(schedule.minuteOfHour).toEpochSecond() * 1000L,
                TimeUnit.DAYS.toMillis(1),
                pendingIntent
            )
        }

        TODO("Not yet implemented")
    }

    companion object {
        const val KEY_EXTRA_SCHEDULE_ID = "scheduleId"
    }
}