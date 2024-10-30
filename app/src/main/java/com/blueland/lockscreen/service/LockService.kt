package com.blueland.lockscreen.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.annotation.StringRes
import com.blueland.lockscreen.R
import com.blueland.lockscreen.receiver.LockReceiver
import com.blueland.lockscreen.util.SimpleNotificationBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LockService : Service() {

    @Inject
    lateinit var lockServiceManager: LockServiceManager
    private val lockReceiver = LockReceiver

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        val notification = createNotificationBuilder()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(SERVICE_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
        } else { // Android 12 이하
            startForeground(SERVICE_ID, notification)
        }
        startLockReceiver()

        return START_STICKY
    }

    override fun onDestroy() {
        stopLockReceiver()
        lockServiceManager.stop()
        super.onDestroy()
    }

    private fun startLockReceiver() {
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
        }
        registerReceiver(lockReceiver, intentFilter)
    }

    private fun stopLockReceiver() {
        unregisterReceiver(lockReceiver)
    }

    private fun createNotificationChannel() {
        val notificationChannel = SimpleNotificationBuilder.createChannel(
            LOCK_CHANNEL,
            getStringWithContext(R.string.app_name),
            NotificationManager.IMPORTANCE_HIGH,
            getStringWithContext(R.string.app_name)
        )

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun getStringWithContext(
        @StringRes stringRes: Int
    ): String {
        return applicationContext.getString(stringRes)
    }

    private fun createNotificationBuilder(): Notification {
        return SimpleNotificationBuilder.createBuilder(
            context = this,
            channelId = LOCK_CHANNEL,
            title = getStringWithContext(R.string.app_name),
            text = getStringWithContext(R.string.app_name),
            icon = R.drawable.ic_launcher_foreground,
        )
    }

    private companion object {
        const val LOCK_CHANNEL = "LOCK_CHANNEL"
        const val SERVICE_ID: Int = 1
    }
}