package com.sporyap.sporyap.data.network.response.base

data class ErrorResponse(
    val Code: Int,
    val Errors: Any,
    val Message: String,
    val Result: Any,
    val Succeeded: Boolean
)