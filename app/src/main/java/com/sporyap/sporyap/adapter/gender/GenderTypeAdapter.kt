package com.sporyap.sporyap.adapter.gender

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sporyap.sporyap.adapter.BaseAdapter
import com.sporyap.sporyap.data.network.response.event.genders.Result
import com.sporyap.sporyap.databinding.ItemCreateEventBinding

class GenderTypeAdapter(private val genderTypes : List<Result>) : BaseAdapter<Result, GenderTypeAdapter.ViewHolder>() {

    class ViewHolder(private val itemBinding : ItemCreateEventBinding) : RecyclerView.ViewHolder(itemBinding.root){

        fun bindItems(genderType : Result){
            itemBinding.textView.text = genderType.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemCreateEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(genderTypes[position])
    }

    override fun getItemCount(): Int {
        return genderTypes.size
    }
}