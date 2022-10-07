package com.sporyap.sporyap.viewmodel.corporate

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CorporateNameViewModel @Inject constructor(): ViewModel() {

    fun isValidCorporateName(corporateName : String):Boolean{
        return corporateName.isNotEmpty()
    }
}