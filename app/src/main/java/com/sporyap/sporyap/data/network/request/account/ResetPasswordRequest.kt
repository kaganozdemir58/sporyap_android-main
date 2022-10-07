package com.sporyap.sporyap.data.network.request.account

data class ResetPasswordRequest(
    val confirmPassword: String,
    val currentPassword: String,
    val email: String,
    val newPassword: String,
    val phone: String
)