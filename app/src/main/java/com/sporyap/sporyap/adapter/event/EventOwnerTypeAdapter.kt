package com.sporyap.sporyap.adapter.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sporyap.sporyap.adapter.BaseAdapter
import com.sporyap.sporyap.data.network.response.event.owner_types.Result
import com.sporyap.sporyap.databinding.ItemEventTypeBinding

class EventOwnerTypeAdapter(private val eventOwnerTypes : List<Result>) : BaseAdapter<Result,EventOwnerTypeAdapter.ViewHolder>(){

    class ViewHolder(private val itemBinding : ItemEventTypeBinding) : RecyclerView.ViewHolder(itemBinding.root){

        fun bindItem(eventOwnerType : Result){
            itemBinding.textViewEventType.text = eventOwnerType.type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemEventTypeBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(eventOwnerTypes[position])
    }
}