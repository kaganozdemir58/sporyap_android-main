package com.sporyap.sporyap.data.rp

import com.sporyap.sporyap.data.AppApi
import javax.inject.Inject

class EventRepository @Inject constructor(private val appApi: AppApi){

    suspend fun getAllEventTypes(token : String) = appApi.getAllEventTypes(token)

    suspend fun getAllGenderTypes(token : String) = appApi.getAllGenderTypes(token)

    suspend fun getAllEventCategories(token : String, sportId : Int) = appApi.getAllEventCategories(token , sportId)

    suspend fun getAllEventOwnerTypes(token : String) = appApi.getAllEventOwnerTypes(token)

}