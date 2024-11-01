package com.blueland.lockscreen.util

import android.app.ActivityManager
import android.content.Context

/**
 * 확장 함수: 주어진 서비스가 실행 중인지 확인
 *
 * @return Boolean: 서비스가 실행 중이면 true, 그렇지 않으면 false
 */
inline fun <reified T> Context.isServiceRunning(): Boolean =
    (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == T::class.java.name }

/**
 * 주어진 서비스 클래스가 실행 중인지 확인
 *
 * @param service: 확인할 서비스 클래스의 타입
 * @return Boolean: 서비스가 실행 중이면 true, 그렇지 않으면 false
 */
fun <T> Context.isServiceRunning(service: Class<T>): Boolean =
    (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == service.name }
