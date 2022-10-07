package com.sporyap.sporyap.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class GlideHelper {

    companion object{
        fun loadImageBase64(context: Context, image: ByteArray, imageView: ImageView){
            Glide.with(context)
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .fitCenter()
                .centerCrop()
                .into(imageView)
        }
        fun loadImage(context: Context, url : String, imageView : ImageView){
            Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .centerCrop()
                .into(imageView)
        }
        fun loadImageWithBorder(context: Context, url : String, imageView: ImageView, round: Int){
            Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(round)))
                .into(imageView)
        }
    }
}