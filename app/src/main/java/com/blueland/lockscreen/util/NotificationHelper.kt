package com.blueland.lockscreen.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import androidx.annotation.DrawableRes

/**
 * 알림을 생성하기 위한 도우미 객체
 */
object NotificationHelper {

    /**
     * 알림 채널을 생성
     *
     * @param channelId: 채널 ID
     * @param name: 채널 이름
     * @param importance: 채널 중요도, 기본값은 IMPORTANCE_HIGH
     * @param description: 채널 설명
     * @return NotificationChannel: 생성된 알림 채널
     */
    fun createChannel(
        channelId: String,
        name: String,
        importance: Int = NotificationManager.IMPORTANCE_HIGH,
        description: String
    ) =
        NotificationChannel(channelId, name, importance).apply {
            setShowBadge(false)
            enableLights(true)
            this.description = description
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            lightColor = Color.BLACK
        }

    /**
     * 알림 빌더를 생성
     *
     * @param context: 애플리케이션 컨텍스트
     * @param channelId: 알림 채널 ID
     * @param title: 알림 제목
     * @param text: 알림 내용
     * @param icon: 알림 아이콘 리소스 ID
     * @return Notification: 생성된 알림
     */
    fun createBuilder(
        context: Context,
        channelId: String,
        title: String,
        text: String,
        @DrawableRes icon: Int,
    ) = Notification.Builder(context, channelId).apply {
        setOngoing(true)
        setShowWhen(true)
        setSmallIcon(icon)
        setContentTitle(title)
        setContentText(text)
    }.build()
}
