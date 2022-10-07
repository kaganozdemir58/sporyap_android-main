package com.sporyap.sporyap.data.network.request.account

data class UserLocation(
    val cityId: Int,
    val countryId: Int,
    val districtId: Int,
    val latitude: Double,
    val locationTypeId: Int,
    val longitude: Double,
    val title: String
)