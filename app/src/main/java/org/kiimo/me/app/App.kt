package org.kiimo.me.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.disposables.CompositeDisposable
import org.kiimo.me.R
import org.kiimo.me.app.di.AppComponent
import org.kiimo.me.app.di.ApplicationModule
import org.kiimo.me.app.di.DaggerAppComponent
import org.kiimo.me.service.network.module.DeliveryHttpModule
import org.kiimo.me.service.network.module.NetworkModule
import org.kiimo.me.service.network.module.OkHttpClientModule
import org.kiimo.me.util.PreferenceUtils


class App : Application(), Application.ActivityLifecycleCallbacks {

    private var appComponent: AppComponent? = null
    val compositeDisposable = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()

        instance = this

        initDependecyIContainer()
        Places.initialize(this, getString(R.string.google_api_key))
        initFCMServices()
    }

    fun initFCMServices() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {

                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result!!.token
                PreferenceUtils.saveFirebaseToken(this, token)
            })
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {

    }

    override fun onActivityPaused(activity: Activity?) {

    }

    override fun onActivityResumed(activity: Activity?) {

    }

    override fun onActivityStarted(activity: Activity?) {

    }

    override fun onActivityDestroyed(activity: Activity?) {

    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

    }

    override fun onActivityStopped(activity: Activity?) {

    }

    fun getAppComponent(): AppComponent {
        return appComponent!!
    }


    private fun initDependecyIContainer() {
        appComponent = DaggerAppComponent.builder().applicationModule(ApplicationModule(this))
            .networkModule(NetworkModule())
            .deliveryHttpModule(DeliveryHttpModule(ServerUrl.BASE_AUTHORIZATION_URL))
            .okHttpClientModule(OkHttpClientModule())
            .build()

    }

    companion object {
        lateinit var instance: App
    }
}