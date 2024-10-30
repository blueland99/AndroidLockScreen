package com.blueland.lockscreen

import SwipeUnlockButton
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.blueland.lockscreen.ui.theme.ComposeLockScreenTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LockActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
        }

        setContent {
            ComposeLockScreenTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    var isUnlocked by remember { mutableStateOf(false) } // 잠금 해제 상태를 관리하는 변수

                    SwipeUnlockButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = "밀어서 잠금 해제",
                        isComplete = isUnlocked,
                        onSwipe = {
                            isUnlocked = true // 스와이프 완료 시 잠금 해제 상태로 변경
                            finish() // 화면 닫기
                        }
                    )
                }
            }
        }
    }
}

