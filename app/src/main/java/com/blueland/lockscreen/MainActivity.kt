package com.blueland.lockscreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.blueland.lockscreen.service.LockServiceManager
import com.blueland.lockscreen.ui.theme.ComposeLockScreenTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val TAG = this.javaClass.simpleName // 클래스 이름을 태그로 사용

    @Inject
    lateinit var lockServiceManager: LockServiceManager // LockServiceManager 주입

    // 오버레이 권한 요청용 런처
    private val overlayPermissionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (isOverlayPermissionGranted(this)) {
            Log.e(TAG, "Overlay permission denied") // 권한 거부 시 로그 출력
        } else {
            Log.d(TAG, "Overlay permission granted") // 권한 허용 시 로그 출력
            startLockService() // 잠금 서비스 시작
        }
    }

    // 오버레이 권한이 필요한지 확인
    private fun isOverlayPermissionGranted(context: Context): Boolean {
        return !Settings.canDrawOverlays(context) // 권한 필요 여부 반환
    }

    private fun checkPermissions() {
        // 오버레이 권한 확인 및 요청
        if (isOverlayPermissionGranted(this)) {
            Log.e(TAG, "checkPermissions: Overlay permission needed")
            val overlayIntent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${packageName}") // 현재 패키지의 오버레이 권한 설정 화면으로 이동
            )
            overlayPermissionLauncher.launch(overlayIntent) // 권한 요청 실행
        } else {
            startLockService() // 권한이 있으면 잠금 서비스 시작
        }
    }

    private fun startLockService() {
        lockServiceManager.start() // 잠금 서비스 시작
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissions() // 권한 확인 및 요청 시작

        setContent {
            ComposeLockScreenTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeLockScreenTheme {
        Greeting("Android")
    }
}
