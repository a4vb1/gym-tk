package com.gymapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gymapp.adapters.MuscleGroupAdapter
import com.gymapp.data.GymDataRepository

/**
 * النشاط الرئيسي لتطبيق الجيم
 * Main activity for Gym application
 */
class MainActivity : AppCompatActivity(), MuscleGroupAdapter.OnMuscleGroupClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MuscleGroupAdapter
    private lateinit var repository: GymDataRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // تهيئة مستودع البيانات - Initialize data repository
        repository = GymDataRepository(this)
        
        // إعداد شريط العنوان - Setup action bar
        supportActionBar?.title = getString(R.string.app_name)
        
        // تهيئة واجهة المستخدم - Initialize UI
        initializeUI()
        
        // تحميل البيانات - Load data
        loadMuscleGroups()
    }

    /**
     * تهيئة واجهة المستخدم - Initialize UI
     */
    private fun initializeUI() {
        recyclerView = findViewById(R.id.recyclerViewMuscleGroups)
        
        // إعداد مدير التخطيط للشبكة - Setup grid layout manager
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
        
        // إعداد المحول - Setup adapter
        adapter = MuscleGroupAdapter(this)
        recyclerView.adapter = adapter
    }

    /**
     * تحميل مجموعات العضلات - Load muscle groups
     */
    private fun loadMuscleGroups() {
        try {
            val muscleGroups = repository.getMuscleGroups()
            adapter.updateMuscleGroups(muscleGroups)
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_loading_data), Toast.LENGTH_LONG).show()
        }
    }

    /**
     * معالج النقر على مجموعة العضلات - Handle muscle group click
     */
    override fun onMuscleGroupClick(muscleGroupId: Int, muscleGroupName: String) {
        val intent = Intent(this, ExerciseListActivity::class.java).apply {
            putExtra(ExerciseListActivity.EXTRA_MUSCLE_GROUP_ID, muscleGroupId)
            putExtra(ExerciseListActivity.EXTRA_MUSCLE_GROUP_NAME, muscleGroupName)
        }
        startActivity(intent)
    }
}