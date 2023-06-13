package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.domain

import at.sunilson.tahomaraffstorecontroller.mobile.entities.Schedule
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.ExecuteActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase

class ExecuteScheduleUseCase(private val executeActionGroup: ExecuteActionGroup) : UseCase<Schedule, Unit>() {
    override suspend fun doWork(params: Schedule) {
        params.actionGroupIds.forEach { executeActionGroup(it) }
    }
}