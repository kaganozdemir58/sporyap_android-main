package com.sporyap.sporyap.viewmodel.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sporyap.sporyap.R
import com.sporyap.sporyap.data.network.request.account.Corporate
import com.sporyap.sporyap.data.network.request.account.RegisterRequest
import com.sporyap.sporyap.data.network.request.account.UserLocation
import com.sporyap.sporyap.data.network.response.account.register.RegisterResponse
import com.sporyap.sporyap.data.rp.AccountRepository
import com.sporyap.sporyap.utils.Constants
import com.sporyap.sporyap.utils.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserVerificationViewModel @Inject constructor(private val accountRepository: AccountRepository): ViewModel(){
    sealed class ViewState{
        class ShowLoading(val isShow : Boolean) : ViewState()
        class ShowErrorMessage(val message : String) : ViewState()
        class OnRegistered(val response : RegisterResponse) : ViewState()
        object Empty : ViewState()
    }

    private val _userVerificationUIState = MutableStateFlow<ViewState>(ViewState.Empty)
    val userVerificationUIState : StateFlow<ViewState> = _userVerificationUIState

    fun register(context: Context, selectedSports : MutableList<Int>,
                 trainerSports: MutableList<Int> , corporateSports : MutableList<Int>, userType : Int , notificationType : Int, corporateName : String? , corporateType: Int) {
        val email = Prefs.getKeySharedPreferences(context , Constants.PREF_EMAIL)
        val genderTypeId = if (Prefs.getKeySharedPreferences(context , Constants.PREF_GENDER) == context.getString(
                R.string.female)){
            1
        }else{
            2
        }
        val name = Prefs.getKeySharedPreferences(context , Constants.PREF_NAME)
        val password = Prefs.getKeySharedPreferences(context , Constants.PREF_PASSWORD)
        val phone = "0" + Prefs.getKeySharedPreferences(context, Constants.PREF_PHONE)
        val surName = Prefs.getKeySharedPreferences(context, Constants.PREF_LAST_NAME)
        val lat = Prefs.getKeySharedPreferences(context, Constants.PREF_LAT)
        val long = Prefs.getKeySharedPreferences(context , Constants.PREF_LONG)
        val subLocality = Prefs.getKeySharedPreferences(context , Constants.PREF_SUB_LOCALITY)
        val city = Prefs.getKeySharedPreferences(context , Constants.PREF_CITY)
        val country = Prefs.getKeySharedPreferences(context , Constants.PREF_COUNTRY)

        val userName = name.trim() + "." + surName.trim()
        Prefs.setKeySharedPreferences(context = context , key = Constants.PREF_USER_NAME , userName)

        val isSendNotifyForEmailVerify = notificationType == 1
        val isSendNotifyForSmsVerify = notificationType == 2

        val userLocations = arrayListOf<UserLocation>()
        val userLocation = UserLocation(cityId = city.toInt() , countryId = country.toInt() , districtId = subLocality.toInt() , latitude = lat.toDouble(), locationTypeId = 7 , longitude = long.toDouble(), title = "My Address")
        userLocations.add(userLocation)

        //Corporate
        var corporate : Corporate? = null
        if(corporateName !=null && corporateType !=0){
            val selectedCorporate = Corporate(
                address = "Corporate Location",
                cityId = city.toInt(),
                contactPerson = "$name $surName",
                corporateTypeId = corporateType,
                countryId = country.toInt(),
                districtId = subLocality.toInt(),
                email = email,
                latitude = lat.toDouble(),
                longitude = long.toDouble(),
                name = corporateName,
                phone = phone
            )
            corporate = selectedCorporate
        }

        val sports = when(userType){
            3->{
                corporateSports
            }else ->{
                selectedSports
            }
        }

        val body = RegisterRequest(
            corporate, email, genderTypeId, "", isSendNotifyForEmailVerify = isSendNotifyForEmailVerify,
            isSendNotifyForSmsVerify = isSendNotifyForSmsVerify,
            name = name,
            password = password,
            phone = phone,
            surname = surName,
            trainerSportIDs = trainerSports,
            userLocations = userLocations,
            userName = userName,
            userTypeId = userType,
            usersSportIDs = sports
        )

        viewModelScope.launch {
            _userVerificationUIState.value = ViewState.ShowLoading(true)
            accountRepository.accountRegister(body).apply {
                _userVerificationUIState.value = ViewState.ShowLoading(false)
                if (this.isSuccessful && this.body()!!.succeeded){
                    _userVerificationUIState.value = ViewState.OnRegistered(this.body()!!)
                }else{
                    _userVerificationUIState.value = ViewState.ShowErrorMessage(this.errorBody()!!.toString())
                }
            }
        }
    }
}