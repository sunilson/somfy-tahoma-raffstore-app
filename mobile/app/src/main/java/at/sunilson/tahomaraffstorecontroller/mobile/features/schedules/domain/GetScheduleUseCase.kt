package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.domain

import at.sunilson.tahomaraffstorecontroller.mobile.entities.Schedule
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.getSuspending
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import com.google.firebase.database.FirebaseDatabase

class GetScheduleUseCase(private val firebaseDatabase: FirebaseDatabase) : UseCase<String, Schedule>() {
    override suspend fun doWork(params: String): Schedule {
        return firebaseDatabase.reference
            .child("schedules/$params")
            .getSuspending<Schedule>() ?: error("Schedule not found")
    }
}