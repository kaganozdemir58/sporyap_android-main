package com.sporyap.sporyap.data.network.response.account.profile_infos

data class UserLocation(
    val cityId: Int,
    val cityName: String,
    val countryId: Int,
    val countryName: String,
    val districtId: Int,
    val districtName: String,
    val latitude: Double,
    val locationTypeId: Int,
    val locationTypeName: String,
    val longitude: Double,
    val title: String,
    val userId: Int
)