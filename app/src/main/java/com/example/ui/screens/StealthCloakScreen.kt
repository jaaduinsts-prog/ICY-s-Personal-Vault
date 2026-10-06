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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.StarkViewModel
import com.example.ui.components.HolographicCard
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
fun StealthCloakScreen(
    viewModel: StarkViewModel,
    modifier: Modifier = Modifier
) {
    var perimeterLocked by remember { mutableStateOf(true) }
    var sentryDronesActive by remember { mutableStateOf(true) }
    var livingRoomTemp by remember { mutableFloatStateOf(71f) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CarbonBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // TOP STEALTH BAR
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "STARK INDUSTRIES // SMART HOME",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "MALIBU COMPOUND GRID",
                        color = TextHolographicWhite,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Disguised quick exit back to medical flight deck
                IconButton(
                    onClick = { viewModel.toggleStealthMode() },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CarbonSurfaceVariant)
                        .border(1.dp, CarbonBorder, RoundedCornerShape(8.dp))
                        .testTag("stealth_exit_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "Disengage Stealth Cloak",
                        tint = ArcCyanPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // FAKE POWER GRID TELEMETRY
        item {
            HolographicCard(borderColor = ArcCyanPrimary.copy(alpha = 0.4f)) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Bolt, null, tint = ArcCyanPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "MICRO ARC POWER GRID",
                                color = TextHolographicWhite,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Text(
                            text = "GRID ONLINE",
                            color = TelemetryGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("CORE GENERATION", color = TextMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                            Text("142.8 kW", color = ArcCyanGlow, fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("HOUSEHOLD DRAW", color = TextMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                            Text("18.4 kW", color = StarkGold, fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("RESERVE BATTERY", color = TextMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                            Text("99.4%", color = TelemetryGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }

        // FAKE CLIMATE CONTROL
        item {
            HolographicCard(borderColor = CarbonBorder) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Thermostat, null, tint = StarkAmber, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "CLIMATE & AIR FILTRATION",
                            color = TextHolographicWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Main Living Sector", color = TextHolographicWhite, fontSize = 13.sp)
                            Text("Optimal Stark HEPA Filter: Active", color = TextMuted, fontSize = 10.sp)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { livingRoomTemp -= 1f }) {
                                Text("-", color = ArcCyanPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            }
                            Text("${livingRoomTemp.toInt()}°F", color = TextHolographicWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            IconButton(onClick = { livingRoomTemp += 1f }) {
                                Text("+", color = ArcCyanPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // FAKE PERIMETER DEFENSE
        item {
            HolographicCard(borderColor = CarbonBorder) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Shield, null, tint = TelemetryGreen, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "PERIMETER DEFENSE SYSTEMS",
                            color = TextHolographicWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Electromagnetic Gate Locks", color = TextHolographicWhite, fontSize = 12.sp)
                        Switch(
                            checked = perimeterLocked,
                            onCheckedChange = { perimeterLocked = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = TelemetryGreen)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Autonomous Sentry Drones", color = TextHolographicWhite, fontSize = 12.sp)
                        Switch(
                            checked = sentryDronesActive,
                            onCheckedChange = { sentryDronesActive = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = ArcCyanPrimary)
                        )
                    }
                }
            }
        }

        // DISCREET UNLOCK HINT
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleStealthMode() }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tap here or the eye icon above to disengage cloak",
                    color = TextMuted,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
