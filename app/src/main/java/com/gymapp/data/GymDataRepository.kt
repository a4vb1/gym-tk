package com.gymapp.data

import android.content.Context
import com.gymapp.R
import com.gymapp.database.GymDatabaseHelper
import com.gymapp.model.Exercise
import com.gymapp.model.ExerciseStats
import com.gymapp.model.MuscleGroup
import com.gymapp.model.WorkoutRecord
import java.util.*

/**
 * مستودع البيانات لتطبيق الجيم
 * Data repository for Gym application
 */
class GymDataRepository(context: Context) {
    
    private val databaseHelper = GymDatabaseHelper(context)
    
    /**
     * الحصول على جميع مجموعات العضلات - Get all muscle groups
     */
    fun getMuscleGroups(): List<MuscleGroup> {
        return listOf(
            MuscleGroup(1, "الظهر", "Back", R.drawable.ic_back),
            MuscleGroup(2, "الصدر", "Chest", R.drawable.ic_chest),
            MuscleGroup(3, "الأرجل", "Legs", R.drawable.ic_legs),
            MuscleGroup(4, "الأكتاف", "Shoulders", R.drawable.ic_shoulders),
            MuscleGroup(5, "البايسبس", "Biceps", R.drawable.ic_biceps),
            MuscleGroup(6, "الترايسبس", "Triceps", R.drawable.ic_triceps),
            MuscleGroup(7, "السواعد", "Forearms", R.drawable.ic_forearms)
        )
    }

    /**
     * الحصول على التمارين لمجموعة عضلات معينة - Get exercises for specific muscle group
     */
    fun getExercisesForMuscleGroup(muscleGroupId: Int): List<Exercise> {
        return when (muscleGroupId) {
            1 -> listOf( // الظهر - Back
                Exercise(1, 1, "الرفعة الميتة", "Deadlift", "تمرين شامل للظهر والأرجل", "Full back and legs exercise"),
                Exercise(2, 1, "العقلة", "Pull-ups", "تمرين لعضلات الظهر العلوية", "Upper back exercise"),
                Exercise(3, 1, "تجديف البار", "T-Bar Row", "تمرين لعضلات الظهر الوسطى", "Middle back exercise")
            )
            2 -> listOf( // الصدر - Chest
                Exercise(4, 2, "البنش برس", "Bench Press", "التمرين الأساسي للصدر", "Main chest exercise"),
                Exercise(5, 2, "البنش المائل بالدمبل", "Incline Dumbbell Press", "تمرين للصدر العلوي", "Upper chest exercise"),
                Exercise(6, 2, "الضغط", "Push-ups", "تمرين الصدر بوزن الجسم", "Bodyweight chest exercise")
            )
            3 -> listOf( // الأرجل - Legs
                Exercise(7, 3, "السكوات", "Squat", "التمرين الأساسي للأرجل", "Main leg exercise"),
                Exercise(8, 3, "مكبس الأرجل", "Leg Press", "تمرين الأرجل بالآلة", "Machine leg exercise"),
                Exercise(9, 3, "الخطوات", "Lunges", "تمرين للأرجل والتوازن", "Legs and balance exercise")
            )
            4 -> listOf( // الأكتاف - Shoulders
                Exercise(10, 4, "الضغط العسكري", "Overhead Press", "التمرين الأساسي للأكتاف", "Main shoulder exercise"),
                Exercise(11, 4, "الرفرفة الجانبية", "Lateral Raise", "تمرين للكتف الجانبي", "Side shoulder exercise"),
                Exercise(12, 4, "الرفرفة الأمامية", "Front Raise", "تمرين للكتف الأمامي", "Front shoulder exercise")
            )
            5 -> listOf( // البايسبس - Biceps
                Exercise(13, 5, "العقل بالبار", "Barbell Curl", "التمرين الأساسي للبايسبس", "Main biceps exercise"),
                Exercise(14, 5, "المطرقة", "Hammer Curl", "تمرين البايسبس والساعد", "Biceps and forearm exercise"),
                Exercise(15, 5, "العقل المركز", "Concentration Curl", "تمرين مركز للبايسبس", "Focused biceps exercise")
            )
            6 -> listOf( // الترايسبس - Triceps
                Exercise(16, 6, "الضغط بالكيبل", "Triceps Pushdown", "تمرين الترايسبس بالكيبل", "Cable triceps exercise"),
                Exercise(17, 6, "كسر الجمجمة", "Skull Crushers", "تمرين مكثف للترايسبس", "Intensive triceps exercise"),
                Exercise(18, 6, "الديبس", "Dips", "تمرين الترايسبس بوزن الجسم", "Bodyweight triceps exercise")
            )
            7 -> listOf( // السواعد - Forearms
                Exercise(19, 7, "عقل الرسغ", "Wrist Curl", "تمرين عضلات الساعد الأمامية", "Front forearm exercise"),
                Exercise(20, 7, "العقل المعكوس", "Reverse Curl", "تمرين عضلات الساعد الخلفية", "Back forearm exercise"),
                Exercise(21, 7, "حمل الفلاح", "Farmer's Carry", "تمرين قوة القبضة", "Grip strength exercise")
            )
            else -> emptyList()
        }
    }

    /**
     * الحصول على تمرين معين بالمعرف - Get specific exercise by ID
     */
    fun getExerciseById(exerciseId: Int): Exercise? {
        return getMuscleGroups().flatMap { muscleGroup ->
            getExercisesForMuscleGroup(muscleGroup.id)
        }.find { it.id == exerciseId }
    }

    /**
     * إضافة سجل تمرين جديد - Add new workout record
     */
    fun saveWorkoutRecord(workoutRecord: WorkoutRecord): Long {
        return databaseHelper.insertWorkoutRecord(workoutRecord)
    }

    /**
     * الحصول على سجلات التمرين لتمرين معين - Get workout records for specific exercise
     */
    fun getWorkoutRecordsForExercise(exerciseId: Int): List<WorkoutRecord> {
        return databaseHelper.getWorkoutRecordsForExercise(exerciseId)
    }

    /**
     * الحصول على آخر سجل تمرين - Get last workout record
     */
    fun getLastWorkoutRecord(exerciseId: Int): WorkoutRecord? {
        return databaseHelper.getLastWorkoutRecord(exerciseId)
    }

    /**
     * الحصول على إحصائيات التمرين - Get exercise statistics
     */
    fun getExerciseStats(exerciseId: Int): ExerciseStats {
        val records = getWorkoutRecordsForExercise(exerciseId)
        
        if (records.isEmpty()) {
            return ExerciseStats(
                exerciseId = exerciseId,
                lastWeight = null,
                lastReps = null,
                maxWeight = null,
                totalWorkouts = 0,
                lastWorkoutDate = null
            )
        }

        val lastRecord = records.first()
        val maxWeight = records.maxByOrNull { it.weight }?.weight

        return ExerciseStats(
            exerciseId = exerciseId,
            lastWeight = lastRecord.weight,
            lastReps = lastRecord.reps,
            maxWeight = maxWeight,
            totalWorkouts = records.size,
            lastWorkoutDate = lastRecord.date
        )
    }
}