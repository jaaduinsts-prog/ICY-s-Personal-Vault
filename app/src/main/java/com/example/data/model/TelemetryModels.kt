package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class FluidType(val displayName: String, val isIrritant: Boolean, val irritantDescription: String) {
    WATER("Hydration Matrix (Water)", false, "Optimal cellular fluid"),
    ELECTROLYTE("Electrolyte Infusion", false, "Bladder-neutral hydration"),
    HERBAL_TEA("Herbal Decaf Infusion", false, "Mild, non-irritating"),
    CAFFEINE("Caffeine Reactor (Coffee/Energy)", true, "High diuretic & detrusor stimulant"),
    TEA_BLACK("Black/Green Tea", true, "Moderate caffeine & acidity"),
    CITRUS_JUICE("Citrus Acid Fluid (Orange/Lemon)", true, "Bladder urothelium irritant"),
    CARBONATED("Carbonated / Sparkling Beverage", true, "Dissolved CO2 sparks bladder spasms"),
    ALCOHOL("Alcoholic Compound", true, "Neurological diuretic & irritant"),
    ARTIFICIAL_SWEETENER("Synthetic Sweetener", true, "Known neuro-sensory bladder trigger")
}

enum class LeakSeverity(val displayName: String, val severityIndex: Int) {
    NONE_SUPPRESSED("Urge Suppressed (Zero Leak)", 0),
    MINOR_DROPS("Micro Anomaly (Few Drops)", 1),
    MODERATE_LEAK("Noticeable Containment Breach (Underwear Damp)", 2),
    SEVERE_LEAK("Complete Containment Failure (Clothing Wet)", 3)
}

enum class UrgeTrigger(val displayName: String) {
    KEY_IN_DOOR("Latch Trigger (Key in Door / Home Arrival)"),
    RUNNING_WATER("Acoustic Trigger (Running Water)"),
    ANXIETY_STRESS("Neural Stress / Panic Spike"),
    COUGH_SNEEZE("Intra-Abdominal Pressure Surge"),
    TRANSITION_RISE("Kinetic Shift (Standing Up / Leaving Chair)"),
    COLD_EXPOSURE("Thermal Shock (Cold Ambient Air)"),
    SPONTANEOUS("Spontaneous Detrusor Reflex (Sudden Urge)")
}

@Entity(tableName = "fluid_logs")
data class FluidLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val amountMl: Int,
    val fluidType: String,
    val isIrritant: Boolean,
    val notes: String = ""
)

@Entity(tableName = "incident_logs")
data class IncidentLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val severity: String,
    val trigger: String,
    val urgeLevel: Int, // 1 to 5 scale
    val padChanged: Boolean = false,
    val wasOverrideUsed: Boolean = false,
    val notes: String = ""
)

@Entity(tableName = "calibration_sessions")
data class CalibrationSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val completedReps: Int,
    val targetReps: Int,
    val holdDurationSeconds: Int,
    val powerScore: Int, // 0 - 100
    val sessionType: String = "CORE_CALIBRATION"
)

@Entity(tableName = "voiding_logs")
data class VoidingLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val wasScheduled: Boolean,
    val urgeLevel: Int, // 1 to 5
    val delayAchievedSeconds: Int = 0, // Delayed voiding training
    val notes: String = ""
)
