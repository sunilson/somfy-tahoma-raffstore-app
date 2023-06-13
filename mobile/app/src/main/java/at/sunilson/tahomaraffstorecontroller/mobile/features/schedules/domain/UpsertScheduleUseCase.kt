package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.domain

import at.sunilson.tahomaraffstorecontroller.mobile.entities.Schedule
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.setValueSuspending
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import com.google.firebase.database.FirebaseDatabase

class UpsertScheduleUseCase(private val firebaseDatabase: FirebaseDatabase) : UseCase<Schedule, Unit>() {
    override suspend fun doWork(params: Schedule) {
        firebaseDatabase.reference
            .child("schedules/${params.id}")
            .setValueSuspending(params)
    }
}