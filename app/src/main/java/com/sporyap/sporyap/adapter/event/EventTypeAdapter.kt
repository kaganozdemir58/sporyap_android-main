package com.sporyap.sporyap.adapter.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sporyap.sporyap.adapter.BaseAdapter
import com.sporyap.sporyap.data.network.response.event.types.Result
import com.sporyap.sporyap.databinding.ItemEventTypeBinding

class EventTypeAdapter(private val eventTypes : List<Result>) : BaseAdapter<Result, EventTypeAdapter.ViewHolder>() {

    class ViewHolder(private val itemBinding: ItemEventTypeBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bindItems(eventType : Result){
            itemBinding.textViewEventType.text = eventType.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemEventTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(eventTypes[position])
    }

    override fun getItemCount(): Int {
        return eventTypes.size
    }
}