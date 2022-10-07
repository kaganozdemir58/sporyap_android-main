package com.sporyap.sporyap.data.network.response.event.categories

data class EventCategoriesResponse(
    val code: Int,
    val errors: List<Any>,
    val message: String,
    val result: List<Result>,
    val succeeded: Boolean
)