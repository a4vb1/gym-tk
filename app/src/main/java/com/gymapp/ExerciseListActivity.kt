package com.gymapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gymapp.adapters.ExerciseAdapter
import com.gymapp.data.GymDataRepository

/**
 * نشاط قائمة التمارين
 * Exercise list activity
 */
class ExerciseListActivity : AppCompatActivity(), ExerciseAdapter.OnExerciseClickListener {

    companion object {
        const val EXTRA_MUSCLE_GROUP_ID = "muscle_group_id"
        const val EXTRA_MUSCLE_GROUP_NAME = "muscle_group_name"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExerciseAdapter
    private lateinit var repository: GymDataRepository
    private var muscleGroupId: Int = 0
    private var muscleGroupName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_list)
        
        // الحصول على البيانات المرسلة - Get passed data
        muscleGroupId = intent.getIntExtra(EXTRA_MUSCLE_GROUP_ID, 0)
        muscleGroupName = intent.getStringExtra(EXTRA_MUSCLE_GROUP_NAME) ?: ""
        
        // تهيئة مستودع البيانات - Initialize data repository
        repository = GymDataRepository(this)
        
        // إعداد شريط العنوان - Setup action bar
        supportActionBar?.apply {
            title = muscleGroupName
            setDisplayHomeAsUpEnabled(true)
        }
        
        // تهيئة واجهة المستخدم - Initialize UI
        initializeUI()
        
        // تحميل التمارين - Load exercises
        loadExercises()
    }

    /**
     * تهيئة واجهة المستخدم - Initialize UI
     */
    private fun initializeUI() {
        recyclerView = findViewById(R.id.recyclerViewExercises)
        
        // إعداد مدير التخطيط - Setup layout manager
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        // إعداد المحول - Setup adapter
        adapter = ExerciseAdapter(this)
        recyclerView.adapter = adapter
    }

    /**
     * تحميل التمارين - Load exercises
     */
    private fun loadExercises() {
        try {
            val exercises = repository.getExercisesForMuscleGroup(muscleGroupId)
            adapter.updateExercises(exercises)
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_loading_exercises), Toast.LENGTH_LONG).show()
        }
    }

    /**
     * معالج النقر على التمرين - Handle exercise click
     */
    override fun onExerciseClick(exerciseId: Int, exerciseName: String) {
        val intent = Intent(this, ExerciseDetailActivity::class.java).apply {
            putExtra(ExerciseDetailActivity.EXTRA_EXERCISE_ID, exerciseId)
            putExtra(ExerciseDetailActivity.EXTRA_EXERCISE_NAME, exerciseName)
        }
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}