package com.sporyap.sporyap.viewmodel.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sporyap.sporyap.data.network.response.account.profile_infos.UserProfileInfoResponse
import com.sporyap.sporyap.data.rp.AccountRepository
import com.sporyap.sporyap.utils.Constants
import com.sporyap.sporyap.utils.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(private val accountRepository: AccountRepository) : ViewModel(){

    sealed class ViewState {
        class ShowLoading(val isShow: Boolean) : ViewState()
        class ShowErrorMessage(val message: String) : ViewState()
        class GetUserProfileInformation(val response: UserProfileInfoResponse) : ViewState()
        object Empty : ViewState()
    }

    private val _accountSettingsUIState = MutableStateFlow<ViewState>(ViewState.Empty)
    val accountSettingsUIState : StateFlow<ViewState> = _accountSettingsUIState

    fun getUserProfileInformation(context : Context){
        viewModelScope.launch {
            _accountSettingsUIState.value = ViewState.ShowLoading(true)
            accountRepository.userProfileInfos(Prefs.getKeySharedPreferences(context, Constants.PREF_TOKEN)).apply {
                _accountSettingsUIState.value = ViewState.ShowLoading(false)
                if (this.isSuccessful && this.body()!!.succeeded){
                    _accountSettingsUIState.value = ViewState.GetUserProfileInformation(this.body()!!)
                }else{
                    _accountSettingsUIState.value = ViewState.ShowErrorMessage("")
                }
            }
        }
    }
}