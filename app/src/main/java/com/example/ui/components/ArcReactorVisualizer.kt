package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ArcCyanDark
import com.example.ui.theme.ArcCyanDim
import com.example.ui.theme.ArcCyanGlow
import com.example.ui.theme.ArcCyanPrimary
import com.example.ui.theme.CarbonBackground
import com.example.ui.theme.CarbonBorder
import com.example.ui.theme.StarkAmber
import com.example.ui.theme.StarkGold
import com.example.ui.theme.TelemetryGreen
import com.example.ui.theme.TextHolographicCyan
import com.example.ui.theme.TextHolographicWhite
import com.example.ui.theme.TextMuted
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

enum class CalibrationPhase(val title: String, val instruction: String, val subtitle: String) {
    READY("STANDBY", "INITIATE CORE STRENGTHENING", "Align posture. Prepare pelvic base."),
    CONTRACT("CONTRACT & LIFT", "ENGAGE PELVIC FLOOR", "Draw upwards and inward. Keep breathing."),
    HOLD("SUSTAIN POWER", "HOLD CONTRACTION", "Steady pressure. Do not clamp abdominal wall."),
    RELAX("RELEASE & VENT", "COMPLETE MUSCLE RELAXATION", "Full release. Vent all muscular tension."),
    COMPLETE("CALIBRATION COMPLETE", "OPTIMAL CORE STABILITY", "Session telemetry stored in vault.")
}

@Composable
fun ArcReactorVisualizer(
    phase: CalibrationPhase,
    progress: Float, // 0f to 1f within current interval
    secondsRemaining: Int,
    currentRep: Int,
    totalReps: Int,
    modifier: Modifier = Modifier,
    sizeDp: Dp = 280.dp
) {
    // Rotation animation for the reactor coil ring
    val infiniteTransition = rememberInfiniteTransition(label = "reactor_rotation")
    val rotationDegrees by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Pulsing energy resonance during contract/hold
    val pulseIntensity by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val isContracting = phase == CalibrationPhase.CONTRACT || phase == CalibrationPhase.HOLD
    val isRelaxing = phase == CalibrationPhase.RELAX

    val activeGlow = if (isContracting) pulseIntensity else 0.8f
    val baseScale = when (phase) {
        CalibrationPhase.CONTRACT -> 0.95f + (0.25f * progress)
        CalibrationPhase.HOLD -> 1.20f * (0.97f + 0.06f * pulseIntensity)
        CalibrationPhase.RELAX -> 1.15f - (0.25f * progress)
        CalibrationPhase.READY -> 0.9f
        CalibrationPhase.COMPLETE -> 1.05f
    }

    Box(
        modifier = modifier
            .size(sizeDp)
            .testTag("arc_reactor_visualizer"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val maxRadius = size.minDimension / 2f - 16.dp.toPx()

            // 1. Dark outer reactor casing
            drawCircle(
                color = CarbonBackground,
                radius = maxRadius,
                center = center
            )

            // Outer chassis stroke with cyan accent
            drawCircle(
                color = CarbonBorder,
                radius = maxRadius,
                center = center,
                style = Stroke(width = 3.dp.toPx())
            )

            // 2. Rotating electromagnet coils (10 Stark coils)
            rotate(rotationDegrees, pivot = center) {
                drawReactorCoils(
                    center = center,
                    coilRadius = maxRadius * 0.78f,
                    coilWidth = 14.dp.toPx(),
                    coilLength = 22.dp.toPx(),
                    isHighEnergy = isContracting,
                    glowMultiplier = activeGlow
                )
            }

            // 3. Counter-rotating inner containment ring
            rotate(-rotationDegrees * 1.5f, pivot = center) {
                drawContainmentSegments(
                    center = center,
                    radius = maxRadius * 0.58f,
                    segments = 12,
                    isHighEnergy = isContracting
                )
            }

            // 4. Progress Sweeping Ring
            val sweepAngle = progress * 360f
            drawArc(
                brush = Brush.sweepGradient(
                    listOf(
                        if (isContracting) StarkGold else ArcCyanPrimary,
                        if (isContracting) ArcCyanGlow else ArcCyanDark
                    )
                ),
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(center.x - maxRadius * 0.92f, center.y - maxRadius * 0.92f),
                size = Size(maxRadius * 1.84f, maxRadius * 1.84f),
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )

            // 5. Central Reactor Heart (Expands/Glows)
            val heartRadius = maxRadius * 0.38f * baseScale
            val heartColor = when {
                isContracting -> ArcCyanGlow
                isRelaxing -> ArcCyanDark
                else -> ArcCyanPrimary
            }

            // Outer heart glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        heartColor.copy(alpha = if (isContracting) 0.65f else 0.3f),
                        heartColor.copy(alpha = 0.15f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = heartRadius * 1.6f
                ),
                radius = heartRadius * 1.6f,
                center = center
            )

            // Solid heart disc
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White,
                        heartColor,
                        CarbonBackground
                    ),
                    center = center,
                    radius = heartRadius
                ),
                radius = heartRadius,
                center = center
            )

            // Central Palladium / Triangular Core Inset
            drawCentralCoreTriangle(
                center = center,
                radius = heartRadius * 0.6f,
                isContracting = isContracting
            )
        }

        // Center HUD Readout
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = phase.title,
                color = when {
                    isContracting -> StarkGold
                    isRelaxing -> ArcCyanGlow
                    phase == CalibrationPhase.COMPLETE -> TelemetryGreen
                    else -> TextHolographicCyan
                },
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )

            if (phase != CalibrationPhase.READY && phase != CalibrationPhase.COMPLETE) {
                Text(
                    text = "${secondsRemaining}s",
                    color = TextHolographicWhite,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace
                )
            }

            Text(
                text = "REP $currentRep / $totalReps",
                color = TextMuted,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

private fun DrawScope.drawReactorCoils(
    center: Offset,
    coilRadius: Float,
    coilWidth: Float,
    coilLength: Float,
    isHighEnergy: Boolean,
    glowMultiplier: Float
) {
    val coilCount = 10
    val angleStep = (2 * PI / coilCount).toFloat()

    val baseCoilColor = if (isHighEnergy) StarkGold else ArcCyanPrimary
    val glowColor = if (isHighEnergy) ArcCyanGlow else ArcCyanDark

    for (i in 0 until coilCount) {
        val angle = i * angleStep
        val x = center.x + coilRadius * cos(angle)
        val y = center.y + coilRadius * sin(angle)

        // Draw electromagnetic copper/cyan segment
        rotate(degrees = Math.toDegrees(angle.toDouble()).toFloat() + 90f, pivot = Offset(x, y)) {
            // Glow backdrop
            drawRect(
                color = glowColor.copy(alpha = 0.5f * glowMultiplier),
                topLeft = Offset(x - coilWidth / 2f - 2f, y - coilLength / 2f - 2f),
                size = Size(coilWidth + 4f, coilLength + 4f)
            )
            // Core coil
            drawRect(
                color = baseCoilColor,
                topLeft = Offset(x - coilWidth / 2f, y - coilLength / 2f),
                size = Size(coilWidth, coilLength)
            )
            // Center hot line
            drawLine(
                color = Color.White,
                start = Offset(x, y - coilLength / 2f + 2f),
                end = Offset(x, y + coilLength / 2f - 2f),
                strokeWidth = 2f
            )
        }
    }
}

private fun DrawScope.drawContainmentSegments(
    center: Offset,
    radius: Float,
    segments: Int,
    isHighEnergy: Boolean
) {
    val strokeWidth = 2.dp.toPx()
    val segmentAngle = (360f / segments)
    val color = if (isHighEnergy) ArcCyanGlow.copy(alpha = 0.8f) else ArcCyanDark.copy(alpha = 0.5f)

    for (i in 0 until segments) {
        drawArc(
            color = color,
            startAngle = i * segmentAngle,
            sweepAngle = segmentAngle * 0.65f,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2f, radius * 2f),
            style = Stroke(width = strokeWidth)
        )
    }
}

private fun DrawScope.drawCentralCoreTriangle(
    center: Offset,
    radius: Float,
    isContracting: Boolean
) {
    val path = Path()
    val p1 = Offset(center.x, center.y - radius)
    val p2 = Offset(center.x + radius * cos(PI / 6).toFloat(), center.y + radius * sin(PI / 6).toFloat())
    val p3 = Offset(center.x - radius * cos(PI / 6).toFloat(), center.y + radius * sin(PI / 6).toFloat())

    path.moveTo(p1.x, p1.y)
    path.lineTo(p2.x, p2.y)
    path.lineTo(p3.x, p3.y)
    path.close()

    val strokeColor = if (isContracting) StarkGold else ArcCyanPrimary
    drawPath(
        path = path,
        color = strokeColor,
        style = Stroke(width = 3.dp.toPx())
    )
}
