package com.sporyap.sporyap.data.rp

import com.sporyap.sporyap.data.AppApi
import com.sporyap.sporyap.data.network.request.location.InsertRegionDetailRequest
import com.sporyap.sporyap.utils.Constants
import javax.inject.Inject

class LocationRepository @Inject constructor(private val appApi: AppApi) {

    suspend fun insertRegionDetail(request : InsertRegionDetailRequest) = appApi.insertRegionDetail(Constants.API_TOKEN, request)
}