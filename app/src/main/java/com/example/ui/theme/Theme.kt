package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val StarkColorScheme = darkColorScheme(
  primary = ArcCyanPrimary,
  onPrimary = Color.Black,
  primaryContainer = ArcCyanDim,
  onPrimaryContainer = ArcCyanGlow,
  secondary = StarkGold,
  onSecondary = Color.Black,
  secondaryContainer = Color(0xFF4A3800),
  onSecondaryContainer = StarkAmber,
  tertiary = TelemetryGreen,
  onTertiary = Color.Black,
  background = CarbonBackground,
  onBackground = TextHolographicWhite,
  surface = CarbonSurface,
  onSurface = TextHolographicWhite,
  surfaceVariant = CarbonSurfaceVariant,
  onSurfaceVariant = TextHolographicCyan,
  outline = CarbonBorder,
  error = TelemetryWarning,
  onError = Color.White
)

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = StarkColorScheme,
    typography = Typography,
    content = content
  )
}

