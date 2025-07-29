package com.gymapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.gymapp.model.WorkoutRecord
import java.text.SimpleDateFormat
import java.util.*

/**
 * مساعد قاعدة البيانات لتطبيق الجيم
 * Database helper for Gym application
 */
class GymDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "gym_database.db"
        private const val DATABASE_VERSION = 1

        // جدول سجلات التمرين - Workout records table
        private const val TABLE_WORKOUT_RECORDS = "workout_records"
        private const val COLUMN_ID = "id"
        private const val COLUMN_EXERCISE_ID = "exercise_id"
        private const val COLUMN_WEIGHT = "weight"
        private const val COLUMN_REPS = "reps"
        private const val COLUMN_SETS = "sets"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_NOTES = "notes"

        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    }

    override fun onCreate(db: SQLiteDatabase) {
        // إنشاء جدول سجلات التمرين - Create workout records table
        val createWorkoutRecordsTable = """
            CREATE TABLE $TABLE_WORKOUT_RECORDS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EXERCISE_ID INTEGER NOT NULL,
                $COLUMN_WEIGHT REAL NOT NULL,
                $COLUMN_REPS INTEGER NOT NULL,
                $COLUMN_SETS INTEGER DEFAULT 1,
                $COLUMN_DATE TEXT NOT NULL,
                $COLUMN_NOTES TEXT DEFAULT ''
            )
        """.trimIndent()
        
        db.execSQL(createWorkoutRecordsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WORKOUT_RECORDS")
        onCreate(db)
    }

    /**
     * إضافة سجل تمرين جديد - Add new workout record
     */
    fun insertWorkoutRecord(workoutRecord: WorkoutRecord): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EXERCISE_ID, workoutRecord.exerciseId)
            put(COLUMN_WEIGHT, workoutRecord.weight)
            put(COLUMN_REPS, workoutRecord.reps)
            put(COLUMN_SETS, workoutRecord.sets)
            put(COLUMN_DATE, dateFormat.format(workoutRecord.date))
            put(COLUMN_NOTES, workoutRecord.notes)
        }
        
        return db.insert(TABLE_WORKOUT_RECORDS, null, values)
    }

    /**
     * الحصول على جميع سجلات التمرين لتمرين معين - Get all workout records for specific exercise
     */
    fun getWorkoutRecordsForExercise(exerciseId: Int): List<WorkoutRecord> {
        val records = mutableListOf<WorkoutRecord>()
        val db = readableDatabase
        
        val cursor = db.query(
            TABLE_WORKOUT_RECORDS,
            null,
            "$COLUMN_EXERCISE_ID = ?",
            arrayOf(exerciseId.toString()),
            null,
            null,
            "$COLUMN_DATE DESC"
        )
        
        cursor.use {
            while (it.moveToNext()) {
                val record = WorkoutRecord(
                    id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID)),
                    exerciseId = it.getInt(it.getColumnIndexOrThrow(COLUMN_EXERCISE_ID)),
                    weight = it.getDouble(it.getColumnIndexOrThrow(COLUMN_WEIGHT)),
                    reps = it.getInt(it.getColumnIndexOrThrow(COLUMN_REPS)),
                    sets = it.getInt(it.getColumnIndexOrThrow(COLUMN_SETS)),
                    date = dateFormat.parse(it.getString(it.getColumnIndexOrThrow(COLUMN_DATE))) ?: Date(),
                    notes = it.getString(it.getColumnIndexOrThrow(COLUMN_NOTES)) ?: ""
                )
                records.add(record)
            }
        }
        
        return records
    }

    /**
     * الحصول على آخر سجل تمرين لتمرين معين - Get last workout record for specific exercise
     */
    fun getLastWorkoutRecord(exerciseId: Int): WorkoutRecord? {
        val db = readableDatabase
        
        val cursor = db.query(
            TABLE_WORKOUT_RECORDS,
            null,
            "$COLUMN_EXERCISE_ID = ?",
            arrayOf(exerciseId.toString()),
            null,
            null,
            "$COLUMN_DATE DESC",
            "1"
        )
        
        cursor.use {
            if (it.moveToFirst()) {
                return WorkoutRecord(
                    id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID)),
                    exerciseId = it.getInt(it.getColumnIndexOrThrow(COLUMN_EXERCISE_ID)),
                    weight = it.getDouble(it.getColumnIndexOrThrow(COLUMN_WEIGHT)),
                    reps = it.getInt(it.getColumnIndexOrThrow(COLUMN_REPS)),
                    sets = it.getInt(it.getColumnIndexOrThrow(COLUMN_SETS)),
                    date = dateFormat.parse(it.getString(it.getColumnIndexOrThrow(COLUMN_DATE))) ?: Date(),
                    notes = it.getString(it.getColumnIndexOrThrow(COLUMN_NOTES)) ?: ""
                )
            }
        }
        
        return null
    }
}