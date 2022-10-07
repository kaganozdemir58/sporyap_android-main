package com.sporyap.sporyap.data.network.request.account

data class AuthenticateRequest(
    val email: String,
    val password: String,
    val phone: String
)