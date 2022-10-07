package com.sporyap.sporyap.data.network.request.account

data class RegisterRequest(
    val corporate: Corporate?,
    val email: String,
    val genderTypeId: Int,
    val image: String,
    val isSendNotifyForEmailVerify: Boolean,
    val isSendNotifyForSmsVerify: Boolean,
    val name: String,
    val password: String,
    val phone: String,
    val surname: String,
    val trainerSportIDs: List<Int>,
    val userLocations: List<UserLocation>,
    val userName: String,
    val userTypeId: Int,
    val usersSportIDs: List<Int>
)