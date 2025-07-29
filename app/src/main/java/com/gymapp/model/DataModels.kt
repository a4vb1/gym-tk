package com.gymapp.model

import java.util.Date

/**
 * نموذج البيانات للعضلات
 * Data model for muscle groups
 */
data class MuscleGroup(
    val id: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val iconResourceId: Int = 0
)

/**
 * نموذج البيانات للتمارين
 * Data model for exercises
 */
data class Exercise(
    val id: Int,
    val muscleGroupId: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val descriptionArabic: String = "",
    val descriptionEnglish: String = "",
    val imageResourceId: Int = 0
)

/**
 * نموذج البيانات لسجلات التمرين
 * Data model for workout records
 */
data class WorkoutRecord(
    val id: Long = 0,
    val exerciseId: Int,
    val weight: Double,
    val reps: Int,
    val sets: Int = 1,
    val date: Date,
    val notes: String = ""
)

/**
 * نموذج البيانات لإحصائيات التمرين
 * Data model for exercise statistics
 */
data class ExerciseStats(
    val exerciseId: Int,
    val lastWeight: Double?,
    val lastReps: Int?,
    val maxWeight: Double?,
    val totalWorkouts: Int,
    val lastWorkoutDate: Date?
)