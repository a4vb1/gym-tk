package com.gymapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gymapp.adapters.WorkoutRecordAdapter
import com.gymapp.data.GymDataRepository
import com.gymapp.model.WorkoutRecord
import java.util.*

/**
 * نشاط تفاصيل التمرين
 * Exercise detail activity
 */
class ExerciseDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_EXERCISE_ID = "exercise_id"
        const val EXTRA_EXERCISE_NAME = "exercise_name"
    }

    private lateinit var repository: GymDataRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WorkoutRecordAdapter
    
    private lateinit var editTextWeight: EditText
    private lateinit var editTextReps: EditText
    private lateinit var editTextSets: EditText
    private lateinit var editTextNotes: EditText
    private lateinit var buttonSave: Button
    private lateinit var textViewLastWorkout: TextView
    private lateinit var textViewStats: TextView
    
    private var exerciseId: Int = 0
    private var exerciseName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_detail)
        
        // الحصول على البيانات المرسلة - Get passed data
        exerciseId = intent.getIntExtra(EXTRA_EXERCISE_ID, 0)
        exerciseName = intent.getStringExtra(EXTRA_EXERCISE_NAME) ?: ""
        
        // تهيئة مستودع البيانات - Initialize data repository
        repository = GymDataRepository(this)
        
        // إعداد شريط العنوان - Setup action bar
        supportActionBar?.apply {
            title = exerciseName
            setDisplayHomeAsUpEnabled(true)
        }
        
        // تهيئة واجهة المستخدم - Initialize UI
        initializeUI()
        
        // تحميل البيانات - Load data
        loadExerciseData()
        
        // إعداد معالج حفظ البيانات - Setup save handler
        setupSaveButton()
    }

    /**
     * تهيئة واجهة المستخدم - Initialize UI
     */
    private fun initializeUI() {
        editTextWeight = findViewById(R.id.editTextWeight)
        editTextReps = findViewById(R.id.editTextReps)
        editTextSets = findViewById(R.id.editTextSets)
        editTextNotes = findViewById(R.id.editTextNotes)
        buttonSave = findViewById(R.id.buttonSave)
        textViewLastWorkout = findViewById(R.id.textViewLastWorkout)
        textViewStats = findViewById(R.id.textViewStats)
        recyclerView = findViewById(R.id.recyclerViewWorkoutHistory)
        
        // إعداد قائمة السجلات - Setup records list
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = WorkoutRecordAdapter()
        recyclerView.adapter = adapter
        
        // تعيين القيم الافتراضية - Set default values
        editTextSets.setText("1")
    }

    /**
     * تحميل بيانات التمرين - Load exercise data
     */
    private fun loadExerciseData() {
        try {
            // تحميل آخر تمرين - Load last workout
            val lastWorkout = repository.getLastWorkoutRecord(exerciseId)
            if (lastWorkout != null) {
                textViewLastWorkout.text = getString(
                    R.string.last_workout_info,
                    lastWorkout.weight,
                    lastWorkout.reps,
                    lastWorkout.sets
                )
                
                // ملء البيانات المقترحة - Fill suggested data
                editTextWeight.setText(lastWorkout.weight.toString())
                editTextReps.setText(lastWorkout.reps.toString())
                editTextSets.setText(lastWorkout.sets.toString())
            } else {
                textViewLastWorkout.text = getString(R.string.no_previous_workouts)
            }
            
            // تحميل الإحصائيات - Load statistics
            val stats = repository.getExerciseStats(exerciseId)
            textViewStats.text = getString(
                R.string.exercise_stats,
                stats.totalWorkouts,
                stats.maxWeight ?: 0.0
            )
            
            // تحميل السجلات السابقة - Load previous records
            val records = repository.getWorkoutRecordsForExercise(exerciseId)
            adapter.updateRecords(records)
            
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_loading_data), Toast.LENGTH_LONG).show()
        }
    }

    /**
     * إعداد زر الحفظ - Setup save button
     */
    private fun setupSaveButton() {
        buttonSave.setOnClickListener {
            saveWorkout()
        }
    }

    /**
     * حفظ التمرين - Save workout
     */
    private fun saveWorkout() {
        try {
            // التحقق من صحة البيانات - Validate input
            val weightText = editTextWeight.text.toString().trim()
            val repsText = editTextReps.text.toString().trim()
            val setsText = editTextSets.text.toString().trim()
            
            if (weightText.isEmpty() || repsText.isEmpty() || setsText.isEmpty()) {
                Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show()
                return
            }
            
            val weight = weightText.toDoubleOrNull()
            val reps = repsText.toIntOrNull()
            val sets = setsText.toIntOrNull()
            
            if (weight == null || weight <= 0) {
                Toast.makeText(this, getString(R.string.error_invalid_weight), Toast.LENGTH_SHORT).show()
                return
            }
            
            if (reps == null || reps <= 0) {
                Toast.makeText(this, getString(R.string.error_invalid_reps), Toast.LENGTH_SHORT).show()
                return
            }
            
            if (sets == null || sets <= 0) {
                Toast.makeText(this, getString(R.string.error_invalid_sets), Toast.LENGTH_SHORT).show()
                return
            }
            
            // إنشاء سجل التمرين - Create workout record
            val workoutRecord = WorkoutRecord(
                exerciseId = exerciseId,
                weight = weight,
                reps = reps,
                sets = sets,
                date = Date(),
                notes = editTextNotes.text.toString().trim()
            )
            
            // حفظ السجل - Save record
            val recordId = repository.saveWorkoutRecord(workoutRecord)
            
            if (recordId > 0) {
                Toast.makeText(this, getString(R.string.workout_saved_successfully), Toast.LENGTH_SHORT).show()
                
                // مسح الحقول - Clear fields
                editTextNotes.text.clear()
                
                // إعادة تحميل البيانات - Reload data
                loadExerciseData()
            } else {
                Toast.makeText(this, getString(R.string.error_saving_workout), Toast.LENGTH_SHORT).show()
            }
            
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_saving_workout), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}