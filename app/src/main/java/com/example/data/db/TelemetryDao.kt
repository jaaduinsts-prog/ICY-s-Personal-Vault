package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.CalibrationSession
import com.example.data.model.FluidLog
import com.example.data.model.IncidentLog
import com.example.data.model.VoidingLog
import kotlinx.coroutines.flow.Flow

@Dao
interface TelemetryDao {
    // Fluid Intake
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFluidLog(log: FluidLog): Long

    @Query("SELECT * FROM fluid_logs ORDER BY timestamp DESC")
    fun getAllFluidLogs(): Flow<List<FluidLog>>

    @Query("SELECT * FROM fluid_logs WHERE timestamp >= :sinceTimestamp ORDER BY timestamp DESC")
    fun getRecentFluidLogs(sinceTimestamp: Long): Flow<List<FluidLog>>

    @Query("DELETE FROM fluid_logs WHERE id = :id")
    suspend fun deleteFluidLog(id: Long)

    // Incidents & Spasms
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncidentLog(incident: IncidentLog): Long

    @Query("SELECT * FROM incident_logs ORDER BY timestamp DESC")
    fun getAllIncidentLogs(): Flow<List<IncidentLog>>

    @Query("SELECT * FROM incident_logs WHERE timestamp >= :sinceTimestamp ORDER BY timestamp DESC")
    fun getRecentIncidentLogs(sinceTimestamp: Long): Flow<List<IncidentLog>>

    @Query("DELETE FROM incident_logs WHERE id = :id")
    suspend fun deleteIncidentLog(id: Long)

    // Calibration Sessions (Kegels)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalibrationSession(session: CalibrationSession): Long

    @Query("SELECT * FROM calibration_sessions ORDER BY timestamp DESC")
    fun getAllCalibrationSessions(): Flow<List<CalibrationSession>>

    @Query("SELECT SUM(completedReps) FROM calibration_sessions")
    fun getTotalCompletedReps(): Flow<Int?>

    // Voiding Logs (Timed Voiding)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVoidingLog(log: VoidingLog): Long

    @Query("SELECT * FROM voiding_logs ORDER BY timestamp DESC")
    fun getAllVoidingLogs(): Flow<List<VoidingLog>>

    @Query("SELECT * FROM voiding_logs WHERE timestamp >= :sinceTimestamp ORDER BY timestamp DESC")
    fun getRecentVoidingLogs(sinceTimestamp: Long): Flow<List<VoidingLog>>
}
