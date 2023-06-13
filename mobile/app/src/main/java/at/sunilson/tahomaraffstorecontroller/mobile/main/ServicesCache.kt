package at.sunilson.tahomaraffstorecontroller.mobile.main

import kotlinx.coroutines.flow.MutableStateFlow

object ServicesCache {
    val isSchedulesServiceRunning: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isTahomaServiceRunning: MutableStateFlow<Boolean> = MutableStateFlow(false)
}