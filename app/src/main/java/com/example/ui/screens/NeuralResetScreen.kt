package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.components.HolographicCard
import com.example.ui.components.JarvisVoiceBanner
import com.example.ui.components.TacticalBreathingRing
import com.example.ui.theme.ArcCyanDark
import com.example.ui.theme.ArcCyanGlow
import com.example.ui.theme.ArcCyanPrimary
import com.example.ui.theme.CarbonBackground
import com.example.ui.theme.CarbonBorder
import com.example.ui.theme.CarbonSurfaceVariant
import com.example.ui.theme.StarkAmber
import com.example.ui.theme.StarkGold
import com.example.ui.theme.TelemetryGreen
import com.example.ui.theme.TextHolographicCyan
import com.example.ui.theme.TextHolographicWhite
import com.example.ui.theme.TextMuted

@Composable
fun NeuralResetScreen(
    viewModel: StarkViewModel,
    modifier: Modifier = Modifier
) {
    val phase by viewModel.neuralResetPhase.collectAsState()
    val secondsLeft by viewModel.neuralSecondsLeft.collectAsState()
    val isAmbientActive by viewModel.isAmbientSoundActive.collectAsState()
    val selectedSound by viewModel.selectedSoundscape.collectAsState()

    var isBreathingActive by remember { mutableStateOf(false) }

    // Equalizer wave animation
    val infiniteTransition = rememberInfiniteTransition(label = "audio_eq")
    val barHeight1 by infiniteTransition.animateFloat(
        initialValue = 6f, targetValue = 24f,
        animationSpec = infiniteRepeatable(tween(400, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "b1"
    )
    val barHeight2 by infiniteTransition.animateFloat(
        initialValue = 18f, targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(550, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "b2"
    )
    val barHeight3 by infiniteTransition.animateFloat(
        initialValue = 10f, targetValue = 28f,
        animationSpec = infiniteRepeatable(tween(350, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "b3"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CarbonBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TOP HUD HEADER
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        viewModel.stopNeuralBreathing()
                        viewModel.navigateTo(Screen.DASHBOARD)
                    },
                    modifier = Modifier.testTag("neural_back")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Return to Dashboard",
                        tint = ArcCyanPrimary
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "NEURAL RESET HUB",
                        color = ArcCyanGlow,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "AUTONOMIC NERVOUS DOWNREGULATION",
                        color = TextMuted,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(ArcCyanGlow)
                )
            }
        }

        // J.A.R.V.I.S. ADVISORY
        item {
            JarvisVoiceBanner(
                speech = "Downshifting neural telemetry, miss. Anxiety floods the bladder wall with sympathetic fight-or-flight sensations. Through tactical breath pacing and auditory resonance, we disengage the stress reflex.",
                statusText = "J.A.R.V.I.S. // NEURAL RESET PROTOCOL",
                accentColor = ArcCyanGlow
            )
        }

        // TACTICAL BOX BREATHING PACER
        item {
            HolographicCard(
                borderColor = ArcCyanPrimary.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "TACTICAL 4-4-4-4 BOX BREATHING",
                        color = TextHolographicWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    TacticalBreathingRing(
                        phase = phase,
                        secondsLeft = secondsLeft,
                        progress = (4 - secondsLeft) / 4f,
                        sizeDp = 220.dp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = phase.cue,
                        color = ArcCyanGlow,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (isBreathingActive) {
                                viewModel.stopNeuralBreathing()
                                isBreathingActive = false
                            } else {
                                viewModel.startNeuralBoxBreathing()
                                isBreathingActive = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isBreathingActive) Color(0xFF8B1E28) else ArcCyanPrimary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("toggle_breathing_button")
                    ) {
                        Text(
                            text = if (isBreathingActive) "PAUSE PACER" else "ENGAGE BOX BREATHING PACER",
                            color = if (isBreathingActive) Color.White else Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // STARK AMBIENT SOUNDSCAPE GENERATOR
        item {
            HolographicCard(
                borderColor = StarkGold.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.VolumeUp, null, tint = StarkGold, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "STARK LAB AUDITORY RESONANCE",
                                color = StarkGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        if (isAmbientActive) {
                            // Animated equalizer bars
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(3.dp),
                                verticalAlignment = Alignment.Bottom,
                                modifier = Modifier.height(24.dp)
                            ) {
                                Box(modifier = Modifier.width(3.dp).height(barHeight1.dp).background(StarkGold))
                                Box(modifier = Modifier.width(3.dp).height(barHeight2.dp).background(ArcCyanPrimary))
                                Box(modifier = Modifier.width(3.dp).height(barHeight3.dp).background(StarkGold))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        SoundscapeChip(
                            title = "Arc Reactor Core Low Hum (432Hz)",
                            subtitle = "Deep alpha frequency calming drone",
                            isSelected = selectedSound == "ARC_REACTOR_HUM",
                            onClick = { viewModel.setSoundscape("ARC_REACTOR_HUM") }
                        )
                        SoundscapeChip(
                            title = "Malibu Coastal Ocean Swell",
                            subtitle = "Rhythmic nature wave downshifting",
                            isSelected = selectedSound == "MALIBU_OCEAN",
                            onClick = { viewModel.setSoundscape("MALIBU_OCEAN") }
                        )
                        SoundscapeChip(
                            title = "Stark Tower Binaural Alpha Waves",
                            subtitle = "Promotes cognitive focus and anxiety suppression",
                            isSelected = selectedSound == "BINAURAL_ALPHA",
                            onClick = { viewModel.setSoundscape("BINAURAL_ALPHA") }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.toggleAmbientAudio() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isAmbientActive) StarkAmber else CarbonSurfaceVariant
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StarkGold),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("toggle_ambient_sound_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isAmbientActive) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = if (isAmbientActive) Color.Black else StarkGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAmbientActive) "CEASE AMBIENT SOUNDSCAPE" else "ACTIVATE SOUND RESONANCE",
                                color = if (isAmbientActive) Color.Black else StarkGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }

        // COGNITIVE REFRAMING SCRIPT CARD
        item {
            HolographicCard(borderColor = CarbonBorder) {
                Column {
                    Text(
                        text = "J.A.R.V.I.S. COGNITIVE REFRAMING SCRIPT",
                        color = TextHolographicCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "“Urges are muscular contractions that crest like ocean waves and diminish within 60-90 seconds. You do not need to fight the wave; you merely balance upon it. Your cortical center commands the bladder, not the other way around.”",
                        color = TextHolographicWhite,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SoundscapeChip(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) StarkGold.copy(alpha = 0.2f) else CarbonSurfaceVariant)
            .border(1.dp, if (isSelected) StarkGold else CarbonBorder, RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Column {
            Text(
                text = title,
                color = if (isSelected) StarkGold else TextHolographicWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = subtitle,
                color = TextMuted,
                fontSize = 10.sp
            )
        }
    }
}
