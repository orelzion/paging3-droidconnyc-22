package com.example.droidconnyc22.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.droidconnyc22.databinding.LoadingViewHolderBinding
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

    override fun areContentsTheSame(oldItem: PatientEntity, newItem: PatientEntity) =
        oldItem == newItem
}

class PatientLoadingAdapter : LoadStateAdapter<LoadingViewHolder>() {
    override fun onBindViewHolder(holder: LoadingViewHolder, loadState: LoadState) {
        holder.itemView.isVisible = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingViewHolder {
        return LoadingViewHolder(
            LoadingViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}

class LoadingViewHolder(viewBinding: LoadingViewHolderBinding) :
    RecyclerView.ViewHolder(viewBinding.root)
