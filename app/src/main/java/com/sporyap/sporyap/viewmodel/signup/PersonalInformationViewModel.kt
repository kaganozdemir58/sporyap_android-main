package com.sporyap.sporyap.viewmodel.signup

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sporyap.sporyap.R
import com.sporyap.sporyap.data.network.request.location.InsertRegionDetailRequest
import com.sporyap.sporyap.data.network.response.location.SaveRegionDetailResponse
import com.sporyap.sporyap.data.network.response.phone_code.PhoneCodeResponse
import com.sporyap.sporyap.data.rp.AccountRepository
import com.sporyap.sporyap.data.rp.LocationRepository
import com.sporyap.sporyap.utils.Constants
import com.sporyap.sporyap.utils.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalInformationViewModel @Inject constructor(private val accountRepository: AccountRepository, private val locationRepository: LocationRepository): ViewModel() {

    sealed class ViewState{
        class ShowLoading(val isShow : Boolean) : ViewState()
        class ShowErrorMessage(val message : String) : ViewState()
        class OnLocationSaved(val response : SaveRegionDetailResponse) : ViewState()
        class PhoneCodesOnLoaded(val response : List<String>) : ViewState()
        object Empty : ViewState()
    }

    private val _personalInformationUIState = MutableStateFlow<ViewState>(ViewState.Empty)
    val personalInformationUIState : StateFlow<ViewState> = _personalInformationUIState

    fun isValidMail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidMobile(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }

    fun isPasswordValid(password: String) : Boolean{
        return password.length in 5..19
    }

    fun validatePassword(password : String , passwordRetry : String):Boolean{
        return password == passwordRetry
    }

    fun getPhoneCodes(){
        viewModelScope.launch {
            _personalInformationUIState.value = ViewState.ShowLoading(true)
            accountRepository.getPhoneCodes().apply {
                _personalInformationUIState.value = ViewState.ShowLoading(false)
                if (this.isSuccessful && this.body() != null && this.body()?.succeeded!!){
                    _personalInformationUIState.value = ViewState.PhoneCodesOnLoaded(this.body()!!.result)
                }else{
                    _personalInformationUIState.value = ViewState.ShowErrorMessage(this.message())
                }
            }
        }
    }

    fun saveLocations(context: Context, country : String, city : String, subLocality : String){
        viewModelScope.launch {
            _personalInformationUIState.value = ViewState.ShowLoading(true)
            val request = InsertRegionDetailRequest(city , country, subLocality, Constants.API_TOKEN)
            locationRepository.insertRegionDetail(request).apply {
                _personalInformationUIState.value = ViewState.ShowLoading(false)
                if (this.isSuccessful){
                    _personalInformationUIState.value = ViewState.OnLocationSaved(this.body()!!)
                }else{
                    _personalInformationUIState.value = ViewState.ShowErrorMessage(context.getString(
                        R.string.please_check_entered_information))
                }
            }
        }
    }

    fun setUserInformation(context: Context , name : String, lastName : String , email :String,
                           phone: String , password : String, gender: String,
                           lat: String , long : String, subLocality : String,
                           city : String , country : String){
        Prefs.setKeySharedPreferences(context , Constants.PREF_NAME , name)
        Prefs.setKeySharedPreferences(context , Constants.PREF_LAST_NAME , lastName)
        Prefs.setKeySharedPreferences(context , Constants.PREF_EMAIL , email)
        Prefs.setKeySharedPreferences(context , Constants.PREF_PHONE , phone)
        Prefs.setKeySharedPreferences(context , Constants.PREF_PASSWORD , password)
        Prefs.setKeySharedPreferences(context , Constants.PREF_GENDER , gender)
        Prefs.setKeySharedPreferences(context , Constants.PREF_LAT , lat)
        Prefs.setKeySharedPreferences(context , Constants.PREF_LONG, long)
        Prefs.setKeySharedPreferences(context, Constants.PREF_SUB_LOCALITY , subLocality)
        Prefs.setKeySharedPreferences(context, Constants.PREF_CITY , city)
        Prefs.setKeySharedPreferences(context, Constants.PREF_COUNTRY , country)
    }
}