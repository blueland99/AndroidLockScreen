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
import com.blueland.lockscreen.util.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LockForegroundService : Service() {

    @Inject
    lateinit var lockServiceManager: LockServiceManager
    private val lockReceiver = LockReceiver

    override fun onBind(intent: Intent?): IBinder? {
        return null // 이 서비스는 바인딩되지 않음
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel() // 알림 채널 생성

        val notification = createNotification() // 알림 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(SERVICE_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC) // 포그라운드 서비스 시작 (Android 14 이상)
        } else { // Android 13 이하
            startForeground(SERVICE_ID, notification) // 포그라운드 서비스 시작
        }
        registerLockReceiver() // LockReceiver 등록

        return START_STICKY // 서비스 재시작 정책 설정
    }

    override fun onDestroy() {
        unregisterLockReceiver() // LockReceiver 해제
        lockServiceManager.stop() // LockServiceManager 중지
        super.onDestroy()
    }

    private fun registerLockReceiver() {
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON) // 화면 켜짐 이벤트 수신
            addAction(Intent.ACTION_SCREEN_OFF) // 화면 꺼짐 이벤트 수신
        }
        registerReceiver(lockReceiver, intentFilter) // BroadcastReceiver 등록
    }

    private fun unregisterLockReceiver() {
        unregisterReceiver(lockReceiver) // BroadcastReceiver 해제
    }

    private fun createNotificationChannel() {
        val notificationChannel = NotificationHelper.createChannel(
            LOCK_CHANNEL,
            getStringResource(R.string.app_name), // 채널 이름
            NotificationManager.IMPORTANCE_HIGH,
            getStringResource(R.string.app_name) // 채널 설명
        )

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(notificationChannel) // 알림 채널 등록
    }

    private fun getStringResource(
        @StringRes stringRes: Int
    ): String {
        return applicationContext.getString(stringRes) // 문자열 리소스 반환
    }

    private fun createNotification(): Notification {
        return NotificationHelper.createBuilder(
            context = this,
            channelId = LOCK_CHANNEL,
            title = getStringResource(R.string.app_name), // 알림 제목
            text = getStringResource(R.string.app_name), // 알림 내용
            icon = R.drawable.ic_launcher_foreground // 알림 아이콘
        )
    }

    private companion object {
        const val LOCK_CHANNEL = "LOCK_CHANNEL" // 알림 채널 ID
        const val SERVICE_ID: Int = 1 // 서비스 ID
    }
}
