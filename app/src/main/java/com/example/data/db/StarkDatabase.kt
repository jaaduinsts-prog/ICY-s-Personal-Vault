package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.CalibrationSession
import com.example.data.model.FluidLog
import com.example.data.model.IncidentLog
import com.example.data.model.VoidingLog

@Database(
    entities = [
        FluidLog::class,
        IncidentLog::class,
        CalibrationSession::class,
        VoidingLog::class
    ],
    version = 1,
    exportSchema = false
)
abstract class StarkDatabase : RoomDatabase() {
    abstract fun telemetryDao(): TelemetryDao

    companion object {
        @Volatile
        private var INSTANCE: StarkDatabase? = null

        fun getDatabase(context: Context): StarkDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StarkDatabase::class.java,
                    "stark_protocol_telemetry.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
