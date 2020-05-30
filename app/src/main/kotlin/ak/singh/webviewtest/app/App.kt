package ak.singh.webviewtest.app

import android.app.Application
import com.crashlytics.android.Crashlytics
import ak.singh.webviewtest.BuildConfig
import io.fabric.sdk.android.Fabric
import timber.log.Timber

internal class App: Application() {

    override fun onCreate() {
        super.onCreate()

        Fabric.with(applicationContext, Crashlytics())

        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}
