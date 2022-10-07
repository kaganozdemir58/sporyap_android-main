package com.sporyap.sporyap.data.network.response.corporate

data class CorporateTypesResponse(
    val code: Int,
    val errors: List<Any>,
    val message: String,
    val result: List<Result>,
    val succeeded: Boolean
)