package com.sporyap.sporyap.data.network.response.location

data class SaveRegionDetailResponse(
    val code: Int,
    val errors: List<Any>,
    val message: String,
    val result: Result,
    val succeeded: Boolean
)