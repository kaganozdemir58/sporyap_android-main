package com.sporyap.sporyap.data.network.response.phone_code

data class PhoneCodeResponse(
    val code: Int,
    val errors: List<Any>,
    val message: String,
    val result: List<String>,
    val succeeded: Boolean
)