package com.example.droidconnyc22.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.droidconnyc22.BuildConfig
import com.example.droidconnyc22.R
import com.example.droidconnyc22.databinding.PatientViewHolderBinding
import com.example.droidconnyc22.model.Patient

class PatientViewHolder(private val viewBinding: PatientViewHolderBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    companion object {
        fun create(parent: ViewGroup): PatientViewHolder {
            return PatientViewHolder(
                PatientViewHolderBinding
                    .inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
            )
        }
    }

    fun bind(patient: Patient, onBookmarkCheckChanged: (checked: Boolean) -> Unit) {
        viewBinding.patientName.text = patient.name
        viewBinding.scanImage.load("${BuildConfig.LOCAL_IP}${patient.photoUrl}") {
            crossfade(true)
            fallback(R.drawable.no_brain)
        }
        viewBinding.bookmarkCount.text = patient.bookmarkCount.toString()
        viewBinding.bookmarkCheckmark.apply {
            setOnCheckedChangeListener(null)
            isChecked = patient.isBookmarked
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked != patient.isBookmarked) {
                    onBookmarkCheckChanged(isChecked)
                }
            }
        }
    }
}