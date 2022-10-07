package com.sporyap.sporyap.data.network.request.location

data class InsertRegionDetailRequest(
    val cityName: String,
    val countryName: String,
    val districtName: String,
    val token: String
)