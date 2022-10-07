package com.sporyap.sporyap.data.network.response.account.profile_infos

data class Corporate(
    val address: String?,
    val cityId: Int,
    val cityName: String?,
    val contactPerson: String?,
    val corporateTypeId: Int,
    val corporateTypeName: String?,
    val countryId: Int,
    val countryName: String?,
    val districtId: Int,
    val districtName: String?,
    val email: String?,
    val latitude: Double,
    val longitude: Double,
    val name: String?,
    val phone: String?
)