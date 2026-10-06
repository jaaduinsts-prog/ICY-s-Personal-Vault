package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.OverridePhase
import com.example.ui.Screen
import com.example.ui.StarkViewModel
import com.example.ui.components.HolographicCard
import com.example.ui.components.JarvisVoiceBanner
import com.example.ui.components.TacticalBreathingRing
import com.example.ui.theme.ArcCyanDark
import com.example.ui.theme.ArcCyanGlow
import com.example.ui.theme.ArcCyanPrimary
import com.example.ui.theme.CarbonBackground
import com.example.ui.theme.CarbonSurfaceVariant
import com.example.ui.theme.StarkGold
import com.example.ui.theme.TelemetryGreen
import com.example.ui.theme.TelemetryWarning
import com.example.ui.theme.TextHolographicCyan
import com.example.ui.theme.TextHolographicWhite
import com.example.ui.theme.TextMuted

@Composable
fun OverrideProtocolScreen(
    viewModel: StarkViewModel,
    modifier: Modifier = Modifier
) {
    val phase by viewModel.overridePhase.collectAsState()
    val freezeSeconds by viewModel.freezeSecondsLeft.collectAsState()
    val quickFlicks by viewModel.quickFlicksCount.collectAsState()
    val breathPhase by viewModel.overrideBreathPhase.collectAsState()
    val breathSecs by viewModel.overrideBreathSecondsLeft.collectAsState()
    val breathCyclesLeft by viewModel.overrideBreathCyclesRemaining.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CarbonBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top HUD Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(Screen.DASHBOARD) },
                modifier = Modifier.testTag("override_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Return to Dashboard",
                    tint = ArcCyanPrimary
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "THE OVERRIDE PROTOCOL",
                    color = TelemetryWarning,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "URGENCY SUPPRESSION ACTIVE",
                    color = TextHolographicCyan,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if (phase == OverridePhase.STABILIZED) TelemetryGreen else TelemetryWarning)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // J.A.R.V.I.S. Tactical Guidance
        val jarvisSpeech = when (phase) {
            OverridePhase.STOP_FREEZE -> "Freeze in place immediately, miss. Do not walk or run toward the facilities yet. Ceasing motion suppresses intra-abdominal pressure."
            OverridePhase.QUICK_FLICKS -> "Initiating Quick Flicks. Tap firmly to execute 5 rapid pelvic floor contractions. This fires inhibitory sacral reflexes directly to the bladder detrusor."
            OverridePhase.TACTICAL_BREATH -> "Splendid reflex activation. Now match the tactical respiratory pacer. Long exhalations will downshift your sympathetic nervous system."
            OverridePhase.STABILIZED -> "Bladder detrusor spasm successfully quelled, miss. You are in command. When you feel ready, proceed calmly."
        }

        JarvisVoiceBanner(
            speech = jarvisSpeech,
            statusText = "J.A.R.V.I.S. // EMERGENCY OVERRIDE",
            accentColor = if (phase == OverridePhase.STABILIZED) TelemetryGreen else TelemetryWarning
        )

        Spacer(modifier = Modifier.height(20.dp))

        // PHASE CONTENT
        when (phase) {
            OverridePhase.STOP_FREEZE -> {
                // Phase 1: STOP & FREEZE
                HolographicCard(
                    borderColor = TelemetryWarning.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "PHASE 1 // FREEZE IN POSITION",
                            color = TelemetryWarning,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Stand or sit still. Do NOT rush.",
                            color = TextHolographicWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                progress = { (5 - freezeSeconds) / 5f },
                                modifier = Modifier.size(120.dp),
                                color = TelemetryWarning,
                                trackColor = CarbonSurfaceVariant,
                                strokeWidth = 8.dp
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$freezeSeconds",
                                    color = TextHolographicWhite,
                                    fontSize = 38.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "SEC",
                                    color = TextMuted,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Transitioning to Quick Flicks in $freezeSeconds seconds...",
                            color = TextMuted,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            OverridePhase.QUICK_FLICKS -> {
                // Phase 2: QUICK FLICKS (5 rapid contractions)
                HolographicCard(
                    borderColor = ArcCyanPrimary.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "PHASE 2 // QUICK FLICKS (5 CONTRACTIONS)",
                            color = ArcCyanPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Squeeze & release rapidly to disrupt bladder spasm",
                            color = TextHolographicWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // 5 Arc Reactor Fuel Cells Indicator
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            for (i in 1..5) {
                                val isFilled = quickFlicks >= i
                                Box(
                                    modifier = Modifier
                                        .size(width = 46.dp, height = 24.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (isFilled) ArcCyanGlow else CarbonSurfaceVariant)
                                        .border(
                                            1.dp,
                                            if (isFilled) ArcCyanPrimary else ArcCyanDark,
                                            RoundedCornerShape(4.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$i",
                                        color = if (isFilled) Color.Black else TextMuted,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Big Interactive Tap Button
                        Button(
                            onClick = { viewModel.triggerQuickFlick() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(72.dp)
                                .testTag("quick_flick_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ArcCyanPrimary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = if (quickFlicks < 5) "SQUEEZE QUICK FLICK ($quickFlicks/5)" else "COMPLETED",
                                    color = Color.Black,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Tap firmly in sync with your pelvic squeeze.",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            OverridePhase.TACTICAL_BREATH -> {
                // Phase 3: TACTICAL DOWNSHIFT
                HolographicCard(
                    borderColor = TelemetryGreen.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "PHASE 3 // TACTICAL DOWNSHIFT",
                            color = TelemetryGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Calming Cycles Remaining: $breathCyclesLeft",
                            color = TextMuted,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        TacticalBreathingRing(
                            phase = breathPhase,
                            secondsLeft = breathSecs,
                            progress = (4 - breathSecs) / 4f,
                            sizeDp = 220.dp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = breathPhase.cue,
                            color = TextHolographicWhite,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            OverridePhase.STABILIZED -> {
                // Phase 4: STABILIZED
                HolographicCard(
                    borderColor = TelemetryGreen,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = TelemetryGreen,
                            modifier = Modifier.size(54.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "URGE REFLEX QUELLED",
                            color = TelemetryGreen,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Spasm safely suppressed. Telemetry victory logged to the Stark Vault.",
                            color = TextHolographicWhite,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { viewModel.navigateTo(Screen.DASHBOARD) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("override_complete_done_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = TelemetryGreen),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "RETURN TO FLIGHT DECK",
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }
}
