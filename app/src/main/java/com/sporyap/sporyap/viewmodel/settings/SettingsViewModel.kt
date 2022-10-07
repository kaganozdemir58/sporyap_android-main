package com.sporyap.sporyap.viewmodel.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sporyap.sporyap.data.network.request.account.LogOutRequest
import com.sporyap.sporyap.data.network.response.base.BaseResponse
import com.sporyap.sporyap.data.rp.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val accountRepository: AccountRepository) : ViewModel(){

    sealed class ViewState{
        class ShowLoading(val isShow : Boolean) : ViewState()
        class ShowErrorMessage(val message : String?) : ViewState()
        class OnLogOut(val response : BaseResponse): ViewState()
        object Empty : ViewState()
    }

    private val _settingsUIState = MutableStateFlow<ViewState>(ViewState.Empty)
    val settingsUIState : StateFlow<ViewState> = _settingsUIState

    fun logOut(userId : Int){
        viewModelScope.launch {
            _settingsUIState.value = ViewState.ShowLoading(true)
            val logOutRequest = LogOutRequest(userId)
            accountRepository.logOut(logOutRequest).apply {
                _settingsUIState.value = ViewState.ShowLoading(false)
                if (this.isSuccessful && this.body()!!.succeeded){
                    _settingsUIState.value = ViewState.OnLogOut(this.body()!!)
                }else{
                    _settingsUIState.value = ViewState.ShowErrorMessage(this.message())
                }
            }
        }
    }
}