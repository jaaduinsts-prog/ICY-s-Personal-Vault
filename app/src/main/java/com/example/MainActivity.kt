package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.Screen
import com.example.ui.StarkViewModel
import com.example.ui.screens.ArcCalibrationScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.NeuralResetScreen
import com.example.ui.screens.OverrideProtocolScreen
import com.example.ui.screens.RoutineMaintenanceScreen
import com.example.ui.screens.StarkVaultScreen
import com.example.ui.screens.StealthCloakScreen
import com.example.ui.screens.SystemTelemetryScreen
import com.example.ui.theme.ArcCyanPrimary
import com.example.ui.theme.CarbonBackground
import com.example.ui.theme.CarbonBorder
import com.example.ui.theme.CarbonSurface
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.StarkGold
import com.example.ui.theme.TextHolographicWhite
import com.example.ui.theme.TextMuted

class MainActivity : ComponentActivity() {
    private val viewModel: StarkViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: StarkViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val isStealthActive by viewModel.isStealthModeActive.collectAsState()

    // Handle back press to always return to Dashboard
    BackHandler(enabled = isStealthActive || currentScreen != Screen.DASHBOARD) {
        if (isStealthActive) {
            viewModel.toggleStealthMode()
        } else {
            viewModel.navigateTo(Screen.DASHBOARD)
        }
    }

    if (isStealthActive) {
        // Render fake Stark Smart Home automation screen
        StealthCloakScreen(viewModel = viewModel)
        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = CarbonBackground,
        bottomBar = {
            if (currentScreen != Screen.OVERRIDE_PROTOCOL) {
                StarkBottomNavigationBar(
                    currentScreen = currentScreen,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                Screen.DASHBOARD -> DashboardScreen(viewModel = viewModel)
                Screen.OVERRIDE_PROTOCOL -> OverrideProtocolScreen(viewModel = viewModel)
                Screen.ARC_CALIBRATION -> ArcCalibrationScreen(viewModel = viewModel)
                Screen.SYSTEM_TELEMETRY -> SystemTelemetryScreen(viewModel = viewModel)
                Screen.ROUTINE_MAINTENANCE -> RoutineMaintenanceScreen(viewModel = viewModel)
                Screen.NEURAL_RESET -> NeuralResetScreen(viewModel = viewModel)
                Screen.VAULT_SECURITY -> StarkVaultScreen(viewModel = viewModel)
                Screen.STEALTH_CLOAK -> StealthCloakScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun StarkBottomNavigationBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    NavigationBar(
        containerColor = CarbonSurface,
        tonalElevation = 4.dp,
        modifier = Modifier.testTag("stark_bottom_nav")
    ) {
        NavigationBarItem(
            selected = currentScreen == Screen.DASHBOARD,
            onClick = { onNavigate(Screen.DASHBOARD) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Deck") },
            label = { Text("Deck", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CarbonBackground,
                selectedTextColor = ArcCyanPrimary,
                indicatorColor = ArcCyanPrimary,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("nav_dashboard")
        )

        NavigationBarItem(
            selected = currentScreen == Screen.ARC_CALIBRATION,
            onClick = { onNavigate(Screen.ARC_CALIBRATION) },
            icon = { Icon(Icons.Default.Bolt, contentDescription = "Calibrate") },
            label = { Text("Core", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CarbonBackground,
                selectedTextColor = ArcCyanPrimary,
                indicatorColor = ArcCyanPrimary,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("nav_calibration")
        )

        NavigationBarItem(
            selected = currentScreen == Screen.SYSTEM_TELEMETRY,
            onClick = { onNavigate(Screen.SYSTEM_TELEMETRY) },
            icon = { Icon(Icons.Default.Speed, contentDescription = "Telemetry") },
            label = { Text("Telemetry", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CarbonBackground,
                selectedTextColor = ArcCyanPrimary,
                indicatorColor = ArcCyanPrimary,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("nav_telemetry")
        )

        NavigationBarItem(
            selected = currentScreen == Screen.ROUTINE_MAINTENANCE,
            onClick = { onNavigate(Screen.ROUTINE_MAINTENANCE) },
            icon = { Icon(Icons.Default.Timer, contentDescription = "Maintenance") },
            label = { Text("Timed", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CarbonBackground,
                selectedTextColor = ArcCyanPrimary,
                indicatorColor = ArcCyanPrimary,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("nav_maintenance")
        )

        NavigationBarItem(
            selected = currentScreen == Screen.NEURAL_RESET,
            onClick = { onNavigate(Screen.NEURAL_RESET) },
            icon = { Icon(Icons.Default.Psychology, contentDescription = "Reset") },
            label = { Text("Neural", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CarbonBackground,
                selectedTextColor = ArcCyanPrimary,
                indicatorColor = ArcCyanPrimary,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("nav_neural")
        )
    }
}
