package com.gymapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gymapp.R
import com.gymapp.model.WorkoutRecord
import java.text.SimpleDateFormat
import java.util.*

/**
 * محول قائمة سجلات التمرين
 * Workout records RecyclerView adapter
 */
class WorkoutRecordAdapter : RecyclerView.Adapter<WorkoutRecordAdapter.WorkoutRecordViewHolder>() {

    private var records = mutableListOf<WorkoutRecord>()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    /**
     * تحديث قائمة السجلات - Update records list
     */
    fun updateRecords(newRecords: List<WorkoutRecord>) {
        records.clear()
        records.addAll(newRecords)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutRecordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout_record, parent, false)
        return WorkoutRecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutRecordViewHolder, position: Int) {
        holder.bind(records[position])
    }

    override fun getItemCount(): Int = records.size

    inner class WorkoutRecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewWeight: TextView = itemView.findViewById(R.id.textViewWeight)
        private val textViewReps: TextView = itemView.findViewById(R.id.textViewReps)
        private val textViewSets: TextView = itemView.findViewById(R.id.textViewSets)
        private val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        private val textViewNotes: TextView = itemView.findViewById(R.id.textViewNotes)

        fun bind(record: WorkoutRecord) {
            val context = itemView.context
            
            textViewWeight.text = context.getString(R.string.weight_kg, record.weight)
            textViewReps.text = context.getString(R.string.reps_count, record.reps)
            textViewSets.text = context.getString(R.string.sets_count, record.sets)
            textViewDate.text = dateFormat.format(record.date)
            
            // إظهار الملاحظات إذا كانت متوفرة - Show notes if available
            if (record.notes.isNotEmpty()) {
                textViewNotes.text = record.notes
                textViewNotes.visibility = View.VISIBLE
            } else {
                textViewNotes.visibility = View.GONE
            }
        }
    }
}