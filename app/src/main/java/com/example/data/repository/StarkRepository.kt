package com.example.data.repository

import com.example.data.db.TelemetryDao
import com.example.data.model.CalibrationSession
import com.example.data.model.FluidLog
import com.example.data.model.IncidentLog
import com.example.data.model.VoidingLog
import kotlinx.coroutines.flow.Flow

class StarkRepository(private val dao: TelemetryDao) {
    val allFluidLogs: Flow<List<FluidLog>> = dao.getAllFluidLogs()
    val allIncidentLogs: Flow<List<IncidentLog>> = dao.getAllIncidentLogs()
    val allCalibrationSessions: Flow<List<CalibrationSession>> = dao.getAllCalibrationSessions()
    val allVoidingLogs: Flow<List<VoidingLog>> = dao.getAllVoidingLogs()
    val totalCompletedReps: Flow<Int?> = dao.getTotalCompletedReps()

    fun getRecentFluidLogs(sinceTimestamp: Long): Flow<List<FluidLog>> =
        dao.getRecentFluidLogs(sinceTimestamp)

    fun getRecentIncidents(sinceTimestamp: Long): Flow<List<IncidentLog>> =
        dao.getRecentIncidentLogs(sinceTimestamp)

    fun getRecentVoidingLogs(sinceTimestamp: Long): Flow<List<VoidingLog>> =
        dao.getRecentVoidingLogs(sinceTimestamp)

    suspend fun logFluid(log: FluidLog): Long = dao.insertFluidLog(log)
    suspend fun deleteFluid(id: Long) = dao.deleteFluidLog(id)

    suspend fun logIncident(incident: IncidentLog): Long = dao.insertIncidentLog(incident)
    suspend fun deleteIncident(id: Long) = dao.deleteIncidentLog(id)

    suspend fun logCalibration(session: CalibrationSession): Long = dao.insertCalibrationSession(session)

    suspend fun logVoiding(log: VoidingLog): Long = dao.insertVoidingLog(log)
}
