package com.sporyap.sporyap.data.network.response.account

data class AuthenticateResponse(
    val code: Int,
    val errors: List<String>,
    val message: String,
    val result: Result,
    val succeeded: Boolean
)