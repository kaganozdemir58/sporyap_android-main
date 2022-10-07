package com.sporyap.sporyap.data.rp

import com.sporyap.sporyap.data.AppApi
import com.sporyap.sporyap.utils.Constants
import javax.inject.Inject

class CorporateRepository @Inject constructor(private val appApi: AppApi) {

    suspend fun getCorporateTypes() = appApi.getCorporateTypes(Constants.API_TOKEN)
}