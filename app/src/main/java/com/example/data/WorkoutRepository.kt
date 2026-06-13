package com.example.data

import kotlinx.coroutines.flow.Flow

class WorkoutRepository(private val workoutDao: WorkoutDao) {
    val allWorkouts: Flow<List<WorkoutLog>> = workoutDao.getAllWorkouts()

    fun getWorkoutsBetween(startMillis: Long, endMillis: Long): Flow<List<WorkoutLog>> {
        return workoutDao.getWorkoutsBetween(startMillis, endMillis)
    }

    suspend fun insert(workout: WorkoutLog) {
        workoutDao.insertWorkout(workout)
    }

    suspend fun update(workout: WorkoutLog) {
        workoutDao.updateWorkout(workout)
    }

    suspend fun delete(workout: WorkoutLog) {
        workoutDao.deleteWorkout(workout)
    }

    suspend fun deleteById(id: Int) {
        workoutDao.deleteWorkoutById(id)
    }

    val allCustomExercises: Flow<List<CustomExercise>> = workoutDao.getAllCustomExercises()

    suspend fun insertCustomExercise(exercise: CustomExercise) {
        workoutDao.insertCustomExercise(exercise)
    }

    suspend fun deleteCustomExercise(exercise: CustomExercise) {
        workoutDao.deleteCustomExercise(exercise)
    }
}
