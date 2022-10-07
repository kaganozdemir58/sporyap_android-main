package com.sporyap.sporyap.adapter.listeners

import com.sporyap.sporyap.data.network.response.event.categories.Result

interface EventCategoryItemClickListener {
    fun onItemClick(eventCategory : Result)
}