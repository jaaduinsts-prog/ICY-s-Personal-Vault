package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.StarkDatabase
import com.example.data.model.CalibrationSession
import com.example.data.model.FluidLog
import com.example.data.model.FluidType
import com.example.data.model.IncidentLog
import com.example.data.model.LeakSeverity
import com.example.data.model.UrgeTrigger
import com.example.data.model.VoidingLog
import com.example.data.repository.StarkRepository
import com.example.ui.components.BreathPhase
import com.example.ui.components.CalibrationPhase
import com.example.ui.components.JarvisAudioHaptics
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen {
    DASHBOARD,
    OVERRIDE_PROTOCOL,
    ARC_CALIBRATION,
    SYSTEM_TELEMETRY,
    ROUTINE_MAINTENANCE,
    NEURAL_RESET,
    VAULT_SECURITY,
    STEALTH_CLOAK
}

enum class OverridePhase {
    STOP_FREEZE,    // 5 seconds standing/sitting still
    QUICK_FLICKS,   // 5 rapid pelvic floor contractions
    TACTICAL_BREATH,// 4-7-8 downshift breathing
    STABILIZED      // Urge successfully suppressed
}

class StarkViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: StarkRepository
    val audioHaptics = JarvisAudioHaptics(application)

    init {
        val db = StarkDatabase.getDatabase(application)
        repository = StarkRepository(db.telemetryDao())
    }

    val fluidLogs = repository.allFluidLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val incidentLogs = repository.allIncidentLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val calibrationSessions = repository.allCalibrationSessions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val voidingLogs = repository.allVoidingLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalReps = repository.totalCompletedReps
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Current Screen
    private val _currentScreen = MutableStateFlow(Screen.DASHBOARD)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Stealth Mode
    private val _isStealthModeActive = MutableStateFlow(false)
    val isStealthModeActive: StateFlow<Boolean> = _isStealthModeActive.asStateFlow()

    private val _stealthAppName = MutableStateFlow("Protocol J")
    val stealthAppName: StateFlow<String> = _stealthAppName.asStateFlow()

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun toggleStealthMode() {
        _isStealthModeActive.value = !_isStealthModeActive.value
    }

    fun setStealthAppName(name: String) {
        _stealthAppName.value = name
    }

    // ==========================================
    // 1. THE OVERRIDE PROTOCOL (Urgency Suppressor)
    // ==========================================
    private val _overridePhase = MutableStateFlow(OverridePhase.STOP_FREEZE)
    val overridePhase: StateFlow<OverridePhase> = _overridePhase.asStateFlow()

    private val _freezeSecondsLeft = MutableStateFlow(5)
    val freezeSecondsLeft: StateFlow<Int> = _freezeSecondsLeft.asStateFlow()

    private val _quickFlicksCount = MutableStateFlow(0)
    val quickFlicksCount: StateFlow<Int> = _quickFlicksCount.asStateFlow()

    private val _overrideBreathPhase = MutableStateFlow(BreathPhase.INHALE)
    val overrideBreathPhase: StateFlow<BreathPhase> = _overrideBreathPhase.asStateFlow()

    private val _overrideBreathSecondsLeft = MutableStateFlow(4)
    val overrideBreathSecondsLeft: StateFlow<Int> = _overrideBreathSecondsLeft.asStateFlow()

    private val _overrideBreathCyclesRemaining = MutableStateFlow(3)
    val overrideBreathCyclesRemaining: StateFlow<Int> = _overrideBreathCyclesRemaining.asStateFlow()

    private var overrideJob: Job? = null

    fun startOverrideProtocol() {
        overrideJob?.cancel()
        _currentScreen.value = Screen.OVERRIDE_PROTOCOL
        _overridePhase.value = OverridePhase.STOP_FREEZE
        _freezeSecondsLeft.value = 5
        _quickFlicksCount.value = 0
        _overrideBreathCyclesRemaining.value = 3

        overrideJob = viewModelScope.launch {
            // Phase 1: STOP & FREEZE (5s)
            audioHaptics.playContractPulse()
            for (sec in 5 downTo 1) {
                _freezeSecondsLeft.value = sec
                delay(1000)
            }
            // Transition to Quick Flicks
            _overridePhase.value = OverridePhase.QUICK_FLICKS
        }
    }

    fun triggerQuickFlick() {
        if (_overridePhase.value != OverridePhase.QUICK_FLICKS) return
        val next = _quickFlicksCount.value + 1
        _quickFlicksCount.value = next
        audioHaptics.playQuickFlickHaptic()

        if (next >= 5) {
            // Transition to Tactical Breathing Downshift
            _overridePhase.value = OverridePhase.TACTICAL_BREATH
            startOverrideBreathingLoop()
        }
    }

    private fun startOverrideBreathingLoop() {
        overrideJob?.cancel()
        overrideJob = viewModelScope.launch {
            while (_overrideBreathCyclesRemaining.value > 0) {
                // Inhale 4s
                _overrideBreathPhase.value = BreathPhase.INHALE
                audioHaptics.playRelaxPulse()
                for (s in 4 downTo 1) {
                    _overrideBreathSecondsLeft.value = s
                    delay(1000)
                }

                // Hold 4s
                _overrideBreathPhase.value = BreathPhase.HOLD
                for (s in 4 downTo 1) {
                    _overrideBreathSecondsLeft.value = s
                    delay(1000)
                }

                // Exhale 6s
                _overrideBreathPhase.value = BreathPhase.EXHALE
                for (s in 6 downTo 1) {
                    _overrideBreathSecondsLeft.value = s
                    delay(1000)
                }

                _overrideBreathCyclesRemaining.value -= 1
            }

            // Stabilized
            _overridePhase.value = OverridePhase.STABILIZED
            audioHaptics.playSuccessFanfare()

            // Automatically log victory
            repository.logIncident(
                IncidentLog(
                    severity = LeakSeverity.NONE_SUPPRESSED.name,
                    trigger = UrgeTrigger.SPONTANEOUS.name,
                    urgeLevel = 5,
                    padChanged = false,
                    wasOverrideUsed = true,
                    notes = "Override Protocol successfully quelled bladder spasm."
                )
            )
        }
    }

    // ==========================================
    // 2. ARC REACTOR CORE CALIBRATION (Kegel Trainer)
    // ==========================================
    private val _calibrationPhase = MutableStateFlow(CalibrationPhase.READY)
    val calibrationPhase: StateFlow<CalibrationPhase> = _calibrationPhase.asStateFlow()

    private val _calibSecondsLeft = MutableStateFlow(0)
    val calibSecondsLeft: StateFlow<Int> = _calibSecondsLeft.asStateFlow()

    private val _calibPhaseProgress = MutableStateFlow(0f)
    val calibPhaseProgress: StateFlow<Float> = _calibPhaseProgress.asStateFlow()

    private val _calibCurrentRep = MutableStateFlow(1)
    val calibCurrentRep: StateFlow<Int> = _calibCurrentRep.asStateFlow()

    private val _calibTotalReps = MutableStateFlow(10)
    val calibTotalReps: StateFlow<Int> = _calibTotalReps.asStateFlow()

    private val _calibHoldSecondsTarget = MutableStateFlow(4)
    val calibHoldSecondsTarget: StateFlow<Int> = _calibHoldSecondsTarget.asStateFlow()

    private val _calibRestSecondsTarget = MutableStateFlow(4)
    val calibRestSecondsTarget: StateFlow<Int> = _calibRestSecondsTarget.asStateFlow()

    private var calibrationJob: Job? = null

    fun setCalibrationHoldDuration(seconds: Int) {
        _calibHoldSecondsTarget.value = seconds
        _calibRestSecondsTarget.value = seconds
    }

    fun startCalibrationSession() {
        calibrationJob?.cancel()
        _calibrationPhase.value = CalibrationPhase.READY
        _calibCurrentRep.value = 1

        calibrationJob = viewModelScope.launch {
            // Ready countdown
            for (sec in 3 downTo 1) {
                _calibSecondsLeft.value = sec
                audioHaptics.playRelaxPulse()
                delay(1000)
            }

            val reps = _calibTotalReps.value
            val holdTime = _calibHoldSecondsTarget.value
            val restTime = _calibRestSecondsTarget.value

            for (rep in 1..reps) {
                _calibCurrentRep.value = rep

                // CONTRACT & HOLD PHASE
                _calibrationPhase.value = CalibrationPhase.CONTRACT
                audioHaptics.playContractPulse()

                val holdSteps = holdTime * 20
                for (step in 1..holdSteps) {
                    val progress = step.toFloat() / holdSteps
                    _calibPhaseProgress.value = progress
                    _calibSecondsLeft.value = (holdTime - (step / 20.0)).toInt().coerceAtLeast(1)
                    if (step == holdSteps / 2) {
                        _calibrationPhase.value = CalibrationPhase.HOLD
                    }
                    delay(50)
                }

                // RELAX / VENT PHASE
                _calibrationPhase.value = CalibrationPhase.RELAX
                audioHaptics.playRelaxPulse()

                val restSteps = restTime * 20
                for (step in 1..restSteps) {
                    val progress = step.toFloat() / restSteps
                    _calibPhaseProgress.value = progress
                    _calibSecondsLeft.value = (restTime - (step / 20.0)).toInt().coerceAtLeast(1)
                    delay(50)
                }
            }

            _calibrationPhase.value = CalibrationPhase.COMPLETE
            audioHaptics.playSuccessFanfare()

            // Save session to Room database
            repository.logCalibration(
                CalibrationSession(
                    completedReps = reps,
                    targetReps = reps,
                    holdDurationSeconds = holdTime,
                    powerScore = 95
                )
            )
        }
    }

    fun stopCalibrationSession() {
        calibrationJob?.cancel()
        _calibrationPhase.value = CalibrationPhase.READY
    }

    // ==========================================
    // 3. SYSTEM TELEMETRY (Fluids & Incidents)
    // ==========================================
    fun logFluidIntake(amountMl: Int, type: FluidType, notes: String = "") {
        viewModelScope.launch {
            repository.logFluid(
                FluidLog(
                    amountMl = amountMl,
                    fluidType = type.name,
                    isIrritant = type.isIrritant,
                    notes = notes
                )
            )
        }
    }

    fun deleteFluidLog(id: Long) {
        viewModelScope.launch {
            repository.deleteFluid(id)
        }
    }

    fun logIncidentReport(
        severity: LeakSeverity,
        trigger: UrgeTrigger,
        urgeLevel: Int,
        padChanged: Boolean,
        notes: String = ""
    ) {
        viewModelScope.launch {
            repository.logIncident(
                IncidentLog(
                    severity = severity.name,
                    trigger = trigger.name,
                    urgeLevel = urgeLevel,
                    padChanged = padChanged,
                    wasOverrideUsed = false,
                    notes = notes
                )
            )
        }
    }

    fun deleteIncidentLog(id: Long) {
        viewModelScope.launch {
            repository.deleteIncident(id)
        }
    }

    // ==========================================
    // 4. ROUTINE MAINTENANCE (Timed Voiding)
    // ==========================================
    private val _voidingIntervalMinutes = MutableStateFlow(120) // 2 hours
    val voidingIntervalMinutes: StateFlow<Int> = _voidingIntervalMinutes.asStateFlow()

    private val _secondsUntilNextVoid = MutableStateFlow(7200) // 2 hours in sec
    val secondsUntilNextVoid: StateFlow<Int> = _secondsUntilNextVoid.asStateFlow()

    private var voidTimerJob: Job? = null

    init {
        startTimedVoidingCountdown()
    }

    fun setVoidingInterval(minutes: Int) {
        _voidingIntervalMinutes.value = minutes
        _secondsUntilNextVoid.value = minutes * 60
    }

    fun logVoidingVisit(wasScheduled: Boolean, urgeLevel: Int, delaySeconds: Int = 0) {
        viewModelScope.launch {
            repository.logVoiding(
                VoidingLog(
                    wasScheduled = wasScheduled,
                    urgeLevel = urgeLevel,
                    delayAchievedSeconds = delaySeconds,
                    notes = if (wasScheduled) "Routine maintenance conducted on protocol." else "Interim scheduled adjustment."
                )
            )
            // Reset timer
            _secondsUntilNextVoid.value = _voidingIntervalMinutes.value * 60
            audioHaptics.playRelaxPulse()
        }
    }

    private fun startTimedVoidingCountdown() {
        voidTimerJob?.cancel()
        voidTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                if (_secondsUntilNextVoid.value > 0) {
                    _secondsUntilNextVoid.value -= 1
                }
            }
        }
    }

    // ==========================================
    // 5. NEURAL RESET (Anxiety & Breathing)
    // ==========================================
    private val _neuralResetPhase = MutableStateFlow(BreathPhase.INHALE)
    val neuralResetPhase: StateFlow<BreathPhase> = _neuralResetPhase.asStateFlow()

    private val _neuralSecondsLeft = MutableStateFlow(4)
    val neuralSecondsLeft: StateFlow<Int> = _neuralSecondsLeft.asStateFlow()

    private val _isAmbientSoundActive = MutableStateFlow(false)
    val isAmbientSoundActive: StateFlow<Boolean> = _isAmbientSoundActive.asStateFlow()

    private val _selectedSoundscape = MutableStateFlow("ARC_REACTOR_HUM")
    val selectedSoundscape: StateFlow<String> = _selectedSoundscape.asStateFlow()

    private var neuralBreathJob: Job? = null

    fun startNeuralBoxBreathing() {
        neuralBreathJob?.cancel()
        neuralBreathJob = viewModelScope.launch {
            while (true) {
                // Inhale 4s
                _neuralResetPhase.value = BreathPhase.INHALE
                audioHaptics.playRelaxPulse()
                for (s in 4 downTo 1) {
                    _neuralSecondsLeft.value = s
                    delay(1000)
                }

                // Hold 4s
                _neuralResetPhase.value = BreathPhase.HOLD
                for (s in 4 downTo 1) {
                    _neuralSecondsLeft.value = s
                    delay(1000)
                }

                // Exhale 4s
                _neuralResetPhase.value = BreathPhase.EXHALE
                for (s in 4 downTo 1) {
                    _neuralSecondsLeft.value = s
                    delay(1000)
                }

                // Rest 4s
                _neuralResetPhase.value = BreathPhase.REST
                for (s in 4 downTo 1) {
                    _neuralSecondsLeft.value = s
                    delay(1000)
                }
            }
        }
    }

    fun stopNeuralBreathing() {
        neuralBreathJob?.cancel()
    }

    fun toggleAmbientAudio() {
        _isAmbientSoundActive.value = !_isAmbientSoundActive.value
    }

    fun setSoundscape(soundscape: String) {
        _selectedSoundscape.value = soundscape
    }

    override fun onCleared() {
        super.onCleared()
        overrideJob?.cancel()
        calibrationJob?.cancel()
        voidTimerJob?.cancel()
        neuralBreathJob?.cancel()
    }
}
