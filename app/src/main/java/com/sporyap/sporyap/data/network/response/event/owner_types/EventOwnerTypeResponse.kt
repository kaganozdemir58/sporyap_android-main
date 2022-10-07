package com.sporyap.sporyap.data.network.response.event.owner_types

data class EventOwnerTypeResponse(
    val code: Int,
    val errors: List<Any>,
    val message: String,
    val result: List<Result>,
    val succeeded: Boolean
)