package com.sporyap.sporyap.viewmodel.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sporyap.sporyap.data.network.request.account.AuthenticateRequest
import com.sporyap.sporyap.data.network.response.account.AuthenticateResponse
import com.sporyap.sporyap.data.rp.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val accountRepository: AccountRepository): ViewModel() {

    sealed class ViewState{
        class ShowLoading(val isShow : Boolean) : ViewState()
        class ShowErrorMessage(val message : String) : ViewState()
        class OnLogged(val response : AuthenticateResponse) : ViewState()
        object Empty : ViewState()
    }

    private val _loginUIState = MutableStateFlow<ViewState>(ViewState.Empty)
    val loginUIState : StateFlow<ViewState> = _loginUIState

    fun login(email:String, phone:String , password: String){
        viewModelScope.launch {
            _loginUIState.value = ViewState.ShowLoading(true)
            val authenticateRequest = AuthenticateRequest(email , password , phone)
            accountRepository.accountAuthenticate(authenticateRequest).apply {
                _loginUIState.value = ViewState.ShowLoading(false)
                if (this.isSuccessful && this.body()!!.succeeded){
                    _loginUIState.value = ViewState.OnLogged(this.body()!!)
                }else{
                    _loginUIState.value = ViewState.ShowErrorMessage(this.message())
                }
            }
        }
    }

    fun isValidMail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidMobile(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }
}