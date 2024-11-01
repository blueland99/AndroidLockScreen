package com.blueland.lockscreen.service.core

import android.app.Service
import android.content.Context
import android.content.Intent
import com.blueland.lockscreen.util.isServiceRunning

/**
 * 포그라운드 서비스를 관리하는 기본 클래스
 *
 * @param T: 관리할 서비스 클래스
 * @property context: 애플리케이션 컨텍스트
 * @property targetClass: 관리할 서비스 클래스 타입
 */
abstract class BaseForegroundServiceManager<T : Service>(
    val context: Context, // 애플리케이션 컨텍스트
    val targetClass: Class<T>, // 관리할 서비스 클래스 타입
) {
    /**
     * 포그라운드 서비스를 시작
     *
     * 이미 서비스가 실행 중이지 않은 경우에만 시작
     */
    fun start() = synchronized(this) {
        val intent = Intent(context, targetClass)

        if (!context.isServiceRunning(targetClass)) {
            context.startForegroundService(intent) // 포그라운드 서비스 시작
        }
    }

    /**
     * 포그라운드 서비스를 중지
     *
     * 현재 서비스가 실행 중인 경우에만 중지
     */
    fun stop() = synchronized(this) {
        val intent = Intent(context, targetClass)

        if (context.isServiceRunning(targetClass)) {
            context.stopService(intent) // 서비스 중지
        }
    }
}
