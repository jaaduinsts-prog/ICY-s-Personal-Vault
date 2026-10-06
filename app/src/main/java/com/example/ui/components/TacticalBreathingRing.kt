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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ArcCyanDark
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

enum class BreathPhase(val title: String, val cue: String) {
    INHALE("INHALE", "Expand diaphragm slowly through nose"),
    HOLD("HOLD & STEADY", "Quiet detrusor reflex. Stay relaxed."),
    EXHALE("EXHALE", "Slow pursed-lip release. Melt shoulder tension."),
    REST("REST", "Maintain autonomic stillness.")
}

@Composable
fun TacticalBreathingRing(
    phase: BreathPhase,
    secondsLeft: Int,
    progress: Float, // 0 to 1 in phase
    modifier: Modifier = Modifier,
    sizeDp: Dp = 240.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_halo")
    val haloPulse by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "halo"
    )

    // Diameter ratio based on breathing phase
    val expansionFraction = when (phase) {
        BreathPhase.INHALE -> 0.45f + (0.50f * progress)
        BreathPhase.HOLD -> 0.95f
        BreathPhase.EXHALE -> 0.95f - (0.50f * progress)
        BreathPhase.REST -> 0.45f
    }

    Box(
        modifier = modifier
            .size(sizeDp)
            .testTag("tactical_breathing_ring"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val maxRadius = size.minDimension / 2f - 12.dp.toPx()

            // Outer target reticle
            drawCircle(
                color = CarbonBorder,
                radius = maxRadius,
                center = center,
                style = Stroke(width = 2.dp.toPx())
            )

            // Tick marks around perimeter
            for (i in 0 until 24) {
                val angle = Math.toRadians((i * 15).toDouble())
                val tickLength = if (i % 6 == 0) 10.dp.toPx() else 5.dp.toPx()
                val start = Offset(
                    (center.x + (maxRadius - tickLength) * kotlin.math.cos(angle)).toFloat(),
                    (center.y + (maxRadius - tickLength) * kotlin.math.sin(angle)).toFloat()
                )
                val end = Offset(
                    (center.x + maxRadius * kotlin.math.cos(angle)).toFloat(),
                    (center.y + maxRadius * kotlin.math.sin(angle)).toFloat()
                )
                drawLine(
                    color = if (i % 6 == 0) ArcCyanPrimary else CarbonBorder,
                    start = start,
                    end = end,
                    strokeWidth = 2.dp.toPx()
                )
            }

            // Expanding/contracting diaphragm sphere
            val currentRadius = maxRadius * expansionFraction
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        ArcCyanPrimary.copy(alpha = 0.45f),
                        ArcCyanDark.copy(alpha = 0.15f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = currentRadius * haloPulse
                ),
                radius = currentRadius * haloPulse,
                center = center
            )

            // Glowing ring edge
            drawCircle(
                color = when (phase) {
                    BreathPhase.INHALE -> ArcCyanGlow
                    BreathPhase.HOLD -> StarkGold
                    BreathPhase.EXHALE -> TelemetryGreen
                    BreathPhase.REST -> ArcCyanDark
                },
                radius = currentRadius,
                center = center,
                style = Stroke(width = 4.dp.toPx())
            )
        }

        // Center HUD
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = phase.title,
                color = when (phase) {
                    BreathPhase.HOLD -> StarkGold
                    BreathPhase.EXHALE -> TelemetryGreen
                    else -> ArcCyanGlow
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.2.sp
            )

            Text(
                text = "${secondsLeft}s",
                color = TextHolographicWhite,
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
            )

            Text(
                text = "TACTICAL PACER",
                color = TextMuted,
                fontSize = 9.sp,
                letterSpacing = 1.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
