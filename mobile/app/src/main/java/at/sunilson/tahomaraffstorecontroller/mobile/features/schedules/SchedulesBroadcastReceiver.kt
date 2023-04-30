package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.domain.UpdateSchedulesUseCase
import timber.log.Timber

class SchedulesBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("Received schedule to execute: ${intent?.extras?.getString(UpdateSchedulesUseCase.KEY_EXTRA_SCHEDULE_ID)}")
    }
}