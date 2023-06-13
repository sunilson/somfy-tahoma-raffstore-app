package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.domain

import at.sunilson.tahomaraffstorecontroller.mobile.entities.Schedule
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.removeValueSuspending
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import com.google.firebase.database.FirebaseDatabase

class DeleteScheduleUseCase(private val firebaseDatabase: FirebaseDatabase) : UseCase<Schedule, Unit>() {
    override suspend fun doWork(params: Schedule) {
        firebaseDatabase.reference
            .child("schedules/${params.id}")
            .removeValueSuspending()
    }
}