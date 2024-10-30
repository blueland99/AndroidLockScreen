package com.blueland.lockscreen.util

import android.app.ActivityManager
import android.content.Context

inline fun <reified T> Context.isServiceRunning() =
    (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == T::class.java.name }

// for type erasure
fun <T> Context.isServiceRunning(service: Class<T>) =
    (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == service.name }