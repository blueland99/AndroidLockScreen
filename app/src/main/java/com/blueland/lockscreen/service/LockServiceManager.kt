package com.blueland.lockscreen.service

import android.content.Context
import com.blueland.lockscreen.service.core.BaseForegroundServiceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * LockForegroundService를 관리하는 클래스
 *
 * @property applicationContext: 애플리케이션 컨텍스트
 */
class LockServiceManager @Inject constructor(
    @ApplicationContext val applicationContext: Context // 애플리케이션 컨텍스트 주입
) : BaseForegroundServiceManager<LockForegroundService>(
    context = applicationContext, // 상위 클래스에 컨텍스트 전달
    targetClass = LockForegroundService::class.java // 관리할 서비스 클래스
)
