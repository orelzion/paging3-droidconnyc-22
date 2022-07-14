package com.example.droidconnyc22.view

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.droidconnyc22.model.Patient

class PatientAdapter(private val onBookmarkChecked: (patient: Patient) -> Unit) :
    ListAdapter<Patient, PatientViewHolder>(PatientDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        return PatientViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        holder.bind(getItem(position)) {
            onBookmarkChecked(getItem(position))
        }
    }
}

class PatientDiffUtil : DiffUtil.ItemCallback<Patient>() {
    override fun areItemsTheSame(oldItem: Patient, newItem: Patient) = oldItem === newItem
    override fun areContentsTheSame(oldItem: Patient, newItem: Patient) = oldItem == newItem
}