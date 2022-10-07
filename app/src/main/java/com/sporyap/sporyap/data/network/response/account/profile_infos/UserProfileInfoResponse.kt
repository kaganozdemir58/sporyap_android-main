package com.sporyap.sporyap.data.network.response.account.profile_infos

data class UserProfileInfoResponse(
    val code: Int,
    val errors: List<String>,
    val message: String,
    val result: Result,
    val succeeded: Boolean
)