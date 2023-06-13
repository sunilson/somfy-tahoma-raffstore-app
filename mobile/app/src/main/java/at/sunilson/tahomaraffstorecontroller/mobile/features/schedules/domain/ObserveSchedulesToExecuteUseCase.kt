package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.domain

import at.sunilson.tahomaraffstorecontroller.mobile.entities.Schedule
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.getChildListFlow
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FlowUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.TimeUtils
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class ObserveSchedulesToExecuteUseCase(
    private val firebaseDatabase: FirebaseDatabase,
    private val checkIfSunnyUseCase: CheckIfSunnyUseCase
) : FlowUseCase<Unit, List<Schedule>>() {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun doWork(params: Unit): Flow<List<Schedule>> {
        return firebaseDatabase.reference
            .child("schedules")
            .getChildListFlow<Schedule>()
            .flatMapLatest { allSchedules ->
                flow {
                    while (true) {
                        delay(1000)
                        emit(checkIfSchedulesNeedExecuting(allSchedules))
                    }
                }
            }
            .filter { it.isNotEmpty() }
    }

    private suspend fun checkIfSchedulesNeedExecuting(allSchedules: List<Schedule>): List<Schedule> {
        val currentDateTime = TimeUtils.getCurrentZoneDateTime()
        val currentWeekDay = currentDateTime.dayOfWeek

        val timeRelevantSchedules = allSchedules.filter { schedule -> schedule.weekdays.contains(currentWeekDay) }
            .filter { it.hourOfDay == currentDateTime.hour && it.minuteOfHour == currentDateTime.minute }

        if (timeRelevantSchedules.isEmpty()) return emptyList()

        val isSunShining = checkIfSunnyUseCase(Unit).getOrElse { false }

        return timeRelevantSchedules.filter { !it.onlyWhenSunIsShining || isSunShining }
    }

}