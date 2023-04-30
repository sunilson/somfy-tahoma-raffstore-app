package at.sunilson.tahomaraffstorecontroller.mobile.main

import android.app.Application
import at.sunilson.tahomaraffstorecontroller.mobile.di.mainModule
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.di.groupsModule
import at.sunilson.tahomaraffstorecontroller.mobile.features.widget.di.widgetModule
import com.google.firebase.FirebaseApp
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

}