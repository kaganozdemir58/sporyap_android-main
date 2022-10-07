package com.sporyap.sporyap.adapter.listeners

import com.sporyap.sporyap.data.network.response.sport.Result

interface OnSportItemClickListener {

    fun onClick(sport: Result)
}