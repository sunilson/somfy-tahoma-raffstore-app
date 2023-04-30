package at.sunilson.tahomaraffstorecontroller.mobile.main

import android.util.Log
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class TahomaLoggingTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.ERROR) {
            val crashlytics = Firebase.crashlytics
            if (t != null) {
                crashlytics.recordException(t)
            } else {
                crashlytics.log(message)
            }
        }
    }
}