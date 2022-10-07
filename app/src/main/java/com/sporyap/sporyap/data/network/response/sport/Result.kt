package com.sporyap.sporyap.data.network.response.sport

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result(
    val id: Int,
    val image: String,
    val name: String,
    var isSelected: Boolean
):Parcelable