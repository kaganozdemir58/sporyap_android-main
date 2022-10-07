package com.sporyap.sporyap.adapter.sport

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.sporyap.sporyap.adapter.BaseAdapter
import com.sporyap.sporyap.data.network.response.sport.Result
import com.sporyap.sporyap.databinding.ItemSportBinding
import com.sporyap.sporyap.adapter.listeners.OnSportItemClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SportsAdapter(private var sports : List<Result>, val context: Context, private val onSportItemClickListener: OnSportItemClickListener) : BaseAdapter<Result, SportsAdapter.ViewHolder>() {

    class ViewHolder(private val itemBinding : ItemSportBinding , private val onSportItemClickListener: OnSportItemClickListener) : RecyclerView.ViewHolder(itemBinding.root){

        fun bindItems(result: Result){

            CoroutineScope(Dispatchers.IO).launch {
                itemBinding.imageViewSport.load(result.image)
            }
            itemBinding.textViewSport.text = result.name
            itemBinding.textViewSport.isVisible = !result.isSelected

            itemBinding.textViewSportTitle.text = result.name
            itemBinding.textViewSportTitle.isVisible = result.isSelected

            itemBinding.imageViewSportBg.isVisible = result.isSelected
            itemBinding.imageViewSportChecked.isVisible = result.isSelected

            itemView.setOnClickListener {
                onSportItemClickListener.onClick(result)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemSportBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(itemBinding , onSportItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(sports[position])
    }

    override fun getItemId(position: Int): Long {
        return currentList[position].hashCode().toLong()
    }

    override fun getItemCount(): Int {
        return sports.size
    }

    fun updateSports(newSports: List<Result>){
        sports = newSports
        notifyItemRangeChanged(0, sports.size)
    }
}