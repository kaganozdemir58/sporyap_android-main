package com.sporyap.sporyap.adapter.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sporyap.sporyap.adapter.BaseAdapter
import com.sporyap.sporyap.adapter.listeners.EventCategoryItemClickListener
import com.sporyap.sporyap.data.network.response.event.categories.Result
import com.sporyap.sporyap.databinding.ItemEventTypeBinding

class EventCategoriesAdapter(private val eventCategories: List<Result>, private val eventCategoryItemClickListener: EventCategoryItemClickListener):BaseAdapter<Result , EventCategoriesAdapter.ViewHolder>(){

    class ViewHolder(private val itemBinding :ItemEventTypeBinding,private val eventCategoryItemClickListener: EventCategoryItemClickListener) : RecyclerView.ViewHolder(itemBinding.root){

        fun bindItem(eventCategory : Result){
            itemBinding.textViewEventType.text = eventCategory.category
            itemView.setOnClickListener {
                eventCategoryItemClickListener.onItemClick(eventCategory)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemEventTypeBinding.inflate(LayoutInflater.from(parent.context) , parent ,false)
        return ViewHolder(itemBinding, eventCategoryItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(eventCategories[position])
    }
}