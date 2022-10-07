package com.sporyap.sporyap.data

import com.sporyap.sporyap.data.network.request.account.AuthenticateRequest
import com.sporyap.sporyap.data.network.request.account.LogOutRequest
import com.sporyap.sporyap.data.network.request.account.RegisterRequest
import com.sporyap.sporyap.data.network.response.account.AuthenticateResponse
import com.sporyap.sporyap.data.network.request.account.RefreshTokenRequest
import com.sporyap.sporyap.data.network.response.base.BaseResponse
import com.sporyap.sporyap.data.network.response.account.register.RegisterResponse
import com.sporyap.sporyap.data.network.request.account.ResetPasswordRequest
import com.sporyap.sporyap.data.network.request.location.InsertRegionDetailRequest
import com.sporyap.sporyap.data.network.response.account.profile_infos.UserProfileInfoResponse
import com.sporyap.sporyap.data.network.response.corporate.CorporateTypesResponse
import com.sporyap.sporyap.data.network.response.event.categories.EventCategoriesResponse
import com.sporyap.sporyap.data.network.response.event.genders.GetAllGenderTypesResponse
import com.sporyap.sporyap.data.network.response.event.owner_types.EventOwnerTypeResponse
import com.sporyap.sporyap.data.network.response.event.types.GetEventTypesResponse
import com.sporyap.sporyap.data.network.response.location.SaveRegionDetailResponse
import com.sporyap.sporyap.data.network.response.phone_code.PhoneCodeResponse
import com.sporyap.sporyap.data.network.response.sport.GetSports
import com.sporyap.sporyap.utils.Constants
import retrofit2.Response
import retrofit2.http.*

interface AppApi {

    @POST(Constants.ACCOUNT_AUTHENTICATE)
    suspend fun accountAuthenticate(@Header(Constants.HEADER_API_KEY) apiKey : String , @Body request: AuthenticateRequest): Response<AuthenticateResponse>

    @POST(Constants.ACCOUNT_REGISTER)
    suspend fun accountRegister(@Header(Constants.HEADER_API_KEY) apiKey : String ,@Body request: RegisterRequest): Response<RegisterResponse>

    @GET(Constants.ACCOUNT_CONFIRM_CODE)
    suspend fun accountConfirmCode(
        @Header(Constants.HEADER_API_KEY) apiKey: String,
        @Query(Constants.QUERY_USER_ID) userId: Int,
        @Query(Constants.NOTIFICATION_TYPE_ID) notificationTypeId: Int,
        @Query(Constants.QUERY_VERIFY_CODE) verifyCode: String
    ): Response<BaseResponse>

    @GET(Constants.ACCOUNT_SEND_CODE_AGAIN)
    suspend fun accountSendCodeAgain(
        @Header(Constants.HEADER_API_KEY) apiKey : String ,
        @Query(Constants.QUERY_TOKEN) token: String,
        @Query(Constants.QUERY_USER_ID) userId: Int,
        @Query(Constants.NOTIFICATION_TYPE_ID) notificationTypeId: Int
    ): Response<BaseResponse>

    @GET(Constants.ACCOUNT_FORGOT_PASSWORD)
    suspend fun accountForgotPassword(
        @Header(Constants.HEADER_API_KEY) apiKey : String ,
        @Query(Constants.QUERY_TOKEN) token: String,
        @Query(Constants.QUERY_USER_ID) userId: Int,
        @Query(Constants.NOTIFICATION_TYPE_ID) notificationTypeId: Int
    ): Response<BaseResponse>

    @POST(Constants.ACCOUNT_RESET_PASSWORD)
    suspend fun accountResetPassword(@Body request: ResetPasswordRequest): Response<BaseResponse>

    @POST(Constants.ACCOUNT_REFRESH_TOKEN)
    suspend fun accountRefreshToken(@Body request: RefreshTokenRequest): Response<BaseResponse>

    @POST(Constants.ACCOUNT_LOG_OUT)
    suspend fun logOut(@Body request: LogOutRequest): Response<BaseResponse>

    @GET(Constants.CORPORATE_TYPES_GET_ALL)
    suspend fun getCorporateTypes(@Header(Constants.HEADER_API_KEY) apiKey: String) : Response<CorporateTypesResponse>

    @GET(Constants.GET_SPORTS)
    suspend fun getSports(@Header(Constants.HEADER_API_KEY) apiKey : String): Response<GetSports>

    @POST(Constants.INSERT_REGION_DETAIL)
    suspend fun insertRegionDetail(@Header(Constants.HEADER_API_KEY) apiKey : String, @Body request : InsertRegionDetailRequest) : Response<SaveRegionDetailResponse>

    @GET(Constants.USER_PROFILE_INFO)
    suspend fun userProfileInfos(@Header(Constants.AUTHORIZATION_KEY) token : String) : Response<UserProfileInfoResponse>

    @GET(Constants.GET_PHONE_CODES)
    suspend fun getPhoneCodes(@Header(Constants.HEADER_API_KEY) apiKey : String) : Response<PhoneCodeResponse>

    @GET(Constants.GET_ALL_EVENT_TYPES)
    suspend fun getAllEventTypes(@Header(Constants.AUTHORIZATION_KEY) token : String) : Response<GetEventTypesResponse>

    @GET(Constants.GET_ALL_GENDER_TYPES)
    suspend fun getAllGenderTypes(@Header(Constants.AUTHORIZATION_KEY) token : String) : Response<GetAllGenderTypesResponse>

    @GET(Constants.GET_SPORTS_BY_PAGES)
    suspend fun getSportsByPage(@Header(Constants.AUTHORIZATION_KEY) token : String , @Query(Constants.PAGE) page :String , @Query(Constants.PAGE_SIZE) pageSize: String) : Response<GetSports>

    @GET(Constants.GET_ALL_EVENT_CATEGORIES)
    suspend fun getAllEventCategories(@Header(Constants.AUTHORIZATION_KEY) token : String , @Query(Constants.QUERY_SPORT_ID) sportId : Int) : Response<EventCategoriesResponse>

    @GET(Constants.GET_ALL_EVENT_OWNER_TYPES)
    suspend fun getAllEventOwnerTypes(@Header(Constants.AUTHORIZATION_KEY) token : String) : Response<EventOwnerTypeResponse>
}