package com.sporyap.sporyap.data.rp

import com.sporyap.sporyap.data.AppApi
import com.sporyap.sporyap.utils.Constants
import javax.inject.Inject

class SportRepository @Inject constructor(private val appApi: AppApi) {

    suspend fun getSports() = appApi.getSports(Constants.API_TOKEN)

    suspend fun getSportsByPage(token : String, page:String, pageSize : String) = appApi.getSportsByPage(token , page , pageSize)
}