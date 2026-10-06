package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.ui.Screen
import com.example.ui.StarkViewModel
import com.example.ui.components.ArcReactorVisualizer
import com.example.ui.components.CalibrationPhase
import com.example.ui.components.HolographicCard
import com.example.ui.components.JarvisVoiceBanner
import com.example.ui.theme.ArcCyanDark
import com.example.ui.theme.ArcCyanGlow
import com.example.ui.theme.ArcCyanPrimary
import com.example.ui.theme.CarbonBackground
import com.example.ui.theme.CarbonSurfaceVariant
import com.example.ui.theme.StarkAmber
import com.example.ui.theme.StarkGold
import com.example.ui.theme.TelemetryGreen
import com.example.ui.theme.TextHolographicCyan
import com.example.ui.theme.TextHolographicWhite
import com.example.ui.theme.TextMuted

@Composable
fun ArcCalibrationScreen(
    viewModel: StarkViewModel,
    modifier: Modifier = Modifier
) {
    val phase by viewModel.calibrationPhase.collectAsState()
    val secondsLeft by viewModel.calibSecondsLeft.collectAsState()
    val progress by viewModel.calibPhaseProgress.collectAsState()
    val currentRep by viewModel.calibCurrentRep.collectAsState()
    val totalReps by viewModel.calibTotalReps.collectAsState()
    val holdTarget by viewModel.calibHoldSecondsTarget.collectAsState()

    val isRunning = phase != CalibrationPhase.READY && phase != CalibrationPhase.COMPLETE

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CarbonBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TOP HUD BAR
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    viewModel.stopCalibrationSession()
                    viewModel.navigateTo(Screen.DASHBOARD)
                },
                modifier = Modifier.testTag("arc_calibration_back")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Return to Dashboard",
                    tint = ArcCyanPrimary
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "CORE CALIBRATION",
                    color = ArcCyanPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "PELVIC FLOOR BIO-FEEDBACK",
                    color = TextMuted,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            Text(
                text = "$holdTarget SEC HOLD",
                color = StarkGold,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // J.A.R.V.I.S. ADVISORY
        val speech = when (phase) {
            CalibrationPhase.READY -> "Ready to calibrate core containment, miss. Select your protocol duration and engage when prepared."
            CalibrationPhase.CONTRACT -> "Contract and lift the pelvic base. Imagine raising a micro Arc Reactor internally."
            CalibrationPhase.HOLD -> "Sustain maximum power output. Maintain steady breathing — do not hold your breath."
            CalibrationPhase.RELAX -> "Complete discharge. Allow the pelvic musculature to fully decompress before the next sequence."
            CalibrationPhase.COMPLETE -> "Exemplary session, miss! Pelvic stability index increased. Telemetry logged to Stark archives."
        }
        JarvisVoiceBanner(speech = speech, statusText = "J.A.R.V.I.S. // CALIBRATION COACH")

        Spacer(modifier = Modifier.height(16.dp))

        // PROTOCOL DIFFICULTY SELECTOR CHIPS
        if (!isRunning) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalibrationPresetChip(
                    title = "Recruit (3s)",
                    seconds = 3,
                    isSelected = holdTarget == 3,
                    onClick = { viewModel.setCalibrationHoldDuration(3) },
                    modifier = Modifier.weight(1f)
                )
                CalibrationPresetChip(
                    title = "Avenger (5s)",
                    seconds = 5,
                    isSelected = holdTarget == 5,
                    onClick = { viewModel.setCalibrationHoldDuration(5) },
                    modifier = Modifier.weight(1f)
                )
                CalibrationPresetChip(
                    title = "Stark (8s)",
                    seconds = 8,
                    isSelected = holdTarget == 8,
                    onClick = { viewModel.setCalibrationHoldDuration(8) },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // ARC REACTOR VISUALIZER (Centerpiece Canvas)
        ArcReactorVisualizer(
            phase = phase,
            progress = progress,
            secondsRemaining = secondsLeft,
            currentRep = currentRep,
            totalReps = totalReps,
            sizeDp = 270.dp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // CLINICAL INSTRUCTION CARD
        HolographicCard(
            borderColor = ArcCyanDark.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = phase.instruction,
                    color = when (phase) {
                        CalibrationPhase.CONTRACT, CalibrationPhase.HOLD -> StarkGold
                        CalibrationPhase.RELAX -> ArcCyanGlow
                        CalibrationPhase.COMPLETE -> TelemetryGreen
                        else -> TextHolographicCyan
                    },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 0.8.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = phase.subtitle,
                    color = TextHolographicWhite,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // CONTROLS (START / ABORT)
        if (!isRunning) {
            Button(
                onClick = { viewModel.startCalibrationSession() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("start_calibration_button"),
                colors = ButtonDefaults.buttonColors(containerColor = ArcCyanPrimary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ENGAGE CALIBRATION (10 REPS)",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                }
            }
        } else {
            Button(
                onClick = { viewModel.stopCalibrationSession() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("stop_calibration_button"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B1E28)),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Color(0xFFFF5252))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Stop,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ABORT SEQUENCE",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}

@Composable
private fun CalibrationPresetChip(
    title: String,
    seconds: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) ArcCyanPrimary.copy(alpha = 0.2f) else CarbonSurfaceVariant)
            .border(
                1.dp,
                if (isSelected) ArcCyanPrimary else CarbonBorder,
                RoundedCornerShape(6.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = if (isSelected) ArcCyanPrimary else TextMuted,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontFamily = FontFamily.Monospace
        )
    }
}
