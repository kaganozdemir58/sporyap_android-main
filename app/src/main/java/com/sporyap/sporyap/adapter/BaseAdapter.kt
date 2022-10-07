package com.sporyap.sporyap.adapter

import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sporyap.sporyap.utils.DiffUtilEquality

abstract class BaseAdapter<T : Any, VH : RecyclerView.ViewHolder?> : ListAdapter<T, VH>(object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(@NonNull oldItem: T, @NonNull newItem: T): Boolean {
        return if (oldItem is DiffUtilEquality) {
            (oldItem as DiffUtilEquality).realEquals(newItem)
        } else oldItem == newItem
    }

    override fun areContentsTheSame(@NonNull oldItem: T, @NonNull newItem: T): Boolean {
        return if (oldItem is DiffUtilEquality) {
            (oldItem as DiffUtilEquality).realEquals(newItem)
        } else false
    }
})