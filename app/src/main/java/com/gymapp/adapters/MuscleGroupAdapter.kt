package com.gymapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gymapp.R
import com.gymapp.model.MuscleGroup

/**
 * محول قائمة مجموعات العضلات
 * Muscle groups RecyclerView adapter
 */
class MuscleGroupAdapter(
    private val clickListener: OnMuscleGroupClickListener
) : RecyclerView.Adapter<MuscleGroupAdapter.MuscleGroupViewHolder>() {

    private var muscleGroups = mutableListOf<MuscleGroup>()

    interface OnMuscleGroupClickListener {
        fun onMuscleGroupClick(muscleGroupId: Int, muscleGroupName: String)
    }

    /**
     * تحديث قائمة مجموعات العضلات - Update muscle groups list
     */
    fun updateMuscleGroups(newMuscleGroups: List<MuscleGroup>) {
        muscleGroups.clear()
        muscleGroups.addAll(newMuscleGroups)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MuscleGroupViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_muscle_group, parent, false)
        return MuscleGroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: MuscleGroupViewHolder, position: Int) {
        holder.bind(muscleGroups[position])
    }

    override fun getItemCount(): Int = muscleGroups.size

    inner class MuscleGroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewIcon: ImageView = itemView.findViewById(R.id.imageViewMuscleIcon)
        private val textViewName: TextView = itemView.findViewById(R.id.textViewMuscleName)

        fun bind(muscleGroup: MuscleGroup) {
            textViewName.text = muscleGroup.nameArabic
            
            // تعيين الأيقونة إذا كانت متوفرة - Set icon if available
            try {
                if (muscleGroup.iconResourceId != 0) {
                    imageViewIcon.setImageResource(muscleGroup.iconResourceId)
                } else {
                    // أيقونة افتراضية - Default icon
                    imageViewIcon.setImageResource(R.drawable.ic_muscle_default)
                }
            } catch (e: Exception) {
                // في حالة عدم وجود الأيقونة، استخدم الافتراضية - Use default icon if not found
                imageViewIcon.setImageResource(R.drawable.ic_muscle_default)
            }

            // معالج النقر - Click handler
            itemView.setOnClickListener {
                clickListener.onMuscleGroupClick(muscleGroup.id, muscleGroup.nameArabic)
            }
        }
    }
}