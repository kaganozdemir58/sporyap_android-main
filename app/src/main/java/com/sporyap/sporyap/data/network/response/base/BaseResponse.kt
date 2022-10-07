package com.sporyap.sporyap.data.network.response.base

data class BaseResponse(
    val code: Int,
    val errors: List<String>,
    val message: String,
    val result: String,
    val succeeded: Boolean
)