package org.kiimo.me.services

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.kiimo.me.app.App
import org.kiimo.me.models.DeviceToken
import org.kiimo.me.service.network.client.KiimoDeliverHttpClient
import org.kiimo.me.util.AppConstants
import org.kiimo.me.util.PreferenceUtils
import org.kiimo.me.util.RxBus
import timber.log.Timber
import javax.inject.Inject


class KiimoFirebaseMessagingService : FirebaseMessagingService() {

    init {
        App.instance.getAppComponent().inject(this)
    }

    private lateinit var localBroadcastManager: LocalBroadcastManager

    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var kiimoAppClient: KiimoDeliverHttpClient

    override fun onCreate() {
        super.onCreate()
        localBroadcastManager = LocalBroadcastManager.getInstance(this)
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Timber.d("Refreshed token: $token")
        compositeDisposable.add(
            kiimoAppClient.putDeviceToken(
                "application/json",
                PreferenceUtils.getUserToken(applicationContext),
                DeviceToken(token)
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val intent = Intent(AppConstants.FIREBASE_BROADCAST)
        intent.putExtra(AppConstants.FIREBASE_PAYLOAD, remoteMessage.data["payload"])

        Log.i("notification", remoteMessage.data["payload"])
        Timber.i("notification data", remoteMessage.data["payload"])
        if (::localBroadcastManager.isInitialized) {
            localBroadcastManager.sendBroadcast(intent)
        } else {
            RxBus.publish(MessageEvent(intent))
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}