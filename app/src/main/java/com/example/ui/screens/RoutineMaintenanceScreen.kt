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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
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
import com.example.ui.Screen
import com.example.ui.StarkViewModel
import com.example.ui.components.HolographicCard
import com.example.ui.components.JarvisVoiceBanner
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RoutineMaintenanceScreen(
    viewModel: StarkViewModel,
    modifier: Modifier = Modifier
) {
    val intervalMinutes by viewModel.voidingIntervalMinutes.collectAsState()
    val secondsLeft by viewModel.secondsUntilNextVoid.collectAsState()
    val voidingLogs by viewModel.voidingLogs.collectAsState()

    val hours = secondsLeft / 3600
    val minutes = (secondsLeft % 3600) / 60
    val seconds = secondsLeft % 60

    val totalIntervalSeconds = intervalMinutes * 60
    val progress = if (totalIntervalSeconds > 0) {
        (totalIntervalSeconds - secondsLeft).toFloat() / totalIntervalSeconds
    } else 0f

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
                    onClick = { viewModel.navigateTo(Screen.DASHBOARD) },
                    modifier = Modifier.testTag("maintenance_back")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Return to Dashboard",
                        tint = ArcCyanPrimary
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "ROUTINE MAINTENANCE",
                        color = TelemetryGreen,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "TIMED VOIDING & BLADDER RETRAINING",
                        color = TextMuted,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(TelemetryGreen)
                )
            }
        }

        // J.A.R.V.I.S. ADVISORY
        item {
            JarvisVoiceBanner(
                speech = "Pardon the interruption, miss, but optimal system protocols suggest a brief maintenance break before sensory thresholds trigger panic. Bladder retraining re-establishes conscious cortical control.",
                statusText = "J.A.R.V.I.S. // SCHEDULE CONTROLLER",
                accentColor = TelemetryGreen
            )
        }

        // COUNTDOWN TIMER DISPLAY
        item {
            HolographicCard(
                borderColor = TelemetryGreen.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "NEXT SCHEDULED PROTOCOL WINDOW",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.size(170.dp),
                            color = TelemetryGreen,
                            trackColor = CarbonSurfaceVariant,
                            strokeWidth = 10.dp
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = String.format("%02d:%02d:%02d", hours, minutes, seconds),
                                color = TextHolographicWhite,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "UNTIL MAINTENANCE",
                                color = TelemetryGreen,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Current Interval: Every $intervalMinutes minutes",
                        color = TextHolographicCyan,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // INTERVAL PRESET BUTTONS
        item {
            HolographicCard(borderColor = CarbonBorder) {
                Column {
                    Text(
                        text = "PROGRESSIVE CAPACITY INTERVAL:",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(60, 90, 120, 150).forEach { mins ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (intervalMinutes == mins) TelemetryGreen else CarbonSurfaceVariant)
                                    .border(1.dp, if (intervalMinutes == mins) TelemetryGreen else CarbonBorder, RoundedCornerShape(6.dp))
                                    .clickable { viewModel.setVoidingInterval(mins) }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${mins}m",
                                    color = if (intervalMinutes == mins) Color.Black else TextHolographicWhite,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Gradually expand your interval by 15-30 minutes every 2-3 weeks as stability improves.",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }
            }
        }

        // PRIMARY ACTION BUTTON: COMPLETE MAINTENANCE VISIT
        item {
            Button(
                onClick = { viewModel.logVoidingVisit(wasScheduled = true, urgeLevel = 2) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("log_void_button"),
                colors = ButtonDefaults.buttonColors(containerColor = TelemetryGreen),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "CONDUCT SCHEDULED MAINTENANCE NOW",
                        color = Color.Black,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // MAINTENANCE LOG HISTORY
        item {
            Text(
                text = "LOGGED MAINTENANCE EVENTS",
                color = TextHolographicCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }

        items(voidingLogs) { log ->
            val dateStr = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(log.timestamp))
            HolographicCard(borderColor = CarbonBorder) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = dateStr,
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = if (log.wasScheduled) "Scheduled Maintenance (On-Time)" else "Interim Adjustment",
                            color = if (log.wasScheduled) TelemetryGreen else StarkGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Urge: ${log.urgeLevel}/5",
                        color = TextHolographicWhite,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}
