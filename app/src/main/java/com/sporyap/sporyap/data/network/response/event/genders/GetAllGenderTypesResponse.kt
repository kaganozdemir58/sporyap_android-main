package com.sporyap.sporyap.data.network.response.event.genders

data class GetAllGenderTypesResponse(
    val code: Int,
    val errors: List<Any>,
    val message: String,
    val result: List<Result>,
    val succeeded: Boolean
)