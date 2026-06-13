package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_exercises")
data class CustomExercise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String, // "CARDIO" or "STRENGTH"
    val unit: String? = null // Optional unit, e.g. "次数", "磅", "分钟"
)
