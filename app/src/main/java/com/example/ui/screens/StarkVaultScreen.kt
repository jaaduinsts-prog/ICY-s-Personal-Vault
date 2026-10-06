package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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

@Composable
fun StarkVaultScreen(
    viewModel: StarkViewModel,
    modifier: Modifier = Modifier
) {
    val stealthName by viewModel.stealthAppName.collectAsState()
    var biometricEnabled by remember { mutableStateOf(true) }
    var panicShakeTrigger by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CarbonBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
                    modifier = Modifier.testTag("vault_back")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Return to Dashboard",
                        tint = ArcCyanPrimary
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "STARK INDUSTRIES VAULT",
                        color = StarkGold,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "STEALTH PROTOCOLS & PRIVACY",
                        color = TextMuted,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(StarkGold)
                )
            }
        }

        // J.A.R.V.I.S. SECURITY STATEMENT
        item {
            JarvisVoiceBanner(
                speech = "All medical telemetry and behavioral logs reside strictly within your local device sandbox. No external servers or cloud repositories will ever access your private health data.",
                statusText = "J.A.R.V.I.S. // SECURITY VAULT",
                accentColor = StarkGold
            )
        }

        // DISGUISE IDENTITIES (3 STEALTH NAMES)
        item {
            HolographicCard(borderColor = StarkGold.copy(alpha = 0.5f)) {
                Column {
                    Text(
                        text = "STEALTH DISGUISE IDENTITY",
                        color = StarkGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Select how the application appears in multitasking and launcher contexts:",
                        color = TextMuted,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        StealthNameOption(
                            name = "Protocol J",
                            description = "Discreet AI assistant identity. Minimalist and non-descript.",
                            isSelected = stealthName == "Protocol J",
                            onClick = { viewModel.setStealthAppName("Protocol J") }
                        )

                        StealthNameOption(
                            name = "Stark Home",
                            description = "Disguised as a smart home & energy automation utility.",
                            isSelected = stealthName == "Stark Home",
                            onClick = { viewModel.setStealthAppName("Stark Home") }
                        )

                        StealthNameOption(
                            name = "Arc Diagnostics",
                            description = "Disguised as an engineering sensor & battery diagnostic tool.",
                            isSelected = stealthName == "Arc Diagnostics",
                            onClick = { viewModel.setStealthAppName("Arc Diagnostics") }
                        )
                    }
                }
            }
        }

        // BIOMETRIC & PANIC TRIGGER SETTINGS
        item {
            HolographicCard(borderColor = CarbonBorder) {
                Column {
                    Text(
                        text = "ACCESS CONTROLS",
                        color = TextHolographicWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Fingerprint, null, tint = ArcCyanPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Biometric Lock (FaceID / Fingerprint)", color = TextHolographicWhite, fontSize = 12.sp)
                                Text("Require biometric authorization on launch", color = TextMuted, fontSize = 10.sp)
                            }
                        }
                        Switch(
                            checked = biometricEnabled,
                            onCheckedChange = { biometricEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = ArcCyanPrimary)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.VisibilityOff, null, tint = StarkAmber, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Emergency Stealth Cloak", color = TextHolographicWhite, fontSize = 12.sp)
                                Text("Quick tap on flight deck swaps to fake smart home", color = TextMuted, fontSize = 10.sp)
                            }
                        }
                        Switch(
                            checked = panicShakeTrigger,
                            onCheckedChange = { panicShakeTrigger = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = StarkAmber)
                        )
                    }
                }
            }
        }

        // STANDALONE APK DISPATCH & SHARE SECTION
        item {
            val context = androidx.compose.ui.platform.LocalContext.current
            HolographicCard(borderColor = ArcCyanPrimary.copy(alpha = 0.6f)) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = ArcCyanPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "STANDALONE APK DISPATCH",
                            color = ArcCyanPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Packages the application binary internally and dispatches the standalone .APK package directly via Android Share Sheet (Bluetooth, Quick Share, Drive, Messaging, or Direct File Transfer).",
                        color = TextHolographicWhite,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            com.example.util.ApkShareHelper.shareAppApk(context)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ArcCyanPrimary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "PACKAGE & DISPATCH STARK APK",
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }

        // LOCAL ZERO-CLOUD DATA ASSURANCE
        item {
            HolographicCard(borderColor = TelemetryGreen.copy(alpha = 0.4f)) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Shield, null, tint = TelemetryGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AIR-GAPPED TELEMETRY ARCHITECTURE",
                            color = TelemetryGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "100% on-device SQLite Room database. No analytics trackers, no telemetry export, no third-party SDKs. Your medical dignity is defended with Stark-grade encryption.",
                        color = TextHolographicWhite,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun StealthNameOption(
    name: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) StarkGold.copy(alpha = 0.15f) else CarbonSurfaceVariant)
            .border(1.dp, if (isSelected) StarkGold else CarbonBorder, RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    color = if (isSelected) StarkGold else TextHolographicWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = description,
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = StarkGold,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
