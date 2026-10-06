package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.ui.theme.CarbonSurface
import com.example.ui.theme.CarbonSurfaceVariant
import com.example.ui.theme.StarkAmber
import com.example.ui.theme.StarkGold
import com.example.ui.theme.TelemetryGreen
import com.example.ui.theme.TextHolographicCyan
import com.example.ui.theme.TextHolographicWhite
import com.example.ui.theme.TextMuted

@Composable
fun HolographicCard(
    modifier: Modifier = Modifier,
    borderColor: Color = ArcCyanPrimary.copy(alpha = 0.4f),
    cornerCut: Dp = 10.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        CarbonSurface.copy(alpha = 0.95f),
                        CarbonSurfaceVariant.copy(alpha = 0.92f)
                    )
                )
            )
            .border(
                BorderStroke(1.dp, borderColor),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        // Holographic corner bracket highlights
        Canvas(modifier = Modifier.fillMaxSize()) {
            val bracketLen = 14.dp.toPx()
            val bracketColor = borderColor.copy(alpha = 0.85f)
            val strokeWidth = 2.dp.toPx()

            // Top Left
            drawLine(bracketColor, Offset(0f, 0f), Offset(bracketLen, 0f), strokeWidth)
            drawLine(bracketColor, Offset(0f, 0f), Offset(0f, bracketLen), strokeWidth)

            // Top Right
            drawLine(bracketColor, Offset(size.width, 0f), Offset(size.width - bracketLen, 0f), strokeWidth)
            drawLine(bracketColor, Offset(size.width, 0f), Offset(size.width, bracketLen), strokeWidth)

            // Bottom Left
            drawLine(bracketColor, Offset(0f, size.height), Offset(bracketLen, size.height), strokeWidth)
            drawLine(bracketColor, Offset(0f, size.height), Offset(0f, size.height - bracketLen), strokeWidth)

            // Bottom Right
            drawLine(bracketColor, Offset(size.width, size.height), Offset(size.width - bracketLen, size.height), strokeWidth)
            drawLine(bracketColor, Offset(size.width, size.height), Offset(size.width, size.height - bracketLen), strokeWidth)
        }

        Box(modifier = Modifier.padding(14.dp)) {
            content()
        }
    }
}

@Composable
fun JarvisVoiceBanner(
    speech: String,
    modifier: Modifier = Modifier,
    statusText: String = "J.A.R.V.I.S. // TACTICAL ADVISORY",
    accentColor: Color = ArcCyanPrimary
) {
    val infiniteTransition = rememberInfiniteTransition(label = "jarvis_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("jarvis_banner"),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = CarbonSurfaceVariant.copy(alpha = 0.95f)),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Animated AI waveform indicator
                    Canvas(modifier = Modifier.size(16.dp)) {
                        drawCircle(
                            color = accentColor.copy(alpha = pulseAlpha),
                            radius = size.minDimension / 2f
                        )
                        drawCircle(
                            color = Color.White,
                            radius = size.minDimension / 4f
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = statusText,
                        color = accentColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Text(
                    text = "V9.4.2",
                    color = TextMuted,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "\"$speech\"",
                color = TextHolographicWhite,
                fontSize = 13.5.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun StarkStatTile(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    subValue: String? = null,
    color: Color = ArcCyanPrimary,
    icon: ImageVector? = null
) {
    HolographicCard(modifier = modifier, borderColor = color.copy(alpha = 0.35f)) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = label.uppercase(),
                    color = TextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 0.8.sp
                )
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                color = TextHolographicWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )

            if (subValue != null) {
                Text(
                    text = subValue,
                    color = color,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
