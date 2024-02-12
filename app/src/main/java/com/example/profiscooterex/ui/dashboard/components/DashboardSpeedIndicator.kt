package com.example.profiscooterex.ui.dashboard.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddRoad
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.profiscooterex.ui.dashboard.DashboardState
import com.example.profiscooterex.ui.dashboard.UiState
import com.example.profiscooterex.ui.theme.AppTheme
import com.example.profiscooterex.ui.theme.Green200
import com.example.profiscooterex.ui.theme.Green500
import com.example.profiscooterex.ui.theme.GreenGradient
import com.example.profiscooterex.ui.theme.LightColor
import com.ramcosta.composedestinations.annotation.Destination
import kotlin.math.floor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Animatable<Float, AnimationVector1D>.toUiState(
    averageSpeed: Float,
    distanceTrip: Float,
    dashboardState: DashboardState
) =
    UiState(
        arcValue = value,
        speed = "%.1f".format(value * 100),
        averageSpeed = "%.1f".format(averageSpeed),
        distanceTrip = "%.1f".format(distanceTrip),
        dashboardStateColor =
            when (dashboardState) {
                DashboardState.Active -> Color.Green
                DashboardState.Fetching -> Color.Cyan
                DashboardState.Ready -> Color.White
                DashboardState.Stopped -> Color.Yellow
                DashboardState.Disabled -> Color.Red
            }
    )

@SuppressLint("UnrememberedMutableState", "CoroutineCreationDuringComposition")
@Destination
@Composable
fun DashboardSpeedIndicator(
    currentSpeed: Float,
    averageSpeed: Float,
    distanceTrip: Float,
    dashboardState: DashboardState,
    onClickDashboard: () -> Unit,
    onLongClickDashboard: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val animation = remember { Animatable(currentSpeed / 100) }
    val initAnimation = remember { mutableStateOf(false) }

    animation.toUiState(averageSpeed, distanceTrip, dashboardState).speed = currentSpeed.toString()

    DashboardSpeedIndicator(
        animation.toUiState(averageSpeed, distanceTrip, dashboardState),
        onClickDashboard,
        onLongClickDashboard
    )

    LaunchedEffect(initAnimation.value) {
        if (
            !initAnimation.value &&
                (dashboardState == DashboardState.Disabled ||
                    dashboardState == DashboardState.Ready)
        ) {
            launchAnimation(coroutineScope, animation) { initAnimation.value = true }
        }
    }
    if (initAnimation.value) {
        start(currentSpeed, coroutineScope, animation)
    }
}

suspend fun launchAnimation(
    coroutineScope: CoroutineScope,
    animation: Animatable<Float, AnimationVector1D>,
    onAnimationComplete: () -> Unit
) {
    coroutineScope.launch {
        animation.animateTo(1f, keyframes { durationMillis = 1000 })
        animation.animateTo(0f, keyframes { durationMillis = 1000 })
        onAnimationComplete()
    }
}

fun start(
    currentSpeed: Float,
    coroutineScope: CoroutineScope,
    animation: Animatable<Float, AnimationVector1D>
) {
    coroutineScope.launch {
        animation.animateTo(currentSpeed / 100, keyframes { durationMillis = 1000 })
    }
}

@Composable
private fun DashboardSpeedIndicator(
    state: UiState,
    onClickDashboard: () -> Unit,
    onLongClickDashboard: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        SpeedIndicator(state = state, onClickDashboard, onLongClickDashboard)
    }
}

@Composable
fun SpeedIndicator(state: UiState, onClickDashboard: () -> Unit, onLongClickDashboard: () -> Unit) {
    val rememberedClick by rememberUpdatedState(onClickDashboard) // Capture updated state

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier =
            Modifier.fillMaxWidth()
                .aspectRatio(1f)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { rememberedClick() },
                        onLongPress = { onLongClickDashboard() },
                    )
                }
                .bounceClick()
    ) {
        CircularSpeedIndicator(state.arcValue, 240f)
        SpeedValue(state.speed, state.averageSpeed, state.distanceTrip, state.dashboardStateColor)
    }
}

@Composable
fun SpeedValue(
    value: String,
    averageSpeed: String,
    distanceTrip: String,
    dashboardStateColor: Color
) {
    Column(
        Modifier.fillMaxSize().padding(top = 50.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            tint = dashboardStateColor,
            contentDescription = "Location On",
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Text(text = value, fontSize = 45.sp, color = Color.White, fontWeight = FontWeight.Bold)
        Text("Kmh", color = Color.White, style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(30.dp))
        Row {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.AddRoad,
                    tint = Color.Gray,
                    contentDescription = "Distance Trip"
                )
                Text(
                    text = distanceTrip,
                    fontSize = 15.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.width(60.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Speed,
                    tint = Color.Gray,
                    contentDescription = "Average speed"
                )
                Text(
                    text = averageSpeed,
                    fontSize = 15.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun CircularSpeedIndicator(value: Float, angle: Float) {
    Canvas(modifier = Modifier.fillMaxSize().padding(40.dp)) {
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

enum class ButtonState {
    Pressed,
    Idle
}

fun Modifier.bounceClick() = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by
        animateFloatAsState(if (buttonState == ButtonState.Pressed) 0.90f else 1f, label = "")

    this.graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .pointerInput(buttonState, Unit) {
            awaitPointerEventScope {
                buttonState =
                    if (buttonState == ButtonState.Pressed) {
                        waitForUpOrCancellation()
                        ButtonState.Idle
                    } else {
                        awaitFirstDown(false)
                        ButtonState.Pressed
                    }
            }
        }
}

@Preview(showBackground = true, backgroundColor = 0xFF00FF00)
@Composable
fun DashboardSpeedIndicatorPreview() {
    AppTheme {
        Surface {
            DashboardSpeedIndicator(
                UiState(
                    speed = "120.5",
                    averageSpeed = "35.00",
                    distanceTrip = "22.00",
                    maxSpeed = "150.0 mbps",
                    arcValue = 0.83f,
                    dashboardStateColor = Color.White
                ),
                onClickDashboard = {},
                onLongClickDashboard = {}
            )
        }
    }
}
