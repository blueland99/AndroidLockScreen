package com.blueland.lockscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun SwipeUnlockButton(
    modifier: Modifier = Modifier,
    text: String,
    isComplete: Boolean,
    doneImageVector: ImageVector = Icons.Rounded.Done,
    onSwipe: () -> Unit
) {
    val buttonWidthPx = with(LocalDensity.current) { 64.dp.toPx() } // 스와이프 아이콘 너비
    var viewWidthPx by remember { mutableFloatStateOf(0f) } // 스와이프 뷰의 실제 너비
    val offsetX = remember { mutableFloatStateOf(0f) }
    val swipeComplete = remember { mutableStateOf(false) }

    val draggableState = rememberDraggableState { delta ->
        if (!swipeComplete.value) {
            offsetX.floatValue = (offsetX.floatValue + delta).coerceIn(0f, viewWidthPx - buttonWidthPx)
        }
    }

    // 텍스트 애니메이션 알파 값
    val alpha by animateFloatAsState(
        targetValue = if (swipeComplete.value) 0f else 1f,
        animationSpec = tween(300, easing = LinearEasing)
    )

    // 스와이프 뷰 너비에 따른 임계값 설정
    LaunchedEffect(offsetX.floatValue, viewWidthPx) {
        val swipeThreshold = viewWidthPx - buttonWidthPx
        if (offsetX.floatValue >= swipeThreshold && !swipeComplete.value) {
            swipeComplete.value = true
            onSwipe()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .onGloballyPositioned { coordinates ->
                viewWidthPx = coordinates.size.width.toFloat() // 스와이프 뷰 너비 가져오기
            }
            .clip(CircleShape)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFFD3D3D3), Color(0xFFB0B0B0)) // 회색 그라데이션
                )
            )
            .animateContentSize()
            .then(if (swipeComplete.value) Modifier.width(64.dp) else Modifier.fillMaxWidth())
    ) {
        SwipeIndicator(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .alpha(alpha)
                .offset { IntOffset(offsetX.floatValue.roundToInt(), 0) }
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    onDragStopped = {
                        if (offsetX.floatValue < viewWidthPx - buttonWidthPx) {
                            offsetX.floatValue = 0f // 완료되지 않으면 원위치로 돌아감
                        }
                    }
                )
        )
        Text(
            text = text,
            color = Color.Black, // 텍스트 색상 변경
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(alpha)
                .padding(horizontal = 80.dp)
                .offset { IntOffset(offsetX.floatValue.roundToInt(), 0) }
        )
        AnimatedVisibility(
            visible = swipeComplete.value && !isComplete,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            CircularProgressIndicator(
                color = Color.Black, // 색상 변경
                strokeWidth = 1.dp,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            )
        }
        AnimatedVisibility(
            visible = isComplete,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Icon(
                imageVector = doneImageVector,
                contentDescription = null,
                tint = Color.Black, // 색상 변경
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(44.dp)
            )
        }
    }
}

@Composable
private fun SwipeIndicator(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxHeight()
            .padding(2.dp)
            .clip(CircleShape)
            .aspectRatio(1f)
            .background(Color.White) // 원형 표시기 배경색
    ) {
        Icon(
            imageVector = Icons.Rounded.Check,
            contentDescription = null,
            tint = Color.Gray, // 색상 변경
            modifier = Modifier.size(36.dp)
        )
    }
}
