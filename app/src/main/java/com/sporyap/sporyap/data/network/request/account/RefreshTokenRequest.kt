package com.sporyap.sporyap.data.network.request.account

data class RefreshTokenRequest(
    val refreshToken: String,
    val token: String,
    val userId: Int
)