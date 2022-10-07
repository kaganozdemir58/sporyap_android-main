package com.sporyap.sporyap.viewmodel.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sporyap.sporyap.R
import com.sporyap.sporyap.data.network.response.base.BaseResponse
import com.sporyap.sporyap.data.rp.AccountRepository
import com.sporyap.sporyap.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompleteVerificationViewModel @Inject constructor(private val accountRepository: AccountRepository) : ViewModel() {

    sealed class ViewState{
        class ShowLoading(val isShow : Boolean) : ViewState()
        class ShowErrorMessage(val message : String) : ViewState()
        class OnCodeReceived(val response : BaseResponse) : ViewState()
        class OnCodeConfirmed(val response : BaseResponse) : ViewState()
        object Empty : ViewState()
    }

    private val _completeVerificationUIState = MutableStateFlow<ViewState>(ViewState.Empty)
    val completeVerificationUIState : StateFlow<ViewState> = _completeVerificationUIState

    fun getVerificationCode(context: Context, userId : Int, notificationTypeId : Int){
        viewModelScope.launch {
            _completeVerificationUIState.value = ViewState.ShowLoading(true)
            accountRepository.accountSendCodeAgain(token = Constants.API_TOKEN , userId = userId , notificationTypeId = notificationTypeId).apply {
                _completeVerificationUIState.value = ViewState.ShowLoading(false)
                if (this.isSuccessful && this.body()!!.succeeded){
                    _completeVerificationUIState.value = ViewState.OnCodeReceived(this.body()!!)
                }else{
                    _completeVerificationUIState.value = ViewState.ShowErrorMessage(context.getString(R.string.please_check_entered_information))
                }
            }
        }
    }

    fun confirmVerificationCode(context: Context , userId : Int , notificationTypeId: Int , verifyCode : String){
        viewModelScope.launch {
            _completeVerificationUIState.value = ViewState.ShowLoading(true)
            accountRepository.accountConfirmCode(userId, notificationTypeId, verifyCode).apply {
                _completeVerificationUIState.value = ViewState.ShowLoading(false)
                if (this.isSuccessful && this.body()!!.succeeded){
                    _completeVerificationUIState.value = ViewState.OnCodeConfirmed(this.body()!!)
                }else{
                    _completeVerificationUIState.value = ViewState.ShowErrorMessage(context.getString(R.string.please_check_entered_information))
                }
            }
        }
    }
}