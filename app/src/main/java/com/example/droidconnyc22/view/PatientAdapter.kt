package com.example.droidconnyc22.view

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.droidconnyc22.model.Patient
import com.example.droidconnyc22.tryOrNull

// Droidcon 1
class PatientAdapter(private val onBookmarkChecked: (patient: Patient) -> Unit) :
    PagingAdapter<Patient>(PatientDiffUtil()) {

    override fun viewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PatientViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = tryOrNull { getItem(position) }
        if (holder is PatientViewHolder && item != null) {
            holder.bind(item) {
                onBookmarkChecked(item)
            }
        }
    }
}

class PatientDiffUtil : DiffUtil.ItemCallback<Patient>() {
    override fun areItemsTheSame(oldItem: Patient, newItem: Patient) = oldItem.patientId == newItem.patientId
    override fun areContentsTheSame(oldItem: Patient, newItem: Patient) = oldItem == newItem
}
