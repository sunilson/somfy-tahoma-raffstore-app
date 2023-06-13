package at.sunilson.tahomaraffstorecontroller.mobile.main

import android.app.Application
import at.sunilson.tahomaraffstorecontroller.mobile.di.mainModule
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.di.groupsModule
import at.sunilson.tahomaraffstorecontroller.mobile.features.widget.di.widgetModule
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import timber.log.Timber

class TahomaControllerApplication : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        signInFirebaseIfRequired()

        startKoin {
            androidLogger()
            androidContext(this@TahomaControllerApplication)
            workManagerFactory()
            modules(listOf(mainModule, groupsModule, widgetModule))
        }

        //FavouritesWidgetUtils.enqueueWidgetUpdate(this)

        Timber.plant(TahomaLoggingTree())
        Timber.plant(Timber.DebugTree())
    }

    private fun signInFirebaseIfRequired() {
        if (Firebase.auth.currentUser == null) {
            Firebase.auth
                .signInWithEmailAndPassword("weisslinus@gmail.com", "99965653")
                .addOnSuccessListener { Timber.d("Signed in to Firebase") }
                .addOnFailureListener { Timber.e(it, "Sign in to Firebase failed") }
        } else {
            Timber.d("Already signed in to Firebase")
        }
    }

}