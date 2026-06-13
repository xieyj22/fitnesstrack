package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workout_logs ORDER BY dateMillis DESC, id DESC")
    fun getAllWorkouts(): Flow<List<WorkoutLog>>

    @Query("SELECT * FROM workout_logs WHERE dateMillis >= :startMillis AND dateMillis <= :endMillis ORDER BY dateMillis DESC")
    fun getWorkoutsBetween(startMillis: Long, endMillis: Long): Flow<List<WorkoutLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutLog)

    @Update
    suspend fun updateWorkout(workout: WorkoutLog)

    @Delete
    suspend fun deleteWorkout(workout: WorkoutLog)

    @Query("DELETE FROM workout_logs WHERE id = :id")
    suspend fun deleteWorkoutById(id: Int)

    @Query("SELECT * FROM custom_exercises ORDER BY id DESC")
    fun getAllCustomExercises(): Flow<List<CustomExercise>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomExercise(exercise: CustomExercise)

    @Delete
    suspend fun deleteCustomExercise(exercise: CustomExercise)
}
