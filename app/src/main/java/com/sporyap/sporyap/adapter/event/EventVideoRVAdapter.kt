package com.sporyap.sporyap.adapter.event

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sporyap.sporyap.data.entity.MediaObject
import com.sporyap.sporyap.databinding.CardDesignMainPageBinding
import com.sporyap.sporyap.viewmodel.event.EventsViewModel

class EventVideoRVAdapter (var mContext: Context, var mList:List<MediaObject>)
    : RecyclerView.Adapter<EventVideoRVAdapter.CardDesignHolder>(){

    inner class CardDesignHolder(design:CardDesignMainPageBinding) : RecyclerView.ViewHolder(design.root){
        var design:CardDesignMainPageBinding
        init {
            this.design = design
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardDesignHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        val design = CardDesignMainPageBinding.inflate(layoutInflater, parent, false)
        return CardDesignHolder(design)
    }

    override fun onBindViewHolder(holder: CardDesignHolder, position: Int) {
        val mediaObject = mList.get(position)

        val d = holder.design

        d.textViewEducationName.setText(mediaObject.SportName)
        d.textViewSporName.setText(mediaObject.EventName)
        d.ratingBar.rating = mediaObject.Rating.toFloat()
        d.textViewRating.setText(mediaObject.Rating.toString())
        d.textViewAdress.setText(mediaObject.LocationName)
        d.textViewTotalPerson.setText(mediaObject.TotalMemberCount.toString())
        d.textViewEventTypeName.setText(mediaObject.EventTypeName.toString())

        d.videoView.setVideoURI((Uri.parse(mediaObject.media_url)))
        d.videoView.start()
        Log.e("deneme","4 çalıştı")


    }

    override fun getItemCount(): Int {
        return mList.size
    }

}