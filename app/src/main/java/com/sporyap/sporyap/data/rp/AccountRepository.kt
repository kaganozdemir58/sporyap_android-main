package com.sporyap.sporyap.data.rp

import com.sporyap.sporyap.data.AppApi
import com.sporyap.sporyap.data.network.request.account.*
import com.sporyap.sporyap.utils.Constants
import javax.inject.Inject

class AccountRepository @Inject constructor(private val appApi: AppApi) {

    suspend fun accountAuthenticate(request : AuthenticateRequest) = appApi.accountAuthenticate(Constants.API_TOKEN,request)

    suspend fun accountRegister(request : RegisterRequest) = appApi.accountRegister(Constants.API_TOKEN,request)

    suspend fun accountConfirmCode(userId: Int, notificationTypeId : Int , verifyCode : String) = appApi.accountConfirmCode(Constants.API_TOKEN , userId, notificationTypeId, verifyCode)

    suspend fun accountSendCodeAgain(token : String, userId: Int , notificationTypeId: Int) = appApi.accountSendCodeAgain(Constants.API_TOKEN,token, userId, notificationTypeId)

    suspend fun accountForgotPassword(token : String, userId: Int , notificationTypeId: Int) = appApi.accountForgotPassword(Constants.API_TOKEN,token, userId, notificationTypeId)

    suspend fun accountResetPassword(request : ResetPasswordRequest) = appApi.accountResetPassword(request)

    suspend fun accountRefreshToken(request : RefreshTokenRequest) = appApi.accountRefreshToken(request)

    suspend fun logOut(request : LogOutRequest) = appApi.logOut(request)

    suspend fun userProfileInfos(token : String) = appApi.userProfileInfos(token)

    suspend fun getPhoneCodes() = appApi.getPhoneCodes(Constants.API_TOKEN)
}