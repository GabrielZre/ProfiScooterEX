package com.example.profiscooterex.ui.dashboard.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.profiscooterex.ui.dashboard.DashboardViewModel
import com.example.profiscooterex.ui.dashboard.UiState
import com.example.profiscooterex.ui.theme.AppTheme
import com.example.profiscooterex.ui.theme.Green200
import com.example.profiscooterex.ui.theme.Green500
import com.example.profiscooterex.ui.theme.GreenGradient
import com.example.profiscooterex.ui.theme.LightColor
import com.ramcosta.composedestinations.annotation.Destination

import kotlinx.coroutines.launch
import kotlin.math.floor


fun Animatable<Float, AnimationVector1D>.toUiState() = UiState(
    arcValue = value,
    speed = "%.1f".format(value * 100),
)

@SuppressLint("UnrememberedMutableState", "CoroutineCreationDuringComposition")
@Destination
@Composable
fun DashboardSpeedIndicator() {
    val coroutineScope = rememberCoroutineScope()
    //val viewModel: DashboardViewModel = hiltViewModel()
    //var currentSpeed = mutableFloatStateOf(viewModel.currentSpeed)
    //val animation = remember { Animatable(viewModel.currentSpeed / 100) }
    val animation = remember { Animatable(0f) }


    //animation.toUiState().speed = currentSpeed.toString()
    DashboardSpeedIndicator(animation.toUiState())
    DisposableEffect(Unit) {
            coroutineScope.launch {
                animation.animateTo(1f, keyframes {
                durationMillis = 1000
            })
            animation.animateTo(0f, keyframes {
                durationMillis = 1000
            })
            }
        onDispose {
            coroutineScope.launch {
                animation.stop()
            }
        }
    }


    fun start() {
        coroutineScope.launch {
            animation.animateTo(0f, keyframes {
                durationMillis = 1000
            })
            /*animation.animateTo(currentSpeed.floatValue / 100, keyframes {
                durationMillis = 1000
            })*/
        }
    }

    fun stop() {
        coroutineScope.launch {
            animation.stop()
        }
    }
}

@Composable
private fun DashboardSpeedIndicator(state: UiState) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        SpeedIndicator(state = state)
    }
}

@Composable
fun SpeedIndicator(state: UiState) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        CircularSpeedIndicator(state.arcValue, 240f)
        SpeedValue(state.speed)
    }
}

@Composable
fun SpeedValue(value: String) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 45.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Kmh",
            color = Color.White,
            style = MaterialTheme.typography.caption
        )
    }
}

@Composable
fun CircularSpeedIndicator(value: Float, angle: Float) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
    ) {
        drawLines(value, angle)
        drawArcs(value, angle)
    }
}

fun DrawScope.drawArcs(progress: Float, maxValue: Float) {
    val startAngle = 270 - maxValue / 2
    val sweepAngle = maxValue * progress

    val topLeft = Offset(50f, 50f)
    val size = Size(size.width - 100f, size.height - 100f)

    fun drawBlur() {
        for (i in 0..20) {
            drawArc(
                color = Green200.copy(alpha = i / 900f),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = size,
                style = Stroke(width = 80f + (20 - i) * 20, cap = StrokeCap.Round)
            )
        }
    }

    fun drawStroke() {
        drawArc(
            color = Green500,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = topLeft,
            size = size,
            style = Stroke(width = 86f, cap = StrokeCap.Round)
        )
    }

    fun drawGradient() {
        drawArc(
            brush = GreenGradient,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = topLeft,
            size = size,
            style = Stroke(width = 80f, cap = StrokeCap.Round)
        )
    }

    drawBlur()
    drawStroke()
    drawGradient()
}

fun DrawScope.drawLines(progress: Float, maxValue: Float, numberOfLines: Int = 40) {
    val oneRotation = maxValue / numberOfLines
    val startValue = if (progress == 0f) 0 else floor(progress * numberOfLines).toInt() + 1

    for (i in startValue..numberOfLines) {
        rotate(i * oneRotation + (180 - maxValue) / 2) {
            drawLine(
                LightColor,
                Offset(if (i % 5 == 0) 80f else 30f, size.height / 2),
                Offset(0f, size.height / 2),
                8f,
                StrokeCap.Round
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00FF00)
@Composable
fun DashboardSpeedIndicatorPreview() {
    AppTheme {
        Surface() {
            DashboardSpeedIndicator(
                UiState(
                    speed = "120.5",
                    ping = "5 ms",
                    maxSpeed = "150.0 mbps",
                    arcValue = 0.83f,
                )
            )
        }
    }
}
