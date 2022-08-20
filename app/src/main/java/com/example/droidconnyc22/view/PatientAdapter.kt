package com.example.droidconnyc22.view

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.droidconnyc22.model.db.PatientEntity
import com.example.droidconnyc22.tryOrNull

class PatientAdapter(private val onBookmarkChecked: (patient: PatientEntity) -> Unit) :
    PagingDataAdapter<PatientEntity, PatientViewHolder>(PatientDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        return PatientViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val item = tryOrNull { getItem(position) }
        if (item != null) {
            holder.bind(item) {
                onBookmarkChecked(item)
            }
        }
    }
}

class PatientDiffUtil : DiffUtil.ItemCallback<PatientEntity>() {
    override fun areItemsTheSame(oldItem: PatientEntity, newItem: PatientEntity) =
        oldItem.patientId == newItem.patientId

    override fun areContentsTheSame(oldItem: PatientEntity, newItem: PatientEntity) = oldItem == newItem
}
