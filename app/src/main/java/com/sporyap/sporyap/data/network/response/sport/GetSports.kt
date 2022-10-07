package com.sporyap.sporyap.data.network.response.sport

data class GetSports(
    val code: Int,
    val errors: List<String>,
    val message: String,
    val result: List<Result>,
    val succeeded: Boolean
)