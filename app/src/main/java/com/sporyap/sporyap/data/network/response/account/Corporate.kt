package com.sporyap.sporyap.data.network.response.account

data class Corporate(
    val address: String,
    val cityId: Int,
    val contactPerson: String,
    val corporateTypeId: Int,
    val corporateTypeName: String,
    val countryId: Int,
    val districtId: Int,
    val email: String,
    val latitude: Int,
    val longitude: Int,
    val name: String,
    val phone: String
)