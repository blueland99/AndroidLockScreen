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

    val TAG = this.javaClass.simpleName

    @Inject
    lateinit var lockServiceManager: LockServiceManager

    // 오버레이 권한 요청용 런처
    private val overlayPermissionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (needsOverlayPermission(this)) {
            // 거부
        } else {
            // 허용
            startLockService()
        }
    }

    // 오버레이 권한이 필요한지 확인
    fun needsOverlayPermission(context: Context): Boolean {
        return !Settings.canDrawOverlays(context)
    }

    private fun checkPermissions() {
        // 오버레이 권한 확인 및 요청
        if (needsOverlayPermission(this)) {
            Log.e(TAG, "checkPermissions: 오버레이")
            val overlayIntent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${packageName}")
            )
            overlayPermissionLauncher.launch(overlayIntent)
        } else {
            startLockService()
        }
    }

    private fun startLockService() {
        lockServiceManager.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 권한 확인 및 요청 시작
        checkPermissions()

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