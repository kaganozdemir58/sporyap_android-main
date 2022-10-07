package com.sporyap.sporyap.data.network.request.account

data class Corporate(
    var address: String,
    var cityId: Int,
    var contactPerson: String,
    var corporateTypeId: Int,
    var countryId: Int,
    var districtId: Int,
    var email: String,
    var latitude: Double,
    var longitude: Double,
    var name: String,
    var phone: String
)