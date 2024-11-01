package com.blueland.lockscreen

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.blueland.lockscreen.ui.theme.ComposeLockScreenTheme
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 잠금 화면을 표시하는 Activity
 */
@AndroidEntryPoint
class LockActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Android O_MR1 이상에서 잠금 화면에 표시되도록 설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
        }

        setContent {
            ComposeLockScreenTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    // 배경 이미지 설정
                    Image(
                        painter = painterResource(id = R.drawable.background), // 여기에 배경 이미지 리소스 ID를 넣으세요
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(), // 이미지가 전체 화면을 채우도록 설정
                        contentScale = ContentScale.FillBounds
                    )
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        // 잠금 해제 상태를 관리하는 변수
                        var isUnlocked by remember { mutableStateOf(false) }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.Center, // 중앙 정렬
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {

                            // 현재 시간을 실시간으로 보여주는 변수
                            var currentTime by remember { mutableStateOf("") }
                            // 오늘 날짜를 실시간으로 보여주는 변수
                            var currentDate by remember { mutableStateOf("") }

                            // 시간 업데이트를 위한 LaunchedEffect
                            LaunchedEffect(Unit) {
                                while (true) {
                                    // 현재 시간 업데이트
                                    currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                                    // 오늘 날짜 업데이트
                                    currentDate = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(Date())
                                    kotlinx.coroutines.delay(1000) // 1초마다 업데이트
                                }
                            }

                            // 현재 날짜 표시
                            Text(
                                text = currentDate, // 현재 날짜 텍스트
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(vertical = 8.dp) // 위아래 패딩 추가
                            )

                            // 현재 시간 표시 (크고 중앙에 배치)
                            Text(
                                text = currentTime, // 현재 시간 텍스트
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = MaterialTheme.typography.headlineLarge.fontSize * 2 // 텍스트 크기 조정
                                ),
                                modifier = Modifier.padding(vertical = 16.dp) // 위아래 패딩 추가
                            )
                        }

                        SwipeUnlockButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = "밀어서 잠금 해제", // 버튼 텍스트
                            isComplete = isUnlocked, // 잠금 해제 여부
                            onSwipe = {
                                isUnlocked = true // 스와이프 완료 시 잠금 해제 상태로 변경
                                finish() // 화면 종료
                            }
                        )
                    }
                }
            }
        }
    }
}
