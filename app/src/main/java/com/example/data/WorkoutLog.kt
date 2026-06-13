package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_logs")
data class WorkoutLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateMillis: Long = System.currentTimeMillis(), // UTC timestamp for the day of workout
    val category: String, // "CARDIO" or "STRENGTH"
    val exerciseName: String,
    
    // Cardio specific
    val durationMinutes: Int = 0,
    val distanceKm: Double = 0.0,
    
    // Strength specific
    val weightKg: Double = 0.0,
    val reps: Int = 0,
    val sets: Int = 0,
    
    // Joint fields
    val caloriesBurned: Int = 0,
    val notes: String = "",

    // Custom fields
    val isCustom: Boolean = false,
    val customUnit: String = "分钟", // e.g. "分钟", "小时" (minutes, hours)
    val customValue: Double = 0.0
)
