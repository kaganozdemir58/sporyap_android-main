package com.sporyap.sporyap.data.network.response.event.types

data class GetEventTypesResponse(
    val code: Int,
    val errors: List<String>,
    val message: String,
    val result: List<Result>,
    val succeeded: Boolean
)