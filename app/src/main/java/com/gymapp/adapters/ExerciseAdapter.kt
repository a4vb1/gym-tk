package com.gymapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gymapp.R
import com.gymapp.model.Exercise

/**
 * محول قائمة التمارين
 * Exercises RecyclerView adapter
 */
class ExerciseAdapter(
    private val clickListener: OnExerciseClickListener
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    private var exercises = mutableListOf<Exercise>()

    interface OnExerciseClickListener {
        fun onExerciseClick(exerciseId: Int, exerciseName: String)
    }

    /**
     * تحديث قائمة التمارين - Update exercises list
     */
    fun updateExercises(newExercises: List<Exercise>) {
        exercises.clear()
        exercises.addAll(newExercises)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

    override fun getItemCount(): Int = exercises.size

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewName: TextView = itemView.findViewById(R.id.textViewExerciseName)
        private val textViewDescription: TextView = itemView.findViewById(R.id.textViewExerciseDescription)

        fun bind(exercise: Exercise) {
            textViewName.text = exercise.nameArabic
            textViewDescription.text = exercise.descriptionArabic

            // معالج النقر - Click handler
            itemView.setOnClickListener {
                clickListener.onExerciseClick(exercise.id, exercise.nameArabic)
            }
        }
    }
}