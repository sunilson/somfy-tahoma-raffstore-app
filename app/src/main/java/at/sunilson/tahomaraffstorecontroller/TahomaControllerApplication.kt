package at.sunilson.tahomaraffstorecontroller

import android.app.Application
import at.sunilson.tahomaraffstorecontroller.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TahomaControllerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@TahomaControllerApplication)
            modules(mainModule)
        }
    }

}