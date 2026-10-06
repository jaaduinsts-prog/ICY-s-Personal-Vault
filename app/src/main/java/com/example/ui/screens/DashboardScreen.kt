package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.Screen
import com.example.ui.StarkViewModel
import com.example.ui.components.HolographicCard
import com.example.ui.components.JarvisVoiceBanner
import com.example.ui.components.StarkStatTile
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
import com.example.ui.theme.TelemetryWarning
import com.example.ui.theme.TextHolographicCyan
import com.example.ui.theme.TextHolographicWhite
import com.example.ui.theme.TextMuted

@Composable
fun DashboardScreen(
    viewModel: StarkViewModel,
    modifier: Modifier = Modifier
) {
    val fluidLogs by viewModel.fluidLogs.collectAsState()
    val incidentLogs by viewModel.incidentLogs.collectAsState()
    val totalReps by viewModel.totalReps.collectAsState()
    val secondsUntilVoid by viewModel.secondsUntilNextVoid.collectAsState()

    val totalWaterMl = fluidLogs.filter { !it.isIrritant }.sumOf { it.amountMl }
    val totalIrritantMl = fluidLogs.filter { it.isIrritant }.sumOf { it.amountMl }

    // Pulsing halo for emergency override protocol button
    val infiniteTransition = rememberInfiniteTransition(label = "emergency_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val nextVoidMinutes = secondsUntilVoid / 60
    val nextVoidSecs = secondsUntilVoid % 60

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CarbonBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // TOP STARK INDUSTRIES HUD STATUS BAR
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("top_status_bar"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(TelemetryGreen)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "STARK INDUSTRIES // MARK L",
                            color = TextMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp
                        )
                    }
                    Text(
                        text = "PROTOCOL J // ONLINE",
                        color = ArcCyanPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.5.sp
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Discreet Stealth Cloak Button
                    IconButton(
                        onClick = { viewModel.toggleStealthMode() },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(CarbonSurfaceVariant)
                            .border(1.dp, ArcCyanPrimary.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                            .testTag("stealth_cloak_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.VisibilityOff,
                            contentDescription = "Stealth Disguise Mode",
                            tint = ArcCyanPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Vault Security Button
                    IconButton(
                        onClick = { viewModel.navigateTo(Screen.VAULT_SECURITY) },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(CarbonSurfaceVariant)
                            .border(1.dp, StarkGold.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                            .testTag("vault_security_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Stark Vault & Security",
                            tint = StarkGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // EMERGENCY OVERRIDE PROTOCOL (Urgency Suppressor)
        item {
            HolographicCard(
                borderColor = TelemetryWarning.copy(alpha = 0.8f),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("override_protocol_card")
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = TelemetryWarning,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "EMERGENCY PROTOCOL // SPASM ACTIVE",
                                color = TelemetryWarning,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 1.sp
                            )
                        }
                        Text(
                            text = "INSTANT TRIGGER",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Pulsing Emergency Button
                    Button(
                        onClick = { viewModel.startOverrideProtocol() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .scale(pulseScale)
                            .testTag("override_emergency_button"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFC01530)
                        ),
                        border = BorderStroke(2.dp, TelemetryWarning)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(horizontalAlignment = Alignment.Start) {
                                Text(
                                    text = "INITIATE OVERRIDE PROTOCOL",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.2.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "QUICK FLICKS + TACTICAL DOWNSHIFT",
                                    color = Color(0xFFFFD2D9),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tap immediately at the first sign of an involuntary detrusor reflex.",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // J.A.R.V.I.S. VOICE ADVISORY BANNER
        item {
            val jarvisQuote = when {
                totalIrritantMl > 300 -> "Diagnostics indicate a notable concentration of irritants in your fluid matrix today, miss. Shall we balance this with additional neutral hydration?"
                incidentLogs.isNotEmpty() -> "All telemetry anomalies have been safely quarantined in the encrypted vault. Your neural and pelvic fortitude remains exemplary."
                else -> "All systems operating at peak containment levels, miss. Ready to proceed with your scheduled core calibrations at your discretion."
            }
            JarvisVoiceBanner(speech = jarvisQuote)
        }

        // CORE TELEMETRY STATUS HUD TILES
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StarkStatTile(
                    label = "Core Stability",
                    value = "${totalReps ?: 0} REPS",
                    subValue = "Calibrations Logged",
                    color = ArcCyanPrimary,
                    icon = Icons.Default.FitnessCenter,
                    modifier = Modifier.weight(1f)
                )

                StarkStatTile(
                    label = "Maintenance",
                    value = String.format("%02d:%02d", nextVoidMinutes, nextVoidSecs),
                    subValue = "Next Timed Void",
                    color = StarkGold,
                    icon = Icons.Default.Timer,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // HYDRATION BALANCE SCANNER MINI-HUD
        item {
            HolographicCard(
                borderColor = ArcCyanDark.copy(alpha = 0.6f),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.navigateTo(Screen.SYSTEM_TELEMETRY) }
                    .testTag("hydration_mini_card")
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Opacity,
                                contentDescription = null,
                                tint = ArcCyanPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "FLUID MATRIX SCANNER",
                                color = TextHolographicWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Text(
                            text = "VIEW TELEMETRY →",
                            color = ArcCyanPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Water / Neutral: ${totalWaterMl}ml",
                            color = TelemetryGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Irritants: ${totalIrritantMl}ml",
                            color = if (totalIrritantMl > 250) TelemetryWarning else StarkAmber,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    val totalMl = (totalWaterMl + totalIrritantMl).coerceAtLeast(1)
                    val waterRatio = totalWaterMl.toFloat() / totalMl
                    LinearProgressIndicator(
                        progress = { waterRatio },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = TelemetryGreen,
                        trackColor = TelemetryWarning,
                        strokeCap = StrokeCap.Round
                    )
                }
            }
        }

        // QUICK PROTOCOL LAUNCHERS (The 5 Core Features)
        item {
            Text(
                text = "ACTIVE STARK PROTOCOLS",
                color = TextHolographicCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )
        }

        item {
            // Protocol 1: Arc Reactor Core Calibration (Kegel)
            ProtocolCard(
                title = "ARC REACTOR CORE CALIBRATION",
                subtitle = "Gamified Pelvic Floor Training",
                description = "Guided 3-to-5 second contractions with holographic Arc Reactor power surges.",
                icon = Icons.Default.Bolt,
                accentColor = ArcCyanPrimary,
                onClick = { viewModel.navigateTo(Screen.ARC_CALIBRATION) },
                testTag = "launch_arc_calibration"
            )
        }

        item {
            // Protocol 3: System Telemetry & Diagnostics
            ProtocolCard(
                title = "SYSTEM TELEMETRY & DIAGNOSTICS",
                subtitle = "Fluid Scanner & Encrypted Incident Log",
                description = "Log water vs. irritants and leaks. Holographic charts and J.A.R.V.I.S. pattern detector.",
                icon = Icons.Default.Speed,
                accentColor = StarkGold,
                onClick = { viewModel.navigateTo(Screen.SYSTEM_TELEMETRY) },
                testTag = "launch_system_telemetry"
            )
        }

        item {
            // Protocol 4: Routine Maintenance
            ProtocolCard(
                title = "ROUTINE MAINTENANCE",
                subtitle = "Timed Voiding Bladder Retraining",
                description = "Scheduled maintenance countdowns to re-educate detrusor capacity before urgency spikes.",
                icon = Icons.Default.Timer,
                accentColor = TelemetryGreen,
                onClick = { viewModel.navigateTo(Screen.ROUTINE_MAINTENANCE) },
                testTag = "launch_routine_maintenance"
            )
        }

        item {
            // Protocol 5: Neural Reset
            ProtocolCard(
                title = "NEURAL RESET HUB",
                subtitle = "Autonomic Anxiety Downregulation",
                description = "Lo-fi ambient Stark audio, tactical box breathing, and calming J.A.R.V.I.S. voice protocols.",
                icon = Icons.Default.Psychology,
                accentColor = ArcCyanGlow,
                onClick = { viewModel.navigateTo(Screen.NEURAL_RESET) },
                testTag = "launch_neural_reset"
            )
        }

        item {
            // Protocol 6: Stark Vault
            ProtocolCard(
                title = "STARK INDUSTRIES VAULT",
                subtitle = "Stealth Cloaking & Security",
                description = "Customizable stealth disguise mode, biometric gate, and local on-device encryption.",
                icon = Icons.Default.Lock,
                accentColor = StarkAmber,
                onClick = { viewModel.navigateTo(Screen.VAULT_SECURITY) },
                testTag = "launch_vault_security"
            )
        }
    }
}

@Composable
fun ProtocolCard(
    title: String,
    subtitle: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    HolographicCard(
        borderColor = accentColor.copy(alpha = 0.4f),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(accentColor.copy(alpha = 0.15f))
                    .border(1.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = TextHolographicWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = subtitle,
                    color = accentColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    color = TextMuted,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }
    }
}
