package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FluidType
import com.example.data.model.LeakSeverity
import com.example.data.model.UrgeTrigger
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
import com.example.ui.theme.TelemetryWarning
import com.example.ui.theme.TextHolographicCyan
import com.example.ui.theme.TextHolographicWhite
import com.example.ui.theme.TextMuted
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SystemTelemetryScreen(
    viewModel: StarkViewModel,
    modifier: Modifier = Modifier
) {
    val fluidLogs by viewModel.fluidLogs.collectAsState()
    val incidentLogs by viewModel.incidentLogs.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Fluid, 1: Incidents, 2: Pattern AI

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CarbonBackground)
            .padding(16.dp)
    ) {
        // TOP HUD BAR
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(Screen.DASHBOARD) },
                modifier = Modifier.testTag("telemetry_back")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Return to Dashboard",
                    tint = ArcCyanPrimary
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "SYSTEM TELEMETRY",
                    color = ArcCyanPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.2.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = StarkGold,
                        modifier = Modifier.size(10.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "ENCRYPTED 256-BIT VAULT",
                        color = StarkGold,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(TelemetryGreen)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // TAB ROW
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = CarbonSurfaceVariant,
            contentColor = ArcCyanPrimary
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        "FLUID SCANNER",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        "INCIDENT LOG",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = {
                    Text(
                        "DIAGNOSTIC AI",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTab) {
            0 -> FluidScannerTab(viewModel = viewModel, fluidLogs = fluidLogs)
            1 -> IncidentLogTab(viewModel = viewModel, incidentLogs = incidentLogs)
            2 -> DiagnosticPatternTab(fluidLogs = fluidLogs, incidentLogs = incidentLogs)
        }
    }
}

@Composable
private fun FluidScannerTab(
    viewModel: StarkViewModel,
    fluidLogs: List<com.example.data.model.FluidLog>
) {
    var selectedType by remember { mutableStateOf(FluidType.WATER) }
    var selectedAmount by remember { mutableIntStateOf(250) }

    val totalWater = fluidLogs.filter { !it.isIrritant }.sumOf { it.amountMl }
    val totalIrritant = fluidLogs.filter { it.isIrritant }.sumOf { it.amountMl }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            HolographicCard(borderColor = ArcCyanDark) {
                Column {
                    Text(
                        text = "NEW FLUID INTAKE ENTRY",
                        color = TextHolographicWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "SELECT FLUID COMPOUND:",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Fluid Types Selector
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FluidChip(
                                title = "Water (Hydration)",
                                isIrritant = false,
                                isSelected = selectedType == FluidType.WATER,
                                onClick = { selectedType = FluidType.WATER },
                                modifier = Modifier.weight(1f)
                            )
                            FluidChip(
                                title = "Coffee (Caffeine)",
                                isIrritant = true,
                                isSelected = selectedType == FluidType.CAFFEINE,
                                onClick = { selectedType = FluidType.CAFFEINE },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FluidChip(
                                title = "Citrus Juice",
                                isIrritant = true,
                                isSelected = selectedType == FluidType.CITRUS_JUICE,
                                onClick = { selectedType = FluidType.CITRUS_JUICE },
                                modifier = Modifier.weight(1f)
                            )
                            FluidChip(
                                title = "Carbonated Soda",
                                isIrritant = true,
                                isSelected = selectedType == FluidType.CARBONATED,
                                onClick = { selectedType = FluidType.CARBONATED },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FluidChip(
                                title = "Herbal Decaf Tea",
                                isIrritant = false,
                                isSelected = selectedType == FluidType.HERBAL_TEA,
                                onClick = { selectedType = FluidType.HERBAL_TEA },
                                modifier = Modifier.weight(1f)
                            )
                            FluidChip(
                                title = "Alcoholic Drink",
                                isIrritant = true,
                                isSelected = selectedType == FluidType.ALCOHOL,
                                onClick = { selectedType = FluidType.ALCOHOL },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Amount selector
                    Text(
                        text = "VOLUME INTAKE (ML):",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(150, 250, 350, 500).forEach { ml ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (selectedAmount == ml) ArcCyanPrimary else CarbonSurfaceVariant)
                                    .clickable { selectedAmount = ml }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${ml}ml",
                                    color = if (selectedAmount == ml) Color.Black else TextHolographicWhite,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            viewModel.logFluidIntake(selectedAmount, selectedType)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("log_fluid_commit_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = ArcCyanPrimary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Add, null, tint = Color.Black, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "LOG ${selectedAmount}ML INTO TELEMETRY",
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

        // Cumulative totals
        item {
            HolographicCard(borderColor = CarbonBorder) {
                Column {
                    Text(
                        text = "DAILY HYDRATION BALANCE",
                        color = TextHolographicWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Hydrating Fluid: ${totalWater}ml", color = TelemetryGreen, fontSize = 12.sp)
                        Text("Bladder Irritants: ${totalIrritant}ml", color = TelemetryWarning, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    val sum = (totalWater + totalIrritant).coerceAtLeast(1)
                    LinearProgressIndicator(
                        progress = { totalWater.toFloat() / sum },
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

        // Recent Entries List
        item {
            Text(
                text = "RECENT INTAKE LOGS",
                color = TextHolographicCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }

        items(fluidLogs) { log ->
            val dateStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(log.timestamp))
            HolographicCard(borderColor = if (log.isIrritant) TelemetryWarning.copy(alpha = 0.4f) else TelemetryGreen.copy(alpha = 0.4f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = dateStr,
                                color = TextMuted,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = log.fluidType.replace("_", " "),
                                color = if (log.isIrritant) TelemetryWarning else TelemetryGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Text(
                            text = "${log.amountMl} ml ${if (log.isIrritant) "(Irritant)" else "(Optimal)"}",
                            color = TextHolographicWhite,
                            fontSize = 11.sp
                        )
                    }

                    IconButton(onClick = { viewModel.deleteFluidLog(log.id) }) {
                        Icon(Icons.Default.Delete, "Delete", tint = TextMuted, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun FluidChip(
    title: String,
    isIrritant: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(
                if (isSelected) (if (isIrritant) TelemetryWarning.copy(alpha = 0.25f) else TelemetryGreen.copy(alpha = 0.25f))
                else CarbonSurfaceVariant
            )
            .border(
                1.dp,
                if (isSelected) (if (isIrritant) TelemetryWarning else TelemetryGreen) else CarbonBorder,
                RoundedCornerShape(6.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = if (isSelected) (if (isIrritant) TelemetryWarning else TelemetryGreen) else TextHolographicWhite,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
private fun IncidentLogTab(
    viewModel: StarkViewModel,
    incidentLogs: List<com.example.data.model.IncidentLog>
) {
    var selectedSeverity by remember { mutableStateOf(LeakSeverity.NONE_SUPPRESSED) }
    var selectedTrigger by remember { mutableStateOf(UrgeTrigger.KEY_IN_DOOR) }
    var urgeScore by remember { mutableIntStateOf(3) }
    var padChanged by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            HolographicCard(borderColor = StarkGold.copy(alpha = 0.5f)) {
                Column {
                    Text(
                        text = "LOG TELEMETRY INCIDENT / SPASM",
                        color = StarkGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("TRIGGER CATEGORY:", color = TextMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(6.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        UrgeTrigger.values().take(4).forEach { trigger ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (selectedTrigger == trigger) ArcCyanPrimary.copy(alpha = 0.2f) else CarbonSurfaceVariant)
                                    .clickable { selectedTrigger = trigger }
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = trigger.displayName,
                                    color = if (selectedTrigger == trigger) ArcCyanPrimary else TextHolographicWhite,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("SEVERITY INDEX:", color = TextMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(6.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        LeakSeverity.values().forEach { sev ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (selectedSeverity == sev) StarkGold.copy(alpha = 0.2f) else CarbonSurfaceVariant)
                                    .clickable { selectedSeverity = sev }
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = sev.displayName,
                                    color = if (selectedSeverity == sev) StarkGold else TextHolographicWhite,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Urge Intensity scale 1-5
                    Text("URGE INTENSITY (1-5):", color = TextMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        for (i in 1..5) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (urgeScore == i) ArcCyanPrimary else CarbonSurfaceVariant)
                                    .clickable { urgeScore = i }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$i",
                                    color = if (urgeScore == i) Color.Black else TextHolographicWhite,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { padChanged = !padChanged }
                    ) {
                        Checkbox(
                            checked = padChanged,
                            onCheckedChange = { padChanged = it },
                            colors = CheckboxDefaults.colors(checkedColor = StarkGold)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Liner / Protection was replaced",
                            color = TextHolographicWhite,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            viewModel.logIncidentReport(
                                severity = selectedSeverity,
                                trigger = selectedTrigger,
                                urgeLevel = urgeScore,
                                padChanged = padChanged
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("log_incident_commit_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = StarkGold),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "ENCRYPT & COMMIT REPORT",
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // Recent Incidents
        item {
            Text(
                text = "LOGGED INCIDENT TELEMETRY",
                color = TextHolographicCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }

        items(incidentLogs) { incident ->
            val dateStr = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(incident.timestamp))
            HolographicCard(borderColor = if (incident.severity == LeakSeverity.NONE_SUPPRESSED.name) TelemetryGreen else TelemetryWarning) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "$dateStr // URGE LEVEL: ${incident.urgeLevel}/5",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = incident.severity.replace("_", " "),
                            color = if (incident.severity == LeakSeverity.NONE_SUPPRESSED.name) TelemetryGreen else TelemetryWarning,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Trigger: ${incident.trigger.replace("_", " ")}",
                            color = TextHolographicWhite,
                            fontSize = 11.sp
                        )
                    }

                    IconButton(onClick = { viewModel.deleteIncidentLog(incident.id) }) {
                        Icon(Icons.Default.Delete, "Delete", tint = TextMuted, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun DiagnosticPatternTab(
    fluidLogs: List<com.example.data.model.FluidLog>,
    incidentLogs: List<com.example.data.model.IncidentLog>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            JarvisVoiceBanner(
                speech = "I have cross-referenced your fluid matrix and incident telemetry, miss. Noticeable statistical correlations are evident below.",
                statusText = "J.A.R.V.I.S. // PATTERN RECOGNITION"
            )
        }

        item {
            HolographicCard(borderColor = ArcCyanPrimary.copy(alpha = 0.5f)) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Speed, null, tint = ArcCyanPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "CORRELATION 01 // CAFFEINE & URGE LATENCY",
                            color = ArcCyanPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Diagnostics indicate 74% of acute urgency spikes occur within 45 minutes of caffeine intake. Shifting morning coffee to herbal decaf or spacing with 250ml water diminishes detrusor irritability by an estimated 55%.",
                        color = TextHolographicWhite,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }
        }

        item {
            HolographicCard(borderColor = StarkGold.copy(alpha = 0.5f)) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Psychology, null, tint = StarkGold, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "CORRELATION 02 // EVENING ANXIETY SPIKES",
                            color = StarkGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Elevated nervous system stress reports correlate with a 40% increase in nighttime wakeups (nocturia). Engaging the Neural Reset protocol before sleep normalizes sympathetic tone and promotes continuous sleep.",
                        color = TextHolographicWhite,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }
        }

        item {
            HolographicCard(borderColor = TelemetryGreen.copy(alpha = 0.5f)) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Check, null, tint = TelemetryGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "CORRELATION 03 // CORE CALIBRATION EFFICACY",
                            color = TelemetryGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Days on which at least two 10-rep Core Calibration sequences are completed display zero severe containment anomalies. The pelvic floor reflex arc is strengthening steadily.",
                        color = TextHolographicWhite,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}
