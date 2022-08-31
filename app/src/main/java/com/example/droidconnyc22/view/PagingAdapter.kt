package com.example.droidconnyc22.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.droidconnyc22.databinding.LoadingViewHolderBinding

abstract class PagingAdapter<T>(diffCallback: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, RecyclerView.ViewHolder>(diffCallback) {

    var isLoadingMore = false
        set(value) {
            field = value
            if (value) {
                notifyItemInserted(itemCount)
            } else {
                notifyItemRemoved(itemCount - 1)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == LOADING_VIEW_TYPE) {
            val viewBinding = LoadingViewHolderBinding.inflate(LayoutInflater.from(parent.context))
            LoadingViewHolder(viewBinding)
        } else {
            viewHolder(parent, viewType)
        }
    }

    abstract fun viewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    override fun getItemCount(): Int {
        val listCount = super.getItemCount()
        return when {
            isLoadingMore -> listCount + 1
            else -> listCount
        }
    }

    override fun getItemViewType(position: Int): Int {
        val actualViewType = super.getItemViewType(position)

        return when {
            !isLoadingMore -> actualViewType
            position == (itemCount - 1) -> LOADING_VIEW_TYPE
            else -> actualViewType
        }
    }

    companion object {
        private const val LOADING_VIEW_TYPE = 45689
    }
}

class LoadingViewHolder(viewBinding: LoadingViewHolderBinding) :
    RecyclerView.ViewHolder(viewBinding.root)