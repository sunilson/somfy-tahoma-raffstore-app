package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.domain

import at.sunilson.tahomaraffstorecontroller.mobile.entities.Schedule
import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.getChildListFlow
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FlowUseCase
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow

class GetSchedulesUseCase(private val firebaseDatabase: FirebaseDatabase) : FlowUseCase<Unit, List<Schedule>>() {
    override fun doWork(params: Unit): Flow<List<Schedule>> {
        return firebaseDatabase.reference
            .child("schedules")
            .getChildListFlow()
    }
}