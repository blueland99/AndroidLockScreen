package com.blueland.lockscreen.service.core

import android.app.Service
import android.content.Context
import android.content.Intent
import com.blueland.lockscreen.util.isServiceRunning

abstract class BaseForegroundServiceManager<T : Service>(
    val context: Context,
    val targetClass: Class<T>,
) {
    fun start() = synchronized(this) {
        val intent = Intent(context, targetClass)

        if (!context.isServiceRunning(targetClass)) {
            context.startForegroundService(intent)
        }
    }

    fun stop() = synchronized(this) {
        val intent = Intent(context, targetClass)

        if (context.isServiceRunning(targetClass)) {
            context.stopService(intent)
        }
    }
}
